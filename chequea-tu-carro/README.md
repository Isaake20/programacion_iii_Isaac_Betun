# Chequea tu Carro 🚗🇪🇨

Consulta gratuita del historial de un vehículo en Ecuador — datos del vehículo, matrícula,
impuestos y multas — **mostrados dentro de la misma página**. Hecho para la comunidad.

No scrapea ni rompe captchas de la ANT/SRI. Los datos se obtienen a través de un **proveedor de
datos autorizado** (por defecto [Verifik](https://verifik.co)) usando tu token, que vive **solo en
el servidor** y nunca llega al navegador.

## Cómo funciona

```
Navegador ──▶ /api/consulta?placa=XXX ──▶ server.js ──▶ Proveedor licenciado (con tu token)
   ▲                                          │
   └────────────  JSON normalizado  ◀─────────┘
```

- **Backend** (`server.js`, Node puro, sin dependencias): valida la placa, llama al proveedor,
  normaliza la respuesta, cachea por placa y limita consultas por IP.
- **Frontend** (`public/`): pinta los datos en la página (ficha, tiles de resumen, tabla de multas).
- **Modo DEMO**: si no hay token, arranca con datos de ejemplo para no bloquear el desarrollo.

## Arrancar en 30 segundos (modo demo)

```bash
cd chequea-tu-carro
cp .env.example .env     # opcional; sin token arranca en modo DEMO
npm start
```

Abre http://localhost:3000 y prueba con `PIS0113` (auto) o `KB485G` (moto).

> Requiere Node 18+ (usa `fetch` nativo). No hay `npm install`: cero dependencias.

## Pasar a datos reales

1. Crea una cuenta en el proveedor y obtén tu **token Bearer**.
2. Ponlo en `.env`:
   ```
   VERIFIK_TOKEN=tu_token_aqui
   ```
3. Reinicia (`npm start`). La pantalla deja de mostrar el aviso "Modo demostración".

El endpoint usado es `GET /v2/ec/vehiculo/placa/multas?plate=XXX`, que devuelve datos del
vehículo + multas en una sola llamada. Si cambias de proveedor, ajusta `lib/providers.js`
(`fetchVerifik` y `normalize`).

## Validación de placas

| Tipo | Formato | Ejemplo |
|------|---------|---------|
| Auto | 3 letras + 3/4 dígitos | `PIS0113`, `ABC123` |
| Moto | 2 letras + 3 dígitos + 1 letra | `KB485G` |

Para autos con 3 dígitos, el proveedor pide anteponer un `0` (`ABC123` → `ABC0123`); el backend lo hace solo.

## Variables de entorno

| Variable | Default | Qué hace |
|----------|---------|----------|
| `PORT` | `3000` | Puerto del servidor |
| `VERIFIK_TOKEN` | *(vacío)* | Token del proveedor. Vacío = modo DEMO |
| `VERIFIK_BASE` | `https://api.verifik.co` | Base del proveedor |
| `CACHE_TTL_SECONDS` | `600` | Cuánto cachear cada placa (ahorra consultas/dinero) |
| `RATE_LIMIT_PER_MIN` | `20` | Consultas por IP por minuto |

## Costos y legalidad

- La ANT y el SRI **no** tienen API pública; sus portales están protegidos. Por eso este proyecto
  usa un **proveedor autorizado de pago por consulta**: es la vía legal de traer los datos a tu página.
- Para que siga siendo **gratis para la comunidad**, el operador (tú) asume el costo por consulta.
  La caché y el rate-limit están para controlar ese gasto.
- Verifica siempre los datos sensibles en los portales oficiales `.gob.ec` antes de comprar o traspasar.

## Licencia

MIT. Úsalo, modifícalo y compártelo.
