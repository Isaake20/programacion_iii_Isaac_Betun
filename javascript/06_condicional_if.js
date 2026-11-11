console.log('Condicional simple if');
let energia = 50;
if (energia > 0) {
    console.log('El jugador tiene energía');
}

console.log('Condicional if-else');
let minutos = 90;
if (minutos >= 90) {
    console.log('El partido terminó');
} else {
    console.log('El partido sigue');
}

console.log('Condicional if - else if - else');
let torneo = 'Mundial';
if (torneo === 'Mundial') {
    console.log('Messi ganó el Mundial 2022');
} else if (torneo === 'Copa América') {
    console.log('Messi ganó la Copa América 2021');
} else {
    console.log('Torneo desconocido');
}

console.log('If anidado');
let titular = true;
let lesionado = false;
if (titular) {
    if (!lesionado) {
        console.log('Messi juega desde el inicio');
    } else {
        console.log('Messi es titular pero está lesionado');
    }
}

console.log('If con operadores lógicos');
let goles = 2;
let asistencias = 1;
if (goles > 0 && asistencias > 0) {
    console.log('Messi fue figura del partido');
} else {
    console.log('Partido normal');
}

console.log('Autorización para jugar');
let fisicoOk = true;
let entrenoOk = true;
let tarjetas = 1;
if (fisicoOk && entrenoOk && tarjetas < 2) {
    console.log('Jugador autorizado');
} else if (!fisicoOk) {
    console.log('No autorizado: problema físico');
} else {
    console.log('No autorizado: revisar entrenamiento o tarjetas');
}
