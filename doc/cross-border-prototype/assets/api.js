/* =========================================================================
 * api.js — API 桩层（Stub）
 * 保持与"未来真实后端"一致的函数签名 / 延迟 / 返回结构；当前内部返回 mock。
 * 后期只需替换函数体（见各函数 // TODO: replace with ... 注解）即可接入真实接口。
 * 统一返回：{ code: 0, data, total? }；code!==0 表示业务失败。
 * ========================================================================= */
(function (g) {
  function delay(ms) { return new Promise(function (r) { setTimeout(r, ms || 380); }); }
  function ok(data, total) { return { code: 0, data: data, total: total }; }

  var API = {
    /* GET /api/products?category=&sort=&q=&page= */
    fetchProducts: async function (opt) {
      opt = opt || {};
      await delay(420);
      var list = DB.PRODUCTS.slice();
      if (opt.category && opt.category !== "all") list = list.filter(function (p) { return p.category === opt.category; });
      if (opt.q) {
        var q = opt.q.toLowerCase();
        list = list.filter(function (p) { return (p.name + p.nameEn + p.brand).toLowerCase().indexOf(q) > -1; });
      }
      if (opt.sort === "price-asc") list.sort(function (a, b) { return a.price.USD - b.price.USD; });
      if (opt.sort === "price-desc") list.sort(function (a, b) { return b.price.USD - a.price.USD; });
      if (opt.sort === "rating") list.sort(function (a, b) { return b.rating - a.rating; });
      if (opt.sort === "sold") list.sort(function (a, b) { return parseFloat(b.sold) - parseFloat(a.sold); });
      var page = opt.page || 1, size = opt.size || 8;
      return ok(list.slice((page - 1) * size, page * size), list.length);
    },
    /* GET /api/products/{id} */
    fetchProduct: async function (id) {
      await delay(300);
      var p = DB.PRODUCTS.find(function (x) { return x.id === id; });
      return p ? ok(p) : { code: 404, message: "商品不存在" };
    },
    /* GET /api/orders?customer=&status= */
    fetchOrders: async function (opt) {
      opt = opt || {};
      await delay(400);
      var list = DB.ORDERS.slice();
      if (opt.status && opt.status !== "all") list = list.filter(function (o) { return o.status === opt.status; });
      return ok(list, list.length);
    },
    /* GET /api/orders/{no} */
    fetchOrder: async function (no) {
      await delay(320);
      var o = DB.ORDERS.find(function (x) { return x.no === no; });
      return o ? ok(o) : { code: 404, message: "订单不存在" };
    },
    /* POST /api/orders  —— 创建订单（结算下单） */
    createOrder: async function (payload) {
      await delay(620);
      return ok({ orderNo: "NC-20261012-" + Math.floor(1000 + Math.random() * 8999), status: "PENDING_PAYMENT" });
    },
    /* POST /api/orders/{no}/pay —— 发起支付（模拟支付成功回调） */
    payOrder: async function (no) {
      await delay(700);
      return ok({ orderNo: no, paymentStatus: "PAID", status: "PAID" });
    },
    /* GET /api/inventory?warehouse= —— 库存（由商品多仓库存派生） */
    fetchInventory: async function (opt) {
      opt = opt || {};
      await delay(380);
      var rows = [];
      DB.PRODUCTS.forEach(function (p) {
        DB.WAREHOUSES.forEach(function (w) {
          if (opt.warehouse && opt.warehouse !== "all" && w.code !== opt.warehouse) return;
          var on = p.stock[w.code] || 0;
          var locked = Math.min(on, Math.floor(on * 0.08));
          rows.push({ sku: p.sku, name: p.name, image: p.images[0], wh: w.code, onHand: on, locked: locked, available: on - locked, safety: 50 });
        });
      });
      return ok(rows, rows.length);
    },
    /* GET /api/pricing —— 定价（由商品价格派生，含有效期） */
    fetchPricing: async function () {
      await delay(360);
      var rows = [];
      DB.PRODUCTS.forEach(function (p) {
        [["US", "DIRECT", "USD"], ["US", "AMAZON", "USD"], ["DE", "AMAZON-DE", "EUR"], ["SG", "TIKTOK", "USD"]].forEach(function (c, i) {
          var cur = c[2];
          rows.push({
            sku: p.sku, name: p.name, country: c[0], channel: c[1], currency: cur,
            list: (p.was[cur] || p.price[cur]), sale: p.price[cur],
            from: "2026-10-" + (10 + i).toString().padStart(2, "0"), to: "2026-12-31", status: i === 3 ? "SCHEDULED" : "ACTIVE"
          });
        });
      });
      return ok(rows, rows.length);
    },
    /* GET /api/aftersales */
    fetchAftersales: async function () {
      await delay(340);
      return ok(DB.REFUNDS, DB.REFUNDS.length);
    },
    /* GET /api/reconciliation?date= */
    fetchReconciliation: async function () {
      await delay(400);
      return ok(DB.RECONCILIATION, DB.RECONCILIATION.length);
    },
    /* GET /api/dashboard */
    fetchDashboard: async function () {
      await delay(460);
      return ok(DB.DASHBOARD);
    },
    fetchShopDashboard: async function () {
      await delay(360);
      return ok(DB.EXTENSIONS.SHOP);
    },
    fetchPlatformDashboard: async function () {
      await delay(420);
      return ok(DB.EXTENSIONS.PLATFORM);
    },
    fetchShopOrders: async function (opt) {
      await delay(340);
      var list = DB.ORDERS.filter(function (o) { return o.channel === "DIRECT" || o.channel === "AMAZON"; });
      if (opt && opt.status && opt.status !== "all") list = list.filter(function (o) { return o.status === opt.status; });
      return ok(list, list.length);
    },
    fetchPlatformShops: async function () {
      await delay(340);
      return ok(DB.EXTENSIONS.PLATFORM.shops, DB.EXTENSIONS.PLATFORM.shops.length);
    },
    /* POST /api/aftersales —— 提交售后申请 */
    submitAftersale: async function (payload) {
      await delay(560);
      return ok({ no: "RF-20261012-" + Math.floor(100 + Math.random() * 899), status: "REQUESTED" });
    }
  };

  g.API = API;
})(window);
