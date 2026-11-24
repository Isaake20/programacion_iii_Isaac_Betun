package com.isaacbetun.estacionamiento.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "La cedula es obligatoria")
        @Pattern(regexp = "\\d{10}", message = "La cedula debe tener 10 digitos")
        String cedula,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 80)
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 80)
        String apellidos,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        String email,

        @Pattern(regexp = "\\d{7,10}", message = "El telefono debe tener entre 7 y 10 digitos")
        String telefono,

        Boolean activo
) {
}
