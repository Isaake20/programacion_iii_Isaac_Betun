let camiseta = 10;
switch (camiseta) {
    case 10:
        console.log('Camiseta de Messi');
        break;
    case 9:
        console.log('Camiseta del delantero centro');
        break;
    case 1:
        console.log('Camiseta del arquero');
        break;
    default:
        console.log('Otra camiseta');
}

let club = 'Barcelona';
switch (club) {
    case 'Barcelona':
    case 'Paris Saint-Germain':
        console.log('Club europeo');
        break;
    case 'Inter Miami':
        console.log('Club de la MLS');
        break;
    default:
        console.log('Club desconocido');
}

let goles = 2;
switch (true) {
    case goles === 0:
        console.log('Sin goles');
        break;
    case goles >= 1 && goles <= 2:
        console.log('Buen partido');
        break;
    case goles >= 3:
        console.log('Triplete o más');
        break;
    default:
        console.log('Dato inválido');
}

console.log('Calculadora');
let a = 10;
let b = 5;
let operacion = 'multiplicacion';
let resultado;
switch (operacion) {
    case 'suma':
        resultado = a + b;
        break;
    case 'resta':
        resultado = a - b;
        break;
    case 'multiplicacion':
        resultado = a * b;
        break;
    case 'division':
        resultado = a / b;
        break;
    default:
        resultado = 'operación no válida';
}
console.log('Resultado:', resultado);
