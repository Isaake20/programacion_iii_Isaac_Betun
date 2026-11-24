// Práctica 02 (mp) - Variables - Autor: Isaac Betun
var pais = 'Argentina';          // var: alcance de función
let golesTemporada = 25;         // let: puede cambiar
const NOMBRE = 'Lionel Messi';   // const: no se reasigna

golesTemporada = golesTemporada + 3;
console.log(NOMBRE, 'de', pais, 'marcó', golesTemporada, 'goles');

{
    let golesBloque = 1;
    var golesFuera = 2;
    console.log('Dentro del bloque:', golesBloque);
}
console.log('var sale del bloque:', golesFuera);
// console.log(golesBloque); // ReferenceError: let no existe fuera del bloque

const jugador = { camiseta: 10 };
jugador.camiseta = 30; // se puede cambiar una propiedad de un objeto const
console.log('Camiseta en el PSG:', jugador.camiseta);

let texto = `${NOMBRE} lleva ${golesTemporada} goles con ${pais}`;
console.log(texto);
