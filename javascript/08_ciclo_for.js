// Ciclo for: repite un bloque un número conocido de veces
console.log('Goles de Messi por temporada con el Barcelona (muestra)');
for (let temporada = 1; temporada <= 5; temporada++) {
    console.log(`Temporada ${temporada}`);
}

// Recorrer en orden inverso
console.log('Cuenta regresiva para el pitazo inicial');
for (let i = 10; i >= 1; i -= 3) {
    console.log(i);
}

// Tabla de multiplicar de la camiseta número 10
for (let i = 1; i <= 10; i++) {
    console.log(`10 x ${i} = ${10 * i}`);
}

// for...of recorre los valores de una cadena
for (const letra of 'MESSI') {
    console.log(letra);
}
