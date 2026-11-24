# Guion para el video (máx. 15 min)

Cámara y micrófono encendidos todo el tiempo.

| Min | Parte | Qué mostrar |
|---|---|---|
| 0:00 – 1:00 | Presentación | Nombre, Grupo 10, caso: gestión de estacionamiento. Problema que resuelve. |
| 1:00 – 3:00 | Arquitectura | Diagrama N capas y paquetes en el IDE: controller → service → repository → entity, DTO/mapper. |
| 3:00 – 4:30 | Base de datos | Diagrama ER, entidades y relaciones (1:N), PK/FK/UNIQUE/CHECK. Consultas personalizadas en los repositorios. |
| 4:30 – 6:00 | Reglas y excepciones | `RegistroEstacionamientoService` (ingreso/salida, cálculo del valor) y `GlobalExceptionHandler`. |
| 6:00 – 7:30 | Middleware y AOP | Los dos interceptores y `AuditoriaAspect`. |
| 7:30 – 8:30 | Bloqueante vs asíncrono | `NotificacionService` con `@Async` y `AsyncConfig`. |
| 8:30 – 13:30 | **Demo en vivo** | Levantar PostgreSQL y la app. Con `peticiones.http` o Postman: crear usuario → vehículo → espacios disponibles → ingreso → doble ingreso (422) → activos/disponibilidad → salida (ver en consola el hilo `notificaciones-`) → GET 999 (404) → PUT usuario → PATCH espacio → DELETE sin rol (403) y con rol (204) → métricas de auditoría. Mostrar los datos en PostgreSQL. |
| 13:30 – 14:30 | CI/CD | Pestaña Actions de GitHub con el workflow en verde e historial de commits. |
| 14:30 – 15:00 | Cierre | Conclusiones y trabajo futuro. |
