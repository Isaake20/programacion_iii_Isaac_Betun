const enlace = document.getElementById('enlace');
console.log(enlace.getAttribute('href'));
document.getElementById('cambiar').addEventListener('click', () => {
    enlace.setAttribute('href', 'https://www.wikipedia.org');
    enlace.setAttribute('target', '_blank');
    enlace.textContent = 'Ir a Wikipedia';
    document.getElementById('foto').setAttribute('alt', 'Foto cambiada');
});
