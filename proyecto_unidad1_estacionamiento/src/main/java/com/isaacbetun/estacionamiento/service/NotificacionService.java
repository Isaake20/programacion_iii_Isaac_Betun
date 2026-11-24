package com.isaacbetun.estacionamiento.service;

import com.isaacbetun.estacionamiento.dto.RegistroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Envio del comprobante de salida al correo del propietario.
 * <p>
 * Se simula con un Thread.sleep porque un envio real por SMTP tarda varios
 * segundos. Si se llamara de forma sincronica el cliente tendria que esperar
 * ese tiempo para recibir la respuesta de la salida (operacion bloqueante).
 * Con @Async se ejecuta en otro hilo del pool "notificaciones-".
 */
@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    @Value("${app.notificacion.demora-ms:3000}")
    private long demoraMs;

    @Async("notificacionesExecutor")
    public CompletableFuture<Boolean> enviarComprobanteSalida(RegistroResponse registro, String email) {
        log.info("Generando comprobante del registro {} para {}", registro.id(), email);
        try {
            Thread.sleep(demoraMs); // simula la conexion con el servidor de correo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.completedFuture(false);
        }
        log.info("Comprobante enviado a {} -> placa {}, tiempo {}, total ${}",
                email, registro.placa(), registro.tiempoPermanencia(), registro.valorPagado());
        return CompletableFuture.completedFuture(true);
    }
}
