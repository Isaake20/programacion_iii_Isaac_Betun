# Chequea tu Carro — Brand Guidelines v1.0

> Última actualización: 2026-11-10
> Estado: Activa
> Producto: herramienta comunitaria y gratuita para revisar un carro/moto usado en Ecuador con fuentes oficiales (ANT, SRI, tránsito municipal).

## Referencia rápida

| Elemento | Valor |
|----------|-------|
| Color primario | #0e8f86 |
| Color primario (dark) | #2dd4bf |
| Franja placa (EC) | #1e3a8a |
| Tipografía display | Archivo |
| Tipografía cuerpo | Public Sans |
| Voz | Cercana, directa, de confianza, comunitaria |

---

## 1. Paleta de color

El sistema es **teal de confianza** como primario (connota "libre / gratis / verificado"), neutros con
un leve sesgo teal, y colores semánticos **separados** del primario para que el estado nunca dependa solo
del color de marca. Tema claro y oscuro con el mismo cuidado.

### Primarios

| Nombre | Hex | RGB | Uso |
|--------|-----|-----|-----|
| Brand Teal | #0e8f86 | rgb(14,143,134) | CTAs, logo, enlaces, acentos |
| Brand Teal Dark | #0a6f68 | rgb(10,111,104) | Hover, énfasis |
| Brand Soft | #e2f2f0 | rgb(226,242,240) | Fondos de chip, icono de fuente |

### Primarios — tema oscuro

| Nombre | Hex | RGB | Uso |
|--------|-----|-----|-----|
| Brand Teal (dark) | #2dd4bf | rgb(45,212,191) | CTAs y acentos sobre fondo oscuro |
| Brand Teal 700 (dark) | #5eead4 | rgb(94,234,212) | Énfasis |
| Brand Soft (dark) | #10322f | rgb(16,50,47) | Fondos sutiles |
| Brand Ink (dark) | #042f2c | rgb(4,47,44) | Texto sobre botón teal brillante |

### Neutros (claro)

| Nombre | Hex | RGB | Uso |
|--------|-----|-----|-----|
| Background | #edf3f2 | rgb(237,243,242) | Fondo de página |
| Surface | #ffffff | rgb(255,255,255) | Tarjetas, hero |
| Surface 2 | #f4f8f7 | rgb(244,248,247) | Campos, cajas internas |
| Ink | #0d1b1a | rgb(13,27,26) | Títulos y cuerpo |
| Muted | #54716e | rgb(84,113,110) | Texto secundario, etiquetas |
| Border | #dce7e5 | rgb(220,231,229) | Bordes y divisores |

### Neutros (oscuro)

| Nombre | Hex | RGB | Uso |
|--------|-----|-----|-----|
| Background | #070f0e | rgb(7,15,14) | Fondo de página |
| Surface | #0f1b19 | rgb(15,27,25) | Tarjetas, hero |
| Surface 2 | #16211f | rgb(22,33,31) | Campos, cajas internas |
| Ink | #e6f1ef | rgb(230,241,239) | Títulos y cuerpo |
| Muted | #8fa7a3 | rgb(143,167,163) | Texto secundario |
| Border | #223531 | rgb(34,53,49) | Bordes y divisores |

### Semánticos

| Estado | Hex | Soft | Uso |
|--------|-----|------|-----|
| Success | #15803d | #dcf2e3 | "Revisado", al día, sin deudas |
| Warning | #b45309 | #f8ebd4 | "Pendiente", precaución |
| Error | #b91c1c | #f7dede | Placa inválida, alertas (no comprar) |

> Dark: Success #4ade80, Warning #fbbf24, Error #f87171 (sobre fondos soft oscuros).

### Marca de placa (objeto físico)

La placa se trata como objeto real y **no** cambia con el tema: fondo claro, tinta oscura y franja azul "EC".

| Elemento | Hex |
|----------|-----|
| Fondo placa | #f7fafc |
| Tinta placa | #0b1120 |
| Franja EC | #1e3a8a |

### Accesibilidad

- Objetivo: **WCAG 2.1 AA** (texto normal 4.5:1, texto grande / UI 3:1).
- Ink sobre Surface (claro y oscuro): contraste muy alto, cumple AAA para cuerpo.
- Brand Teal #0e8f86 se usa para UI, íconos y texto grande/semibold; para texto pequeño sobre blanco
  preferir Brand Teal Dark #0a6f68. **Verificar** combinaciones nuevas en un contrast checker.
- El estado nunca se comunica solo con color: siempre acompaña icono o etiqueta ("Pendiente" / "Revisado").

---

## 2. Tipografía

### Font stack

```css
--font-display: "Archivo", system-ui, sans-serif;      /* títulos, wordmark, placas, botones */
--font-body:    "Public Sans", system-ui, -apple-system, sans-serif; /* cuerpo y UI */
```

**Public Sans** es una tipografía de uso cívico/público: refuerza el mensaje de "datos públicos".
**Archivo** aporta títulos con carácter sin caer en los defaults sobreusados.

### Carga

```html
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Archivo:wght@500;600;700;800;900&family=Public+Sans:ital,wght@0,400;0,500;0,600;1,400&display=swap" rel="stylesheet">
```

### Escala tipográfica

| Elemento | Tamaño | Peso | Familia |
|----------|--------|------|---------|
| H1 (hero) | ~1.9rem (1.6rem móvil) | 800 | Archivo |
| H3 (tarjeta) | 1.08rem | 800 | Archivo |
| Wordmark | 1.18rem | 900 | Archivo |
| Cuerpo | 1rem | 400 | Public Sans |
| Tagline | 0.96rem | 400 | Public Sans |
| Etiqueta / caption | 0.72–0.84rem | 600–700 | Public Sans |
| Placa (número) | 1.15–1.5rem, letter-spacing .14–.22em | 800 | Archivo |

