const titulo = document.getElementById('titulo');
const mensaje = document.getElementById('mensaje');
console.log(document.title, titulo.textContent);

document.getElementById('boton').addEventListener('click', () => {
    titulo.textContent = 'El DOM cambió';
    mensaje.style.color = 'green';
    mensaje.textContent = 'Texto modificado desde JavaScript';
});
