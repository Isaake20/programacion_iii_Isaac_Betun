package com.isaacbetun.estacionamiento.repository;

import com.isaacbetun.estacionamiento.entity.Espacio;
import com.isaacbetun.estacionamiento.entity.EstadoEspacio;
import com.isaacbetun.estacionamiento.entity.TipoVehiculo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EspacioRepository extends JpaRepository<Espacio, Long> {

    boolean existsByCodigoIgnoreCase(String codigo);

    // Consulta derivada: espacios libres para un tipo de vehiculo
    List<Espacio> findByEstadoAndTipoVehiculoOrderByCodigoAsc(EstadoEspacio estado, TipoVehiculo tipo);

    List<Espacio> findByEstadoOrderByCodigoAsc(EstadoEspacio estado);

    Optional<Espacio> findFirstByEstadoAndTipoVehiculoOrderByPisoAscCodigoAsc(EstadoEspacio estado, TipoVehiculo tipo);

    long countByEstado(EstadoEspacio estado);

    // Bloquea la fila mientras se registra un ingreso para que dos
    // solicitudes al mismo tiempo no ocupen el mismo espacio
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Espacio e WHERE e.id = :id")
    Optional<Espacio> buscarParaActualizar(@Param("id") Long id);
}
