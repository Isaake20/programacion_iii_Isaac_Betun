// Práctica 03 (mp) - Servidor HTTP con Node - Autor: Isaac Betun
// Ejecutar con: node 03_server_mp_betun_isaac.js  y abrir http://127.0.0.1:3001
import { createServer } from 'node:http';

const titulos = ['Copa del Mundo 2022', 'Copa América 2021', 'Copa América 2024', 'Finalissima 2022'];

const server = createServer((req, res) => {
    if (req.url === '/') {
        res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
        res.end('<h1>Servidor de Isaac Betun</h1><p><a href="/titulos">Ver títulos de Messi</a></p>');
    } else if (req.url === '/titulos') {
        res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
        res.end(JSON.stringify({ jugador: 'Lionel Messi', titulos }));
    } else {
        res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
        res.end('Ruta no encontrada');
    }
});

server.listen(3001, '127.0.0.1', () => {
    console.log('Servidor en http://127.0.0.1:3001');
});
