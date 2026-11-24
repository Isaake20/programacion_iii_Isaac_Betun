const entrada = document.getElementById('entrada');
const lista = document.getElementById('lista');
document.getElementById('agregar').addEventListener('click', () => {
    const texto = entrada.value.trim();
    if (!texto) return;
    const li = document.createElement('li');
    li.textContent = texto; // textContent evita interpretar HTML del usuario
    lista.appendChild(li);
    entrada.value = '';
});
