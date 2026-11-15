package com.isaacbetun.estacionamiento.service;

import com.isaacbetun.estacionamiento.dto.CambioEstadoEspacioRequest;
import com.isaacbetun.estacionamiento.dto.DisponibilidadResponse;
import com.isaacbetun.estacionamiento.dto.EspacioRequest;
import com.isaacbetun.estacionamiento.dto.EspacioResponse;
import com.isaacbetun.estacionamiento.entity.Espacio;
import com.isaacbetun.estacionamiento.entity.EstadoEspacio;
import com.isaacbetun.estacionamiento.entity.TipoVehiculo;
import com.isaacbetun.estacionamiento.exception.RecursoDuplicadoException;
import com.isaacbetun.estacionamiento.exception.RecursoNoEncontradoException;
import com.isaacbetun.estacionamiento.exception.ReglaNegocioException;
import com.isaacbetun.estacionamiento.mapper.EstacionamientoMapper;
import com.isaacbetun.estacionamiento.repository.EspacioRepository;
import com.isaacbetun.estacionamiento.repository.RegistroEstacionamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EspacioService {

    private final EspacioRepository espacioRepository;
    private final RegistroEstacionamientoRepository registroRepository;

    @Transactional(readOnly = true)
    public List<EspacioResponse> listar() {
        return espacioRepository.findAll().stream().map(EstacionamientoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EspacioResponse obtener(Long id) {
        return EstacionamientoMapper.toResponse(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public List<EspacioResponse> listarDisponibles(TipoVehiculo tipo) {
        List<Espacio> espacios = (tipo == null)
                ? espacioRepository.findByEstadoOrderByCodigoAsc(EstadoEspacio.DISPONIBLE)
                : espacioRepository.findByEstadoAndTipoVehiculoOrderByCodigoAsc(EstadoEspacio.DISPONIBLE, tipo);
        return espacios.stream().map(EstacionamientoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DisponibilidadResponse resumenDisponibilidad() {
        long total = espacioRepository.count();
        long disponibles = espacioRepository.countByEstado(EstadoEspacio.DISPONIBLE);
        long ocupados = espacioRepository.countByEstado(EstadoEspacio.OCUPADO);
        long mantenimiento = espacioRepository.countByEstado(EstadoEspacio.MANTENIMIENTO);
        double porcentaje = total == 0 ? 0 : Math.round(ocupados * 10000.0 / total) / 100.0;

        Map<String, Long> porTipo = new LinkedHashMap<>();
        for (TipoVehiculo tipo : TipoVehiculo.values()) {
            porTipo.put(tipo.name(), (long) espacioRepository
                    .findByEstadoAndTipoVehiculoOrderByCodigoAsc(EstadoEspacio.DISPONIBLE, tipo).size());
        }
        return new DisponibilidadResponse(total, disponibles, ocupados, mantenimiento, porcentaje, porTipo);
    }

    @Transactional
    public EspacioResponse crear(EspacioRequest req) {
        String codigo = req.codigo().trim().toUpperCase();
        if (espacioRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new RecursoDuplicadoException("Ya existe un espacio con el codigo " + codigo);
        }
        Espacio espacio = new Espacio();
        espacio.setCodigo(codigo);
        espacio.setPiso(req.piso());
        espacio.setTipoVehiculo(req.tipoVehiculo());
        espacio.setTarifaHora(req.tarifaHora());
        espacio.setEstado(EstadoEspacio.DISPONIBLE);
        return EstacionamientoMapper.toResponse(espacioRepository.save(espacio));
    }

    @Transactional
    public EspacioResponse actualizar(Long id, EspacioRequest req) {
        Espacio espacio = buscarEntidad(id);
        String codigo = req.codigo().trim().toUpperCase();
        if (!espacio.getCodigo().equalsIgnoreCase(codigo) && espacioRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new RecursoDuplicadoException("Ya existe un espacio con el codigo " + codigo);
        }
        if (espacio.getEstado() == EstadoEspacio.OCUPADO && espacio.getTipoVehiculo() != req.tipoVehiculo()) {
            throw new ReglaNegocioException("No se puede cambiar el tipo de un espacio que esta ocupado");
        }
        espacio.setCodigo(codigo);
        espacio.setPiso(req.piso());
        espacio.setTipoVehiculo(req.tipoVehiculo());
        espacio.setTarifaHora(req.tarifaHora());
        return EstacionamientoMapper.toResponse(espacioRepository.save(espacio));
    }

    /**
     * Permite poner un espacio en mantenimiento o volverlo a habilitar.
     * El estado OCUPADO solo lo maneja el sistema con los ingresos y salidas.
     */
    @Transactional
    public EspacioResponse cambiarEstado(Long id, CambioEstadoEspacioRequest req) {
        Espacio espacio = buscarEntidad(id);
        if (req.estado() == EstadoEspacio.OCUPADO) {
            throw new ReglaNegocioException("El estado OCUPADO se asigna automaticamente al registrar un ingreso");
        }
        if (espacio.getEstado() == EstadoEspacio.OCUPADO) {
            throw new ReglaNegocioException("El espacio " + espacio.getCodigo()
                    + " esta ocupado. Registre la salida del vehiculo antes de cambiar su estado");
        }
        espacio.setEstado(req.estado());
        return EstacionamientoMapper.toResponse(espacioRepository.save(espacio));
    }

    @Transactional
    public void eliminar(Long id) {
        Espacio espacio = buscarEntidad(id);
        if (espacio.getEstado() == EstadoEspacio.OCUPADO) {
            throw new ReglaNegocioException("No se puede eliminar el espacio " + espacio.getCodigo() + " porque esta ocupado");
        }
        if (registroRepository.existsByEspacioId(id)) {
            throw new ReglaNegocioException("El espacio " + espacio.getCodigo()
                    + " tiene historial de registros. Pongalo en MANTENIMIENTO en lugar de eliminarlo");
        }
        espacioRepository.delete(espacio);
    }

    Espacio buscarEntidad(Long id) {
        return espacioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Espacio", id));
    }
}