Usar `text-wrap: balance` en títulos y `font-variant-numeric: tabular-nums` donde se alineen cifras.

---

## 3. Logo

### Marca
Escudo con **check** interior = verificación y confianza. Trazo redondeado, color `--brand`.
Se entrega inline en SVG (ver el componente en `index.html`).

### Variantes

| Variante | Uso |
|----------|-----|
| Lockup horizontal (marca + wordmark + bajada) | Cabecera |
| Solo marca (escudo+check) | Favicon, espacios pequeños |
| Monocromo | Contextos de un solo color |

- **Espacio libre mínimo:** la altura de la marca alrededor del lockup.
- **Tamaño mínimo:** marca 24px; lockup 120px de ancho.

### No hacer
- No rotar, inclinar ni deformar.
- No cambiar el color fuera de la paleta aprobada.
- No añadir sombras o efectos al escudo.
- No colocar sobre fondos ocupados sin contraste suficiente.

---

## 4. Voz y tono

Hablamos como **un amigo que sabe de carros** y quiere que no te estafen: claro, directo y de confianza.
El servicio es gratis y lo decimos sin letra pequeña.

### Personalidad

| Rasgo | Descripción |
|-------|-------------|
| **Cercana** | Lenguaje cotidiano ecuatoriano, sin tecnicismos innecesarios. |
| **Directa** | Va al grano: qué revisar y qué significa. |
| **De confianza** | Transparente sobre qué es oficial, qué es gratis y qué no hacemos. |
| **Comunitaria** | Para la gente, no para vender. Código abierto. |

### Somos / No somos

| Rasgo | Somos | No somos |
|-------|-------|----------|
| Cercana | Claros, humanos | Burocráticos, fríos |
| Directa | Concretos | Vagos, relleno |
| De confianza | Honestos sobre límites | Promesas exageradas |
| Comunitaria | Gratis, abiertos | Intermediarios que cobran |

### Tono por contexto

| Contexto | Tono | Ejemplo |
|----------|------|---------|
| Hero / marketing | Motivador, útil | "Revisa un usado antes de comprarlo." |
| Instrucciones | Claro, paso a paso | "Pega la placa en Consulta de citaciones." |
| Errores | Calmado, con solución | "Revisa la placa: auto ABC1234, moto AB123C." |
| Éxito | Breve | "¡Placa copiada!" |

### Términos a evitar

| Evitar | Motivo |
|--------|--------|
| "Reporte premium" | Sugiere pago; aquí todo es gratis |
| "Datos exclusivos" | Son datos públicos |
| "Al instante / mágico" | El paso oficial lo hace el usuario |
| Anglicismos innecesarios | Rompe la cercanía |

---

## 5. Iconografía

- Estilo: **outline**, grilla base 24px.
- Trazo: 1.7px, `stroke-linecap/linejoin: round`.
- Sin relleno (solo contorno), color heredado del contenedor.
- Cada fuente tiene su icono (ANT = edificio público, SRI = documento, Municipal = pin, Revisión = llave).
- Contenedor de icono: 42px, radio 11px, fondo `--brand-050`, color `--brand`
  (pasa a verde `--good` cuando la fuente está marcada como revisada).

---

## 6. Componentes

### Botones

| Tipo | Fondo | Texto | Radio |
|------|-------|-------|-------|
| Primario | #0e8f86 | #ffffff | 11px |
| Secundario (alt) | Surface 2 | Ink + borde | 11px |
| Copiar (estado) | Surface 2 → Success soft al copiar | Ink → Success | 11px |

### Radio de borde

| Elemento | Radio |
|----------|-------|
| Botones / inputs / chips grandes | 11px |
| Tarjetas / hero | 16px |
| Pills de estado | 999px |
| Placa | 7–10px |

### Elevación

```css
--shadow: 0 1px 2px rgba(13,27,26,.05), 0 6px 20px -8px rgba(13,27,26,.14);
```

### Patrones propios
- **Input de placa:** caja estilo matrícula ecuatoriana con franja azul "EC" a la izquierda; texto centrado, grande y espaciado.
- **Ficha-resumen:** placa + tipo de vehículo + anillo de progreso (SVG) con % y conteo de fuentes revisadas.
- **Tarjeta de fuente:** icono, título, pill de estado (Pendiente/Revisado), "Qué necesitas" + pasos, lista de cómo leer el resultado, y acciones (abrir oficial + copiar placa).
- **Pill de estado:** Warning soft = Pendiente; Success soft = Revisado.

---

## 7. Principios de producto (no negociables)

1. **Siempre gratis.** Nunca se cobra por datos públicos.
2. **Fuentes oficiales .gob.ec.** Enlazamos a la entidad; no revendemos ni scrapeamos su servicio protegido.
3. **No guardamos datos.** Todo ocurre en el navegador del usuario (localStorage para su propio progreso).
4. **El paso con captcha lo hace el usuario** en el sitio oficial: así es legal y transparente.
5. **Honestidad sobre límites.** Si algo requiere trámite oficial o pago estatal, se dice.

---

## Changelog

| Versión | Fecha | Cambios |
|---------|-------|---------|
| 1.0 | 2026-11-10 | Guía inicial a partir del rediseño de la herramienta (marca, dashboard, paleta teal EC). |
