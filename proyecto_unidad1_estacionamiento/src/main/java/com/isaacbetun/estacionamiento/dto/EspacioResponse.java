package com.isaacbetun.estacionamiento.dto;

import com.isaacbetun.estacionamiento.entity.EstadoEspacio;
import com.isaacbetun.estacionamiento.entity.TipoVehiculo;

import java.math.BigDecimal;

public record EspacioResponse(
        Long id,
        String codigo,
        Integer piso,
        TipoVehiculo tipoVehiculo,
        EstadoEspacio estado,
        BigDecimal tarifaHora
) {
}
