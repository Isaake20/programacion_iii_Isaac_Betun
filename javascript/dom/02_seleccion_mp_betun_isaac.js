const clubes = document.querySelectorAll('.club');
console.log(`Clubes: ${clubes.length}`);
console.log(document.querySelector('#actual').textContent);
console.log(document.getElementsByTagName('li')[0].textContent);

document.getElementById('boton').addEventListener('click', () => {
    clubes.forEach((li) => li.classList.add('resaltado'));
});
document.getElementById('quitar').addEventListener('click', () => {
    clubes.forEach((li) => li.classList.remove('resaltado'));
});
