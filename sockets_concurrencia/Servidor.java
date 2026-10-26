import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * SERVIDOR CONCURRENTE (Sockets + Threads)
 * Escucha en el puerto 5000 y atiende a cada cliente en un HILO distinto,
 * por eso puede atender a varios usuarios al mismo tiempo.
 * El cliente envia el nombre de una tabla y el servidor le devuelve sus datos.
 */
public class Servidor {

    static final int PUERTO = 5000;

    public static void main(String[] args) throws Exception {
        ServerSocket servidor = new ServerSocket(PUERTO);
        System.out.println("Servidor escuchando en puerto " + PUERTO);
        System.out.println("Procesadores disponibles: " + Runtime.getRuntime().availableProcessors());

        while (true) {                                  // lazo infinito: espera clientes
            Socket cliente = servidor.accept();         // se conecta un cliente
            System.out.println("\nCliente conectado desde: " + cliente.getInetAddress().getHostAddress());
            new Thread(new Atencion(cliente)).start();  // un hilo nuevo por cada cliente
        }
    }
}

/** Codigo que ejecuta cada hilo (run) para atender a UN cliente. */
class Atencion implements Runnable {

    private final Socket cliente;

    Atencion(Socket cliente) {          // constructor
        this.cliente = cliente;
    }

    public void run() {
        String hilo = Thread.currentThread().getName();
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true); // true = flush automatico

            String tabla = entrada.readLine();  // mensaje del cliente: nombre de la tabla
            System.out.println("[" + hilo + "] Inicio " + System.currentTimeMillis() + " - pide tabla: " + tabla);

            Thread.sleep(10000);                // 10 s "consultando" -> da tiempo a lanzar el 2do cliente

            salida.println(Datos.consultar(tabla));  // respuesta al cliente
            salida.println("FIN");
            System.out.println("[" + hilo + "] Fin    " + System.currentTimeMillis() + " - tabla enviada: " + tabla);
            cliente.close();
        } catch (Exception e) {
            System.out.println("[" + hilo + "] Error: " + e.getMessage());
        }
    }
}

/**
 * Capa de datos: aqui estan las tablas de las pantallas simples del proyecto.
 * CAMBIA estos datos por los de TU modulo (nombre de tabla, columnas y filas).
 */
class Datos {

    static String consultar(String tabla) {
        if (tabla == null) tabla = "";
        String t = tabla.trim().toLowerCase();

        if (t.equals("clientes") || t.equals("1")) {
            return armar("CLIENTES",
                new String[]{"ID", "CEDULA", "NOMBRE", "TELEFONO"},
                new String[][]{
                    {"1", "1712345678", "Juan Perez", "0991111111"},
                    {"2", "1723456789", "Maria Lopez", "0992222222"},
                    {"3", "1734567890", "Carlos Ruiz", "0993333333"},
                    {"4", "1745678901", "Ana Torres", "0994444444"}
                });
        }
        if (t.equals("productos") || t.equals("2")) {
            return armar("PRODUCTOS",
                new String[]{"ID", "DESCRIPCION", "PRECIO", "STOCK"},
                new String[][]{
                    {"1", "Teclado USB", "15.50", "20"},
                    {"2", "Mouse optico", "8.75", "35"},
                    {"3", "Monitor 22p", "120.00", "5"}
                });
        }
        return "Tabla '" + tabla + "' no existe. Opciones: clientes (1) o productos (2)";
    }

    /** Arma la tabla como texto con columnas alineadas. */
    static String armar(String nombre, String[] columnas, String[][] filas) {
        StringBuilder sb = new StringBuilder();
        String linea = "+" + repetir("-", columnas.length * 16 - 1) + "+";
        sb.append("TABLA ").append(nombre).append("\n").append(linea).append("\n|");
        for (String c : columnas) sb.append(String.format(" %-14s|", c));
        sb.append("\n").append(linea).append("\n");
        for (String[] f : filas) {
            sb.append("|");
            for (String v : f) sb.append(String.format(" %-14s|", v));
            sb.append("\n");
        }
        sb.append(linea).append("\n").append(filas.length).append(" registro(s)");
        return sb.toString();
    }

    static String repetir(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }
}
