package com.isaacbetun.estacionamiento.dto;

import java.util.Map;

public record DisponibilidadResponse(
        long totalEspacios,
        long disponibles,
        long ocupados,
        long enMantenimiento,
        double porcentajeOcupacion,
        Map<String, Long> disponiblesPorTipo
) {
}
