import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * CLIENTE
 * Se conecta al servidor (puerto 5000), envia el nombre de la tabla
 * y publica en consola los datos que le devuelve el servidor.
 *
 * Uso:  java Cliente [ip_del_servidor]
 *   - En la maquina virtual (VirtualBox, red NAT por defecto): java Cliente 10.0.2.2
 *   - En la misma PC del servidor:                            java Cliente
 */
public class Cliente {

    public static void main(String[] args) throws Exception {
        String ip = args.length > 0 ? args[0] : "127.0.0.1";
        int puerto = 5000;

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Tablas disponibles: 1) clientes   2) productos");
        System.out.print("Escriba la tabla a consultar: ");
        String tabla = teclado.readLine();

        Socket socket = new Socket(ip, puerto);
        PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("Conectado a " + ip + ":" + puerto + " - Inicio " + System.currentTimeMillis());
        salida.println(tabla);                      // envia la peticion al servidor
        System.out.println("Esperando respuesta del servidor...\n");

        String linea;
        while ((linea = entrada.readLine()) != null && !linea.equals("FIN")) {
            System.out.println(linea);              // publica la tabla en consola
        }
        System.out.println("\nFin " + System.currentTimeMillis());
        socket.close();
    }
}
