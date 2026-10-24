/* =========================================================================
 * shell.js — App Shell（商城端 header/footer + 运营端 sidebar/topbar）
 * 唯一来源：所有页面的导航均由本文件生成，保证跨页 byte-identical，杜绝导航漂移。
 * 页面只需在 body 上声明：
 *   data-surface="storefront|admin"  data-page="<nav key>"
 *   运营端另需：data-title="页面标题"  data-crumb="分组 / 页面"
 * ========================================================================= */
(function () {
  var surface = document.body.dataset.surface || "storefront";
  var page = document.body.dataset.page || "";
  var title = document.body.dataset.title || "";
  var crumb = document.body.dataset.crumb || "";

  /* ---- 冻结导航配置（禁止各页增删改序） ---- */
  var STOREFRONT_NAV = [
    { key: "index", label: "首页", href: "index.html" },
    { key: "shop", label: "全部商品", href: "index.html#catalog" },
    { key: "orders", label: "我的订单", href: "orders.html" },
    { key: "cart", label: "购物车", href: "cart.html" }
  ];
  var SHOP_NAV = [
    { group: "店铺总览", items: [
      { key: "shop-dashboard", label: "店铺工作台", href: "shop-dashboard.html", icon: "grid" }
    ]},
    { group: "商品与订单", items: [
      { key: "shop-products", label: "店铺商品", href: "shop-products.html", icon: "box" },
      { key: "shop-orders", label: "店铺订单", href: "shop-orders.html", icon: "receipt" },
      { key: "shop-logistics", label: "物流管理", href: "shop-logistics.html", icon: "truck" }
    ]},
    { group: "客户与数据", items: [
      { key: "shop-customer", label: "客户服务与权限", href: "shop-customer.html", icon: "users" },
      { key: "shop-reports", label: "经营报表", href: "shop-reports.html", icon: "bar-chart" }
    ]},
    { group: "经营管理", items: [
      { key: "shop-marketing", label: "营销活动", href: "shop-marketing.html", icon: "percent" },
      { key: "shop-management", label: "店铺管理", href: "shop-management.html", icon: "store" },
      { key: "shop-settlement", label: "店铺结算", href: "shop-settlement.html", icon: "wallet" }
    ]}
  ];
  var PLATFORM_NAV = [
    { group: "平台总览", items: [
      { key: "platform-dashboard", label: "平台驾驶舱", href: "platform-dashboard.html", icon: "grid" }
    ]},
    { group: "平台治理", items: [
      { key: "platform-shops", label: "店铺管理", href: "platform-shops.html", icon: "store" },
      { key: "platform-users", label: "用户与权限", href: "platform-users.html", icon: "users" },
      { key: "platform-audit", label: "风控与审计", href: "platform-audit.html", icon: "shield-check" }
    ]},
    { group: "平台运营", items: [
      { key: "platform-channels", label: "渠道配置", href: "platform-channels.html", icon: "globe" },
      { key: "platform-settings", label: "平台设置", href: "platform-settings.html", icon: "sliders" }
    ]}
  ];

  function navForSurface() {
    if (surface === "shop") return SHOP_NAV;
    if (surface === "platform") return PLATFORM_NAV;
    return ADMIN_NAV;
  }

  var ADMIN_NAV = [
    { group: "总览", items: [
      { key: "dashboard", label: "运营工作台", href: "admin-dashboard.html", icon: "grid" }
    ]},
    { group: "交易", items: [
      { key: "orders", label: "订单管理", href: "admin-orders.html", icon: "receipt" },
      { key: "promotions", label: "打折促销活动", href: "admin-promotions.html", icon: "percent" },
      { key: "aftersale", label: "售后与退款", href: "admin-aftersale.html", icon: "refresh" }
    ]},
    { group: "履约", items: [
      { key: "fulfillment", label: "履约与物流", href: "admin-fulfillment.html", icon: "truck" },
      { key: "inventory", label: "库存中心", href: "admin-inventory.html", icon: "warehouse" }
    ]},
    { group: "商品与价格", items: [
      { key: "products", label: "商品中心", href: "admin-products.html", icon: "box" },
      { key: "pricing", label: "定价中心", href: "admin-pricing.html", icon: "tag" }
    ]},
    { group: "数据", items: [
      { key: "reconciliation", label: "数据与对账", href: "admin-reconciliation.html", icon: "bar-chart" }
    ]},
    { group: "智能运营", items: [
      { key: "recommendation", label: "推荐系统", href: "../ecommerce-recommendation-prototype/index.html", icon: "sparkles" },
      { key: "tag-platform", label: "标签系统", href: "../cross-border-tag-platform/index.html", icon: "tag" }
    ]}
  ];

  function cartCount() {
    try { return JSON.parse(localStorage.getItem("nc_cart") || "[]").reduce(function (a, b) { return a + b.qty; }, 0); }
    catch (e) { return 0; }
  }

  /* ================= 商城端 ================= */
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
            '<a class="tool-btn strong" href="account.html" id="accountLink">' + icon("user", { size: 16 }) + "<span>账户</span></a>" +
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

  /* ================= 运营端 ================= */
  function renderAdmin() {
    var nav = document.getElementById("app-nav");
    if (nav) {
      var activeNav = navForSurface();
      var groups = activeNav.map(function (g) {
        var items = g.items.map(function (n) {
          var active = n.key === page ? " active" : "";
          return '<a class="nav-item' + active + '" href="' + n.href + '">' +
            icon(n.icon, { size: 18 }) + "<span>" + n.label + "</span></a>";
        }).join("");
        return '<div class="nav-group"><div class="nav-group-label">' + g.group + "</div>" + items + "</div>";
      }).join("");
      var shellName = surface === "shop" ? "店铺端" : surface === "platform" ? "平台管理端" : "运营中台";
      var home = surface === "shop" ? "shop-dashboard.html" : surface === "platform" ? "platform-dashboard.html" : "admin-dashboard.html";
      var switchLink = surface === "shop" ? '<a class="nav-item" href="platform-dashboard.html">' + icon("shield-check", { size: 18 }) + "<span>平台管理端</span></a>" : surface === "platform" ? '<a class="nav-item" href="shop-dashboard.html">' + icon("store", { size: 18 }) + "<span>切换店铺端</span></a>" : '<a class="nav-item" href="shop-dashboard.html">' + icon("store", { size: 18 }) + "<span>店铺端</span></a>";
      var recommendationLink = '<a class="nav-item" href="../ecommerce-recommendation-prototype/index.html">' + icon("sparkles", { size: 18 }) + "<span>推荐系统</span></a>";
      var tagPlatformLink = '<a class="nav-item" href="../cross-border-tag-platform/index.html">' + icon("tag", { size: 18 }) + "<span>标签系统</span></a>";
      nav.innerHTML =
        '<a class="nav-brand" href="' + home + '">' +
          '<span class="brand-mark">' + icon("store", { size: 20 }) + "</span>" +
          '<span class="brand-text"><b>NovaCross</b><i>' + shellName + "</i></span>" +
        "</a>" +
        '<div class="nav-scroll">' + groups + "</div>" +
        '<div class="nav-foot">' +
          switchLink +
          (surface === "admin" ? "" : recommendationLink) +
          (surface === "admin" ? "" : tagPlatformLink) +
          (surface === "platform" ? '<a class="nav-item" href="admin-dashboard.html">' + icon("grid", { size: 18 }) + "<span>运营中台</span></a>" : "") +
          '<a class="nav-item" href="index.html">' + icon("external", { size: 18 }) + "<span>前往商城端</span></a>" +
          '<div class="nav-user">' +
            '<span class="avatar">YL</span>' +
            '<span class="nav-user-meta"><b>杨琳</b><i>跨境运营主管</i></span>' +
          "</div>" +
        "</div>";
    }
    var top = document.getElementById("app-topbar");
    if (top) {
      top.innerHTML =
        '<div class="topbar-left">' +
          '<button class="icon-btn nav-toggle" type="button" data-nav-toggle title="导航">' + icon("menu", { size: 18 }) + "</button>" +
          '<div><h1 class="page-title">' + (title || "运营中台") + "</h1>" +
          (crumb ? '<div class="crumb">' + crumb + "</div>" : "") + "</div>" +
        "</div>" +
        '<div class="topbar-right">' +
          '<label class="search-box">' + icon("search", { size: 16 }) +
            '<input type="search" placeholder="搜索订单号 / SKU / 客户" aria-label="全局搜索"></label>' +
          '<button class="icon-btn" type="button" title="汇率与币种">' + icon("globe", { size: 18 }) + "</button>" +
          '<button class="icon-btn" type="button" title="通知">' + icon("bell", { size: 18 }) + '<i class="dot"></i></button>' +
          '<button class="icon-btn" type="button" title="设置">' + icon("sliders", { size: 18 }) + "</button>" +
        "</div>";
    }
  }

  if (surface === "storefront") renderStorefront(); else renderAdmin();
})();
