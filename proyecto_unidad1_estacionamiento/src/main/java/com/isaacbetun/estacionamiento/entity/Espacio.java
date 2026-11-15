package com.isaacbetun.estacionamiento.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Plaza fisica del estacionamiento (ej: A-01).
 */
@Entity
@Table(name = "espacios")
@Getter
@Setter
@NoArgsConstructor
public class Espacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false)
    private Integer piso;

    /** Tipo de vehiculo que puede ocupar el espacio */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo", nullable = false, length = 15)
    private TipoVehiculo tipoVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoEspacio estado = EstadoEspacio.DISPONIBLE;

    @Column(name = "tarifa_hora", nullable = false, precision = 6, scale = 2)
    private BigDecimal tarifaHora;
}
