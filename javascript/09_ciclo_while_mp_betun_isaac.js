// Práctica 09 (mp) - Ciclo while - Autor: Isaac Betun
let golesParaRecord = 5;
let partido = 0;
while (golesParaRecord > 0) {
    partido++;
    const golesHoy = partido % 2 === 0 ? 2 : 1;
    golesParaRecord -= golesHoy;
    console.log(`Partido ${partido}: ${golesHoy} gol(es), faltan ${Math.max(golesParaRecord, 0)}`);
}
console.log(`Récord alcanzado en ${partido} partidos`);

let penal = 0;
do {
    penal++;
    console.log(`Penal ${penal}: ¡gol!`);
} while (penal < 3);

let minuto = 0;
while (minuto < 90) {
    minuto += 15;
    if (minuto === 45) {
        console.log('Medio tiempo');
        continue;
    }
    if (minuto > 75) break;
    console.log(`Minuto ${minuto}`);
}
