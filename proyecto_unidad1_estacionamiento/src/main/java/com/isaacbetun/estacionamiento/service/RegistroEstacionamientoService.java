package com.isaacbetun.estacionamiento.service;

import com.isaacbetun.estacionamiento.dto.IngresoRequest;
import com.isaacbetun.estacionamiento.dto.RegistroResponse;
import com.isaacbetun.estacionamiento.entity.*;
import com.isaacbetun.estacionamiento.exception.EspacioNoDisponibleException;
import com.isaacbetun.estacionamiento.exception.RecursoNoEncontradoException;
import com.isaacbetun.estacionamiento.exception.ReglaNegocioException;
import com.isaacbetun.estacionamiento.mapper.EstacionamientoMapper;
import com.isaacbetun.estacionamiento.repository.EspacioRepository;
import com.isaacbetun.estacionamiento.repository.RegistroEstacionamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Logica principal: control de ingreso y salida de vehiculos.
 */
@Service
@RequiredArgsConstructor
public class RegistroEstacionamientoService {

    private final RegistroEstacionamientoRepository registroRepository;
    private final EspacioRepository espacioRepository;
    private final VehiculoService vehiculoService;
    private final NotificacionService notificacionService;
    private final Clock reloj;

    @Value("${app.estacionamiento.minutos-gracia:10}")
    private long minutosGracia;

    @Transactional
    public RegistroResponse registrarIngreso(IngresoRequest req) {
        Vehiculo vehiculo = vehiculoService.buscarPorPlacaEntidad(req.placa());

        // RN1: el propietario debe estar activo
        if (!vehiculo.getUsuario().isActivo()) {
            throw new ReglaNegocioException("El propietario del vehiculo " + vehiculo.getPlaca() + " esta inactivo");
        }
        // RN2: un vehiculo no puede ingresar dos veces sin haber salido
        if (registroRepository.existsByVehiculoIdAndEstado(vehiculo.getId(), EstadoRegistro.EN_CURSO)) {
            throw new ReglaNegocioException("El vehiculo " + vehiculo.getPlaca() + " ya se encuentra dentro del estacionamiento");
        }

        Espacio espacio = (req.espacioId() != null)
                ? obtenerEspacioSolicitado(req.espacioId(), vehiculo)
                : asignarEspacioAutomatico(vehiculo);

        espacio.setEstado(EstadoEspacio.OCUPADO);
        espacioRepository.save(espacio);

        RegistroEstacionamiento registro = new RegistroEstacionamiento();
        registro.setVehiculo(vehiculo);
        registro.setEspacio(espacio);
        registro.setFechaEntrada(LocalDateTime.now(reloj));
        registro.setEstado(EstadoRegistro.EN_CURSO);
        registro.setObservacion(req.observacion());

        return EstacionamientoMapper.toResponse(registroRepository.save(registro));
    }

    @Transactional
    public RegistroResponse registrarSalida(Long registroId) {
        RegistroEstacionamiento registro = buscarEntidad(registroId);

        // RN4: no se puede cerrar dos veces el mismo registro
        if (registro.getEstado() == EstadoRegistro.FINALIZADO) {
            throw new ReglaNegocioException("El registro " + registroId + " ya tiene registrada la salida");
        }

        LocalDateTime salida = LocalDateTime.now(reloj);
        long minutos = Duration.between(registro.getFechaEntrada(), salida).toMinutes();

        registro.setFechaSalida(salida);
        registro.setMinutosPermanencia(minutos);
        registro.setValorPagado(calcularValor(minutos, registro.getEspacio().getTarifaHora()));
        registro.setEstado(EstadoRegistro.FINALIZADO);

        Espacio espacio = registro.getEspacio();
        espacio.setEstado(EstadoEspacio.DISPONIBLE);
        espacioRepository.save(espacio);

        RegistroResponse respuesta = EstacionamientoMapper.toResponse(registroRepository.save(registro));

        // Envio del comprobante: es lento (simula correo), por eso se ejecuta de forma asincrona
        notificacionService.enviarComprobanteSalida(respuesta, registro.getVehiculo().getUsuario().getEmail());

        return respuesta;
    }

    @Transactional(readOnly = true)
    public RegistroResponse obtener(Long id) {
        return EstacionamientoMapper.toResponse(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public List<RegistroResponse> listarActivos() {
        return registroRepository.findByEstadoOrderByFechaEntradaAsc(EstadoRegistro.EN_CURSO).stream()
                .map(EstacionamientoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<RegistroResponse> historialPorPlaca(String placa) {
        vehiculoService.buscarPorPlacaEntidad(placa); // 404 si la placa no existe
        return registroRepository.historialPorPlaca(VehiculoService.normalizarPlaca(placa)).stream()
                .map(EstacionamientoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<RegistroResponse> buscarPorFechas(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);
        return registroRepository.buscarPorRangoDeFechas(desde.atStartOfDay(), hasta.atTime(23, 59, 59)).stream()
                .map(EstacionamientoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal totalRecaudado(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);
        return registroRepository.totalRecaudado(desde.atStartOfDay(), hasta.atTime(23, 59, 59));
    }

    @Transactional
    public void eliminar(Long id) {
        RegistroEstacionamiento registro = buscarEntidad(id);
        if (registro.getEstado() == EstadoRegistro.EN_CURSO) {
            throw new ReglaNegocioException("No se puede eliminar un registro en curso, primero registre la salida");
        }
        registroRepository.delete(registro);
    }

    /**
     * RN5: tarifa por hora o fraccion. Los primeros minutos de gracia no se cobran.
     */
    BigDecimal calcularValor(long minutos, BigDecimal tarifaHora) {
        if (minutos <= minutosGracia) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        long horasCobradas = (long) Math.ceil(minutos / 60.0);
        return tarifaHora.multiply(BigDecimal.valueOf(horasCobradas)).setScale(2, RoundingMode.HALF_UP);
    }

    private Espacio obtenerEspacioSolicitado(Long espacioId, Vehiculo vehiculo) {
        Espacio espacio = espacioRepository.buscarParaActualizar(espacioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Espacio", espacioId));
        // RN3: el espacio debe estar libre y aceptar ese tipo de vehiculo
        if (espacio.getEstado() != EstadoEspacio.DISPONIBLE) {
            throw new EspacioNoDisponibleException("El espacio " + espacio.getCodigo()
                    + " no esta disponible (estado actual: " + espacio.getEstado() + ")");
        }
        if (espacio.getTipoVehiculo() != vehiculo.getTipo()) {
            throw new ReglaNegocioException("El espacio " + espacio.getCodigo() + " es para "
                    + espacio.getTipoVehiculo() + " y el vehiculo es de tipo " + vehiculo.getTipo());
        }
        return espacio;
    }

    private Espacio asignarEspacioAutomatico(Vehiculo vehiculo) {
        Espacio libre = espacioRepository
                .findFirstByEstadoAndTipoVehiculoOrderByPisoAscCodigoAsc(EstadoEspacio.DISPONIBLE, vehiculo.getTipo())
                .orElseThrow(() -> new EspacioNoDisponibleException(
                        "No hay espacios disponibles para vehiculos de tipo " + vehiculo.getTipo()));
        return espacioRepository.buscarParaActualizar(libre.getId()).orElseThrow();
    }

    private void validarRango(LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta)) {
            throw new ReglaNegocioException("La fecha 'desde' no puede ser mayor que la fecha 'hasta'");
        }
    }

    private RegistroEstacionamiento buscarEntidad(Long id) {
        return registroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro", id));
    }
}
