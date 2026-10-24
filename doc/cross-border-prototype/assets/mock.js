/* =========================================================================
 * mock.js — 唯一 mock 数据源（所有页面统一读取，禁止各页硬编码）
 * 覆盖：商品 / 渠道映射 / 价格 / 客户 / 订单 / 支付 / 履约 / 物流 / 售后 / 对账
 * 金额一律以最小货币单位整数存储（BIGINT 语义），禁用浮点。
 * ========================================================================= */
(function (g) {
  // 统一图片生成入口（按系统规范使用文生图接口）
  function img(prompt, size) {
    return "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=" +
      encodeURIComponent(prompt) + "&image_size=" + (size || "square_hd");
  }
  function shots(prompt) {
    return [img(prompt), img(prompt + ", angled three-quarter view"), img(prompt + ", detail close-up shot"), img(prompt + ", in real use scene")];
  }

  var WAREHOUSES = [
    { code: "US-WEST", name: "美国仓 · 洛杉矶", country: "US", type: "海外仓", color: "#1B6360" },
    { code: "DE-FRA", name: "德国仓 · 法兰克福", country: "DE", type: "海外仓", color: "#C9622B" },
    { code: "SG-SIN", name: "新加坡仓 · 裕廊", country: "SG", type: "海外仓", color: "#2B6484" },
    { code: "CN-SZ", name: "中国仓 · 深圳", country: "CN", type: "国内仓", color: "#B77A12" }
  ];
  var CHANNELS = [
    { code: "DIRECT", name: "独立站 NovaCross.com", type: "独立站", country: "US", currency: "USD", status: "在线" },
    { code: "AMAZON", name: "Amazon 北美站", type: "第三方平台", country: "US", currency: "USD", status: "在线" },
    { code: "AMAZON-DE", name: "Amazon 德国站", type: "第三方平台", country: "DE", currency: "EUR", status: "在线" },
    { code: "TIKTOK", name: "TikTok Shop SG", type: "社交渠道", country: "SG", currency: "USD", status: "同步中" },
    { code: "SHOPEE", name: "Shopee 新加坡", type: "第三方平台", country: "SG", currency: "USD", status: "暂停" }
  ];
  var CATEGORIES = [
    { key: "all", label: "全部商品" },
    { key: "audio", label: "影音数码" },
    { key: "smart", label: "智能穿戴" },
    { key: "home", label: "家居厨房" },
    { key: "outdoor", label: "户外运动" },
    { key: "pet", label: "宠物用品" },
    { key: "personal", label: "个护健康" }
  ];

  var PRODUCTS = [
    { id: "P001", spu: "SPU-100231", sku: "SKU-100231-GR", name: "NovaSound 头戴式主动降噪耳机", nameEn: "NovaSound ANC Headphones",
      category: "audio", categoryLabel: "影音数码", brand: "NovaSound", origin: "CN", hsCode: "8518300000",
      declaredName: "Wireless Headphones", declaredValue: 38.5, weightG: 268, status: "ON_SALE", badge: "热销",
      price: { USD: 12999, EUR: 11999, CNY: 89900 }, was: { USD: 15999, EUR: 14999, CNY: 109900 },
      rating: 4.8, reviews: 1264, sold: "2.3k", colors: ["石墨黑", "雾白", "黛蓝"], sizes: [],
      tags: ["双馈降噪", "40h 续航", "多点连接"],
      stock: { "US-WEST": 320, "DE-FRA": 140, "SG-SIN": 0, "CN-SZ": 860 },
      channels: { DIRECT: "在售", AMAZON: "在售", "AMAZON-DE": "在售", TIKTOK: "草稿" },
      images: shots("wireless over-ear active noise cancelling headphones, matte graphite finish, professional e-commerce product photo, clean light grey background, studio softbox lighting"),
      desc: "双馈主动降噪与 40 小时续航，支持多点连接与低延迟游戏模式，为跨境通勤与长途飞行打造。",
      specs: { "蓝牙版本": "5.3", "续航": "40 小时（ANC 开启）", "单元": "40mm 动圈", "充电": "USB-C / 快充 10 分钟听 5 小时", "重量": "268g", "防水": "IPX4" } },

    { id: "P002", spu: "SPU-100244", sku: "SKU-100244-BK", name: "PulseFit 智能运动手表 S3", nameEn: "PulseFit Smart Watch S3",
      category: "smart", categoryLabel: "智能穿戴", brand: "PulseFit", origin: "CN", hsCode: "8517620000",
      declaredName: "Smart Watch", declaredValue: 52.0, weightG: 46, status: "ON_SALE", badge: "新品",
      price: { USD: 9999, EUR: 9499, CNY: 69900 }, was: { USD: 12999, EUR: 12499, CNY: 89900 },
      rating: 4.7, reviews: 872, sold: "1.6k", colors: ["曜石黑", "星光银"], sizes: ["42mm", "46mm"],
      tags: ["血氧监测", "双频 GPS", "14 天续航"],
      stock: { "US-WEST": 210, "DE-FRA": 96, "SG-SIN": 150, "CN-SZ": 1240 },
      channels: { DIRECT: "在售", AMAZON: "在售", "AMAZON-DE": "草稿", TIKTOK: "在售" },
      images: shots("modern smartwatch with fitness tracking, black sport band, AMOLED display, professional product photo, clean light background"),
      desc: "1.43 英寸 AMOLED 屏，双频 GPS 与血氧监测，14 天超长续航，泳池级防水。",
      specs: { "屏幕": "1.43\" AMOLED", "续航": "14 天", "定位": "双频 GPS", "防水": "5ATM", "传感器": "心率 / 血氧 / 睡眠", "重量": "46g" } },

    { id: "P003", spu: "SPU-100260", sku: "SKU-100260-WH", name: "FreshGo 便携充电榨汁杯", nameEn: "FreshGo Portable Blender",
      category: "home", categoryLabel: "家居厨房", brand: "FreshGo", origin: "CN", hsCode: "8509400000",
      declaredName: "Portable Blender", declaredValue: 12.8, weightG: 430, status: "ON_SALE", badge: "",
      price: { USD: 3599, EUR: 3399, CNY: 24900 }, was: { USD: 4599, EUR: 4299, CNY: 31900 },
      rating: 4.5, reviews: 2310, sold: "5.8k", colors: ["奶白", "薄荷绿"], sizes: [],
      tags: ["六叶刀头", "USB-C", "随身携带"],
      stock: { "US-WEST": 640, "DE-FRA": 388, "SG-SIN": 220, "CN-SZ": 3200 },
      channels: { DIRECT: "在售", AMAZON: "在售", "AMAZON-DE": "在售", TIKTOK: "在售" },
      images: shots("portable USB rechargeable blender bottle filled with fresh fruit smoothie, professional product photo, clean light background"),
      desc: "六叶立体刀头 30 秒破壁，USB-C 充电，400ml 随行杯体，办公室与健身房随身果汁。",
      specs: { "容量": "400ml", "刀头": "六叶 304 不锈钢", "充电": "USB-C", "续航": "约 15 杯", "重量": "430g", "材质": "Tritan 杯体" } },

    { id: "P004", spu: "SPU-100275", sku: "SKU-100275-GN", name: "TrailNest 自动速开露营帐篷", nameEn: "TrailNest Auto Camping Tent",
      category: "outdoor", categoryLabel: "户外运动", brand: "TrailNest", origin: "CN", hsCode: "6306220000",
      declaredName: "Camping Tent", declaredValue: 34.0, weightG: 2900, status: "ON_SALE", badge: "大件",
      price: { USD: 8999, EUR: 8499, CNY: 62900 }, was: { USD: 11999, EUR: 11499, CNY: 83900 },
      rating: 4.6, reviews: 486, sold: "0.9k", colors: ["苔绿", "沙棕"], sizes: ["3-4 人"],
      tags: ["3 秒速开", "PU3000 防水", "双层结构"],
      stock: { "US-WEST": 88, "DE-FRA": 42, "SG-SIN": 0, "CN-SZ": 460 },
      channels: { DIRECT: "在售", AMAZON: "在售", "AMAZON-DE": "在售", TIKTOK: "草稿" },
      images: shots("compact automatic pop-up camping tent pitched on grass during golden hour, outdoor gear product photography"),
      desc: "3 秒液压速开，PU3000 防水涂层与双层结构，3-4 人适用，适合跨境露营地与家庭露营。",
      specs: { "适用人数": "3-4 人", "展开尺寸": "240×210×135cm", "防水": "PU3000mm", "面料": "190T 银胶", "重量": "2.9kg", "收纳": "78×18×18cm" } },

    { id: "P005", spu: "SPU-100288", sku: "SKU-100288-WH", name: "CleanMate 激光导航扫地机器人", nameEn: "CleanMate Lidar Robot Vacuum",
      category: "home", categoryLabel: "家居厨房", brand: "CleanMate", origin: "CN", hsCode: "8508110000",
      declaredName: "Robot Vacuum", declaredValue: 96.0, weightG: 3600, status: "ON_SALE", badge: "高客单",
      price: { USD: 24999, EUR: 23999, CNY: 179900 }, was: { USD: 29999, EUR: 28999, CNY: 209900 },
      rating: 4.7, reviews: 731, sold: "1.1k", colors: ["瓷白"], sizes: [],
      tags: ["激光导航", "自动集尘", "4000Pa"],
      stock: { "US-WEST": 120, "DE-FRA": 64, "SG-SIN": 30, "CN-SZ": 380 },
      channels: { DIRECT: "在售", AMAZON: "在售", "AMAZON-DE": "在售", TIKTOK: "草稿" },
      images: shots("white lidar robot vacuum cleaner on light wooden floor of a modern minimal living room, product photography"),
      desc: "LDS 激光导航与 4000Pa 强吸力，自动集尘基站，支持多层地图与禁区设置。",
      specs: { "吸力": "4000Pa", "导航": "LDS 激光", "电池": "5200mAh", "集尘": "自动集尘 60 天", "越障": "2cm", "噪音": "≤65dB" } },

    { id: "P006", spu: "SPU-100301", sku: "SKU-100301-WH", name: "KeyForge 三模机械键盘 87 键", nameEn: "KeyForge Mechanical Keyboard 87",
      category: "audio", categoryLabel: "影音数码", brand: "KeyForge", origin: "CN", hsCode: "8471606000",
      declaredName: "Mechanical Keyboard", declaredValue: 26.0, weightG: 880, status: "ON_SALE", badge: "",
      price: { USD: 6999, EUR: 6599, CNY: 49900 }, was: { USD: 8999, EUR: 8499, CNY: 62900 },
      rating: 4.9, reviews: 1580, sold: "3.2k", colors: ["奶白", "深空灰"], sizes: ["青轴", "茶轴", "静音红轴"],
      tags: ["三模连接", "热插拔", "Gasket 结构"],
      stock: { "US-WEST": 260, "DE-FRA": 180, "SG-SIN": 90, "CN-SZ": 1520 },
      channels: { DIRECT: "在售", AMAZON: "在售", "AMAZON-DE": "在售", TIKTOK: "在售" },
      images: shots("compact 87-key mechanical keyboard with white keycaps and subtle RGB backlight, product photo on clean desk"),
      desc: "Gasket 结构 + 热插拔轴座，三模连接（有线 / 2.4G / 蓝牙），87 键紧凑布局。",
      specs: { "配列": "87 键", "连接": "有线 / 2.4G / 蓝牙", "轴体": "热插拔", "结构": "Gasket", "电池": "4000mAh", "重量": "880g" } },

    { id: "P007", spu: "SPU-100318", sku: "SKU-100318-CE", name: "Aurea 陶瓷香薰加湿器", nameEn: "Aurea Ceramic Aroma Diffuser",
      category: "home", categoryLabel: "家居厨房", brand: "Aurea", origin: "CN", hsCode: "8479899990",
      declaredName: "Aroma Diffuser", declaredValue: 9.6, weightG: 520, status: "ON_SALE", badge: "",
      price: { USD: 4299, EUR: 3999, CNY: 29900 }, was: { USD: 5299, EUR: 4999, CNY: 36900 },
      rating: 4.4, reviews: 940, sold: "2.1k", colors: ["米白", "岩灰"], sizes: [],
      tags: ["超声波雾化", "七彩氛围灯", "静音"],
      stock: { "US-WEST": 420, "DE-FRA": 260, "SG-SIN": 180, "CN-SZ": 2100 },
      channels: { DIRECT: "在售", AMAZON: "草稿", "AMAZON-DE": "在售", TIKTOK: "在售" },
      images: shots("minimal ceramic aroma diffuser humidifier releasing fine mist, warm neutral background, product photography"),
      desc: "超声波静音雾化，陶瓷外壳与七彩氛围灯，300ml 容量适合卧室与办公桌。",
      specs: { "容量": "300ml", "雾化": "超声波", "噪音": "≤28dB", "定时": "2/4/6h", "灯效": "七彩可调", "重量": "520g" } },

    { id: "P008", spu: "SPU-100330", sku: "SKU-100330-WH", name: "BrightSmile 声波电动牙刷", nameEn: "BrightSmile Sonic Toothbrush",
      category: "personal", categoryLabel: "个护健康", brand: "BrightSmile", origin: "CN", hsCode: "8509800000",
      declaredName: "Electric Toothbrush", declaredValue: 11.2, weightG: 210, status: "ON_SALE", badge: "",
      price: { USD: 3999, EUR: 3799, CNY: 27900 }, was: { USD: 4999, EUR: 4699, CNY: 34900 },
      rating: 4.6, reviews: 1120, sold: "2.7k", colors: ["雪白", "雾粉"], sizes: [],
      tags: ["5 种模式", "IPX7", "90 天续航"],
      stock: { "US-WEST": 380, "DE-FRA": 210, "SG-SIN": 260, "CN-SZ": 1800 },
      channels: { DIRECT: "在售", AMAZON: "在售", "AMAZON-DE": "在售", TIKTOK: "在售" },
      images: shots("electric sonic toothbrush with charging base, white handle, clean bright product photo"),
      desc: "每分钟 38,000 次声波震动，5 种清洁模式，IPX7 全身水洗，一次充电用 90 天。",
      specs: { "频率": "38000 次/分", "模式": "5 种", "防水": "IPX7", "续航": "90 天", "刷头": "标配 3 支", "重量": "210g" } },

    { id: "P009", spu: "SPU-100345", sku: "SKU-100345-BK", name: "CrispAir 5.5L 可视化空气炸锅", nameEn: "CrispAir Digital Air Fryer",
      category: "home", categoryLabel: "家居厨房", brand: "CrispAir", origin: "CN", hsCode: "8516609000",
      declaredName: "Air Fryer", declaredValue: 32.0, weightG: 5200, status: "ON_SALE", badge: "大件",
      price: { USD: 7999, EUR: 7599, CNY: 55900 }, was: { USD: 9999, EUR: 9499, CNY: 69900 },
      rating: 4.5, reviews: 623, sold: "1.4k", colors: ["哑黑"], sizes: ["5.5L"],
      tags: ["可视窗", "1500W", "8 种预设"],
      stock: { "US-WEST": 150, "DE-FRA": 78, "SG-SIN": 0, "CN-SZ": 520 },
      channels: { DIRECT: "在售", AMAZON: "在售", "AMAZON-DE": "在售", TIKTOK: "草稿" },
      images: shots("modern digital air fryer with visible window on a kitchen countertop, product photography"),
      desc: "5.5L 大容量与可视窗设计，1500W 热风循环，8 种智能预设一键出餐。",
      specs: { "容量": "5.5L", "功率": "1500W", "预设": "8 种", "温度": "80-200℃", "定时": "60 分钟", "重量": "5.2kg" } },

    { id: "P010", spu: "SPU-100360", sku: "SKU-100360-BK", name: "SunVolt 20000mAh 太阳能充电宝", nameEn: "SunVolt Solar Power Bank",
      category: "outdoor", categoryLabel: "户外运动", brand: "SunVolt", origin: "CN", hsCode: "8507600090",
      declaredName: "Solar Power Bank", declaredValue: 13.5, weightG: 460, status: "ON_SALE", badge: "带电",
      price: { USD: 4599, EUR: 4299, CNY: 31900 }, was: { USD: 5599, EUR: 5299, CNY: 38900 },
      rating: 4.3, reviews: 1050, sold: "2.9k", colors: ["战术黑", "迷彩"], sizes: [],
      tags: ["20000mAh", "太阳能板", "IP65"],
      stock: { "US-WEST": 290, "DE-FRA": 0, "SG-SIN": 120, "CN-SZ": 1400 },
      channels: { DIRECT: "在售", AMAZON: "在售", "AMAZON-DE": "暂停", TIKTOK: "在售" },
      images: shots("rugged solar power bank with carabiner and solar panel, outdoor product photography, clean light background"),
      desc: "20000mAh 大容量户外电源，IP65 防尘防水，折叠太阳能板与四重保护电路。",
      specs: { "容量": "20000mAh", "输出": "22.5W 快充", "太阳能": "5W 折叠板", "防护": "IP65", "接口": "USB-A ×2 / USB-C", "重量": "460g" } },

    { id: "P011", spu: "SPU-100377", sku: "SKU-100377-BK", name: "GlideX 折叠电动滑板车", nameEn: "GlideX Folding E-Scooter",
      category: "outdoor", categoryLabel: "户外运动", brand: "GlideX", origin: "CN", hsCode: "8711600000",
      declaredName: "Electric Scooter", declaredValue: 180.0, weightG: 14500, status: "ON_SALE", badge: "带电 · 大件",
      price: { USD: 39999, EUR: 37999, CNY: 279900 }, was: { USD: 46999, EUR: 44999, CNY: 329900 },
      rating: 4.4, reviews: 218, sold: "0.3k", colors: ["哑光黑"], sizes: [],
      tags: ["350W 电机", "35km 续航", "一键折叠"],
      stock: { "US-WEST": 42, "DE-FRA": 26, "SG-SIN": 0, "CN-SZ": 96 },
      channels: { DIRECT: "在售", AMAZON: "草稿", "AMAZON-DE": "在售", TIKTOK: "草稿" },
      images: shots("folding electric scooter matte black standing on a modern city street, product photography"),
      desc: "350W 无刷电机与 35km 续航，10 英寸充气胎，3 秒一键折叠，通勤代步利器。",
      specs: { "电机": "350W", "续航": "35km", "最高时速": "25km/h", "轮胎": "10\" 充气", "折叠": "3 秒", "整车重": "14.5kg" } },

    { id: "P012", spu: "SPU-100390", sku: "SKU-100390-WH", name: "PawFeed 智能定时宠物喂食器", nameEn: "PawFeed Smart Pet Feeder",
      category: "pet", categoryLabel: "宠物用品", brand: "PawFeed", origin: "CN", hsCode: "8509800090",
      declaredName: "Pet Feeder", declaredValue: 18.9, weightG: 1900, status: "ON_SALE", badge: "",
      price: { USD: 5999, EUR: 5699, CNY: 42900 }, was: { USD: 7299, EUR: 6899, CNY: 51900 },
      rating: 4.7, reviews: 512, sold: "1.0k", colors: ["暖白"], sizes: ["4L"],
      tags: ["App 控制", "1080P 摄像头", "双向语音"],
      stock: { "US-WEST": 175, "DE-FRA": 88, "SG-SIN": 60, "CN-SZ": 700 },
      channels: { DIRECT: "在售", AMAZON: "在售", "AMAZON-DE": "在售", TIKTOK: "在售" },
      images: shots("smart automatic pet feeder with app control placed next to a cat, product photography, clean light background"),
      desc: "4L 密封粮仓与定时投喂，1080P 摄像头 + 双向语音，App 远程喂食与录像回看。",
      specs: { "容量": "4L", "摄像头": "1080P", "语音": "双向", "连接": "Wi-Fi 2.4G", "供电": "USB 适配器 + 4×D 电池", "重量": "1.9kg" } }
  ];

  /* ---------- 客户 ---------- */
  var CUSTOMERS = [
    { id: "C001", name: "Emily Carter", email: "emily.carter@example.com", country: "US", city: "Los Angeles, CA", tier: "VIP" },
    { id: "C002", name: "Lukas Müller", email: "lukas.mueller@example.de", country: "DE", city: "Frankfurt", tier: "标准" },
    { id: "C003", name: "Tan Wei Jie", email: "weijie.tan@example.sg", country: "SG", city: "Singapore", tier: "标准" },
    { id: "C004", name: "Sofia Rossi", email: "sofia.rossi@example.it", country: "IT", city: "Milan", tier: "VIP" },
    { id: "C005", name: "James Miller", email: "james.miller@example.com", country: "US", city: "Austin, TX", tier: "标准" }
  ];

  function item(pid, qty, unit) {
    var p = PRODUCTS.find(function (x) { return x.id === pid; });
    return { productId: pid, name: p.name, sku: p.sku, image: p.images[0], spec: p.colors[0] + (p.sizes[0] ? " / " + p.sizes[0] : ""), qty: qty, unit: unit };
  }

  /* ---------- 订单（含快照 / 支付 / 履约 / 物流 / 状态历史） ---------- */
  var ORDERS = [
    { no: "NC-20261012-0431", customerId: "C001", channel: "DIRECT", country: "US", currency: "USD",
      status: "PENDING_PAYMENT", payStatus: "UNPAID", fulStatus: "UNFULFILLED", afsStatus: "NONE",
      placedAt: "2026-10-12 09:14", paidAt: null,
      amounts: { subtotal: 12999, discount: 1300, shipping: 0, tax: 1098, total: 12797 },
      items: [item("P001", 1, 12999)],
      pay: [{ no: "PAY-20261012-8821", provider: "Stripe", method: "Visa •••• 4242", amount: 12797, status: "PENDING", at: "2026-10-12 09:14" }],
      ful: [], ship: [],
      history: [
        { t: "订单创建", at: "2026-10-12 09:14", by: "系统", note: "来源：独立站下单，已生成商品与价格快照" },
        { t: "库存锁定", at: "2026-10-12 09:14", by: "库存中心", note: "美国仓锁定 1 件" },
        { t: "等待支付", at: "2026-10-12 09:14", by: "支付中心", note: "支付超时时间 2026-10-12 09:44" }
      ],
      snapshot: { recipient: "Emily Carter", phone: "+1 310-555-0132", addr: "1420 Sunset Blvd, Los Angeles, CA 90026", taxRule: "US-CA-v2026.09", rateVer: "FX-2026.10.12", rate: 1.0 } },

    { no: "NC-20261012-0398", customerId: "C005", channel: "DIRECT", country: "US", currency: "USD",
      status: "PAID", payStatus: "PAID", fulStatus: "UNFULFILLED", afsStatus: "NONE",
      placedAt: "2026-10-12 08:02", paidAt: "2026-10-12 08:05",
      amounts: { subtotal: 16998, discount: 1700, shipping: 0, tax: 1436, total: 16734 },
      items: [item("P006", 1, 6999), item("P008", 2, 3999)],
      pay: [{ no: "PAY-20261012-8760", provider: "Stripe", method: "Apple Pay", amount: 16734, status: "SUCCEEDED", at: "2026-10-12 08:05" }],
      ful: [{ no: "FO-20261012-0761", wh: "US-WEST", status: "CREATED", eta: "2026-10-13" }], ship: [],
      history: [
        { t: "订单创建", at: "2026-10-12 08:02", by: "系统", note: "独立站下单" },
        { t: "支付成功", at: "2026-10-12 08:05", by: "支付中心", note: "Stripe 回调校验通过，幂等键 PAY-20261012-8760" },
        { t: "订单确认", at: "2026-10-12 08:05", by: "订单中心", note: "生成履约单 FO-20261012-0761" }
      ],
      snapshot: { recipient: "James Miller", phone: "+1 512-555-0177", addr: "88 Congress Ave, Austin, TX 78701", taxRule: "US-TX-v2026.09", rateVer: "FX-2026.10.12", rate: 1.0 } },

    { no: "NC-20261011-0356", customerId: "C002", channel: "AMAZON-DE", country: "DE", currency: "EUR",
      status: "FULFILLING", payStatus: "PAID", fulStatus: "PARTIALLY_FULFILLED", afsStatus: "NONE",
      placedAt: "2026-10-11 21:40", paidAt: "2026-10-11 21:41",
      amounts: { subtotal: 23998, discount: 0, shipping: 699, tax: 4693, total: 29390 },
      items: [item("P005", 1, 23999), item("P009", 1, 7599)],
      pay: [{ no: "PAY-20261011-8640", provider: "Adyen", method: "SEPA Direct Debit", amount: 29390, status: "SUCCEEDED", at: "2026-10-11 21:41" }],
      ful: [
        { no: "FO-20261011-0725", wh: "DE-FRA", status: "PACKED", eta: "2026-10-13" },
        { no: "FO-20261011-0726", wh: "CN-SZ", status: "ALLOCATED", eta: "2026-10-18", note: "德国仓缺货，拆分至中国仓直发" }
      ], ship: [],
      history: [
        { t: "订单接入", at: "2026-10-11 21:40", by: "渠道适配器", note: "Amazon DE 订单拉取，外部单号 302-8841203-9917" },
        { t: "支付成功", at: "2026-10-11 21:41", by: "支付中心", note: "Adyen 回调校验通过" },
        { t: "履约拆分", at: "2026-10-11 22:10", by: "履约中心", note: "按仓可用性拆分为 2 个履约单" },
        { t: "打包完成", at: "2026-10-12 07:20", by: "德国仓", note: "FO-20261011-0725 已打包待出库" }
      ],
      snapshot: { recipient: "Lukas Müller", phone: "+49 69 5550 118", addr: "Neue Mainzer Str. 42, 60311 Frankfurt am Main", taxRule: "DE-VAT19-v2026.08", rateVer: "FX-2026.10.11", rate: 0.92 } },

    { no: "NC-20261010-0291", customerId: "C003", channel: "TIKTOK", country: "SG", currency: "USD",
      status: "SHIPPED", payStatus: "PAID", fulStatus: "FULFILLED", afsStatus: "NONE",
      placedAt: "2026-10-10 15:22", paidAt: "2026-10-10 15:24",
      amounts: { subtotal: 9999, discount: 500, shipping: 399, tax: 712, total: 10610 },
      items: [item("P002", 1, 9999)],
      pay: [{ no: "PAY-20261010-8512", provider: "Stripe", method: "Visa •••• 1881", amount: 10610, status: "SUCCEEDED", at: "2026-10-10 15:24" }],
      ful: [{ no: "FO-20261010-0702", wh: "SG-SIN", status: "SHIPPED", eta: "2026-10-12" }],
      ship: [{ no: "SH-20261010-0513", carrier: "Ninja Van", tracking: "NV9821147365SG", status: "IN_TRANSIT",
        events: [
          { at: "2026-10-10 18:40", code: "已揽收", loc: "Singapore Jurong Hub" },
          { at: "2026-10-11 02:15", code: "运输中", loc: "Singapore Sortation Center" },
          { at: "2026-10-12 07:05", code: "派送中", loc: "Singapore Bedok" }
        ] }],
      history: [
        { t: "订单接入", at: "2026-10-10 15:22", by: "渠道适配器", note: "TikTok Shop SG Webhook" },
        { t: "支付成功", at: "2026-10-10 15:24", by: "支付中心", note: "Stripe 回调" },
        { t: "已发货", at: "2026-10-10 18:40", by: "新加坡仓", note: "运单 NV9821147365SG" }
      ],
      snapshot: { recipient: "Tan Wei Jie", phone: "+65 8123 4470", addr: "Blk 214 Bedok North St 1 #08-155, Singapore 460214", taxRule: "SG-GST9-v2026.07", rateVer: "FX-2026.10.10", rate: 1.0 } },

    { no: "NC-20261008-0244", customerId: "C001", channel: "DIRECT", country: "US", currency: "USD",
      status: "IN_TRANSIT", payStatus: "PAID", fulStatus: "FULFILLED", afsStatus: "NONE",
      placedAt: "2026-10-08 11:35", paidAt: "2026-10-08 11:36",
      amounts: { subtotal: 24999, discount: 2500, shipping: 0, tax: 2025, total: 24524 },
      items: [item("P005", 1, 24999)],
      pay: [{ no: "PAY-20261008-8390", provider: "Stripe", method: "Visa •••• 4242", amount: 24524, status: "SUCCEEDED", at: "2026-10-08 11:36" }],
      ful: [{ no: "FO-20261008-0644", wh: "US-WEST", status: "SHIPPED", eta: "2026-10-12" }],
      ship: [{ no: "SH-20261008-0471", carrier: "FedEx", tracking: "7749 2201 8834", status: "IN_TRANSIT",
        events: [
          { at: "2026-10-08 16:10", code: "已出库", loc: "Los Angeles, CA" },
          { at: "2026-10-09 09:22", code: "运输中", loc: "Phoenix, AZ" },
          { at: "2026-10-11 13:48", code: "运输中", loc: "Houston, TX" }
        ] }],
      history: [
        { t: "订单创建", at: "2026-10-08 11:35", by: "系统", note: "独立站下单" },
        { t: "支付成功", at: "2026-10-08 11:36", by: "支付中心", note: "Stripe 回调" },
        { t: "已发货", at: "2026-10-08 16:10", by: "美国仓", note: "FedEx 面单已打印" },
        { t: "运输中", at: "2026-10-09 09:22", by: "物流中心", note: "轨迹事件回传" }
      ],
      snapshot: { recipient: "Emily Carter", phone: "+1 310-555-0132", addr: "1420 Sunset Blvd, Los Angeles, CA 90026", taxRule: "US-CA-v2026.09", rateVer: "FX-2026.10.08", rate: 1.0 } },

    { no: "NC-20261005-0187", customerId: "C002", channel: "AMAZON-DE", country: "DE", currency: "EUR",
      status: "DELIVERED", payStatus: "PAID", fulStatus: "FULFILLED", afsStatus: "REQUESTED",
      placedAt: "2026-10-05 10:12", paidAt: "2026-10-05 10:13",
      amounts: { subtotal: 11798, discount: 0, shipping: 499, tax: 2336, total: 14633 },
      items: [item("P007", 1, 3999), item("P008", 1, 3799), item("P003", 1, 3999)],
      pay: [{ no: "PAY-20261005-8102", provider: "Adyen", method: "Mastercard •••• 5521", amount: 14633, status: "SUCCEEDED", at: "2026-10-05 10:13" }],
      ful: [{ no: "FO-20261005-0588", wh: "DE-FRA", status: "DELIVERED", eta: "2026-10-08" }],
      ship: [{ no: "SH-20261005-0390", carrier: "DHL", tracking: "JJD00022288841", status: "DELIVERED",
        events: [
          { at: "2026-10-05 19:05", code: "已出库", loc: "Frankfurt, DE" },
          { at: "2026-10-07 08:30", code: "派送中", loc: "Frankfurt, DE" },
          { at: "2026-10-08 11:12", code: "已妥投", loc: "Frankfurt, DE", note: "签收人 L. Müller" }
        ] }],
      history: [
        { t: "订单接入", at: "2026-10-05 10:12", by: "渠道适配器", note: "Amazon DE 外部单号 302-5590128-3320" },
        { t: "支付成功", at: "2026-10-05 10:13", by: "支付中心", note: "Adyen 回调" },
        { t: "已发货", at: "2026-10-05 19:05", by: "德国仓", note: "DHL 面单" },
        { t: "已妥投", at: "2026-10-08 11:12", by: "物流中心", note: "POD 已上传" },
        { t: "售后申请", at: "2026-10-09 09:40", by: "客服", note: "香薰加湿器出雾异常，申请退货" }
      ],
      snapshot: { recipient: "Lukas Müller", phone: "+49 69 5550 118", addr: "Neue Mainzer Str. 42, 60311 Frankfurt am Main", taxRule: "DE-VAT19-v2026.08", rateVer: "FX-2026.10.05", rate: 0.92 } },

    { no: "NC-20260930-0112", customerId: "C004", channel: "DIRECT", country: "IT", currency: "EUR",
      status: "COMPLETED", payStatus: "PAID", fulStatus: "FULFILLED", afsStatus: "NONE",
      placedAt: "2026-09-30 14:50", paidAt: "2026-09-30 14:51",
      amounts: { subtotal: 6599, discount: 660, shipping: 599, tax: 1437, total: 7975 },
      items: [item("P006", 1, 6599)],
      pay: [{ no: "PAY-20260930-7788", provider: "Stripe", method: "Visa •••• 9012", amount: 7975, status: "SUCCEEDED", at: "2026-09-30 14:51" }],
      ful: [{ no: "FO-20260930-0411", wh: "CN-SZ", status: "DELIVERED", eta: "2026-10-07" }],
      ship: [{ no: "SH-20260930-0288", carrier: "4PX", tracking: "4PX2026093000IT", status: "DELIVERED",
        events: [
          { at: "2026-09-30 20:15", code: "已出库", loc: "Shenzhen, CN" },
          { at: "2026-10-03 06:40", code: "清关中", loc: "Milan Malpensa, IT" },
          { at: "2026-10-05 09:30", code: "清关完成", loc: "Milan, IT" },
          { at: "2026-10-07 15:20", code: "已妥投", loc: "Milan, IT" }
        ] }],
      history: [
        { t: "订单创建", at: "2026-09-30 14:50", by: "系统", note: "独立站下单，中国仓直发" },
        { t: "支付成功", at: "2026-09-30 14:51", by: "支付中心", note: "Stripe 回调" },
        { t: "已发货", at: "2026-09-30 20:15", by: "中国仓", note: "4PX 专线" },
        { t: "已妥投", at: "2026-10-07 15:20", by: "物流中心", note: "订单完成" }
      ],
      snapshot: { recipient: "Sofia Rossi", phone: "+39 02 5550 771", addr: "Via Torino 61, 20123 Milano MI", taxRule: "IT-VAT22-v2026.06", rateVer: "FX-2026.09.30", rate: 0.92 } },

    { no: "NC-20260928-0064", customerId: "C003", channel: "SHOPEE", country: "SG", currency: "USD",
      status: "CANCELLED", payStatus: "FAILED", fulStatus: "UNFULFILLED", afsStatus: "NONE",
      placedAt: "2026-09-28 19:31", paidAt: null,
      amounts: { subtotal: 4599, discount: 0, shipping: 399, tax: 450, total: 5448 },
      items: [item("P010", 1, 4599)],
      pay: [{ no: "PAY-20260928-7011", provider: "Shopee Pay", method: "Wallet", amount: 5448, status: "FAILED", at: "2026-09-28 19:31" }],
      ful: [], ship: [],
      history: [
        { t: "订单接入", at: "2026-09-28 19:31", by: "渠道适配器", note: "Shopee SG 订单" },
        { t: "支付失败", at: "2026-09-28 19:31", by: "支付中心", note: "钱包余额不足" },
        { t: "订单取消", at: "2026-09-28 20:01", by: "系统", note: "超时未支付，释放库存" }
      ],
      snapshot: { recipient: "Tan Wei Jie", phone: "+65 8123 4470", addr: "Blk 214 Bedok North St 1 #08-155, Singapore 460214", taxRule: "SG-GST9-v2026.07", rateVer: "FX-2026.09.28", rate: 1.0 } }
  ];

  /* ---------- 售后 / 退款 ---------- */
  var REFUNDS = [
    { no: "RF-20261009-0142", orderNo: "NC-20261005-0187", customer: "Lukas Müller", type: "退货退款", amount: 3999, currency: "EUR",
      status: "PROCESSING", reason: "商品功能异常", at: "2026-10-09 09:40", channel: "AMAZON-DE",
      flow: ["申请已提交", "客服审核通过", "退货物流在途", "待收货入库", "退款待发起"] },
    { no: "RF-20261007-0138", orderNo: "NC-20260930-0112", customer: "Sofia Rossi", type: "仅退款", amount: 500, currency: "EUR",
      status: "SUCCEEDED", reason: "物流延误补偿", at: "2026-10-07 16:20", channel: "DIRECT",
      flow: ["申请已提交", "自动审核通过", "退款成功"] },
    { no: "RF-20261006-0131", orderNo: "NC-20261005-0187", customer: "Lukas Müller", type: "换货", amount: 0, currency: "EUR",
      status: "REJECTED", reason: "重复提交", at: "2026-10-06 11:05", channel: "AMAZON-DE",
      flow: ["申请已提交", "客服驳回（重复工单）"] },
    { no: "RF-20261003-0120", orderNo: "NC-20260930-0112", customer: "Sofia Rossi", type: "退货退款", amount: 6599, currency: "EUR",
      status: "SUCCEEDED", reason: "七天无理由", at: "2026-10-03 10:12", channel: "DIRECT",
      flow: ["申请已提交", "客服审核通过", "退货已入库", "退款成功"] },
    { no: "RF-20261002-0117", orderNo: "NC-20261008-0244", customer: "Emily Carter", type: "拒付", amount: 24524, currency: "USD",
      status: "REQUESTED", reason: "持卡人发起拒付（Chargeback）", at: "2026-10-02 08:00", channel: "DIRECT",
      flow: ["银行拒付通知", "证据收集", "待提交申诉"] }
  ];
  var AFS_STATUS = {
    REQUESTED: { label: "待处理", cls: "warning" }, PROCESSING: { label: "处理中", cls: "accent" },
    SUCCEEDED: { label: "已完成", cls: "success" }, REJECTED: { label: "已驳回", cls: "neutral" }
  };

  /* ---------- 支付对账 ---------- */
  var RECONCILIATION = [
    { id: "RC-20261012-01", date: "2026-10-12", channel: "Stripe", currency: "USD", internal: 128, platform: 128, amount: 1842300, diff: 0, status: "MATCHED" },
    { id: "RC-20261012-02", date: "2026-10-12", channel: "Adyen", currency: "EUR", internal: 76, platform: 75, amount: 926400, diff: -14633, status: "DIFF" },
    { id: "RC-20261011-03", date: "2026-10-11", channel: "Stripe", currency: "USD", internal: 143, platform: 143, amount: 2108900, diff: 0, status: "MATCHED" },
    { id: "RC-20261011-04", date: "2026-10-11", channel: "Shopee Pay", currency: "USD", internal: 22, platform: 23, amount: 118300, diff: 5448, status: "DIFF" },
    { id: "RC-20261010-05", date: "2026-10-10", channel: "Stripe", currency: "USD", internal: 166, platform: 166, amount: 2488100, diff: 0, status: "MATCHED" },
    { id: "RC-20261010-06", date: "2026-10-10", channel: "Adyen", currency: "EUR", internal: 91, platform: 91, amount: 1104200, diff: 0, status: "MATCHED" },
    { id: "RC-20261009-07", date: "2026-10-09", channel: "Ninja Van", currency: "USD", internal: 54, platform: 53, amount: 0, diff: 0, status: "PENDING" }
  ];
  var RECON_STATUS = { MATCHED: { label: "已平账", cls: "success" }, DIFF: { label: "存在差异", cls: "danger" }, PENDING: { label: "待核对", cls: "warning" } };

  /* ---------- 库存流水 ---------- */
  var INVENTORY_LOG = [
    { at: "2026-10-12 09:14", sku: "SKU-100231-GR", wh: "US-WEST", type: "锁定", qty: -1, ref: "NC-20261012-0431", by: "库存中心" },
    { at: "2026-10-12 08:05", sku: "SKU-100301-WH", wh: "US-WEST", type: "扣减", qty: -1, ref: "NC-20261012-0398", by: "库存中心" },
    { at: "2026-10-12 08:05", sku: "SKU-100330-WH", wh: "US-WEST", type: "扣减", qty: -2, ref: "NC-20261012-0398", by: "库存中心" },
    { at: "2026-10-12 07:20", sku: "SKU-100288-WH", wh: "DE-FRA", type: "扣减", qty: -1, ref: "FO-20261011-0725", by: "德国仓" },
    { at: "2026-10-11 22:10", sku: "SKU-100345-BK", wh: "CN-SZ", type: "分配", qty: -1, ref: "FO-20261011-0726", by: "履约中心" },
    { at: "2026-10-11 14:02", sku: "SKU-100360-BK", wh: "US-WEST", type: "入库", qty: 200, ref: "ASN-20261011-0092", by: "美国仓" },
    { at: "2026-10-10 18:40", sku: "SKU-100244-BK", wh: "SG-SIN", type: "扣减", qty: -1, ref: "FO-20261010-0702", by: "新加坡仓" },
    { at: "2026-10-09 10:08", sku: "SKU-100318-CE", wh: "DE-FRA", type: "回补", qty: 1, ref: "RF-20261009-0142", by: "德国仓" }
  ];

  /* ---------- 运营工作台指标 ---------- */
  var DASHBOARD = {
    kpis: [
      { key: "gmv", label: "今日 GMV", value: "$184,230", delta: "+12.4%", dir: "up", glyph: "wallet", tone: "" },
      { key: "orders", label: "今日订单", value: "1,286", delta: "+8.1%", dir: "up", glyph: "receipt", tone: "accent" },
      { key: "aov", label: "客单价 AOV", value: "$143.26", delta: "+3.6%", dir: "up", glyph: "percent", tone: "gold" },
      { key: "payrate", label: "支付成功率", value: "96.8%", delta: "-0.4%", dir: "down", glyph: "credit-card", tone: "info" }
    ],
    trend: [
      { d: "10/06", v: 62 }, { d: "10/07", v: 74 }, { d: "10/08", v: 58 }, { d: "10/09", v: 86 },
      { d: "10/10", v: 92 }, { d: "10/11", v: 78 }, { d: "10/12", v: 100 }
    ],
    markets: [
      { name: "美国 US", v: 46, color: "#1B6360" }, { name: "德国 DE", v: 22, color: "#2C7E79" },
      { name: "新加坡 SG", v: 14, color: "#C9622B" }, { name: "意大利 IT", v: 10, color: "#D9A441" },
      { name: "其他", v: 8, color: "#8FBDB8" }
    ],
    channels: [
      { name: "独立站", v: 42, orders: 540 }, { name: "Amazon 北美", v: 28, orders: 360 },
      { name: "Amazon 德国", v: 16, orders: 206 }, { name: "TikTok Shop", v: 9, orders: 116 },
      { name: "Shopee", v: 5, orders: 64 }
    ],
    todos: [
      { t: "3 笔订单待审核（含 1 笔拒付）", lvl: "danger", to: "admin-aftersale.html" },
      { t: "2 条对账差异待处理", lvl: "warning", to: "admin-reconciliation.html" },
      { t: "德国仓 2 个 SKU 低库存预警", lvl: "warning", to: "admin-inventory.html" },
      { t: "1 个履约单超时未发货", lvl: "danger", to: "admin-fulfillment.html" },
      { t: "TikTok 渠道 6 个商品待同步", lvl: "info", to: "admin-products.html" }
    ]
  };

  var EXTENSIONS = {
    SHOP: {
      name: "NovaCross 官方旗舰店", code: "SHOP-NOVA-001", owner: "诺瓦跨境贸易有限公司", health: 96,
      kpis: [{ label: "本月销售额", value: "$184,230", delta: "+12.4%", glyph: "wallet", tone: "" }, { label: "本月订单", value: "1,286", delta: "+8.1%", glyph: "receipt", tone: "accent" }, { label: "商品动销率", value: "78.6%", delta: "+4.2%", glyph: "box", tone: "gold" }, { label: "店铺评分", value: "4.86", delta: "+0.08", glyph: "star", tone: "info" }],
      todos: ["3 个商品库存低于安全线", "2 条活动素材待审核", "1 笔订单待补发面单", "本周结算单已生成"],
      campaigns: [{ name: "黑五提前购", status: "进行中", reach: "82.4k", conversion: "4.8%", budget: "$12,000" }, { name: "新客首单 15% OFF", status: "已结束", reach: "31.2k", conversion: "3.2%", budget: "$4,800" }]
    },
    PLATFORM: {
      kpis: [{ label: "平台 GMV", value: "$2.48M", delta: "+18.6%", glyph: "trend-up", tone: "" }, { label: "活跃店铺", value: "128", delta: "+12", glyph: "store", tone: "accent" }, { label: "今日交易订单", value: "8,642", delta: "+9.8%", glyph: "receipt", tone: "gold" }, { label: "风控拦截", value: "36", delta: "-22.4%", glyph: "shield-check", tone: "info" }],
      shops: [{ name: "NovaCross 官方旗舰店", code: "SHOP-NOVA-001", category: "3C 数码", gmv: "$184,230", orders: 1286, health: 96, status: "正常" }, { name: "森野户外旗舰店", code: "SHOP-WILD-018", category: "户外运动", gmv: "$92,480", orders: 684, health: 89, status: "正常" }, { name: "Luma Home 欧洲店", code: "SHOP-LUMA-042", category: "家居生活", gmv: "$67,190", orders: 512, health: 72, status: "待整改" }, { name: "PawMate 宠物用品店", code: "SHOP-PAW-077", category: "宠物用品", gmv: "$43,260", orders: 328, health: 94, status: "正常" }],
      audits: [{ time: "10:24:16", event: "店铺登录异常", target: "SHOP-LUMA-042", level: "高风险", actor: "risk-engine" }, { time: "09:48:02", event: "批量改价", target: "SKU-100231-GR", level: "需复核", actor: "杨琳" }, { time: "09:12:44", event: "新店铺入驻", target: "SHOP-PAW-077", level: "正常", actor: "system" }]
    }
  };


  var ORDER_STATUS = {
    CREATED: { label: "已创建", cls: "neutral" }, PENDING_PAYMENT: { label: "待支付", cls: "warning" },
    PAID: { label: "已支付", cls: "info" }, CONFIRMED: { label: "已确认", cls: "brand" },
    FULFILLING: { label: "履约中", cls: "accent" }, SHIPPED: { label: "已发货", cls: "info" },
    IN_TRANSIT: { label: "运输中", cls: "info" }, DELIVERED: { label: "已妥投", cls: "success" },
    COMPLETED: { label: "已完成", cls: "success" }, CANCELLED: { label: "已取消", cls: "neutral" }
  };
  var PAY_STATUS = {
    UNPAID: { label: "未支付", cls: "warning" }, PAID: { label: "已支付", cls: "success" },
    PARTIALLY_PAID: { label: "部分支付", cls: "info" }, REFUNDING: { label: "退款中", cls: "accent" },
    REFUNDED: { label: "已退款", cls: "neutral" }, FAILED: { label: "支付失败", cls: "danger" }
  };
  var FUL_STATUS = {
    UNFULFILLED: { label: "未履约", cls: "neutral" }, PARTIALLY_FULFILLED: { label: "部分履约", cls: "warning" },
    FULFILLED: { label: "已履约", cls: "success" }
  };
  var FUL_ORDER_STATUS = {
    CREATED: { label: "已创建", cls: "neutral" }, ALLOCATED: { label: "已分配", cls: "info" },
    PICKING: { label: "拣货中", cls: "accent" }, PACKED: { label: "已打包", cls: "accent" },
    SHIPPED: { label: "已发货", cls: "info" }, DELIVERED: { label: "已妥投", cls: "success" },
    CANCELLED: { label: "已取消", cls: "neutral" }
  };
  var PAY_TXN_STATUS = {
    PENDING: { label: "支付中", cls: "warning" }, SUCCEEDED: { label: "成功", cls: "success" },
    FAILED: { label: "失败", cls: "danger" }, EXPIRED: { label: "已过期", cls: "neutral" }
  };
  var PRODUCT_STATUS = { ON_SALE: { label: "在售", cls: "success" }, PAUSED: { label: "暂停", cls: "warning" }, OFF_SALE: { label: "下架", cls: "neutral" } };
  var CHANNEL_LISTING_STATUS = { "在售": "success", "草稿": "neutral", "暂停": "warning", "同步中": "accent" };

  g.DB = {
    BRAND: { name: "NovaCross", cn: "诺瓦跨境优选" },
    WAREHOUSES: WAREHOUSES, CHANNELS: CHANNELS, CATEGORIES: CATEGORIES,
    PRODUCTS: PRODUCTS, CUSTOMERS: CUSTOMERS, ORDERS: ORDERS, REFUNDS: REFUNDS,
    RECONCILIATION: RECONCILIATION, INVENTORY_LOG: INVENTORY_LOG, DASHBOARD: DASHBOARD,
    ORDER_STATUS: ORDER_STATUS, PAY_STATUS: PAY_STATUS, FUL_STATUS: FUL_STATUS,
    FUL_ORDER_STATUS: FUL_ORDER_STATUS, PAY_TXN_STATUS: PAY_TXN_STATUS,
    PRODUCT_STATUS: PRODUCT_STATUS, CHANNEL_LISTING_STATUS: CHANNEL_LISTING_STATUS, AFS_STATUS: AFS_STATUS,
    RECON_STATUS: RECON_STATUS,
    EXTENSIONS: EXTENSIONS,
    img: img
  };
})(window);
