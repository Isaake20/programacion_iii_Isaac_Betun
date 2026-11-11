import { createServer } from 'node:http';

const server = createServer((req, res) => {
    if (req.url === '/') {
        res.writeHead(200, { 'Content-Type': 'text/plain; charset=utf-8' });
        res.end('Hola desde el servidor de Messi\n');
    } else if (req.url === '/goles') {
        res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
        res.end('<h1>Goles</h1><p>Messi marcó más de 800 goles en su carrera.</p>');
    } else {
        res.writeHead(404, { 'Content-Type': 'text/html; charset=utf-8' });
        res.end('<h1>404 - Página no encontrada</h1>');
    }
});

server.listen(3000, '127.0.0.1', () => {
    console.log('Escuchando en http://127.0.0.1:3000');
});
