package com.isaacbetun.estacionamiento.exception;

/** Operacion que viola una regla de negocio del estacionamiento (HTTP 422). */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
