"use strict";

/**
 * Validación, proveedores de datos y normalización.
 *
 * Reglas de placa (Ecuador):
 *   - Auto: 3 letras + 3 o 4 dígitos  (ABC1234 / ABC123)
 *   - Moto: 2 letras + 3 dígitos + 1 letra (AB123C)
 *
 * La app nunca scrapea ni rompe captchas de la ANT/SRI. Los datos vienen de
 * un proveedor licenciado (Verifik) con tu token, o de datos DEMO si no hay token.
 */

function normalizePlate(v) {
  return String(v || "").toUpperCase().replace(/[^A-Z0-9]/g, "");
}

function classify(p) {
  if (/^[A-Z]{3}[0-9]{3,4}$/.test(p)) return "auto";
  if (/^[A-Z]{2}[0-9]{3}[A-Z]$/.test(p)) return "moto";
  return null;
}

// El proveedor espera 7 caracteres para autos; si trae 3 dígitos, anteponer 0.
function providerPlate(p) {
  const m = p.match(/^([A-Z]{3})([0-9]{3})$/);
  return m ? m[1] + "0" + m[2] : p;
}

function toNumber(x) {
  const n = parseFloat(String(x == null ? "" : x).replace(",", "."));
  return isNaN(n) ? 0 : n;
}

// "YYYY/MM/DD" o "YYYY-MM-DD" -> Date | null
function parseDate(s) {
  if (!s) return null;
  const m = String(s).match(/(\d{4})[/-](\d{2})[/-](\d{2})/);
  if (!m) return null;
  const d = new Date(Number(m[1]), Number(m[2]) - 1, Number(m[3]));
  return isNaN(d.getTime()) ? null : d;
}

/**
 * Mapea la respuesta del proveedor (Verifik, endpoint .../placa/multas) al
 * formato estable que consume el frontend.
 */
function normalize(data, placa, tipo, demo) {
  const multasRaw = Array.isArray(data.multas) ? data.multas : [];
  const multas = multasRaw.map(function (m, i) {
    return {
      id: m.id || String(i + 1),
      entidad: m.entidad || "—",
      infraccion: m.infraccion || "",
      citacion: m.citacion || "",
      articulo: m.articuloliteral || m.articulo || "",
      fechaEmision: m.fechaDeEmision || m.fechaNotificacion || "",
      fechaLimite: m.fechaLimiteDePago || "",
      puntos: toNumber(m.puntos),
      total: toNumber(m.totalAPagar != null ? m.totalAPagar : m.multa),
      pagada: String(m.pag || "").toUpperCase() === "S",
      bloqueo: String(m.bloqueo || "").toUpperCase() === "S"
    };
  });

  const pendientes = multas.filter(function (m) { return !m.pagada; });
  const totalMultas = pendientes.reduce(function (s, m) { return s + m.total; }, 0);
  const vence = parseDate(data.registrationExpirationDate);
  const hoy = new Date(); hoy.setHours(0, 0, 0, 0);

  return {
    placa: placa,
    tipo: tipo,
    demo: !!demo,
    fuente: demo ? "DEMO (datos de ejemplo)" : "Proveedor licenciado · registros oficiales EC",
    consultadoEn: new Date().toISOString(),
    vehiculo: {
      modelo: data.model || "—",
      clase: data.class || (tipo === "moto" ? "MOTOCICLETA" : "AUTOMOVIL"),
      anio: data.year || "—",
      cilindraje: data.cylinderCapacity || "—",
      servicio: data.service || "—",
      paisFabricacion: data.manufactureCountry || "—",
      cantonMatricula: data.registrationCanton || "—",
      fechaCompra: data.purchaseDate || "—",
      identificador: data.camvCpn || "—"
    },
    matricula: {
      estado: data.status || "—",
      ultimaRevision: data.lastCheckUpDate || "—",
      ultimoPago: data.lastPaymentYear || "—",
      ultimaMatricula: data.lastRegistrationDate || "—",
      vence: data.registrationExpirationDate || "—",
      vigente: vence ? vence >= hoy : null,
      valorAPagar: toNumber(data.total)
    },
    multas: multas,
    resumen: {
      numMultas: multas.length,
      numPendientes: pendientes.length,
      totalMultas: Math.round(totalMultas * 100) / 100,
      tieneBloqueo: multas.some(function (m) { return m.bloqueo; }),
      valorMatricula: toNumber(data.total)
    }
  };
}

/* ----------------------- Proveedor real: Verifik ----------------------- */

