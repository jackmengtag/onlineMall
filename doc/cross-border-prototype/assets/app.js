/* =========================================================================
 * app.js — 共享工具层（格式化 / 币种 / 购物车 / 提示 / 弹层 / 图片降级 / 组件渲染）
 * 状态、数据、视图分离：本文件负责状态与通用视图片段，数据统一来自 mock.js
 * ========================================================================= */
(function (g) {
  /* ---------- 币种 ---------- */
  var CUR_KEY = "nc_currency";
  var CUR_SYMBOL = { USD: "$", EUR: "€", CNY: "¥" };
  function currency() { return localStorage.getItem(CUR_KEY) || "USD"; }
  function setCurrency(c) { localStorage.setItem(CUR_KEY, c); }
  function symbolOf(c) { return CUR_SYMBOL[c || currency()] || "$"; }

  /* 金额：mock 中金额为最小货币单位（分）→ 展示两位小数 */
  function money(minor, cur) {
    var c = cur || currency();
    var v = (minor / 100);
    return symbolOf(c) + v.toLocaleString("en-US", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }
  function moneyRaw(minor) { return (minor / 100).toLocaleString("en-US", { minimumFractionDigits: 2, maximumFractionDigits: 2 }); }
  /* 商品多币种单价（整数面额，mock 直接给出各币种） */
  function priceOf(product, cur) {
    var c = cur || currency();
    return product.price[c] != null ? product.price[c] : product.price.USD;
  }
  function wasOf(product, cur) {
    var c = cur || currency();
    if (!product.was) return null;
    return product.was[c] != null ? product.was[c] : product.was.USD;
  }

  /* ---------- DOM ---------- */
  function qs(sel, root) { return (root || document).querySelector(sel); }
  function qsa(sel, root) { return Array.prototype.slice.call((root || document).querySelectorAll(sel)); }
  function esc(s) { return String(s == null ? "" : s).replace(/[&<>"']/g, function (m) { return ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[m]; }); }

  /* 图片降级：网络不可用时显示渐变占位（非空态，保证布局稳定） */
  var FALLBACK_BG = ["linear-gradient(135deg,#1B6360,#0C2E2C)", "linear-gradient(135deg,#C9622B,#96441F)", "linear-gradient(135deg,#2B6484,#12403D)", "linear-gradient(135deg,#D9A441,#96441F)"];
  g.__imgFail = function (el, text) {
    var seed = (String(text || "N").charCodeAt(0) || 78);
    var wrap = document.createElement("div");
    wrap.className = "img-fallback";
    wrap.style.background = FALLBACK_BG[seed % FALLBACK_BG.length];
    wrap.textContent = (String(text || "N").trim()[0] || "N").toUpperCase();
    el.parentNode.replaceChild(wrap, el);
  };
  function imgTag(src, alt, cls, fallbackText) {
    return '<img class="' + (cls || "") + '" src="' + esc(src) + '" alt="' + esc(alt) + '" loading="lazy" onerror="__imgFail(this,\'' + esc(fallbackText || alt).replace(/'/g, "") + '\')">';
  }

  /* ---------- Badge ---------- */
  function badge(map, key) {
    var s = map[key] || { label: key, cls: "neutral" };
    return '<span class="badge ' + s.cls + '"><i class="dt"></i>' + esc(s.label) + "</span>";
  }
  function stars(rating) {
    return '<span class="star">' + icon("star", { size: 13 }) + "<b>" + rating.toFixed(1) + "</b></span>";
  }

  /* ---------- Toast ---------- */
  function toast(msg, type) {
    var wrap = qs(".toast-wrap");
    if (!wrap) { wrap = document.createElement("div"); wrap.className = "toast-wrap"; document.body.appendChild(wrap); }
    var ic = type === "error" ? "alert-triangle" : type === "info" ? "info" : "check-circle";
    var el = document.createElement("div");
    el.className = "toast " + (type || "success");
    el.innerHTML = icon(ic, { size: 18 }) + "<span>" + esc(msg) + "</span>";
    wrap.appendChild(el);
    setTimeout(function () { el.style.transition = "opacity .3s"; el.style.opacity = "0"; setTimeout(function () { el.remove(); }, 320); }, 2600);
  }

  /* ---------- Modal / Drawer ---------- */
  function openModal(id) { var m = qs("#" + id); if (m) m.classList.add("open"); }
  function closeModal(id) { var m = qs("#" + id); if (m) m.classList.remove("open"); }
  function openDrawer(id) { var d = qs("#" + id); if (d) d.classList.add("open"); }
  function closeDrawer(id) { var d = qs("#" + id); if (d) d.classList.remove("open"); }

  var AUTH_KEY = "nc_session";
  function session() { try { return JSON.parse(localStorage.getItem(AUTH_KEY) || "null"); } catch (e) { return null; } }
  function requireAuth(next) { if (session()) return true; location.href = "login.html?redirect=" + encodeURIComponent(next || location.pathname.split("/").pop()); return false; }

  /* ---------- 购物车（localStorage 持久化） ---------- */
  var CART_KEY = "nc_cart";
  function cartRaw() { try { return JSON.parse(localStorage.getItem(CART_KEY) || "[]"); } catch (e) { return []; } }
  function cartSave(items) { localStorage.setItem(CART_KEY, JSON.stringify(items)); refreshCartBadge(); }
  var Cart = {
    items: cartRaw,
    count: function () { return cartRaw().reduce(function (a, b) { return a + b.qty; }, 0); },
    add: function (id, qty, spec) {
      var items = cartRaw();
      var hit = items.find(function (x) { return x.id === id && x.spec === spec; });
      if (hit) hit.qty += (qty || 1); else items.push({ id: id, qty: qty || 1, spec: spec || "" });
      cartSave(items);
    },
    setQty: function (idx, qty) { var items = cartRaw(); if (items[idx]) { items[idx].qty = Math.max(1, qty); cartSave(items); } },
    remove: function (idx) { var items = cartRaw(); items.splice(idx, 1); cartSave(items); },
    clear: function () { cartSave([]); },
    /* 明细（解析商品 + 小计） */
    detail: function () {
      return cartRaw().map(function (it, idx) {
        var p = DB.PRODUCTS.find(function (x) { return x.id === it.id; });
        if (!p) return null;
        var unit = priceOf(p, "USD");
        return { idx: idx, product: p, qty: it.qty, spec: it.spec, unit: unit, line: unit * it.qty };
      }).filter(Boolean);
    },
    subtotal: function () { return Cart.detail().reduce(function (a, b) { return a + b.line; }, 0); }
  };
  function refreshCartBadge() {
    var link = qs('.snav-link[href="cart.html"]');
    if (!link) return;
    var n = Cart.count();
    var b = link.querySelector(".snav-badge");
    if (n) { if (b) b.textContent = n; else link.insertAdjacentHTML("beforeend", '<span class="snav-badge">' + n + "</span>"); }
    else if (b) b.remove();
  }

  /* ---------- 商品卡片 ---------- */
  function productCard(p) {
    var cur = currency();
    var now = priceOf(p, cur), was = wasOf(p, cur);
    var off = was ? Math.round((1 - now / was) * 100) : 0;
    var badgeHtml = p.badge ? '<span class="badge accent">' + esc(p.badge) + "</span>" : "";
    return '<article class="pcard" data-id="' + p.id + '">' +
      '<div class="pcard-media">' +
        imgTag(p.images[0], p.name, "", p.name) +
        '<div class="pcard-tags">' + badgeHtml + (off ? '<span class="badge danger">-' + off + "%</span>" : "") + "</div>" +
        '<button class="pcard-fav" type="button" title="收藏">' + icon("heart", { size: 16 }) + "</button>" +
      "</div>" +
      '<div class="pcard-body">' +
        '<div class="pcard-cat">' + esc(p.brand) + " · " + esc(p.categoryLabel) + "</div>" +
        '<a class="pcard-name" href="product.html?id=' + p.id + '">' + esc(p.name) + "</a>" +
        '<div class="pcard-meta">' + stars(p.rating) + "<span>已售 " + esc(p.sold) + "</span></div>" +
        '<div class="pcard-foot">' +
          '<div class="pcard-price">' + money(now, cur) + (was ? "<s>" + money(was, cur) + "</s>" : "") + "</div>" +
          '<button class="btn primary sm" type="button" data-add="' + p.id + '">' + icon("plus", { size: 15 }) + "加购</button>" +
        "</div>" +
      "</div>" +
    "</article>";
  }

  /* ---------- 通用事件绑定：加购 / 收藏 ---------- */
  function bindCardActions(root) {
    qsa("[data-add]", root || document).forEach(function (btn) {
      btn.addEventListener("click", function (e) {
        e.preventDefault(); e.stopPropagation();
        var id = btn.getAttribute("data-add");
        var p = DB.PRODUCTS.find(function (x) { return x.id === id; });
        Cart.add(id, 1, p.colors[0] + (p.sizes[0] ? " / " + p.sizes[0] : ""));
        toast("已加入购物车：" + p.name);
      });
    });
    qsa(".pcard-fav", root || document).forEach(function (b) {
      b.addEventListener("click", function (e) { e.preventDefault(); e.stopPropagation(); b.classList.toggle("on"); });
    });
  }

  /* ---------- 币种切换按钮（header #curSwitch） ---------- */
  function initCurrencySwitch() {
    var btn = qs("#curSwitch");
    if (!btn) return;
    function label() { var s = btn.querySelector("span"); if (s) s.textContent = currency(); }
    label();
    btn.addEventListener("click", function () {
      var order = ["USD", "EUR", "CNY"];
      var next = order[(order.indexOf(currency()) + 1) % order.length];
      setCurrency(next); label();
      toast("已切换结算币种：" + next, "info");
      document.dispatchEvent(new CustomEvent("currency:change", { detail: next }));
    });
  }

  /* ---------- 侧边栏移动端开关 ---------- */
  function initNavToggle() {
    var btn = qs("[data-nav-toggle]");
    var nav = qs("#app-nav");
    if (btn && nav) btn.addEventListener("click", function () { nav.classList.toggle("open"); });
  }

  g.FMT = { money: money, moneyRaw: moneyRaw, priceOf: priceOf, wasOf: wasOf, currency: currency, setCurrency: setCurrency, symbolOf: symbolOf, esc: esc };
  g.UI = { toast: toast, openModal: openModal, closeModal: closeModal, openDrawer: openDrawer, closeDrawer: closeDrawer, badge: badge, stars: stars, imgTag: imgTag, qs: qs, qsa: qsa, session: session, requireAuth: requireAuth };
  g.Cart = Cart;
  g.productCard = productCard;
  g.bindCardActions = bindCardActions;
  g.refreshCartBadge = refreshCartBadge;

  document.addEventListener("DOMContentLoaded", function () {
    initCurrencySwitch();
    initNavToggle();
    refreshCartBadge();
  });
})(window);
