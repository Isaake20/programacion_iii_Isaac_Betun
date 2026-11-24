// Práctica 10 (mp) - Funciones con tema Messi
function anunciarGol(jugador, minuto = 90) {
    return `¡Gol de ${jugador} en el minuto ${minuto}!`;
}
console.log(anunciarGol('Messi', 23));
console.log(anunciarGol('Messi'));

const puntos = (victorias, empates) => victorias * 3 + empates;
console.log(`Puntos: ${puntos(5, 2)}`);

const promedioGoles = (...goles) => goles.reduce((a, g) => a + g, 0) / goles.length;
console.log(promedioGoles(2, 0, 3, 1));

function crearContador() {
    let goles = 0;
    return () => ++goles;
}
const golesMessi = crearContador();
golesMessi();
golesMessi();
console.log(`Goles contados: ${golesMessi()}`);
