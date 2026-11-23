const goles = [3, 1, 0, 2, 4, 1];

// forEach: ejecuta una función por cada elemento
goles.forEach((g, i) => console.log(`Partido ${i + 1}: ${g} goles`));

// map: transforma cada elemento y devuelve otro arreglo
const dobles = goles.map((g) => g * 2);
console.log(dobles);

// filter: se queda con los elementos que cumplen la condición
const conGol = goles.filter((g) => g > 0);
console.log(conGol);

// find: primer elemento que cumple
console.log(goles.find((g) => g >= 3));

// reduce: acumula todos los valores en uno solo
const total = goles.reduce((acum, g) => acum + g, 0);
console.log(`Total de goles: ${total}`);

// some y every
console.log(goles.some((g) => g === 0));
console.log(goles.every((g) => g >= 0));

// for...of con índice usando entries()
for (const [i, g] of goles.entries()) {
    console.log(i, g);
}
