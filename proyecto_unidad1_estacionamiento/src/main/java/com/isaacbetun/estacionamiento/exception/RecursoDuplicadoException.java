package com.isaacbetun.estacionamiento.exception;

/** Se lanza cuando se intenta registrar un dato unico que ya existe (HTTP 409). */
public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
