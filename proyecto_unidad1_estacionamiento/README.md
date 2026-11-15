# API de Gestión de Estacionamiento

Proyecto Grupal – Unidad 1 – **Programación Avanzada** (Grupo 10)

**Autor:** Isaac Betún

API REST desarrollada con **Spring Boot** para administrar un estacionamiento: usuarios (propietarios), vehículos, espacios y los registros de entrada y salida. Calcula el tiempo de permanencia y el valor a pagar, y controla la disponibilidad de los espacios.

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje |
| Spring Boot 3.3.5 | Framework base |
| Spring Web | API REST |
| Spring Data JPA / Hibernate | Persistencia |
| PostgreSQL 16 | Base de datos relacional |
| H2 | Base en memoria solo para las pruebas automáticas |
| Spring AOP | Aspecto de auditoría |
| Bean Validation | Validación de los DTO |
| Lombok | Menos código repetitivo en las entidades |
| JUnit 5 + Mockito + MockMvc | Pruebas |
| GitHub Actions | Pipeline CI |

## Arquitectura (N capas)

```
controller  ->  service  ->  repository  ->  PostgreSQL
    |             |              |
   dto         mapper          entity
interceptor (middleware)   aspect (AOP)   exception   config
```

```
src/main/java/com/isaacbetun/estacionamiento
├── aspect/        AuditoriaAspect (log, tiempos y errores de los servicios)
├── config/        WebConfig, AsyncConfig, TiempoConfig, DatosInicialesConfig
├── controller/    Endpoints REST
├── dto/           Objetos de entrada/salida (records)
├── entity/        Usuario, Vehiculo, Espacio, RegistroEstacionamiento + enums
├── exception/     Excepciones personalizadas + GlobalExceptionHandler
├── interceptor/   RegistroSolicitudInterceptor, ControlAccesoInterceptor
├── mapper/        Conversión entidad <-> DTO
├── repository/    Repositorios Spring Data con consultas personalizadas
└── service/       Lógica de negocio
```

## Cómo ejecutar

1. Levantar PostgreSQL (con Docker):

```bash
docker compose up -d
```

   O crear manualmente la base `estacionamiento_db` con usuario `estacionamiento` / clave `estacionamiento123`.
   También se pueden usar las variables `DB_URL`, `DB_USER` y `DB_PASSWORD`.

2. Ejecutar la aplicación:

```bash
mvn spring-boot:run
```

La API queda en `http://localhost:8080`. La primera vez se crean automáticamente 12 espacios (A-01..A-06 autos, B-01..B-02 camionetas, M-01..M-04 motos).

3. Ejecutar las pruebas (usan H2, no necesitan PostgreSQL):

```bash
mvn test
```

