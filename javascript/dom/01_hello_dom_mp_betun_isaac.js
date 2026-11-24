const titulo = document.getElementById('titulo');
const mensaje = document.getElementById('mensaje');
let goles = 0;

document.getElementById('boton').addEventListener('click', () => {
    goles++;
    titulo.textContent = `Messi lleva ${goles} gol${goles === 1 ? '' : 'es'}`;
    mensaje.style.color = '#75aadb';
});
document.getElementById('reiniciar').addEventListener('click', () => {
    goles = 0;
    titulo.textContent = 'Hola, Messi';
    mensaje.style.color = '';
});
