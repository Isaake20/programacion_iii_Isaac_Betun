package com.isaacbetun.estacionamiento.exception;

/** La lanza el interceptor cuando falta la cabecera de rol requerida (HTTP 403). */
public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
