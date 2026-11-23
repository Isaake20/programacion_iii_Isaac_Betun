// Objeto literal
const jugador = {
    nombre: 'Lionel Messi',
    camiseta: 10,
    pais: 'Argentina',
    clubes: ['Barcelona', 'Paris Saint-Germain', 'Inter Miami'],
    presentarse() {
        return `${this.nombre}, camiseta ${this.camiseta}`;
    }
};

console.log(jugador.nombre);
console.log(jugador['pais']);
console.log(jugador.presentarse());

// Agregar y eliminar propiedades
jugador.balonesDeOro = 8;
delete jugador.pais;
console.log(jugador);

// Recorrer las propiedades
for (const clave in jugador) {
    console.log(`${clave}:`, jugador[clave]);
}
console.log(Object.keys(jugador));
console.log(Object.values(jugador).length);

// Desestructuración
const { nombre, camiseta } = jugador;
console.log(nombre, camiseta);

// Constructor con clase
class Futbolista {
    constructor(nombre, posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
    }
    describir() {
        return `${this.nombre} juega de ${this.posicion}`;
    }
}
const otro = new Futbolista('Julián Álvarez', 'delantero');
console.log(otro.describir());
