package com.isaacbetun.estacionamiento.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * Middleware que registra cada solicitud HTTP: metodo, ruta, IP y
 * navegador/cliente, codigo de respuesta y tiempo total de procesamiento.
 * Ademas agrega la cabecera X-Request-Id para poder rastrear la peticion.
 */
@Component
public class RegistroSolicitudInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RegistroSolicitudInterceptor.class);
    private static final String INICIO = "tiempoInicio";
    private static final String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        request.setAttribute(INICIO, System.currentTimeMillis());
        request.setAttribute(REQUEST_ID, requestId);
        response.setHeader("X-Request-Id", requestId);

        String query = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        log.info("[{}] --> {} {}{} | cliente={} | agente={}",
                requestId, request.getMethod(), request.getRequestURI(), query,
                obtenerIp(request), request.getHeader("User-Agent"));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Long inicio = (Long) request.getAttribute(INICIO);
        long duracion = inicio == null ? -1 : System.currentTimeMillis() - inicio;
        log.info("[{}] <-- {} {} | status={} | {} ms",
                request.getAttribute(REQUEST_ID), request.getMethod(), request.getRequestURI(),
                response.getStatus(), duracion);
    }

    private String obtenerIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return (forwarded != null && !forwarded.isBlank()) ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
    }
}
