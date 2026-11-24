"use strict";

/**
 * Chequea tu Carro — servidor.
 *
 * - Sirve el frontend estático desde ./public
 * - Expone GET /api/consulta?placa=XXX  -> datos del vehículo en JSON
 *
 * El backend es quien habla con el proveedor de datos (así el token nunca
 * llega al navegador). Sin VERIFIK_TOKEN arranca en modo DEMO.
 *
 * Carga .env si existe, sin dependencias externas.
 */

const http = require("http");
const fs = require("fs");
const path = require("path");
const url = require("url");
const providers = require("./lib/providers");

// --- carga simple de .env (sin dependencias) ---
(function loadEnv() {
  try {
    const envPath = path.join(__dirname, ".env");
    if (!fs.existsSync(envPath)) return;
    const txt = fs.readFileSync(envPath, "utf8");
    txt.split(/\r?\n/).forEach(function (line) {
      const m = line.match(/^\s*([A-Z0-9_]+)\s*=\s*(.*)\s*$/i);
      if (!m) return;
      const key = m[1];
      let val = m[2];
      if (/^".*"$/.test(val) || /^'.*'$/.test(val)) val = val.slice(1, -1);
      if (process.env[key] === undefined) process.env[key] = val;
    });
  } catch (e) { /* ignore */ }
})();

const PORT = Number(process.env.PORT) || 3000;
const TOKEN = (process.env.VERIFIK_TOKEN || "").trim();
const BASE = (process.env.VERIFIK_BASE || "https://api.verifik.co").trim();
const CACHE_TTL = (Number(process.env.CACHE_TTL_SECONDS) || 600) * 1000;
const RATE_LIMIT = Number(process.env.RATE_LIMIT_PER_MIN) || 20;
const DEMO_MODE = TOKEN.length === 0;
const PUBLIC_DIR = path.join(__dirname, "public");

// --- caché en memoria por placa ---
const cache = new Map(); // placa -> { ts, payload }
function cacheGet(k) {
  const hit = cache.get(k);
  if (hit && Date.now() - hit.ts < CACHE_TTL) return hit.payload;
  if (hit) cache.delete(k);
  return null;
}
function cacheSet(k, payload) { cache.set(k, { ts: Date.now(), payload: payload }); }

// --- rate limit simple por IP ---
const hits = new Map(); // ip -> [timestamps]
function rateLimited(ip) {
  const now = Date.now();
  const arr = (hits.get(ip) || []).filter(function (t) { return now - t < 60000; });
  arr.push(now);
  hits.set(ip, arr);
  return arr.length > RATE_LIMIT;
}

// --- estáticos ---
const MIME = {
  ".html": "text/html; charset=utf-8",
  ".css": "text/css; charset=utf-8",
  ".js": "text/javascript; charset=utf-8",
  ".json": "application/json; charset=utf-8",
  ".svg": "image/svg+xml",
  ".ico": "image/x-icon"
};

function sendJson(res, status, obj) {
  const body = JSON.stringify(obj);
  res.writeHead(status, {
    "Content-Type": "application/json; charset=utf-8",
    "Cache-Control": "no-store"
  });
  res.end(body);
}

function serveStatic(req, res, pathname) {
  let rel = pathname === "/" ? "/index.html" : pathname;
  // evitar path traversal
  const safe = path.normalize(rel).replace(/^(\.\.[/\\])+/, "");
  const file = path.join(PUBLIC_DIR, safe);
  if (!file.startsWith(PUBLIC_DIR)) { res.writeHead(403); return res.end("Forbidden"); }
  fs.readFile(file, function (err, data) {
    if (err) {
      // fallback a index.html para rutas del frontend
      fs.readFile(path.join(PUBLIC_DIR, "index.html"), function (e2, html) {
        if (e2) { res.writeHead(404); return res.end("Not found"); }
        res.writeHead(200, { "Content-Type": MIME[".html"] });
        res.end(html);
      });
      return;
    }
    const ext = path.extname(file).toLowerCase();
    res.writeHead(200, { "Content-Type": MIME[ext] || "application/octet-stream" });
    res.end(data);
  });
}

async function handleConsulta(req, res, query) {
  const ip = (req.headers["x-forwarded-for"] || req.socket.remoteAddress || "local").split(",")[0].trim();
  if (rateLimited(ip)) {
    return sendJson(res, 429, { error: "Demasiadas consultas en poco tiempo. Espera un momento." });
  }

  const placa = providers.normalizePlate(query.placa);
  const tipo = providers.classify(placa);
  if (!tipo) {
    return sendJson(res, 400, { error: "Placa inválida. Usa auto ABC1234 o moto AB123C." });
  }

  const cached = cacheGet(placa);
  if (cached) return sendJson(res, 200, Object.assign({}, cached, { cache: true }));

  try {
    let payload;
    if (DEMO_MODE) {
      payload = providers.demoData(placa, tipo);
    } else {
      payload = await providers.fetchVerifik(placa, tipo, { token: TOKEN, base: BASE, timeoutMs: 15000 });
    }
    cacheSet(placa, payload);
    return sendJson(res, 200, payload);
  } catch (e) {
    const status = e && e.httpStatus ? e.httpStatus : 500;
    return sendJson(res, status, { error: e && e.message ? e.message : "Error consultando el vehículo." });
  }
}

const server = http.createServer(function (req, res) {
  const parsed = url.parse(req.url, true);
  const pathname = parsed.pathname;

  if (pathname === "/api/health") {
    return sendJson(res, 200, { ok: true, demo: DEMO_MODE });
  }
  if (pathname === "/api/consulta" && req.method === "GET") {
    return handleConsulta(req, res, parsed.query);
  }
  if (req.method === "GET") {
    return serveStatic(req, res, pathname);
  }
  res.writeHead(405); res.end("Method not allowed");
});

server.listen(PORT, function () {
  console.log("");
  console.log("  Chequea tu Carro  ->  http://localhost:" + PORT);
  console.log("  Modo: " + (DEMO_MODE
    ? "DEMO (sin VERIFIK_TOKEN) — datos de ejemplo"
    : "REAL — usando proveedor licenciado"));
  console.log("  Caché: " + (CACHE_TTL / 1000) + "s · Rate limit: " + RATE_LIMIT + "/min");
  console.log("");
});
