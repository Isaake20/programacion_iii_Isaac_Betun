package com.isaacbetun.estacionamiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class EstacionamientoApplication {

    public static void main(String[] args) {
        // Toda la aplicacion trabaja con la hora de Ecuador (fechas de entrada/salida y errores)
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guayaquil"));
        SpringApplication.run(EstacionamientoApplication.class, args);
    }
}
