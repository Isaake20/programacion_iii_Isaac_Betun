package com.isaacbetun.estacionamiento.dto;

import com.isaacbetun.estacionamiento.entity.TipoVehiculo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record EspacioRequest(
        @NotBlank(message = "El codigo del espacio es obligatorio")
        @Size(max = 10)
        String codigo,

        @NotNull(message = "El piso es obligatorio")
        @Min(value = -3, message = "El piso minimo es -3")
        @Max(value = 10, message = "El piso maximo es 10")
        Integer piso,

        @NotNull(message = "El tipo de vehiculo permitido es obligatorio")
        TipoVehiculo tipoVehiculo,

        @NotNull(message = "La tarifa por hora es obligatoria")
        @DecimalMin(value = "0.25", message = "La tarifa minima es 0.25")
        BigDecimal tarifaHora
) {
}
