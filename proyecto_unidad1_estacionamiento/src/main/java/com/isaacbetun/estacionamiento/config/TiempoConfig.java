package com.isaacbetun.estacionamiento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Reloj de la aplicacion. Se inyecta en los servicios para poder
 * controlar la hora en las pruebas (calculo de permanencia y tarifa).
 */
@Configuration
public class TiempoConfig {

    @Bean
    public Clock reloj() {
        return Clock.systemDefaultZone();
    }
}
