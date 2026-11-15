package com.isaacbetun.estacionamiento.dto;

import com.isaacbetun.estacionamiento.entity.TipoVehiculo;

public record VehiculoResponse(
        Long id,
        String placa,
        String marca,
        String modelo,
        String color,
        TipoVehiculo tipo,
        Long usuarioId,
        String propietario
) {
}
