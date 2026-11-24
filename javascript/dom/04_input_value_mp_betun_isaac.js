document.getElementById('calcular').addEventListener('click', () => {
    const victorias = Number(document.getElementById('victorias').value);
    const empates = Number(document.getElementById('empates').value);
    const torneo = document.getElementById('torneo').value;
    const local = document.getElementById('local').checked;
    const puntos = victorias * 3 + empates;
    document.getElementById('resultado').textContent =
        `${torneo}: ${puntos} puntos${local ? ' (jugó de local)' : ''}`;
});
