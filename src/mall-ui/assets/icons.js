/* =========================================================================
 * icons.js — 统一图标库（Lucide 风格，内联 SVG，零外部依赖，可离线打开）
 * 用法：icon('cart')            -> 返回 24x24 stroke 图标字符串
 *       icon('cart', {size:18}) -> 自定义尺寸
 * 约定：stroke-width 1.75，颜色 currentColor（跟随文字色）
 * ========================================================================= */
(function (global) {
  var P = {
    home: '<path d="M3 10.5 12 3l9 7.5"/><path d="M5 9.5V20a1 1 0 0 0 1 1h4v-6h4v6h4a1 1 0 0 0 1-1V9.5"/>',
    grid: '<rect x="3" y="3" width="7" height="7" rx="1.6"/><rect x="14" y="3" width="7" height="7" rx="1.6"/><rect x="3" y="14" width="7" height="7" rx="1.6"/><rect x="14" y="14" width="7" height="7" rx="1.6"/>',
    box: '<path d="m12 2 8 4.5v9L12 20l-8-4.5v-9L12 2Z"/><path d="M4 6.5 12 11l8-4.5"/><path d="M12 11v9"/>',
    layers: '<path d="m12 3 9 4.5-9 4.5L3 7.5 12 3Z"/><path d="m3 12 9 4.5 9-4.5"/><path d="m3 16.5 9 4.5 9-4.5"/>',
    cart: '<circle cx="9" cy="20" r="1.4"/><circle cx="18" cy="20" r="1.4"/><path d="M2.5 3h2.2l2.2 11.2a1.6 1.6 0 0 0 1.6 1.3h8.9a1.6 1.6 0 0 0 1.6-1.3L21 7H6"/>',
    receipt: '<path d="M6 3h12a1 1 0 0 1 1 1v16l-3-2-3 2-3-2-3 2V4a1 1 0 0 1 1-1Z"/><path d="M9 8h6M9 12h6"/>',
    "credit-card": '<rect x="2.5" y="5" width="19" height="14" rx="2.5"/><path d="M2.5 10h19"/><path d="M6.5 15h3"/>',
    truck: '<path d="M2.5 6.5h11v9h-11z"/><path d="M13.5 9.5H18l3 3v3h-7.5z"/><circle cx="7" cy="17.5" r="1.8"/><circle cx="17" cy="17.5" r="1.8"/>',
    warehouse: '<path d="M3 21V9.5L12 4l9 5.5V21"/><path d="M3 21h18"/><path d="M7 21v-7h10v7"/><path d="M7 17h10"/>',
    tag: '<path d="M3 12.5 12.5 3H21v8.5L11.5 21 3 12.5Z"/><circle cx="16.5" cy="7.5" r="1.4"/>',
    refresh: '<path d="M3 12a9 9 0 1 0 3-6.7"/><path d="M3 4v5h5"/>',
    "bar-chart": '<path d="M3 21h18"/><rect x="5" y="11" width="3.2" height="7" rx="1"/><rect x="10.4" y="6" width="3.2" height="12" rx="1"/><rect x="15.8" y="9" width="3.2" height="9" rx="1"/>',
    users: '<circle cx="9" cy="8" r="3.2"/><path d="M2.8 20a6.2 6.2 0 0 1 12.4 0"/><path d="M16.5 5.6a3 3 0 0 1 0 5.6"/><path d="M17.5 14.4a6 6 0 0 1 3.7 5.6"/>',
    user: '<circle cx="12" cy="8" r="3.4"/><path d="M5 20a7 7 0 0 1 14 0"/>',
    globe: '<circle cx="12" cy="12" r="9"/><path d="M3 12h18"/><path d="M12 3a14 14 0 0 1 0 18a14 14 0 0 1 0-18Z"/>',
    search: '<circle cx="11" cy="11" r="7"/><path d="m20 20-3.6-3.6"/>',
    menu: '<path d="M3.5 6h17M3.5 12h17M3.5 18h17"/>',
    x: '<path d="M5 5l14 14M19 5 5 19"/>',
    "chevron-right": '<path d="m9 5 7 7-7 7"/>',
    "chevron-down": '<path d="m5 9 7 7 7-7"/>',
    "chevron-left": '<path d="m15 5-7 7 7 7"/>',
    plus: '<path d="M12 5v14M5 12h14"/>',
    minus: '<path d="M5 12h14"/>',
    trash: '<path d="M4 6.5h16"/><path d="M9 6.5V4.5h6v2"/><path d="M6.5 6.5 7.4 20a1 1 0 0 0 1 .9h7.2a1 1 0 0 0 1-.9l.9-13.5"/><path d="M10.5 10.5v6M13.5 10.5v6"/>',
    check: '<path d="m4.5 12.5 5 5 10-11"/>',
    "check-circle": '<circle cx="12" cy="12" r="9"/><path d="m8.5 12.2 2.4 2.4 4.6-5"/>',
    "alert-triangle": '<path d="M12 3.5 21.5 20H2.5L12 3.5Z"/><path d="M12 9.5v5"/><circle cx="12" cy="17.4" r=".9" fill="currentColor" stroke="none"/>',
    info: '<circle cx="12" cy="12" r="9"/><path d="M12 11v5"/><circle cx="12" cy="7.8" r=".9" fill="currentColor" stroke="none"/>',
    star: '<path d="m12 3.6 2.6 5.3 5.9.9-4.2 4.1 1 5.8L12 17l-5.3 2.7 1-5.8L3.5 9.8l5.9-.9L12 3.6Z"/>',
    heart: '<path d="M12 20s-7-4.4-7-9.4A4 4 0 0 1 12 8a4 4 0 0 1 7 2.6c0 5-7 9.4-7 9.4Z"/>',
    filter: '<path d="M3.5 5h17l-6.5 7.6V20l-4-2.4v-5L3.5 5Z"/>',
    "arrow-right": '<path d="M4 12h15"/><path d="m13 6 6 6-6 6"/>',
    shield: '<path d="M12 3 20 6v6c0 5-4 8-8 9-4-1-8-4-8-9V6l8-3Z"/><path d="m9 12 2 2 4-4.5"/>',
    clock: '<circle cx="12" cy="12" r="9"/><path d="M12 7.5V12l3 2"/>',
    "map-pin": '<path d="M12 21s7-5.6 7-11a7 7 0 1 0-14 0c0 5.4 7 11 7 11Z"/><circle cx="12" cy="10" r="2.5"/>',
    download: '<path d="M12 3.5v11"/><path d="m7.5 10 4.5 4.5 4.5-4.5"/><path d="M4.5 20h15"/>',
    bell: '<path d="M18 16V11a6 6 0 1 0-12 0v5l-1.5 2.5h15L18 16Z"/><path d="M10 20a2 2 0 0 0 4 0"/>',
    sliders: '<path d="M4 8h9M17 8h3"/><circle cx="15" cy="8" r="2.2"/><path d="M4 16h3M11 16h9"/><circle cx="9" cy="16" r="2.2"/>',
    store: '<path d="M3.5 9.5 5 4.5h14l1.5 5"/><path d="M4 9.5V20h16V9.5"/><path d="M3.5 9.5a2.6 2.6 0 0 0 4.4 1 2.6 2.6 0 0 0 4.1 0 2.6 2.6 0 0 0 4.1 0 2.6 2.6 0 0 0 4.4-1"/><path d="M9.5 20v-5h5v5"/>',
    sparkles: '<path d="M12 4.5 13.3 9l4.5 1.3-4.5 1.3L12 16l-1.3-4.4L6.2 10.3 10.7 9 12 4.5Z"/><path d="M18.5 4v3M17 5.5h3"/><path d="M6 17v2.4M4.8 18.2h2.4"/>',
    "trend-up": '<path d="M3.5 17 9 11.5l3.5 3.5L20.5 7"/><path d="M15 7h5.5v5.5"/>',
    external: '<path d="M14 4.5h5.5V10"/><path d="M19.5 4.5 11 13"/><path d="M18 14v5a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V7a1 1 0 0 1 1-1h5"/>',
    "file-text": '<path d="M6 3h7l5 5v13H6z"/><path d="M13 3v5h5"/><path d="M9 13h6M9 16.5h6"/>',
    logout: '<path d="M14 4.5H6a1 1 0 0 0-1 1v13a1 1 0 0 0 1 1h8"/><path d="M17 15.5 20.5 12 17 8.5"/><path d="M20 12H10"/>',
    wallet: '<rect x="3" y="6" width="18" height="13" rx="2.5"/><path d="M3 10h18"/><path d="M16.5 14.5h2"/>',
    zap: '<path d="M13 3 5 13h6l-1 8 8-10h-6l1-8Z"/>',
    percent: '<path d="m6 18 12-12"/><circle cx="7.5" cy="7.5" r="2.2"/><circle cx="16.5" cy="16.5" r="2.2"/>',
    flag: '<path d="M5 21V4"/><path d="M5 5h12l-2.5 3.5L17 12H5"/>',
    "shield-check": '<path d="M12 3 20 6v6c0 5-4 8-8 9-4-1-8-4-8-9V6l8-3Z"/><path d="m9 12 2 2 4-4.5"/>',
    leaf: '<path d="M4 20c8 2 16-4 16-16C10 4 2 8 4 20Z"/><path d="M9 15c2-3 5-5 9-6"/>',
    headphones: '<path d="M4 14v-2a8 8 0 0 1 16 0v2"/><rect x="2.5" y="13.5" width="4" height="7" rx="1.6"/><rect x="17.5" y="13.5" width="4" height="7" rx="1.6"/>',
    smartphone: '<rect x="6.5" y="2.5" width="11" height="19" rx="2.6"/><path d="M10.5 18.5h3"/>',
    scissors: '<circle cx="6" cy="6" r="2.4"/><circle cx="6" cy="18" r="2.4"/><path d="M8 7.5 20 18M8 16.5 20 6"/>',
    image: '<rect x="3" y="4" width="18" height="16" rx="2.4"/><circle cx="8.5" cy="9.5" r="1.6"/><path d="m4 18 5-5 4 4 3-2.5 4 3.5"/>',
    printer: '<path d="M7 8V3.5h10V8"/><rect x="3.5" y="8" width="17" height="8" rx="2"/><path d="M7 16h10v4.5H7z"/>',
    copy: '<rect x="8.5" y="8.5" width="12" height="12" rx="2.4"/><path d="M5.5 15.5H4.5a1 1 0 0 1-1-1v-10a1 1 0 0 1 1-1h10a1 1 0 0 1 1 1v1"/>',
    eye: '<path d="M2.5 12S6 5.5 12 5.5 21.5 12 21.5 12 18 18.5 12 18.5 2.5 12 2.5 12Z"/><circle cx="12" cy="12" r="3"/>',
    edit: '<path d="M4 20h4L19 9a1.8 1.8 0 0 0-2.5-2.5L5.5 17.5 4 20Z"/><path d="M14 7.5 16.5 10"/>'
  };

  global.icon = function (name, opts) {
    var o = opts || {};
    var size = o.size || 20;
    var sw = o.stroke || 1.75;
    var body = P[name] || P["box"];
    return '<svg class="ic ' + (o.cls || '') + '" width="' + size + '" height="' + size +
      '" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="' + sw +
      '" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">' + body + '</svg>';
  };
  global.ICON_NAMES = Object.keys(P);
})(window);
