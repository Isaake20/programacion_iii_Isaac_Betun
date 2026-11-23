// Ciclo while: se repite mientras la condición sea verdadera
let goles = 0;
while (goles < 5) {
    goles++;
    console.log(`Gol número ${goles}`);
}

// do...while: se ejecuta al menos una vez
let intentos = 0;
do {
    intentos++;
    console.log(`Tiro libre ${intentos}`);
} while (intentos < 3);

// break y continue
let partido = 0;
while (true) {
    partido++;
    if (partido === 2) {
        continue; // se salta el partido 2
    }
    if (partido > 4) {
        break; // termina el ciclo
    }
    console.log(`Messi juega el partido ${partido}`);
}
