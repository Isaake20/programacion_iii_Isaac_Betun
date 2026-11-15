package com.isaacbetun.estacionamiento.exception;

/** Se lanza cuando se busca un registro que no existe (HTTP 404). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Object identificador) {
        super(recurso + " con identificador '" + identificador + "' no existe");
    }
}
