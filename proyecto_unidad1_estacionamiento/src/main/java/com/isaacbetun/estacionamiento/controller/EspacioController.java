package com.isaacbetun.estacionamiento.controller;

import com.isaacbetun.estacionamiento.dto.CambioEstadoEspacioRequest;
import com.isaacbetun.estacionamiento.dto.DisponibilidadResponse;
import com.isaacbetun.estacionamiento.dto.EspacioRequest;
import com.isaacbetun.estacionamiento.dto.EspacioResponse;
import com.isaacbetun.estacionamiento.entity.TipoVehiculo;
import com.isaacbetun.estacionamiento.service.EspacioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/espacios")
@RequiredArgsConstructor
public class EspacioController {

    private final EspacioService espacioService;

    @GetMapping
    public List<EspacioResponse> listar() {
        return espacioService.listar();
    }

    @GetMapping("/{id}")
    public EspacioResponse obtener(@PathVariable Long id) {
        return espacioService.obtener(id);
    }

    @GetMapping("/disponibles")
    public List<EspacioResponse> disponibles(@RequestParam(required = false) TipoVehiculo tipo) {
        return espacioService.listarDisponibles(tipo);
    }

    @GetMapping("/disponibilidad")
    public DisponibilidadResponse resumenDisponibilidad() {
        return espacioService.resumenDisponibilidad();
    }

    @PostMapping
    public ResponseEntity<EspacioResponse> crear(@Valid @RequestBody EspacioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(espacioService.crear(request));
    }

    @PutMapping("/{id}")
    public EspacioResponse actualizar(@PathVariable Long id, @Valid @RequestBody EspacioRequest request) {
        return espacioService.actualizar(id, request);
    }

    @PatchMapping("/{id}/estado")
    public EspacioResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambioEstadoEspacioRequest request) {
        return espacioService.cambiarEstado(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        espacioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
