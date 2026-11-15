package com.isaacbetun.estacionamiento.exception;

/** El espacio solicitado esta ocupado, en mantenimiento o no hay espacios libres (HTTP 409). */
public class EspacioNoDisponibleException extends RuntimeException {

    public EspacioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
