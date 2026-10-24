/* =========================================================================
 * shell.js — 买家端 App Shell（header / footer 唯一来源）
 * 所有页面只需在 body 上声明 data-surface="storefront" data-page="<nav key>"。
 * ========================================================================= */
(function () {
  var page = document.body.dataset.page || "";

  /* ---- 冻结导航配置（买家端 7 个页面互相跳转） ---- */
  var STOREFRONT_NAV = [
    { key: "index", label: "首页", href: "index.html" },
    { key: "shop", label: "全部商品", href: "index.html#catalog" },
    { key: "orders", label: "我的订单", href: "orders.html" },
    { key: "cart", label: "购物车", href: "cart.html" }
  ];

  function cartCount() {
    try {
      return JSON.parse(localStorage.getItem("nc_cart") || "[]")
        .reduce(function (a, b) { return a + (Number(b.qty) || 0); }, 0);
    } catch (e) { return 0; }
  }

  function accountLabel() {
    try {
      var info = JSON.parse(localStorage.getItem("nc_customer") || "null");
      if (localStorage.getItem("nc_token") && info) return info.displayName || "账户";
    } catch (e) { /* ignore */ }
    return "登录";
  }

  function renderStorefront() {
    var head = document.getElementById("site-header");
    if (head) {
      var links = STOREFRONT_NAV.map(function (n) {
        var active = n.key === page ? " active" : "";
        return '<a class="snav-link' + active + '" href="' + n.href + '">' + n.label +
          (n.key === "cart" && cartCount() ? '<span class="snav-badge">' + cartCount() + "</span>" : "") + "</a>";
      }).join("");
      head.innerHTML =
        '<div class="site-header-inner">' +
          '<a class="brand" href="index.html">' +
            '<span class="brand-mark">' + icon("store", { size: 20 }) + "</span>" +
            '<span class="brand-text"><b>NovaCross</b><i>诺瓦跨境优选</i></span>' +
          "</a>" +
          '<nav class="site-nav">' + links + "</nav>" +
          '<div class="site-tools">' +
            '<button class="tool-btn" type="button" id="curSwitch">' + icon("globe", { size: 16 }) + "<span>USD</span>" + icon("chevron-down", { size: 14 }) + "</button>" +
            '<a class="tool-btn" href="index.html#faq">' + icon("headphones", { size: 16 }) + "<span>客服</span></a>" +
            '<a class="tool-btn strong" href="account.html" id="accountLink">' + icon("user", { size: 16 }) + "<span>" + accountLabel() + "</span></a>" +
          "</div>" +
        "</div>";
    }
    var foot = document.getElementById("site-footer");
    if (foot) {
      foot.innerHTML =
        '<div class="site-footer-inner">' +
          '<div class="foot-col wide">' +
            '<div class="brand light">' + icon("store", { size: 20 }) + '<span class="brand-text"><b>NovaCross</b><i>诺瓦跨境优选</i></span></div>' +
            "<p>面向美国 / 欧洲 / 东南亚的跨境直营，支持多币种结算、海外仓发货与全程物流追踪。</p>" +
          "</div>" +
          '<div class="foot-col"><h5>购物指南</h5><a href="index.html#catalog">全部商品</a><a href="cart.html">购物车</a><a href="orders.html">我的订单</a></div>' +
          '<div class="foot-col"><h5>配送与售后</h5><a href="orders.html">物流追踪</a><a href="index.html#faq">退换货政策</a><a href="index.html#faq">常见问题</a></div>' +
          '<div class="foot-col"><h5>关于我们</h5><a href="account.html">账户中心</a><a href="index.html#faq">跨境合规</a><a href="index.html#faq">联系客服</a></div>' +
        "</div>" +
        '<div class="site-footer-base"><span>© 2026 NovaCross Global Commerce</span><span>USD · EUR · CNY 多币种结算</span></div>';
    }
  }

  renderStorefront();
})();
