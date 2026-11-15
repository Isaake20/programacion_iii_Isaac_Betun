package com.isaacbetun.estacionamiento.interceptor;

import com.isaacbetun.estacionamiento.exception.AccesoDenegadoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Control basico de acceso: las operaciones DELETE solo las puede
 * hacer un administrador, que se identifica con la cabecera X-Rol: ADMIN.
 * (Es una version simplificada, no reemplaza a Spring Security.)
 */
@Component
public class ControlAccesoInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ControlAccesoInterceptor.class);
    public static final String CABECERA_ROL = "X-Rol";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("DELETE".equalsIgnoreCase(request.getMethod())) {
            String rol = request.getHeader(CABECERA_ROL);
            if (!"ADMIN".equalsIgnoreCase(rol)) {
                log.warn("Acceso denegado a DELETE {} (rol recibido: {})", request.getRequestURI(), rol);
                throw new AccesoDenegadoException(
                        "Solo un administrador puede eliminar registros. Envie la cabecera X-Rol: ADMIN");
            }
        }
        return true;
    }
}
