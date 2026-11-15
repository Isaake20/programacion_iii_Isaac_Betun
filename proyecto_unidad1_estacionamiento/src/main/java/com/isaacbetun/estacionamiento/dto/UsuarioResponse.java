package com.isaacbetun.estacionamiento.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String cedula,
        String nombres,
        String apellidos,
        String email,
        String telefono,
        boolean activo,
        LocalDateTime fechaRegistro,
        int totalVehiculos
) {
}
