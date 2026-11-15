package com.isaacbetun.estacionamiento.mapper;

import com.isaacbetun.estacionamiento.dto.*;
import com.isaacbetun.estacionamiento.entity.*;

/**
 * Conversiones entre entidades y DTO para no exponer las entidades JPA en los controladores.
 */
public final class EstacionamientoMapper {

    private EstacionamientoMapper() {
    }

    public static UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getCedula(), u.getNombres(), u.getApellidos(),
                u.getEmail(), u.getTelefono(), u.isActivo(), u.getFechaRegistro(),
                u.getVehiculos() == null ? 0 : u.getVehiculos().size());
    }

    public static VehiculoResponse toResponse(Vehiculo v) {
        Usuario u = v.getUsuario();
        return new VehiculoResponse(v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(), v.getColor(),
                v.getTipo(), u.getId(), u.getNombres() + " " + u.getApellidos());
    }

    public static EspacioResponse toResponse(Espacio e) {
        return new EspacioResponse(e.getId(), e.getCodigo(), e.getPiso(), e.getTipoVehiculo(),
                e.getEstado(), e.getTarifaHora());
    }

    public static RegistroResponse toResponse(RegistroEstacionamiento r) {
        return new RegistroResponse(
                r.getId(),
                r.getVehiculo().getPlaca(),
                r.getVehiculo().getTipo().name(),
                r.getEspacio().getCodigo(),
                r.getFechaEntrada(),
                r.getFechaSalida(),
                r.getMinutosPermanencia(),
                formatearTiempo(r.getMinutosPermanencia()),
                r.getValorPagado(),
                r.getEstado(),
                r.getObservacion());
    }

    private static String formatearTiempo(Long minutos) {
        if (minutos == null) {
            return null;
        }
        return String.format("%d h %02d min", minutos / 60, minutos % 60);
    }
}
