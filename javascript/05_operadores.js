console.log('Operadores aritméticos');
let goles = 10;
let asistencias = 5;
console.log('suma', goles + asistencias);
console.log('resta', goles - asistencias);
console.log('multiplicación', goles * asistencias);
console.log('división', goles / asistencias);
console.log('módulo', goles % asistencias);

console.log('Operadores de comparación');
console.log('goles == asistencias', goles == asistencias);
console.log('goles != asistencias', goles != asistencias);
console.log('goles > asistencias', goles > asistencias);
console.log('goles < asistencias', goles < asistencias);
console.log('goles >= asistencias', goles >= asistencias);
console.log('goles <= asistencias', goles <= asistencias);

console.log('Operadores lógicos');
let juega = true;
let lesionado = false;
console.log('juega AND lesionado', juega && lesionado);
console.log('juega OR lesionado', juega || lesionado);
console.log('NOT juega', !juega);

console.log('Operadores de asignación');
let puntos = 10;
puntos += 5;
console.log('puntos += 5:', puntos);
puntos -= 3;
console.log('puntos -= 3:', puntos);
puntos *= 2;
console.log('puntos *= 2:', puntos);
puntos /= 4;
console.log('puntos /= 4:', puntos);
puntos %= 3;
console.log('puntos %= 3:', puntos);

console.log('Incremento y decremento');
let partidos = 5;
partidos++;
console.log('partidos++:', partidos);
partidos--;
console.log('partidos--:', partidos);

console.log('Concatenación');
let nombre = 'Lionel';
let apellido = 'Messi';
console.log('Nombre completo:', nombre + ' ' + apellido);

console.log('Comparación estricta');
let numero = 10;
let texto = '10';
console.log('numero == texto', numero == texto);
console.log('numero === texto', numero === texto);
