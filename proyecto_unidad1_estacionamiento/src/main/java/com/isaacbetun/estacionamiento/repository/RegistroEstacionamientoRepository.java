package com.isaacbetun.estacionamiento.repository;

import com.isaacbetun.estacionamiento.entity.EstadoRegistro;
import com.isaacbetun.estacionamiento.entity.RegistroEstacionamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RegistroEstacionamientoRepository extends JpaRepository<RegistroEstacionamiento, Long> {

    boolean existsByVehiculoIdAndEstado(Long vehiculoId, EstadoRegistro estado);

    boolean existsByVehiculoId(Long vehiculoId);

    boolean existsByEspacioId(Long espacioId);

    List<RegistroEstacionamiento> findByEstadoOrderByFechaEntradaAsc(EstadoRegistro estado);

    // Historial de un vehiculo por su placa (JPQL con JOIN)
    @Query("""
            SELECT r FROM RegistroEstacionamiento r
            JOIN r.vehiculo v
            WHERE UPPER(v.placa) = UPPER(:placa)
            ORDER BY r.fechaEntrada DESC
            """)
    List<RegistroEstacionamiento> historialPorPlaca(@Param("placa") String placa);

    // Registros cuya entrada esta dentro de un rango de fechas
    @Query("""
            SELECT r FROM RegistroEstacionamiento r
            WHERE r.fechaEntrada BETWEEN :desde AND :hasta
            ORDER BY r.fechaEntrada
            """)
    List<RegistroEstacionamiento> buscarPorRangoDeFechas(@Param("desde") LocalDateTime desde,
                                                         @Param("hasta") LocalDateTime hasta);

    // Total recaudado en un rango de fechas de salida
    @Query("""
            SELECT COALESCE(SUM(r.valorPagado), 0) FROM RegistroEstacionamiento r
            WHERE r.estado = com.isaacbetun.estacionamiento.entity.EstadoRegistro.FINALIZADO
              AND r.fechaSalida BETWEEN :desde AND :hasta
            """)
    BigDecimal totalRecaudado(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
}
