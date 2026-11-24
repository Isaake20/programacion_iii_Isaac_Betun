const fotos = [
    'imagenes/messi.jpg',
    'imagenes/messi3.jpg',
    'imagenes/messi4.jpeg'
];
let actual = 0;
const foto = document.getElementById('foto');
const pie = document.getElementById('pie');

function mostrar() {
    foto.setAttribute('src', fotos[actual]);
    foto.setAttribute('alt', `Messi ${actual + 1}`);
    pie.textContent = `Foto ${actual + 1} de ${fotos.length} - Autor: Isaac Betun`;
}
document.getElementById('siguiente').addEventListener('click', () => {
    actual = (actual + 1) % fotos.length;
    mostrar();
});
document.getElementById('anterior').addEventListener('click', () => {
    actual = (actual - 1 + fotos.length) % fotos.length;
    mostrar();
});
document.getElementById('ocultar').addEventListener('click', () => {
    foto.classList.toggle('oculto');
});
