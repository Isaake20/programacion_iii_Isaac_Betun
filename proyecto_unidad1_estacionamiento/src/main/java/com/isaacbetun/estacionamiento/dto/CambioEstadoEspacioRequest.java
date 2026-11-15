package com.isaacbetun.estacionamiento.dto;

import com.isaacbetun.estacionamiento.entity.EstadoEspacio;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoEspacioRequest(
        @NotNull(message = "El nuevo estado es obligatorio (DISPONIBLE o MANTENIMIENTO)")
        EstadoEspacio estado
) {
}
