package com.isaacbetun.estacionamiento.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Aspecto de auditoria sobre la capa de servicios.
 * - Registra cada llamada con sus argumentos y el tiempo que tardo.
 * - Registra los errores (excepciones) que lanzan los servicios.
 * - Acumula estadisticas de uso que se pueden consultar en /api/auditoria/metricas.
 * Asi no hay que repetir codigo de logs en cada metodo del servicio.
 */
@Aspect
@Component
public class AuditoriaAspect {

    private static final Logger log = LoggerFactory.getLogger("AUDITORIA");

    private final Map<String, AtomicLong> llamadas = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> tiempoTotal = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> errores = new ConcurrentHashMap<>();

    @Pointcut("execution(public * com.isaacbetun.estacionamiento.service..*(..))")
    public void metodosDeServicio() {
    }

    // No se audita el envio de correos porque corre en otro hilo
    @Pointcut("execution(* com.isaacbetun.estacionamiento.service.NotificacionService.*(..))")
    public void notificaciones() {
    }

    @Around("metodosDeServicio() && !notificaciones()")
    public Object auditar(ProceedingJoinPoint jp) throws Throwable {
        String metodo = nombre(jp);
        long inicio = System.nanoTime();
        log.info("-> {} args={}", metodo, Arrays.toString(jp.getArgs()));
        try {
            Object resultado = jp.proceed();
            long ms = (System.nanoTime() - inicio) / 1_000_000;
            log.info("<- {} OK en {} ms", metodo, ms);
            acumular(metodo, ms);
            return resultado;
        } catch (Throwable t) {
            acumular(metodo, (System.nanoTime() - inicio) / 1_000_000);
            throw t;
        }
    }

    @AfterThrowing(pointcut = "metodosDeServicio()", throwing = "ex")
    public void registrarError(JoinPoint jp, Throwable ex) {
        String metodo = nombre(jp);
        errores.computeIfAbsent(metodo, k -> new AtomicLong()).incrementAndGet();
        log.warn("x  {} fallo: {} - {}", metodo, ex.getClass().getSimpleName(), ex.getMessage());
    }

    public Map<String, Map<String, Object>> obtenerMetricas() {
        Map<String, Map<String, Object>> resultado = new TreeMap<>();
        llamadas.forEach((metodo, total) -> {
            long n = total.get();
            long tiempo = tiempoTotal.getOrDefault(metodo, new AtomicLong()).get();
            resultado.put(metodo, Map.of(
                    "llamadas", n,
                    "errores", errores.getOrDefault(metodo, new AtomicLong()).get(),
                    "tiempoPromedioMs", n == 0 ? 0 : Math.round(tiempo * 100.0 / n) / 100.0));
        });
        return resultado;
    }

    private void acumular(String metodo, long ms) {
        llamadas.computeIfAbsent(metodo, k -> new AtomicLong()).incrementAndGet();
        tiempoTotal.computeIfAbsent(metodo, k -> new AtomicLong()).addAndGet(ms);
    }

    private String nombre(JoinPoint jp) {
        return jp.getSignature().getDeclaringType().getSimpleName() + "." + jp.getSignature().getName();
    }
}
