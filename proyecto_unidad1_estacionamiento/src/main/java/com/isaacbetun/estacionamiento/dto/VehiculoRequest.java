package com.isaacbetun.estacionamiento.dto;

import com.isaacbetun.estacionamiento.entity.TipoVehiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VehiculoRequest(
        // Formato de placa ecuatoriana: ABC-1234 (autos) o AB-123C (motos)
        @NotBlank(message = "La placa es obligatoria")
        @Pattern(regexp = "^[A-Za-z]{2,3}-?\\d{3,4}[A-Za-z]?$", message = "La placa no tiene un formato valido (ej: PBA-1234)")
        String placa,

        @NotBlank(message = "La marca es obligatoria")
        @Size(max = 40)
        String marca,

        @NotBlank(message = "El modelo es obligatorio")
        @Size(max = 40)
        String modelo,

        @Size(max = 30)
        String color,

        @NotNull(message = "El tipo de vehiculo es obligatorio (AUTO, MOTO, CAMIONETA)")
        TipoVehiculo tipo,

        @NotNull(message = "El id del usuario propietario es obligatorio")
        Long usuarioId
) {
}
