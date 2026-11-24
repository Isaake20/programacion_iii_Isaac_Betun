const entrada = document.getElementById('entrada');
const lista = document.getElementById('lista');

function escapar(texto) {
    const div = document.createElement('div');
    div.textContent = texto;
    return div.innerHTML;
}

document.getElementById('agregar').addEventListener('click', () => {
    const nombre = entrada.value.trim();
    if (!nombre) return;
    lista.innerHTML += `<li>${escapar(nombre)}</li>`;
    entrada.value = '';
    entrada.focus();
});
document.getElementById('limpiar').addEventListener('click', () => {
    lista.innerHTML = '';
});
