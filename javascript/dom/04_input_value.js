document.getElementById('sumar').addEventListener('click', () => {
    const a = Number(document.getElementById('a').value);
    const b = Number(document.getElementById('b').value);
    document.getElementById('resultado').textContent = `Resultado: ${a + b}`;
});
