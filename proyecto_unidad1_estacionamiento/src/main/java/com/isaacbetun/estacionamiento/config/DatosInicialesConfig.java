package com.isaacbetun.estacionamiento.config;

import com.isaacbetun.estacionamiento.entity.Espacio;
import com.isaacbetun.estacionamiento.entity.EstadoEspacio;
import com.isaacbetun.estacionamiento.entity.TipoVehiculo;
import com.isaacbetun.estacionamiento.repository.EspacioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

/**
 * Crea los espacios del estacionamiento la primera vez que se levanta la aplicacion
 * (solo si la tabla esta vacia). No se ejecuta en el perfil de pruebas.
 */
@Configuration
@Profile("!test")
public class DatosInicialesConfig {

    private static final Logger log = LoggerFactory.getLogger(DatosInicialesConfig.class);

    @Bean
    CommandLineRunner cargarEspacios(EspacioRepository repo) {
        return args -> {
            if (repo.count() > 0) {
                return;
            }
            // Planta baja: autos A-01..A-06, piso 1: camionetas B-01..B-02 y motos M-01..M-04
            for (int i = 1; i <= 6; i++) {
                repo.save(espacio(String.format("A-%02d", i), 0, TipoVehiculo.AUTO, "1.00"));
            }
            for (int i = 1; i <= 2; i++) {
                repo.save(espacio(String.format("B-%02d", i), 1, TipoVehiculo.CAMIONETA, "1.50"));
            }
            for (int i = 1; i <= 4; i++) {
                repo.save(espacio(String.format("M-%02d", i), 1, TipoVehiculo.MOTO, "0.50"));
            }
            log.info("Se crearon {} espacios iniciales", repo.count());
        };
    }

    private Espacio espacio(String codigo, int piso, TipoVehiculo tipo, String tarifa) {
        Espacio e = new Espacio();
        e.setCodigo(codigo);
        e.setPiso(piso);
        e.setTipoVehiculo(tipo);
        e.setEstado(EstadoEspacio.DISPONIBLE);
        e.setTarifaHora(new BigDecimal(tarifa));
        return e;
    }
}
