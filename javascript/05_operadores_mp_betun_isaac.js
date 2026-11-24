// Práctica 05 (mp) - Operadores - Autor: Isaac Betun
const golesLiga = 30, golesCopa = 8;
console.log('Suma:', golesLiga + golesCopa);
console.log('Resta:', golesLiga - golesCopa);
console.log('Multiplicación (3 temporadas):', golesLiga * 3);
console.log('División (por mes):', golesLiga / 10);
console.log('Módulo:', golesLiga % 7);
console.log('Potencia:', 2 ** 10);

let asistencias = 10;
asistencias += 5;
asistencias++;
console.log('Asistencias:', asistencias);

console.log('Igual valor:', 10 == '10');
console.log('Igual valor y tipo:', 10 === '10');
console.log('Distinto:', golesLiga !== golesCopa);
console.log('Mayor:', golesLiga > golesCopa);

const titular = true, lesionado = false;
console.log('Juega (AND):', titular && !lesionado);
console.log('Va a la lista (OR):', titular || lesionado);

const club = null;
console.log('Club (??):', club ?? 'Sin club asignado');
console.log('Ternario:', golesLiga >= 30 ? 'Pichichi' : 'Sigue intentando');
