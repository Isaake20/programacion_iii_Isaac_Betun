package com.isaacbetun.estacionamiento.config;

import com.isaacbetun.estacionamiento.interceptor.ControlAccesoInterceptor;
import com.isaacbetun.estacionamiento.interceptor.RegistroSolicitudInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RegistroSolicitudInterceptor registroSolicitudInterceptor;
    private final ControlAccesoInterceptor controlAccesoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // El orden importa: primero se registra la solicitud y luego se valida el acceso
        registry.addInterceptor(registroSolicitudInterceptor).addPathPatterns("/api/**").order(1);
        registry.addInterceptor(controlAccesoInterceptor).addPathPatterns("/api/**").order(2);
    }
}