async function fetchVerifik(placa, tipo, cfg) {
  const url =
    cfg.base.replace(/\/+$/, "") +
    "/v2/ec/vehiculo/placa/multas?plate=" +
    encodeURIComponent(providerPlate(placa));

  const ctrl = new AbortController();
  const timer = setTimeout(function () { ctrl.abort(); }, cfg.timeoutMs || 15000);
  let res;
  try {
    res = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + cfg.token
      },
      signal: ctrl.signal
    });
  } catch (e) {
    clearTimeout(timer);
    const msg = e && e.name === "AbortError" ? "El proveedor tardó demasiado en responder." : "No se pudo contactar al proveedor de datos.";
    const err = new Error(msg); err.httpStatus = 504; throw err;
  }
  clearTimeout(timer);

  if (res.status === 401 || res.status === 403) {
    const err = new Error("El token del proveedor es inválido o no tiene permisos."); err.httpStatus = 502; throw err;
  }
  if (res.status === 404) {
    const err = new Error("No se encontró información para esa placa."); err.httpStatus = 404; throw err;
  }
  if (!res.ok) {
    const err = new Error("El proveedor respondió con un error (" + res.status + ")."); err.httpStatus = 502; throw err;
  }

  let json;
  try { json = await res.json(); } catch (e) {
    const err = new Error("El proveedor devolvió una respuesta no válida."); err.httpStatus = 502; throw err;
  }
  const data = json && json.data ? json.data : json;
  if (!data || (typeof data === "object" && Object.keys(data).length === 0)) {
    const err = new Error("No se encontró información para esa placa."); err.httpStatus = 404; throw err;
  }
  return normalize(data, placa, tipo, false);
}

/* --------------------------- Datos DEMO -------------------------------- */

const DEMO_DB = {
  PIS0113: {
    model: "CHEVROLET AVEO FAMILY 1.5",
    class: "AUTOMOVIL",
    year: "2015",
    cylinderCapacity: "1500",
    service: "PARTICULAR",
    manufactureCountry: "ECUADOR",
    registrationCanton: "QUITO",
    purchaseDate: "2015/02/18",
    camvCpn: "T00512233",
    status: "ASIGNADO",
    lastCheckUpDate: "2026/03/18",
    lastPaymentYear: "2026",
    lastRegistrationDate: "2026/03/20",
    registrationExpirationDate: "2027/03/20",
    total: "64.30",
    multas: [
      {
        id: "1", infraccion: "92507343", entidad: "AMT - QUITO", citacion: "N-ACT0349-00184",
        articuloliteral: "Art. 391 - Lit. 05. Estacionar en sitios prohibidos",
        fechaDeEmision: "2024-03-11 09:43:26.0", fechaLimiteDePago: "21-03-2024 09:43:26",
        puntos: "0", pag: "N", bloqueo: "N", totalAPagar: "23.00"
      }
    ]
  },
  KB485G: {
    model: "YAMAHA FZ 150",
    class: "MOTOCICLETA",
    year: "2019",
    cylinderCapacity: "150",
    service: "PARTICULAR",
    manufactureCountry: "COLOMBIA",
    registrationCanton: "GUAYAQUIL",
    purchaseDate: "2019/05/09",
    camvCpn: "T00890145",
    status: "ASIGNADO",
    lastCheckUpDate: "2023/08/31",
    lastPaymentYear: "2023",
    lastRegistrationDate: "2023/09/02",
    registrationExpirationDate: "2024/09/02",
    total: "41.80",
    multas: [
      {
        id: "1", infraccion: "77120994", entidad: "ATM - GUAYAQUIL", citacion: "G-ATM-0098-55120",
        articuloliteral: "Art. 386. No portar casco de seguridad",
        fechaDeEmision: "2023-11-20 14:10:00.0", fechaLimiteDePago: "30-11-2023 14:10:00",
        puntos: "4.5", pag: "N", bloqueo: "N", totalAPagar: "46.25"
      },
      {
        id: "2", infraccion: "77140771", entidad: "ANT", citacion: "N-ANT-0441-77001",
        articuloliteral: "Art. 390. Exceso de velocidad (rango moderado)",
        fechaDeEmision: "2024-01-05 08:02:11.0", fechaLimiteDePago: "15-01-2024 08:02:11",
        puntos: "6", pag: "S", bloqueo: "N", totalAPagar: "46.25"
      }
    ]
  }
};

function demoData(placa, tipo) {
  const base = DEMO_DB[placa] || {
    model: tipo === "moto" ? "MOTOCICLETA (ejemplo)" : "AUTOMÓVIL (ejemplo)",
    class: tipo === "moto" ? "MOTOCICLETA" : "AUTOMOVIL",
    year: "2018",
    cylinderCapacity: tipo === "moto" ? "200" : "1600",
    service: "PARTICULAR",
    manufactureCountry: "—",
    registrationCanton: "—",
    purchaseDate: "—",
    camvCpn: "—",
    status: "ASIGNADO",
    lastCheckUpDate: "—",
    lastPaymentYear: "2024",
    lastRegistrationDate: "—",
    registrationExpirationDate: "2025/12/31",
    total: "0.00",
    multas: []
  };
  return normalize(base, placa, tipo, true);
}

module.exports = {
  normalizePlate: normalizePlate,
  classify: classify,
  providerPlate: providerPlate,
  fetchVerifik: fetchVerifik,
  demoData: demoData,
  normalize: normalize
};
