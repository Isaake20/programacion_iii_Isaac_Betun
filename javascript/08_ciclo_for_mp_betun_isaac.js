// Práctica 08 (mp) - Ciclo for - Autor: Isaac Betun
const balonesDeOro = [2009, 2010, 2011, 2012, 2015, 2019, 2021, 2023];

for (let i = 0; i < balonesDeOro.length; i++) {
    console.log(`Balón de Oro ${i + 1}: ${balonesDeOro[i]}`);
}

for (const anio of balonesDeOro) {
    if (anio > 2015) console.log('Balón de Oro reciente:', anio);
}

const ficha = { nombre: 'Messi', camiseta: 10, pais: 'Argentina' };
for (const clave in ficha) {
    console.log(clave, '=', ficha[clave]);
}

let fila = '';
for (let i = 1; i <= 10; i++) {
    fila += i % 2 === 0 ? '⚽ ' : '· ';
}
console.log(fila);
