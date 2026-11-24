// Práctica 06 (mp) - Condicional if - Autor: Isaac Betun
function calificarTemporada(goles) {
    if (goles >= 50) {
        return 'Temporada histórica';
    } else if (goles >= 30) {
        return 'Gran temporada';
    } else if (goles >= 15) {
        return 'Buena temporada';
    } else {
        return 'Temporada regular';
    }
}

for (const goles of [73, 41, 19, 8]) {
    console.log(`${goles} goles: ${calificarTemporada(goles)}`);
}

const edad = 39;
const enForma = true;
if (edad > 35 && enForma) {
    console.log('Messi sigue jugando a gran nivel');
}
if (!(edad < 18)) {
    console.log('Es mayor de edad');
}
