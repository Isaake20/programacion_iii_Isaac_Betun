// Práctica 11 (mp) - Objetos con tema Messi
const messi = {
    nombre: 'Lionel Messi',
    nacimiento: 1987,
    equipos: ['Barcelona', 'PSG', 'Inter Miami'],
    estadisticas: { partidos: 1100, goles: 850 },
    edad(anioActual) {
        return anioActual - this.nacimiento;
    }
};

console.log(messi.nombre, messi.edad(2026));
console.log(messi.estadisticas.goles);
messi.estadisticas.asistencias = 380;
console.log(Object.entries(messi.estadisticas));

const { equipos, estadisticas: { partidos } } = messi;
console.log(equipos.length, partidos);

const copia = { ...messi, nombre: 'Leo Messi' };
console.log(copia.nombre, messi.nombre);
console.log(JSON.stringify({ nombre: messi.nombre, equipos }));
