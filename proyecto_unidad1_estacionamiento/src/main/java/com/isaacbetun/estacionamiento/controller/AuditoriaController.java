package com.isaacbetun.estacionamiento.controller;

import com.isaacbetun.estacionamiento.aspect.AuditoriaAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Permite ver las metricas que va acumulando el aspecto de auditoria. */
@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaAspect auditoriaAspect;

    @GetMapping("/metricas")
    public Map<String, Map<String, Object>> metricas() {
        return auditoriaAspect.obtenerMetricas();
    }
}
