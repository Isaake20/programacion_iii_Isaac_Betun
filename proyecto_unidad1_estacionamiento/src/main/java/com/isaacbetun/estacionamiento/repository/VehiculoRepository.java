package com.isaacbetun.estacionamiento.repository;

import com.isaacbetun.estacionamiento.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    Optional<Vehiculo> findByPlacaIgnoreCase(String placa);

    boolean existsByPlacaIgnoreCase(String placa);

    List<Vehiculo> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);
}
