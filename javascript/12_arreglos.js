const mundiales = [2006, 2010, 2014, 2018, 2022];

console.log(mundiales);
console.log(mundiales.length);
console.log(mundiales[0], mundiales[mundiales.length - 1]);

// Agregar y quitar elementos
mundiales.push(2026);
console.log(mundiales);
mundiales.pop();
mundiales.unshift(2002);
mundiales.shift();
console.log(mundiales);

// Buscar
console.log(mundiales.includes(2022));
console.log(mundiales.indexOf(2014));

// Copiar un tramo y unir arreglos
console.log(mundiales.slice(1, 3));
const clubes = ['Barcelona', 'PSG'].concat(['Inter Miami']);
console.log(clubes, clubes.join(' - '));

// Modificar con splice (posición, cuántos borrar, qué insertar)
clubes.splice(1, 1, 'Paris Saint-Germain');
console.log(clubes);

// Ordenar e invertir
const numeros = [10, 2, 33, 4];
console.log(numeros.sort((a, b) => a - b));
console.log(numeros.reverse());
