console.log('Declaración de variables');
/*
Comentario de varias líneas:
let, var y const son las tres formas de declarar variables.
*/

// let: el valor puede cambiar
let jugador = 'Messi';
let camiseta = 10;
let club = 'Barcelona';
let esCapitan = true;
console.log(jugador, camiseta, club, esCapitan);

jugador = 'Lionel Messi';
camiseta = 30;
club = 'Paris Saint-Germain';
esCapitan = false;
console.log(jugador, camiseta, club, esCapitan);

// var: forma antigua de declarar
var seleccion = 'Argentina';
console.log('Selección:', seleccion);
seleccion = 'Albiceleste';
console.log('Nueva selección:', seleccion);

// const: el valor no puede cambiar
const nombreCompleto = 'Lionel Andrés Messi';
console.log('Nombre completo:', nombreCompleto);
