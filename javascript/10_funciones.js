// Declaración de función
function saludar(nombre) {
    return `Hola, ${nombre}`;
}
console.log(saludar('Lionel'));

// Función con parámetros por defecto
function presentar(nombre, club = 'Inter Miami') {
    return `${nombre} juega en ${club}`;
}
console.log(presentar('Messi'));
console.log(presentar('Messi', 'Barcelona'));

// Función anónima guardada en una variable
const sumar = function (a, b) {
    return a + b;
};
console.log(sumar(8, 2));

// Función flecha
const promedio = (a, b, c) => (a + b + c) / 3;
console.log(promedio(10, 8, 9));

// Función que recibe otra función (callback)
function repetir(veces, accion) {
    for (let i = 1; i <= veces; i++) {
        accion(i);
    }
}
repetir(3, (n) => console.log(`Gol ${n}`));
