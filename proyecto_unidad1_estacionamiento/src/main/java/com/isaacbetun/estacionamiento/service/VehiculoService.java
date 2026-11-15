package com.isaacbetun.estacionamiento.service;

import com.isaacbetun.estacionamiento.dto.VehiculoRequest;
import com.isaacbetun.estacionamiento.dto.VehiculoResponse;
import com.isaacbetun.estacionamiento.entity.EstadoRegistro;
import com.isaacbetun.estacionamiento.entity.Usuario;
import com.isaacbetun.estacionamiento.entity.Vehiculo;
import com.isaacbetun.estacionamiento.exception.RecursoDuplicadoException;
import com.isaacbetun.estacionamiento.exception.RecursoNoEncontradoException;
import com.isaacbetun.estacionamiento.exception.ReglaNegocioException;
import com.isaacbetun.estacionamiento.mapper.EstacionamientoMapper;
import com.isaacbetun.estacionamiento.repository.RegistroEstacionamientoRepository;
import com.isaacbetun.estacionamiento.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final RegistroEstacionamientoRepository registroRepository;
    private final UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public List<VehiculoResponse> listar() {
        return vehiculoRepository.findAll().stream().map(EstacionamientoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public VehiculoResponse obtener(Long id) {
        return EstacionamientoMapper.toResponse(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public VehiculoResponse buscarPorPlaca(String placa) {
        return EstacionamientoMapper.toResponse(buscarPorPlacaEntidad(placa));
    }

    @Transactional(readOnly = true)
    public List<VehiculoResponse> listarPorUsuario(Long usuarioId) {
        usuarioService.buscarEntidad(usuarioId); // valida que exista
        return vehiculoRepository.findByUsuarioId(usuarioId).stream()
                .map(EstacionamientoMapper::toResponse).toList();
    }

    @Transactional
    public VehiculoResponse crear(VehiculoRequest req) {
        String placa = normalizarPlaca(req.placa());
        // RN: la placa es unica en el sistema
        if (vehiculoRepository.existsByPlacaIgnoreCase(placa)) {
            throw new RecursoDuplicadoException("Ya existe un vehiculo registrado con la placa " + placa);
        }
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        copiarDatos(req, vehiculo);
        return EstacionamientoMapper.toResponse(vehiculoRepository.save(vehiculo));
    }

    @Transactional
    public VehiculoResponse actualizar(Long id, VehiculoRequest req) {
        Vehiculo vehiculo = buscarEntidad(id);
        String placa = normalizarPlaca(req.placa());
        if (!vehiculo.getPlaca().equalsIgnoreCase(placa) && vehiculoRepository.existsByPlacaIgnoreCase(placa)) {
            throw new RecursoDuplicadoException("Ya existe un vehiculo registrado con la placa " + placa);
        }
        if (vehiculo.getTipo() != req.tipo()
                && registroRepository.existsByVehiculoIdAndEstado(id, EstadoRegistro.EN_CURSO)) {
            throw new ReglaNegocioException("No se puede cambiar el tipo de un vehiculo que esta dentro del estacionamiento");
        }
        vehiculo.setPlaca(placa);
        copiarDatos(req, vehiculo);
        return EstacionamientoMapper.toResponse(vehiculoRepository.save(vehiculo));
    }

    @Transactional
    public void eliminar(Long id) {
        Vehiculo vehiculo = buscarEntidad(id);
        if (registroRepository.existsByVehiculoIdAndEstado(id, EstadoRegistro.EN_CURSO)) {
            throw new ReglaNegocioException("No se puede eliminar un vehiculo que se encuentra dentro del estacionamiento");
        }
        if (registroRepository.existsByVehiculoId(id)) {
            throw new ReglaNegocioException("No se puede eliminar el vehiculo porque tiene historial de ingresos");
        }
        vehiculoRepository.delete(vehiculo);
    }

    Vehiculo buscarEntidad(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehiculo", id));
    }

    Vehiculo buscarPorPlacaEntidad(String placa) {
        String normalizada = normalizarPlaca(placa);
        return vehiculoRepository.findByPlacaIgnoreCase(normalizada)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehiculo con placa", normalizada));
    }

    private void copiarDatos(VehiculoRequest req, Vehiculo vehiculo) {
        Usuario propietario = usuarioService.buscarEntidad(req.usuarioId());
        vehiculo.setMarca(req.marca().trim());
        vehiculo.setModelo(req.modelo().trim());
        vehiculo.setColor(req.color());
        vehiculo.setTipo(req.tipo());
        vehiculo.setUsuario(propietario);
    }

    /** Guarda todas las placas en mayusculas y con guion: pba1234 -> PBA-1234 */
    static String normalizarPlaca(String placa) {
        String p = placa.trim().toUpperCase().replace(" ", "");
        if (!p.contains("-")) {
            p = p.replaceFirst("^([A-Z]{2,3})(\\d)", "$1-$2");
        }
        return p;
    }
}
