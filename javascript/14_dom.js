// Seleccionar elementos
const titulo = document.getElementById('titulo');
const datos = document.querySelectorAll('.dato');
const contador = document.querySelector('#contador');
const lista = document.getElementById('clubes');

// Leer y cambiar contenido y estilos
console.log(titulo.textContent);
titulo.textContent = 'Manipulación del DOM con Messi';
titulo.style.color = '#75aadb';
datos.forEach((p) => (p.style.fontWeight = 'bold'));

// Eventos: contar goles
let goles = 0;
document.getElementById('btnGol').addEventListener('click', () => {
    goles++;
    contador.textContent = goles;
    contador.classList.toggle('par', goles % 2 === 0);
});

// Crear nodos y agregarlos al documento
document.getElementById('btnAgregar').addEventListener('click', () => {
    const entrada = document.getElementById('nuevoClub');
    const nombre = entrada.value.trim();
    if (nombre === '') {
        alert('Escribe el nombre del club');
        return;
    }
    const li = document.createElement('li');
    li.textContent = nombre;
    // Eliminar el elemento al hacer clic sobre él
    li.addEventListener('click', () => li.remove());
    lista.appendChild(li);
    entrada.value = '';
});
