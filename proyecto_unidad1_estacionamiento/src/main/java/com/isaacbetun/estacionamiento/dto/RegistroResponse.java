package com.isaacbetun.estacionamiento.dto;

import com.isaacbetun.estacionamiento.entity.EstadoRegistro;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RegistroResponse(
        Long id,
        String placa,
        String tipoVehiculo,
        String codigoEspacio,
        LocalDateTime fechaEntrada,
        LocalDateTime fechaSalida,
        Long minutosPermanencia,
        String tiempoPermanencia,
        BigDecimal valorPagado,
        EstadoRegistro estado,
        String observacion
) {
}
