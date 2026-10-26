# Taller: Servidor concurrente y Cliente (Sockets + Threads)

- **Servidor** (`Servidor.java`): corre en la **PC física**, escucha en el puerto **5000** y crea **un hilo por cada cliente**, así atiende a varios a la vez. Devuelve los datos de una tabla.
- **Cliente** (`Cliente.java`): corre en la **máquina virtual Win7**, pide una tabla y la muestra en consola.

> Antes de grabar, cambia los datos de la clase `Datos` en `Servidor.java` por la tabla de una pantalla simple de **tu módulo** (nombre, columnas, filas).

---

## 0. Qué necesitas

| Dónde | Qué |
|---|---|
| PC física | JDK (el que ya usas con NetBeans) |
| VM Win7 | **Java 8 (JRE)**: es el último que funciona en Win7. Si no lo tienes, pasa el instalador a la VM arrastrándolo o por carpeta compartida |
| Red | **Nada que configurar**: la VM de VirtualBox viene en **NAT** por defecto y desde ella la PC física se ve siempre en **`10.0.2.2`** |

## 1. Compilar (en la PC física)

Abre `cmd` en la carpeta `sockets_concurrencia`:

```
javac -source 8 -target 8 Servidor.java Cliente.java
```

(`-source 8 -target 8` hace que el `.class` funcione en el Java 8 de Win7. Puede salir un *warning*, ignóralo.)

Se generan `Servidor.class`, `Atencion.class`, `Datos.class` y `Cliente.class`.

## 2. Pasar el cliente a la VM

Copia **solo `Cliente.class`** a la VM, por ejemplo a `C:\taller\`
(arrastrar y soltar, carpeta compartida o una USB).

## 3. Levantar el servidor (PC física)

```
java Servidor
```

Debe mostrar:

```
Servidor escuchando en puerto 5000
Procesadores disponibles: 4
```

Si Windows pregunta por el **Firewall**, dale **Permitir**.

## 4. Ejecutar 2 clientes (VM Win7)

Abre **dos ventanas de cmd** en la VM, en las dos:

```
cd C:\taller
java Cliente 10.0.2.2
```

- Ventana 1: escribe `clientes` y Enter.
- Ventana 2: **enseguida** (tienes 10 segundos) escribe `productos` y Enter.

Truco para la demo: escribe la tabla en las dos ventanas **sin dar Enter**, y luego da Enter en una y Enter en la otra rápidamente.

## 5. Qué mostrar en la demostración (lo que pide el profe)

En la **consola del servidor** verás:

```
Cliente conectado desde: 127.0.0.1
[Thread-0] Inicio 1792983563393 - pide tabla: clientes

Cliente conectado desde: 127.0.0.1
[Thread-1] Inicio 1792983565390 - pide tabla: productos
[Thread-0] Fin    1792983573401 - tabla enviada: clientes
[Thread-1] Fin    1792983575392 - tabla enviada: productos
```

Explica:
1. **Dos hilos distintos** (`Thread-0`, `Thread-1`) = dos usuarios atendidos.
2. El **segundo empezó antes de que terminara el primero** → atención **simultánea / concurrente**. Si fuera secuencial, el segundo tendría que esperar los 10 s del primero (20 s en total); aquí todo termina en ~12 s.
3. El `Thread.sleep(10000)` simula la consulta a la base de datos para que se note la concurrencia.

En cada **ventana cliente** se publica la tabla:

```
TABLA CLIENTES
+---------------------------------------------------------------+
| ID            | CEDULA        | NOMBRE        | TELEFONO      |
+---------------------------------------------------------------+
| 1             | 1712345678    | Juan Perez    | 0991111111    |
...
4 registro(s)
```

Toma capturas de: servidor con los 2 hilos + las 2 ventanas cliente con sus tablas.

## 6. Explicación rápida del código (por si preguntan)

- `new ServerSocket(5000)` → abre el puerto.
- `while (true) { servidor.accept(); ... }` → espera clientes sin parar.
- `new Thread(new Atencion(cliente)).start()` → **un hilo por cliente** (esto es la concurrencia). `start()` llama a `run()`.
- `Atencion.run()` → lee el nombre de la tabla, “consulta” (sleep), responde y cierra.
- `Datos.consultar(tabla)` → capa de datos: devuelve la tabla como texto.
- `new PrintWriter(..., true)` → el `true` hace el `flush` automático.
- Cliente: `new Socket(ip, 5000)`, envía el nombre con `println`, lee líneas hasta `FIN`.

## Problemas comunes

| Problema | Solución |
|---|---|
| `Connection refused` / `timed out` en la VM | ¿El servidor está corriendo? ¿Pusiste `10.0.2.2`? Revisa que la VM esté en **NAT** (Configuración → Red) |
| Usas **VMware** en vez de VirtualBox | En la VM: `ipconfig` → la IP del servidor es la de la *puerta de enlace* cambiando el último número por `.1` (ej. `192.168.40.2` → `192.168.40.1`). Permite Java en el firewall de la PC |
| `UnsupportedClassVersionError` en la VM | No compilaste con `-source 8 -target 8` |
| `'java' no se reconoce` | Instala Java 8 en la VM o usa la ruta completa: `"C:\Program Files\Java\jre1.8.0_xxx\bin\java" Cliente 10.0.2.2` |
| `Address already in use` | Ya hay un servidor abierto; ciérralo (Ctrl+C) |

Prueba rápida todo en la PC física (sin VM): `java Servidor` en una ventana y `java Cliente` en otras dos.
