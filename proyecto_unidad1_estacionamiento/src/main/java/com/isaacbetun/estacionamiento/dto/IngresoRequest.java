package com.isaacbetun.estacionamiento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para registrar el ingreso de un vehiculo.
 * Si no se envia espacioId el sistema asigna el primer espacio libre compatible.
 */
public record IngresoRequest(
        @NotBlank(message = "La placa del vehiculo es obligatoria")
        String placa,

        Long espacioId,

        @Size(max = 200)
        String observacion
) {
}
