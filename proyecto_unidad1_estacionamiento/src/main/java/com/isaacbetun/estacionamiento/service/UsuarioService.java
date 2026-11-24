package com.isaacbetun.estacionamiento.service;

import com.isaacbetun.estacionamiento.dto.UsuarioRequest;
import com.isaacbetun.estacionamiento.dto.UsuarioResponse;
import com.isaacbetun.estacionamiento.entity.Usuario;
import com.isaacbetun.estacionamiento.exception.RecursoDuplicadoException;
import com.isaacbetun.estacionamiento.exception.RecursoNoEncontradoException;
import com.isaacbetun.estacionamiento.exception.ReglaNegocioException;
import com.isaacbetun.estacionamiento.mapper.EstacionamientoMapper;
import com.isaacbetun.estacionamiento.repository.UsuarioRepository;
import com.isaacbetun.estacionamiento.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar(boolean soloActivos) {
        List<Usuario> usuarios = soloActivos
                ? usuarioRepository.findByActivoTrueOrderByApellidosAsc()
                : usuarioRepository.findAll();
        return usuarios.stream().map(EstacionamientoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(Long id) {
        return EstacionamientoMapper.toResponse(buscarEntidad(id));
    }

    @Transactional
    public UsuarioResponse crear(UsuarioRequest req) {
        if (usuarioRepository.existsByCedula(req.cedula())) {
            throw new RecursoDuplicadoException("Ya existe un usuario con la cedula " + req.cedula());
        }
        if (usuarioRepository.existsByEmailIgnoreCase(req.email())) {
            throw new RecursoDuplicadoException("Ya existe un usuario con el email " + req.email());
        }
        Usuario usuario = new Usuario();
        copiarDatos(req, usuario);
        return EstacionamientoMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioRequest req) {
        Usuario usuario = buscarEntidad(id);
        if (!usuario.getCedula().equals(req.cedula()) && usuarioRepository.existsByCedula(req.cedula())) {
            throw new RecursoDuplicadoException("Ya existe un usuario con la cedula " + req.cedula());
        }
        if (!usuario.getEmail().equalsIgnoreCase(req.email()) && usuarioRepository.existsByEmailIgnoreCase(req.email())) {
            throw new RecursoDuplicadoException("Ya existe un usuario con el email " + req.email());
        }
        copiarDatos(req, usuario);
        return EstacionamientoMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = buscarEntidad(id);
        // RN: no se puede borrar un usuario que todavia tiene vehiculos registrados
        if (vehiculoRepository.existsByUsuarioId(usuario.getId())) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el usuario porque tiene vehiculos registrados. Elimine o reasigne sus vehiculos primero");
        }
        usuarioRepository.delete(usuario);
    }

    Usuario buscarEntidad(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
    }

    private void copiarDatos(UsuarioRequest req, Usuario usuario) {
        usuario.setCedula(req.cedula());
        usuario.setNombres(req.nombres().trim());
        usuario.setApellidos(req.apellidos().trim());
        usuario.setEmail(req.email().trim().toLowerCase());
        usuario.setTelefono(req.telefono());
        if (req.activo() != null) {
            usuario.setActivo(req.activo());
        }
    }
}
