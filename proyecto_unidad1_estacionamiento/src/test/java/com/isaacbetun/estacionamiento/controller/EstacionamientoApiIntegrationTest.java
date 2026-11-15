package com.isaacbetun.estacionamiento.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba de punta a punta del flujo principal usando la API real
 * (controller -> service -> repository -> base H2).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EstacionamientoApiIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private static long usuarioId;
    private static long espacioId;
    private static long registroId;

    @Test
    @Order(1)
    void registraUsuarioVehiculoYEspacio() throws Exception {
        MvcResult r = mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content("""
                        {"cedula":"1718293045","nombres":"Isaac","apellidos":"Betun",
                         "email":"isaac.betun@test.com","telefono":"0991234567"}"""))
                .andExpect(status().isCreated())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(jsonPath("$.activo").value(true))
                .andReturn();
        usuarioId = leer(r).get("id").asLong();

        mvc.perform(post("/api/vehiculos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"placa":"pba1234","marca":"Chevrolet","modelo":"Aveo","color":"Rojo",
                         "tipo":"AUTO","usuarioId":%d}""".formatted(usuarioId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.placa").value("PBA-1234"));

        MvcResult e = mvc.perform(post("/api/espacios").contentType(MediaType.APPLICATION_JSON).content("""
                        {"codigo":"t-01","piso":0,"tipoVehiculo":"AUTO","tarifaHora":1.00}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"))
                .andReturn();
        espacioId = leer(e).get("id").asLong();
    }

    @Test
    @Order(2)
    void rechazaDatosInvalidosYDuplicados() throws Exception {
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content("""
                        {"cedula":"123","nombres":"","apellidos":"X","email":"no-es-email"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles").isArray());

        mvc.perform(post("/api/vehiculos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"placa":"PBA-1234","marca":"Kia","modelo":"Rio","tipo":"AUTO","usuarioId":%d}"""
                        .formatted(usuarioId)))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    void registraIngresoYNoPermiteDobleIngreso() throws Exception {
        MvcResult r = mvc.perform(post("/api/registros/ingreso").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placa\":\"PBA-1234\",\"espacioId\":%d}".formatted(espacioId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("EN_CURSO"))
                .andReturn();
        registroId = leer(r).get("id").asLong();

        mvc.perform(get("/api/espacios/" + espacioId))
                .andExpect(jsonPath("$.estado").value("OCUPADO"));

        mvc.perform(post("/api/registros/ingreso").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placa\":\"PBA-1234\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.mensaje").value("El vehiculo PBA-1234 ya se encuentra dentro del estacionamiento"));
    }

    @Test
    @Order(4)
    void noPermiteEliminarEspacioOcupado() throws Exception {
        mvc.perform(delete("/api/espacios/" + espacioId).header("X-Rol", "ADMIN"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Order(5)
    void registraSalidaYLiberaElEspacio() throws Exception {
        mvc.perform(patch("/api/registros/" + registroId + "/salida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("FINALIZADO"))
                .andExpect(jsonPath("$.valorPagado").value(0.0));

        mvc.perform(get("/api/espacios/" + espacioId))
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"));

        mvc.perform(patch("/api/registros/" + registroId + "/salida"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Order(6)
    void devuelve404CuandoNoExiste() throws Exception {
        mvc.perform(get("/api/usuarios/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Usuario con identificador '9999' no existe"));
        mvc.perform(get("/api/vehiculos/placa/XXX-0000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    void interceptorBloqueaDeleteSinRolAdmin() throws Exception {
        mvc.perform(delete("/api/registros/" + registroId))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/api/registros/" + registroId).header("X-Rol", "ADMIN"))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/registros/" + registroId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    void actualizaYConsultaDisponibilidad() throws Exception {
        mvc.perform(patch("/api/espacios/" + espacioId + "/estado").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"MANTENIMIENTO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("MANTENIMIENTO"));

        mvc.perform(get("/api/espacios/disponibilidad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enMantenimiento").value(1));

        mvc.perform(get("/api/auditoria/metricas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['RegistroEstacionamientoService.registrarIngreso'].llamadas").value(2));
    }

    private JsonNode leer(MvcResult r) throws Exception {
        return mapper.readTree(r.getResponse().getContentAsString());
    }
}
