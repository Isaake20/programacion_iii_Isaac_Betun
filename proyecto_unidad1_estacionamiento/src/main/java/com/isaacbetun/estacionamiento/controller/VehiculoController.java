package com.isaacbetun.estacionamiento.controller;

import com.isaacbetun.estacionamiento.dto.VehiculoRequest;
import com.isaacbetun.estacionamiento.dto.VehiculoResponse;
import com.isaacbetun.estacionamiento.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping
    public List<VehiculoResponse> listar() {
        return vehiculoService.listar();
    }

    @GetMapping("/{id}")
    public VehiculoResponse obtener(@PathVariable Long id) {
        return vehiculoService.obtener(id);
    }

    @GetMapping("/placa/{placa}")
    public VehiculoResponse buscarPorPlaca(@PathVariable String placa) {
        return vehiculoService.buscarPorPlaca(placa);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<VehiculoResponse> listarPorUsuario(@PathVariable Long usuarioId) {
        return vehiculoService.listarPorUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<VehiculoResponse> crear(@Valid @RequestBody VehiculoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoService.crear(request));
    }

    @PutMapping("/{id}")
    public VehiculoResponse actualizar(@PathVariable Long id, @Valid @RequestBody VehiculoRequest request) {
        return vehiculoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
