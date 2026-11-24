console.log(document.getElementById('principal'));
console.log(document.getElementsByClassName('fruta').length);
console.log(document.getElementsByTagName('p').length);
console.log(document.querySelector('.fruta').textContent);
const todas = document.querySelectorAll('p.fruta');
document.getElementById('boton').addEventListener('click', () => {
    todas.forEach((p) => p.classList.add('resaltado'));
});
