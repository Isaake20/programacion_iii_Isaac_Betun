(function () {
  "use strict";

  // ---- Tema ----
  var root = document.documentElement, tBtn = document.getElementById("themeBtn");
  try { var saved = localStorage.getItem("ctc-theme"); if (saved) root.setAttribute("data-theme", saved); } catch (e) {}
  tBtn.addEventListener("click", function () {
    var cur = root.getAttribute("data-theme");
    var dark = cur ? cur === "dark" : matchMedia("(prefers-color-scheme:dark)").matches;
    var next = dark ? "light" : "dark";
    root.setAttribute("data-theme", next);
    try { localStorage.setItem("ctc-theme", next); } catch (e) {}
  });

  var placaInput = document.getElementById("placa");
  var errEl = document.getElementById("placaErr");
  var goBtn = document.getElementById("goBtn");
  var resultEl = document.getElementById("result");

  var ICON = {
    car: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><path d="M5 11l1.5-4.5A2 2 0 018.4 5h7.2a2 2 0 011.9 1.5L19 11m-14 0h14m-14 0a2 2 0 00-2 2v3h2m14-5a2 2 0 012 2v3h-2M7 16h10"/><circle cx="7.5" cy="16" r="1.4"/><circle cx="16.5" cy="16" r="1.4"/></svg>',
    doc: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><path d="M6 2h9l4 4v16H6z"/><path d="M14 2v5h5M9 13h6M9 17h6M9 9h2"/></svg>',
    ticket: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><path d="M4 7a1 1 0 011-1h14a1 1 0 011 1v3a2 2 0 000 4v3a1 1 0 01-1 1H5a1 1 0 01-1-1v-3a2 2 0 000-4z"/><path d="M9 6v12" stroke-dasharray="2 2"/></svg>'
  };

  function normalize(v) { return (v || "").toUpperCase().replace(/[^A-Z0-9]/g, ""); }
  function classify(p) {
    if (/^[A-Z]{3}[0-9]{3,4}$/.test(p)) return "auto";
    if (/^[A-Z]{2}[0-9]{3}[A-Z]$/.test(p)) return "moto";
    return null;
  }
  function esc(s) {
    return String(s == null ? "" : s).replace(/[&<>"']/g, function (c) {
      return { "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c];
    });
  }
  function money(n) { return "$" + (Number(n) || 0).toFixed(2); }

  placaInput.addEventListener("input", function () {
    var c = placaInput.selectionStart;
    placaInput.value = normalize(placaInput.value).slice(0, 8);
    placaInput.setSelectionRange(c, c);
    errEl.textContent = "";
  });

  function setLoading(on, placa) {
    goBtn.disabled = on;
    goBtn.textContent = on ? "Consultando…" : "Consultar";
    if (on) {
      resultEl.innerHTML =
        '<div class="state"><div class="spinner"></div>Consultando el historial de <b>' + esc(placa) + '</b>…</div>';
    }
  }

  function showError(msg) {
    resultEl.innerHTML =
      '<div class="state error"><b>No se pudo completar la consulta.</b><br>' + esc(msg) + '</div>';
  }

  function plateBadge(placa) {
    return '<span class="plate-badge"><span class="ec">EC</span><span class="num">' + esc(placa) + '</span></span>';
  }

  function render(d) {
    var tipoTxt = d.tipo === "moto" ? "Motocicleta" : "Automóvil";
    var r = d.resumen || {};
    var m = d.matricula || {};
    var v = d.vehiculo || {};

    var html = "";

    if (d.demo) {
      html +=
        '<div class="demo-banner">' +
          '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2"><path d="M12 9v4m0 4h.01M10.3 4.3 2.6 18a2 2 0 0 0 1.7 3h15.4a2 2 0 0 0 1.7-3L13.7 4.3a2 2 0 0 0-3.4 0Z" stroke-linecap="round" stroke-linejoin="round"/></svg>' +
          '<span>Modo demostración: estos son datos de ejemplo. Para datos reales, configura el token del proveedor en el servidor.</span>' +
        '</div>';
    }

    // Ficha
    html +=
      '<div class="ficha"><div class="ficha-left">' + plateBadge(d.placa) +
        '<div class="ficha-meta"><div class="label">VEHÍCULO</div>' +
        '<div class="val">' + esc(v.modelo) + '</div>' +
        '<div class="sub">' + esc(tipoTxt) + ' · ' + esc(v.anio) + ' · ' + esc(v.cantonMatricula) + '</div></div>' +
      '</div></div>';

    // Tiles
    var multClass = r.numPendientes > 0 ? "crit" : "good";
    var matClass = m.vigente === false ? "crit" : (m.vigente === true ? "good" : "");
    html +=
      '<div class="tiles">' +
        '<div class="tile ' + multClass + '"><span class="t-label">Multas pendientes</span>' +
          '<span class="t-val">' + (r.numPendientes || 0) + '</span>' +
          '<span class="t-sub">' + money(r.totalMultas) + ' por pagar</span></div>' +
        '<div class="tile ' + matClass + '"><span class="t-label">Matrícula</span>' +
          '<span class="t-val">' + (m.vigente === false ? "Vencida" : (m.vigente === true ? "Vigente" : "—")) + '</span>' +
          '<span class="t-sub">vence ' + esc(m.vence) + '</span></div>' +
        '<div class="tile"><span class="t-label">Valor a pagar</span>' +
          '<span class="t-val">' + money(m.valorAPagar) + '</span>' +
          '<span class="t-sub">matrícula + rubros</span></div>' +
      '</div>';

    // Alertas
    if (r.tieneBloqueo) {
      html += '<div class="alert crit"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2"><circle cx="12" cy="12" r="9"/><path d="M12 8v4m0 4h.01" stroke-linecap="round"/></svg>Este vehículo tiene un <b>bloqueo</b> por multas: no podrá matricularse ni traspasarse hasta resolverlo.</div>';
    }

    // Datos del vehículo
    html +=
      '<div class="sec"><h2><span class="ico">' + ICON.car + '</span>Datos del vehículo</h2>' +
        '<div class="kv">' +
          kvRow("Modelo", v.modelo) + kvRow("Clase", v.clase) +
          kvRow("Año", v.anio) + kvRow("Cilindraje", v.cilindraje + " cc") +
          kvRow("Servicio", v.servicio) + kvRow("País de fabricación", v.paisFabricacion) +
          kvRow("Cantón de matrícula", v.cantonMatricula) + kvRow("Fecha de compra", v.fechaCompra) +
        '</div></div>';

    // Matrícula e impuestos
    html +=
      '<div class="sec"><h2><span class="ico">' + ICON.doc + '</span>Matrícula e impuestos</h2>' +
        '<div class="kv">' +
          kvRow("Estado", m.estado) +
          kvRow("Matrícula vigente", m.vigente === false ? "No (vencida)" : (m.vigente === true ? "Sí" : "—")) +
          kvRow("Vence", m.vence) + kvRow("Última revisión (RTV)", m.ultimaRevision) +
          kvRow("Último pago", m.ultimoPago) + kvRow("Valor a pagar", money(m.valorAPagar)) +
        '</div></div>';

    // Multas
    html += '<div class="sec"><h2><span class="ico">' + ICON.ticket + '</span>Multas y citaciones</h2>';
    if (!d.multas || d.multas.length === 0) {
      html += '<div class="empty-good"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2"><path d="m5 13 4 4L19 7" stroke-linecap="round" stroke-linejoin="round"/></svg>Sin multas registradas. ¡Buena señal!</div>';
    } else {
      html += '<div class="table-wrap"><table><thead><tr>' +
        '<th>Entidad</th><th>Infracción</th><th>Emisión</th><th>Puntos</th><th>Estado</th><th>Valor</th>' +
        '</tr></thead><tbody>';
      d.multas.forEach(function (mu) {
        var estado = mu.pagada
          ? '<span class="pill good">Pagada</span>'
          : (mu.bloqueo ? '<span class="pill crit">Bloqueo</span>' : '<span class="pill warn">Pendiente</span>');
        html += '<tr>' +
          '<td>' + esc(mu.entidad) + '<br><span style="color:var(--muted);font-size:.8em">' + esc(mu.articulo) + '</span></td>' +
          '<td>' + esc(mu.infraccion) + '</td>' +
          '<td>' + esc((mu.fechaEmision || "").split(" ")[0]) + '</td>' +
          '<td>' + esc(mu.puntos) + '</td>' +
          '<td>' + estado + '</td>' +
          '<td class="money">' + money(mu.total) + '</td>' +
        '</tr>';
      });
      html += '</tbody></table></div>';
    }
    html += '</div>';

    html += '<div class="src-foot">Fuente: ' + esc(d.fuente) + (d.cache ? ' · (caché)' : '') + '</div>';

    resultEl.innerHTML = html;
    resultEl.scrollIntoView({ behavior: "smooth", block: "start" });
  }

  function kvRow(k, v) {
    return '<div class="row"><span class="k">' + esc(k) + '</span><span class="v">' + esc(v) + '</span></div>';
  }

  function consultar(placaRaw) {
    var p = normalize(placaRaw);
    if (!classify(p)) { errEl.textContent = "Revisa la placa: auto ABC1234, moto AB123C."; return; }
    errEl.textContent = "";
    setLoading(true, p);
    fetch("/api/consulta?placa=" + encodeURIComponent(p))
      .then(function (res) { return res.json().then(function (j) { return { ok: res.ok, j: j }; }); })
      .then(function (out) {
        setLoading(false);
        if (!out.ok) { showError(out.j && out.j.error ? out.j.error : "Error del servidor."); return; }
        render(out.j);
      })
      .catch(function () { setLoading(false); showError("No se pudo conectar con el servidor."); });
  }

  goBtn.addEventListener("click", function () { consultar(placaInput.value); });
  placaInput.addEventListener("keydown", function (e) { if (e.key === "Enter") { e.preventDefault(); consultar(placaInput.value); } });
  Array.prototype.forEach.call(document.querySelectorAll(".examples button"), function (b) {
    b.addEventListener("click", function () { placaInput.value = b.getAttribute("data-ex"); consultar(b.getAttribute("data-ex")); });
  });
})();