## Endpoints principales

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/usuarios` | Lista usuarios (`?soloActivos=true`) |
| GET | `/api/usuarios/{id}` | Usuario por id |
| POST | `/api/usuarios` | Registra un usuario |
| PUT | `/api/usuarios/{id}` | Actualiza un usuario |
| DELETE | `/api/usuarios/{id}` | Elimina un usuario (requiere `X-Rol: ADMIN`) |
| GET | `/api/vehiculos` | Lista vehículos |
| GET | `/api/vehiculos/{id}` | Vehículo por id |
| GET | `/api/vehiculos/placa/{placa}` | Busca por placa |
| GET | `/api/vehiculos/usuario/{usuarioId}` | Vehículos de un usuario |
| POST | `/api/vehiculos` | Registra un vehículo |
| PUT | `/api/vehiculos/{id}` | Actualiza un vehículo |
| DELETE | `/api/vehiculos/{id}` | Elimina un vehículo (requiere `X-Rol: ADMIN`) |
| GET | `/api/espacios` | Lista espacios |
| GET | `/api/espacios/{id}` | Espacio por id |
| GET | `/api/espacios/disponibles?tipo=AUTO` | Espacios libres (filtro opcional por tipo) |
| GET | `/api/espacios/disponibilidad` | Resumen de ocupación |
| POST | `/api/espacios` | Registra un espacio |
| PUT | `/api/espacios/{id}` | Actualiza un espacio |
| PATCH | `/api/espacios/{id}/estado` | DISPONIBLE / MANTENIMIENTO |
| DELETE | `/api/espacios/{id}` | Elimina un espacio (requiere `X-Rol: ADMIN`) |
| POST | `/api/registros/ingreso` | Registra el ingreso de un vehículo |
| PATCH | `/api/registros/{id}/salida` | Registra la salida y calcula el valor |
| GET | `/api/registros/{id}` | Registro por id |
| GET | `/api/registros/activos` | Vehículos que están dentro |
| GET | `/api/registros/vehiculo/{placa}` | Historial de un vehículo |
| GET | `/api/registros?desde=AAAA-MM-DD&hasta=AAAA-MM-DD` | Registros por rango de fechas |
| GET | `/api/registros/recaudacion?desde=...&hasta=...` | Total recaudado |
| DELETE | `/api/registros/{id}` | Elimina un registro finalizado (requiere `X-Rol: ADMIN`) |
| GET | `/api/auditoria/metricas` | Métricas acumuladas por el aspecto |

En el archivo [`peticiones.http`](peticiones.http) están las peticiones listas para probar (VS Code REST Client / IntelliJ HTTP Client).

## Reglas de negocio

1. La placa, la cédula, el email y el código de espacio son únicos.
2. Un vehículo no puede ingresar si ya tiene un registro **EN_CURSO**.
3. Solo se puede ocupar un espacio **DISPONIBLE** y del mismo tipo que el vehículo (si no se envía espacio, se asigna el primero libre).
4. El propietario del vehículo debe estar activo para ingresar.
5. La salida no se puede registrar dos veces. Al salir el espacio vuelve a DISPONIBLE.
6. Cobro por hora o fracción según la tarifa del espacio; los primeros 10 minutos son gratis.
7. El estado OCUPADO lo maneja solo el sistema; no se puede cambiar el estado de un espacio ocupado.
8. No se elimina un usuario con vehículos, un vehículo/espacio con historial o un espacio ocupado.

## Manejo de errores

Todas las respuestas de error tienen el mismo formato:

```json
{
  "timestamp": "2026-11-14T23:40:47.601",
  "status": 404,
  "error": "Not Found",
  "mensaje": "Usuario con identificador '999' no existe",
  "ruta": "/api/usuarios/999"
}
```

| Excepción | Código |
|---|---|
| `RecursoNoEncontradoException` | 404 |
| `RecursoDuplicadoException` | 409 |
| `EspacioNoDisponibleException` | 409 |
| `ReglaNegocioException` | 422 |
| `AccesoDenegadoException` | 403 |
| Validación de datos (`@Valid`) | 400 |

## Middleware, AOP y procesamiento asíncrono

- **RegistroSolicitudInterceptor:** registra método, ruta, IP, cliente, código de respuesta y tiempo de cada petición; agrega la cabecera `X-Request-Id`.
- **ControlAccesoInterceptor:** las peticiones `DELETE` necesitan la cabecera `X-Rol: ADMIN` (si no, 403).
- **AuditoriaAspect:** registra cada llamada a los servicios, su duración y los errores; las métricas se ven en `/api/auditoria/metricas`.
- **NotificacionService (@Async):** el envío del comprobante de salida (lento, simula un correo de 3 s) se ejecuta en otro hilo para no bloquear la respuesta.

## CI/CD

El workflow [`.github/workflows/ci-estacionamiento.yml`](../.github/workflows/ci-estacionamiento.yml) se ejecuta en cada push: descarga el código, configura Java 17, compila, ejecuta las pruebas, empaqueta el `.jar` e informa si la construcción fue exitosa o falló.
