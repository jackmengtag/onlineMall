/* =========================================================================
 * api.js — 买家端 API 层（真实后端）
 * 统一走同源 /api/storefront/*，由 Vite 代理到 http://localhost:8080。
 * 返回结构统一为 { code, data, total, message }；code === 0 表示成功，
 * 失败时 Promise.reject(message)；HTTP 401 / code 401 时清空登录态并跳转登录页。
 * 商品接口失败时回退 mock.js（仅用于演示兜底，不影响其余真实业务）。
 * ========================================================================= */
(function (g) {
  var TOKEN_KEY = "nc_token";
  var CUSTOMER_KEY = "nc_customer";
  var BASE = "";

  /* ---------- 登录态 ---------- */
  function token() {
    try { return localStorage.getItem(TOKEN_KEY) || ""; } catch (e) { return ""; }
  }
  function customer() {
    try { return JSON.parse(localStorage.getItem(CUSTOMER_KEY) || "null"); } catch (e) { return null; }
  }
  function saveSession(data) {
    if (!data) return null;
    var info = {
      customerId: data.customerId,
      displayName: data.displayName,
      email: data.email
    };
    try {
      localStorage.setItem(TOKEN_KEY, data.token || "");
      localStorage.setItem(CUSTOMER_KEY, JSON.stringify(info));
    } catch (e) { /* 忽略隐私模式下的存储异常 */ }
    return info;
  }
  function clearSession() {
    try {
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(CUSTOMER_KEY);
    } catch (e) { /* ignore */ }
  }
  function goLogin() {
    clearSession();
    if (/login\.html$/.test(location.pathname)) return;
    var next = location.pathname.split("/").pop() + location.search;
    location.href = "login.html?redirect=" + encodeURIComponent(next || "index.html");
  }

  /* ---------- 请求封装 ---------- */
  function query(params) {
    var parts = [];
    Object.keys(params || {}).forEach(function (k) {
      var v = params[k];
      if (v === undefined || v === null || v === "") return;
      parts.push(encodeURIComponent(k) + "=" + encodeURIComponent(v));
    });
    return parts.length ? "?" + parts.join("&") : "";
  }

  async function request(path, options) {
    options = options || {};
    var headers = { Accept: "application/json" };
    var t = token();
    if (t) headers["X-Customer-Token"] = t;
    if (options.body !== undefined) headers["Content-Type"] = "application/json";

    var res;
    try {
      res = await fetch(BASE + path, {
        method: options.method || "GET",
        headers: headers,
        body: options.body === undefined ? undefined : JSON.stringify(options.body)
      });
    } catch (e) {
      throw new Error("网络异常，请检查后端服务是否已启动");
    }

    var payload = null;
    try { payload = await res.json(); } catch (e) { payload = null; }

    if (res.status === 401 || (payload && payload.code === 401)) {
      goLogin();
      throw new Error((payload && payload.message) || "登录已失效，请重新登录");
    }
    if (!res.ok) {
      throw new Error((payload && payload.message) || ("请求失败（HTTP " + res.status + "）"));
    }
    if (!payload) throw new Error("响应解析失败");
    if (payload.code !== 0) throw new Error(payload.message || "请求失败");

    return { code: 0, data: payload.data, total: payload.total };
  }

  /* ---------- 数据归一化 ---------- */
  function num(v, dft) {
    var n = Number(v);
    return Number.isFinite(n) ? n : (dft == null ? 0 : dft);
  }
  function list(v) {
    return Array.isArray(v) ? v.slice() : [];
  }
  function fallbackImage(name) {
    if (typeof DB !== "undefined" && DB.img) {
      return DB.img((name || "cross-border product") + ", professional e-commerce product photo, clean light background");
    }
    return "";
  }
  function priceMap(p) {
    var src = p || {};
    return {
      USD: num(src.USD),
      EUR: num(src.EUR ? src.EUR : src.USD),
      CNY: num(src.CNY ? src.CNY : src.USD)
    };
  }

  /** 统一商品结构，缺失字段给默认值，页面渲染逻辑无需改动 */
  function normalizeProduct(raw) {
    var p = raw || {};
    var images = list(p.images);
    if (!images.length) images = [p.image || fallbackImage(p.name)];
    var price = priceMap(p.price);
    var wasRaw = priceMap(p.was);
    var hasWas = wasRaw.USD > 0 || wasRaw.EUR > 0 || wasRaw.CNY > 0;
    var was = hasWas ? wasRaw : null;
    var colors = list(p.colors);
    if (!colors.length) colors = ["默认"];
    var stock = p.stock && typeof p.stock === "object" ? p.stock : {};
    var skus = list(p.skus).map(function (s) {
      return {
        id: s.id != null ? s.id : s.skuId,
        sku: s.sku || s.skuCode || "",
        attributes: s.attributes || {},
        stock: num(s.stock),
        price: priceMap(s.price),
        weightG: num(s.weightG),
        hsCode: s.hsCode || "",
        declaredName: s.declaredName || "",
        declaredValue: num(s.declaredValue),
        status: s.status || "ACTIVE"
      };
    });
    return {
      id: p.id != null ? p.id : p.spuId,
      spu: p.spu || p.spuCode || "",
      sku: p.sku || p.skuCode || (skus[0] && skus[0].sku) || "",
      name: p.name || p.defaultName || "未命名商品",
      nameEn: p.nameEn || "",
      category: p.category || p.categoryKey || "all",
      categoryLabel: p.categoryLabel || p.categoryName || "",
      brand: p.brand || p.brandName || "",
      origin: p.origin || p.originCountryCode || "CN",
      hsCode: p.hsCode || "",
      declaredName: p.declaredName || "",
      declaredValue: num(p.declaredValue),
      weightG: num(p.weightG),
      status: p.status || "ON_SALE",
      badge: p.badge || "",
      price: price,
      was: was,
      rating: num(p.rating, 4.5),
      reviews: num(p.reviews),
      sold: p.sold != null ? String(p.sold) : "0",
      colors: colors,
      sizes: list(p.sizes),
      tags: list(p.tags),
      stock: stock,
      channels: p.channels || {},
      images: images,
      desc: p.desc || p.description || "",
      specs: p.specs || {},
      skus: skus
    };
  }

  function statusLabel(status) {
    if (typeof DB !== "undefined" && DB.ORDER_STATUS && DB.ORDER_STATUS[status]) {
      return DB.ORDER_STATUS[status].label;
    }
    return status || "-";
  }

  function normalizeOrderItem(it) {
    var qty = num(it.qty != null ? it.qty : it.quantity);
    var unit = num(it.unit != null ? it.unit : it.unitPriceAmount);
    return {
      orderItemId: it.orderItemId,
      skuId: it.skuId != null ? it.skuId : it.productId,
      sku: it.sku || it.skuCode || "",
      name: it.name || "商品",
      image: it.image || fallbackImage(it.name),
      spec: it.spec || "",
      qty: qty,
      quantity: qty,
      unit: unit,
      unitPriceAmount: unit,
      lineTotalAmount: num(it.lineTotalAmount, unit * qty),
      currencyCode: it.currencyCode || "USD"
    };
  }

  /** 订单列表 / 详情统一结构（保留后端字段，同时提供页面渲染所需的派生字段） */
  function normalizeOrder(raw) {
    var o = raw || {};
    var cur = o.currencyCode || "USD";
    var status = o.orderStatus || o.status || "CREATED";
    var items = list(o.items).map(normalizeOrderItem);
    var total = num(o.totalAmount);
    return {
      orderId: o.orderId,
      orderNo: o.orderNo,
      no: o.orderNo,
      orderStatus: status,
      status: status,
      paymentStatus: o.paymentStatus || "UNPAID",
      payStatus: o.paymentStatus || "UNPAID",
      fulfillmentStatus: o.fulfillmentStatus || "UNFULFILLED",
      afterSaleStatus: o.afterSaleStatus || "NONE",
      channelCode: o.channelCode || "DIRECT",
      channel: o.channelCode || "DIRECT",
      countryCode: o.countryCode || "US",
      currencyCode: cur,
      currency: cur,
      subtotalAmount: num(o.subtotalAmount),
      discountAmount: num(o.discountAmount),
      shippingAmount: num(o.shippingAmount),
      taxAmount: num(o.taxAmount),
      totalAmount: total,
      amounts: {
        subtotal: num(o.subtotalAmount),
        discount: num(o.discountAmount),
        shipping: num(o.shippingAmount),
        tax: num(o.taxAmount),
        total: total
      },
      createTime: o.createTime || o.placedAt || "",
      placedAt: o.createTime || o.placedAt || "",
      paidAt: o.paidAt || "",
      items: items,
      ship: [],
      ful: [],
      pay: [],
      history: [],
      snapshot: {}
    };
  }

  function normalizeOrderDetail(raw) {
    var r = raw || {};
    var order = normalizeOrder(r.order);
    if (r.items && r.items.length) order.items = list(r.items).map(normalizeOrderItem);
    var sn = r.snapshot || {};
    order.snapshot = {
      recipient: sn.recipientName || "",
      phone: sn.recipientPhone || "",
      addr: [sn.addressLine1, sn.addressLine2, sn.city, sn.stateCode, sn.postalCode, sn.countryCode]
        .filter(function (x) { return !!x; }).join(", "),
      countryCode: sn.countryCode || "",
      taxRule: sn.taxRule || "",
      rateVer: sn.rateVer || "",
      rate: sn.rate == null ? "" : sn.rate
    };
    order.history = list(r.histories).map(function (h) {
      return {
        t: statusLabel(h.toStatus),
        at: h.occurredAt || "",
        by: h.operatorType || "",
        note: [h.reason, h.fromStatus ? h.fromStatus + " → " + h.toStatus : ""]
          .filter(function (x) { return !!x; }).join("；")
      };
    });
    order.itemSnapshots = list(r.itemSnapshots);
    return order;
  }

  /* ---------- 商品接口演练兜底（仅商品目录） ---------- */
  function mockProducts(opt) {
    opt = opt || {};
    var all = (typeof DB !== "undefined" && DB.PRODUCTS) ? DB.PRODUCTS : [];
    var items = all.slice();
    if (opt.category && opt.category !== "all") {
      items = items.filter(function (p) { return p.category === opt.category; });
    }
    if (opt.q) {
      var q = String(opt.q).toLowerCase();
      items = items.filter(function (p) {
        return (p.name + p.nameEn + p.brand).toLowerCase().indexOf(q) > -1;
      });
    }
    if (opt.sort === "price-asc") items.sort(function (a, b) { return a.price.USD - b.price.USD; });
    if (opt.sort === "price-desc") items.sort(function (a, b) { return b.price.USD - a.price.USD; });
    if (opt.sort === "rating") items.sort(function (a, b) { return b.rating - a.rating; });
    if (opt.sort === "sold") items.sort(function (a, b) { return parseFloat(b.sold) - parseFloat(a.sold); });
    var page = opt.page || 1, size = opt.size || 8;
    return {
      code: 0,
      data: items.slice((page - 1) * size, page * size).map(normalizeProduct),
      total: items.length,
      source: "mock"
    };
  }

  function findMockProduct(id) {
    if (typeof DB === "undefined" || !DB.PRODUCTS) return null;
    var key = String(id);
    var hit = DB.PRODUCTS.find(function (p) { return String(p.id) === key || p.sku === key || p.spu === key; });
    if (hit) return hit;
    if (/^\d+$/.test(key)) return DB.PRODUCTS[Number(key) - 1] || null;
    return null;
  }

  /* ---------- 对外 API ---------- */
  var API = {
    token: token,
    customer: customer,
    saveSession: saveSession,
    clearSession: clearSession,

    /* GET /api/storefront/categories */
    fetchCategories: async function () {
      try {
        var r = await request("/api/storefront/categories");
        return { code: 0, data: list(r.data) };
      } catch (e) {
        var cats = (typeof DB !== "undefined" && DB.CATEGORIES) ? DB.CATEGORIES : [{ key: "all", label: "全部商品" }];
        return { code: 0, data: cats, source: "mock" };
      }
    },

    /* GET /api/storefront/products?category=&sort=&q=&page=&size= */
    fetchProducts: async function (opt) {
      opt = opt || {};
      try {
        var r = await request("/api/storefront/products" + query({
          category: opt.category === "all" ? "" : opt.category,
          sort: opt.sort === "popular" ? "" : opt.sort,
          q: opt.q,
          page: opt.page || 1,
          size: opt.size || 8
        }));
        var items = list(r.data).map(normalizeProduct);
        return { code: 0, data: items, total: r.total == null ? items.length : r.total };
      } catch (e) {
        console.warn("[api] 商品列表接口不可用，回退演示数据：", e.message);
        return mockProducts(opt);
      }
    },

    /* GET /api/storefront/products/{id} */
    fetchProduct: async function (id) {
      try {
        var r = await request("/api/storefront/products/" + encodeURIComponent(id));
        return { code: 0, data: normalizeProduct(r.data) };
      } catch (e) {
        var p = findMockProduct(id);
        if (p) {
          console.warn("[api] 商品详情接口不可用，回退演示数据：", e.message);
          return { code: 0, data: normalizeProduct(p), source: "mock" };
        }
        throw e;
      }
    },

    /* POST /api/storefront/login */
    login: async function (email, password) {
      var r = await request("/api/storefront/login", {
        method: "POST",
        body: { email: email, password: password }
      });
      var info = saveSession(r.data || {});
      return { code: 0, data: info };
    },

    /* POST /api/storefront/logout */
    logout: async function () {
      try { await request("/api/storefront/logout", { method: "POST" }); } catch (e) { /* 本地登录态仍然清除 */ }
      clearSession();
      return { code: 0, data: null };
    },

    /* GET /api/storefront/account */
    account: async function () {
      var r = await request("/api/storefront/account");
      return { code: 0, data: r.data };
    },

    /* GET /api/storefront/addresses */
    fetchAddresses: async function () {
      var r = await request("/api/storefront/addresses");
      return { code: 0, data: list(r.data), total: r.total == null ? list(r.data).length : r.total };
    },

    /* POST /api/storefront/addresses */
    addAddress: async function (payload) {
      var r = await request("/api/storefront/addresses", { method: "POST", body: payload });
      return { code: 0, data: r.data };
    },

    /* GET /api/storefront/cart */
    fetchCart: async function () {
      var r = await request("/api/storefront/cart");
      var rows = list(r.data).map(function (it) {
        return {
          cartItemId: it.cartItemId,
          skuId: it.skuId,
          skuCode: it.skuCode || "",
          name: it.name || ("SKU " + (it.skuCode || it.skuId || "")),
          image: it.image || fallbackImage(it.name),
          unitPriceAmount: num(it.unitPriceAmount),
          currencyCode: it.currencyCode || "USD",
          quantity: num(it.quantity, 1),
          stock: num(it.stock)
        };
      });
      return { code: 0, data: rows, total: rows.length };
    },

    /* POST /api/storefront/cart */
    addCart: async function (skuId, quantity) {
      var body = typeof skuId === "object" ? skuId : { skuId: skuId, quantity: quantity || 1 };
      var r = await request("/api/storefront/cart", { method: "POST", body: body });
      return { code: 0, data: r.data };
    },

    /* PUT /api/storefront/cart */
    updateCart: async function (cartItemId, quantity) {
      var body = typeof cartItemId === "object" ? cartItemId : { cartItemId: cartItemId, quantity: quantity };
      var r = await request("/api/storefront/cart", { method: "PUT", body: body });
      return { code: 0, data: r.data };
    },

    /* DELETE /api/storefront/cart/{cartItemId} */
    removeCart: async function (cartItemId) {
      var r = await request("/api/storefront/cart/" + encodeURIComponent(cartItemId), { method: "DELETE" });
      return { code: 0, data: r.data };
    },

    /* GET /api/storefront/orders?status= */
    fetchOrders: async function (opt) {
      opt = opt || {};
      var r = await request("/api/storefront/orders" + query({
        status: opt.status && opt.status !== "all" ? opt.status : ""
      }));
      var rows = list(r.data).map(normalizeOrder);
      return { code: 0, data: rows, total: r.total == null ? rows.length : r.total };
    },

    /* GET /api/storefront/orders/{orderNo} */
    fetchOrder: async function (orderNo) {
      var r = await request("/api/storefront/orders/" + encodeURIComponent(orderNo));
      return { code: 0, data: normalizeOrderDetail(r.data) };
    },

    /* POST /api/storefront/orders */
    createOrder: async function (payload) {
      var r = await request("/api/storefront/orders", { method: "POST", body: payload || {} });
      return { code: 0, data: r.data };
    },

    /* POST /api/storefront/orders/{orderNo}/pay */
    payOrder: async function (orderNo) {
      var r = await request("/api/storefront/orders/" + encodeURIComponent(orderNo) + "/pay", { method: "POST" });
      return { code: 0, data: r.data };
    },

    /* POST /api/storefront/aftersales */
    submitAftersale: async function (payload) {
      var r = await request("/api/storefront/aftersales", { method: "POST", body: payload || {} });
      return { code: 0, data: r.data };
    },

    /* GET /api/storefront/aftersales */
    fetchAftersales: async function () {
      var r = await request("/api/storefront/aftersales");
      var rows = list(r.data);
      return { code: 0, data: rows, total: r.total == null ? rows.length : r.total };
    }
  };

  g.API = API;
})(window);
