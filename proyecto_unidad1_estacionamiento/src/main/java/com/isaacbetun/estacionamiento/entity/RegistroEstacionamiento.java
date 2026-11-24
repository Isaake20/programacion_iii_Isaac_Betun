package com.isaacbetun.estacionamiento.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Registro de entrada y salida de un vehiculo en un espacio.
 * Mientras el vehiculo esta dentro, fechaSalida es null y el estado es EN_CURSO.
 */
@Entity
@Table(name = "registros_estacionamiento")
@Getter
@Setter
@NoArgsConstructor
public class RegistroEstacionamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_registro_vehiculo"))
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "espacio_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_registro_espacio"))
    private Espacio espacio;

    @Column(name = "fecha_entrada", nullable = false)
    private LocalDateTime fechaEntrada;

    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;

    @Column(name = "minutos_permanencia")
    private Long minutosPermanencia;

    @Column(name = "valor_pagado", precision = 8, scale = 2)
    private BigDecimal valorPagado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoRegistro estado = EstadoRegistro.EN_CURSO;

    @Column(length = 200)
    private String observacion;
}
