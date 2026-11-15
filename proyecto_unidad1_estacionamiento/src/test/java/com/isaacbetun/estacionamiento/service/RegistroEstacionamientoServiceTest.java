package com.isaacbetun.estacionamiento.service;

import com.isaacbetun.estacionamiento.dto.IngresoRequest;
import com.isaacbetun.estacionamiento.entity.*;
import com.isaacbetun.estacionamiento.exception.EspacioNoDisponibleException;
import com.isaacbetun.estacionamiento.exception.ReglaNegocioException;
import com.isaacbetun.estacionamiento.repository.EspacioRepository;
import com.isaacbetun.estacionamiento.repository.RegistroEstacionamientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroEstacionamientoServiceTest {

    @Mock
    private RegistroEstacionamientoRepository registroRepository;
    @Mock
    private EspacioRepository espacioRepository;
    @Mock
    private VehiculoService vehiculoService;
    @Mock
    private NotificacionService notificacionService;

    private RegistroEstacionamientoService service;
    private Vehiculo auto;

    @BeforeEach
    void setUp() {
        Clock reloj = Clock.fixed(Instant.parse("2026-11-10T15:00:00Z"), ZoneId.of("America/Guayaquil"));
        service = new RegistroEstacionamientoService(registroRepository, espacioRepository,
                vehiculoService, notificacionService, reloj);
        ReflectionTestUtils.setField(service, "minutosGracia", 10L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setActivo(true);
        usuario.setEmail("isaac@test.com");
        auto = new Vehiculo();
        auto.setId(5L);
        auto.setPlaca("PBA-1234");
        auto.setTipo(TipoVehiculo.AUTO);
        auto.setUsuario(usuario);
    }

    @Test
    void noCobraDentroDelTiempoDeGracia() {
        assertThat(service.calcularValor(8, new BigDecimal("1.00"))).isEqualByComparingTo("0.00");
    }

    @Test
    void cobraPorHoraOFraccion() {
        // 61 minutos = 2 horas cobradas
        assertThat(service.calcularValor(61, new BigDecimal("1.00"))).isEqualByComparingTo("2.00");
        assertThat(service.calcularValor(30, new BigDecimal("0.50"))).isEqualByComparingTo("0.50");
    }

    @Test
    void noPermiteIngresoSiElVehiculoYaEstaDentro() {
        when(vehiculoService.buscarPorPlacaEntidad("PBA-1234")).thenReturn(auto);
        when(registroRepository.existsByVehiculoIdAndEstado(5L, EstadoRegistro.EN_CURSO)).thenReturn(true);

        assertThatThrownBy(() -> service.registrarIngreso(new IngresoRequest("PBA-1234", null, null)))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("ya se encuentra dentro");
        verify(registroRepository, never()).save(any());
    }

    @Test
    void noPermiteIngresoDeUsuarioInactivo() {
        auto.getUsuario().setActivo(false);
        when(vehiculoService.buscarPorPlacaEntidad("PBA-1234")).thenReturn(auto);

        assertThatThrownBy(() -> service.registrarIngreso(new IngresoRequest("PBA-1234", null, null)))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("inactivo");
    }

    @Test
    void noPermiteUsarEspacioDeOtroTipo() {
        Espacio moto = new Espacio();
        moto.setId(9L);
        moto.setCodigo("M-01");
        moto.setTipoVehiculo(TipoVehiculo.MOTO);
        moto.setEstado(EstadoEspacio.DISPONIBLE);
        when(vehiculoService.buscarPorPlacaEntidad("PBA-1234")).thenReturn(auto);
        when(espacioRepository.buscarParaActualizar(9L)).thenReturn(Optional.of(moto));

        assertThatThrownBy(() -> service.registrarIngreso(new IngresoRequest("PBA-1234", 9L, null)))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("es para MOTO");
    }

    @Test
    void lanzaErrorCuandoNoHayEspaciosLibres() {
        when(vehiculoService.buscarPorPlacaEntidad("PBA-1234")).thenReturn(auto);
        when(espacioRepository.findFirstByEstadoAndTipoVehiculoOrderByPisoAscCodigoAsc(
                EstadoEspacio.DISPONIBLE, TipoVehiculo.AUTO)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrarIngreso(new IngresoRequest("PBA-1234", null, null)))
                .isInstanceOf(EspacioNoDisponibleException.class);
    }

    @Test
    void normalizaLaPlaca() {
        assertThat(VehiculoService.normalizarPlaca(" pba1234 ")).isEqualTo("PBA-1234");
        assertThat(VehiculoService.normalizarPlaca("ab-123c")).isEqualTo("AB-123C");
    }
}
