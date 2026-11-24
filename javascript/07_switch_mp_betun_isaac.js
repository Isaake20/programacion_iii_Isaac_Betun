// Práctica 07 (mp) - switch - Autor: Isaac Betun
function trofeo(anio) {
    switch (anio) {
        case 2021:
            return 'Copa América';
        case 2022:
            return 'Copa del Mundo y Finalissima';
        case 2024:
            return 'Copa América';
        default:
            return 'Sin títulos con la selección';
    }
}
[2019, 2021, 2022, 2024].forEach((a) => console.log(a, '->', trofeo(a)));

const posicion = 'mediapunta';
switch (posicion) {
    case 'delantero':
    case 'mediapunta':
        console.log('Jugador ofensivo');
        break;
    case 'defensa':
        console.log('Jugador defensivo');
        break;
    default:
        console.log('Posición desconocida');
}
