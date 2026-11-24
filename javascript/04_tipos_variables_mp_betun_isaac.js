// Práctica 04 (mp) - Tipos de datos - Autor: Isaac Betun
const nombre = 'Lionel Messi';       // string
const goles = 850;                   // number
const promedio = 0.78;               // number (decimal)
const esCampeon = true;              // boolean
let proximoClub;                     // undefined
const lesion = null;                 // null
const golesHistoricos = 1000n;       // bigint
const id = Symbol('jugador');        // symbol
const clubes = ['Barcelona', 'PSG']; // object (arreglo)
const ficha = { nombre, goles };     // object

for (const valor of [nombre, goles, promedio, esCampeon, proximoClub, lesion, golesHistoricos, id, clubes, ficha]) {
    console.log(typeof valor, '->', valor);
}
console.log('¿Es arreglo?', Array.isArray(clubes));
console.log('Conversión:', Number('10') + 5, String(goles) + ' goles', Boolean(0));
