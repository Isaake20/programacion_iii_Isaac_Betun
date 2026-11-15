package com.isaacbetun.estacionamiento.repository;

import com.isaacbetun.estacionamiento.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCedula(String cedula);

    boolean existsByCedula(String cedula);

    boolean existsByEmailIgnoreCase(String email);

    List<Usuario> findByActivoTrueOrderByApellidosAsc();
}
