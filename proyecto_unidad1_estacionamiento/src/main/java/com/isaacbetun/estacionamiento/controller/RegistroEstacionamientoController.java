package com.isaacbetun.estacionamiento.controller;

import com.isaacbetun.estacionamiento.dto.IngresoRequest;
import com.isaacbetun.estacionamiento.dto.RegistroResponse;
import com.isaacbetun.estacionamiento.service.RegistroEstacionamientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registros")
@RequiredArgsConstructor
public class RegistroEstacionamientoController {

    private final RegistroEstacionamientoService registroService;

    @PostMapping("/ingreso")
    public ResponseEntity<RegistroResponse> registrarIngreso(@Valid @RequestBody IngresoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroService.registrarIngreso(request));
    }

    @PatchMapping("/{id}/salida")
    public RegistroResponse registrarSalida(@PathVariable Long id) {
        return registroService.registrarSalida(id);
    }

    @GetMapping("/{id}")
    public RegistroResponse obtener(@PathVariable Long id) {
        return registroService.obtener(id);
    }

    @GetMapping("/activos")
    public List<RegistroResponse> activos() {
        return registroService.listarActivos();
    }

    @GetMapping("/vehiculo/{placa}")
    public List<RegistroResponse> historialPorPlaca(@PathVariable String placa) {
        return registroService.historialPorPlaca(placa);
    }

    @GetMapping
    public List<RegistroResponse> buscarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return registroService.buscarPorFechas(desde, hasta);
    }

    @GetMapping("/recaudacion")
    public Map<String, Object> recaudacion(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        BigDecimal total = registroService.totalRecaudado(desde, hasta);
        return Map.of("desde", desde, "hasta", hasta, "totalRecaudado", total);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        registroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
