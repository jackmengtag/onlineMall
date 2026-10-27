-- ============================================================================
-- NovaCross 跨境电商平台 · 四端数据模型（MySQL 8 / RuoYi-Vue 3.9.2）
-- 依据：doc/跨境电商平台需求说明书、架构设计文档、doc/cross-border-prototype
--
-- 端：买家商城 / 运营后台 / 店铺中心 / 平台中心
-- 说明：
--   1. 文档基线为 PostgreSQL 15+，本项目落地若依默认 MySQL 8（库 ry-cloud）。
--   2. 金额统一 BIGINT 最小货币单位；json 字段存 JSON 文本。
--   3. 价格有效期排他约束由 SkuPriceService 服务层校验。
--   4. 审计字段沿用若依 BaseEntity：create_by/create_time/update_by/update_time/remark。
-- ============================================================================

CREATE DATABASE IF NOT EXISTS `ry-cloud` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `ry-cloud`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------
-- 1. 平台与店铺域（平台中心 / 店铺中心）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS shop;
CREATE TABLE shop (
    shop_id           bigint       NOT NULL AUTO_INCREMENT COMMENT '店铺主键',
    shop_code         varchar(64)  NOT NULL COMMENT '店铺编码',
    shop_name         varchar(128) NOT NULL COMMENT '店铺名称',
    logo_url          text                  DEFAULT NULL COMMENT '店铺 Logo',
    contact_name      varchar(128)          DEFAULT NULL COMMENT '联系人',
    contact_email     varchar(320)          DEFAULT NULL COMMENT '联系邮箱',
    country_code      char(2)               DEFAULT NULL COMMENT '国家/地区',
    currency_code     char(3)      NOT NULL DEFAULT 'USD' COMMENT '结算币种',
    shop_type         varchar(32)  NOT NULL DEFAULT 'SELF' COMMENT '类型：SELF 自营 / THIRD_PARTY 三方',
    commission_rate   decimal(5,4) NOT NULL DEFAULT 0.0500 COMMENT '平台佣金比例',
    status            varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/SUSPENDED/CLOSED',
    settled_amount    bigint       NOT NULL DEFAULT 0 COMMENT '已结算金额',
    pending_amount    bigint       NOT NULL DEFAULT 0 COMMENT '待结算金额',
    create_by         varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time       datetime              DEFAULT NULL COMMENT '创建时间',
    update_by         varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time       datetime              DEFAULT NULL COMMENT '更新时间',
    remark            varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (shop_id),
    UNIQUE KEY uq_shop_code (shop_code),
    CONSTRAINT ck_shop_type CHECK (shop_type IN ('SELF', 'THIRD_PARTY')),
    CONSTRAINT ck_shop_status CHECK (status IN ('ACTIVE', 'SUSPENDED', 'CLOSED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '店铺';

DROP TABLE IF EXISTS shop_user;
CREATE TABLE shop_user (
    id          bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    shop_id     bigint      NOT NULL COMMENT '店铺',
    user_id     bigint      NOT NULL COMMENT '若依用户',
    shop_role   varchar(32) NOT NULL DEFAULT 'OPERATOR' COMMENT '角色：OWNER/OPERATOR/CUSTOMER_SERVICE/FINANCE',
    status      varchar(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/INACTIVE',
    create_time datetime             DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uq_shop_user (shop_id, user_id),
    KEY idx_shop_user_user (user_id),
    CONSTRAINT fk_shop_user_shop FOREIGN KEY (shop_id) REFERENCES shop (shop_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '店铺成员';

DROP TABLE IF EXISTS shop_settlement;
CREATE TABLE shop_settlement (
    settlement_id     bigint       NOT NULL AUTO_INCREMENT COMMENT '结算单主键',
    settlement_no     varchar(64)  NOT NULL COMMENT '结算单号',
    shop_id           bigint       NOT NULL COMMENT '店铺',
    period_start      date         NOT NULL COMMENT '账期开始',
    period_end        date         NOT NULL COMMENT '账期结束',
    order_count       int          NOT NULL DEFAULT 0 COMMENT '订单数',
    gross_amount      bigint       NOT NULL DEFAULT 0 COMMENT '成交总额',
    commission_amount bigint       NOT NULL DEFAULT 0 COMMENT '平台佣金',
    net_amount        bigint       NOT NULL DEFAULT 0 COMMENT '应结金额',
    status            varchar(32)  NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING/PAID',
    paid_at           datetime              DEFAULT NULL COMMENT '打款时间',
    create_by         varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time       datetime              DEFAULT NULL COMMENT '创建时间',
    update_by         varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time       datetime              DEFAULT NULL COMMENT '更新时间',
    remark            varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (settlement_id),
    UNIQUE KEY uq_settlement_no (settlement_no),
    KEY idx_settlement_shop (shop_id, period_end),
    CONSTRAINT fk_settlement_shop FOREIGN KEY (shop_id) REFERENCES shop (shop_id),
    CONSTRAINT ck_settlement_status CHECK (status IN ('PENDING', 'PAID'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '店铺结算单';

DROP TABLE IF EXISTS platform_config;
CREATE TABLE platform_config (
    config_id    bigint       NOT NULL AUTO_INCREMENT COMMENT '配置主键',
    config_key   varchar(128) NOT NULL COMMENT '配置键',
    config_value varchar(1000)         DEFAULT NULL COMMENT '配置值',
    config_group varchar(64)           DEFAULT 'GENERAL' COMMENT '分组',
    create_by    varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time  datetime              DEFAULT NULL COMMENT '创建时间',
    update_by    varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time  datetime              DEFAULT NULL COMMENT '更新时间',
    remark       varchar(500)          DEFAULT NULL COMMENT '说明',
    PRIMARY KEY (config_id),
    UNIQUE KEY uq_platform_config_key (config_key)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '平台设置';

DROP TABLE IF EXISTS channel_config;
CREATE TABLE channel_config (
    channel_config_id bigint       NOT NULL AUTO_INCREMENT COMMENT '渠道配置主键',
    channel_code      varchar(64)  NOT NULL COMMENT '渠道编码',
    channel_name      varchar(128) NOT NULL COMMENT '渠道名称',
    channel_type      varchar(32)  NOT NULL COMMENT '类型：DIRECT/PLATFORM/SOCIAL',
    country_code      char(2)               DEFAULT NULL COMMENT '国家/地区',
    currency_code     char(3)      NOT NULL DEFAULT 'USD' COMMENT '币种',
    endpoint          varchar(255)          DEFAULT NULL COMMENT '接口地址',
    app_key           varchar(128)          DEFAULT NULL COMMENT '应用 Key',
    status            varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/PAUSED/DISABLED',
    last_sync_time    datetime              DEFAULT NULL COMMENT '最近同步时间',
    create_by         varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time       datetime              DEFAULT NULL COMMENT '创建时间',
    update_by         varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time       datetime              DEFAULT NULL COMMENT '更新时间',
    remark            varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (channel_config_id),
    UNIQUE KEY uq_channel_config_code (channel_code),
    CONSTRAINT ck_channel_config_status CHECK (status IN ('ACTIVE', 'PAUSED', 'DISABLED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '渠道配置';

DROP TABLE IF EXISTS audit_log;
CREATE TABLE audit_log (
    audit_id    bigint       NOT NULL AUTO_INCREMENT COMMENT '审计主键',
    shop_id     bigint                DEFAULT NULL COMMENT '店铺（平台级为空）',
    module      varchar(64)  NOT NULL COMMENT '模块',
    action      varchar(64)  NOT NULL COMMENT '动作',
    target_type varchar(64)           DEFAULT NULL COMMENT '对象类型',
    target_id   varchar(128)          DEFAULT NULL COMMENT '对象 ID',
    operator    varchar(128)          DEFAULT NULL COMMENT '操作人',
    detail      varchar(1000)         DEFAULT NULL COMMENT '详情',
    ip          varchar(64)           DEFAULT NULL COMMENT 'IP',
    result      varchar(32)  NOT NULL DEFAULT 'SUCCESS' COMMENT '结果：SUCCESS/FAIL',
    create_time datetime              DEFAULT NULL COMMENT '发生时间',
    PRIMARY KEY (audit_id),
    KEY idx_audit_shop (shop_id, create_time),
    KEY idx_audit_module (module, create_time)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '审计与风控日志';

-- ------------------------------------------------------------
-- 2. 商品域
-- ------------------------------------------------------------
DROP TABLE IF EXISTS product_spu;
CREATE TABLE product_spu (
    spu_id              bigint       NOT NULL AUTO_INCREMENT COMMENT 'SPU 主键',
    shop_id             bigint       NOT NULL DEFAULT 1 COMMENT '所属店铺',
    spu_code            varchar(64)  NOT NULL COMMENT 'SPU 编码',
    brand_id            bigint                DEFAULT NULL COMMENT '品牌 ID',
    brand_name          varchar(128)          DEFAULT NULL COMMENT '品牌名称',
    category_id         bigint                DEFAULT NULL COMMENT '类目 ID',
    category_key        varchar(64)           DEFAULT NULL COMMENT '类目键（前台筛选）',
    category_name       varchar(128)          DEFAULT NULL COMMENT '类目名称',
    default_name        varchar(255) NOT NULL COMMENT '商品名称',
    name_en             varchar(255)          DEFAULT NULL COMMENT '英文名称',
    default_description text                  DEFAULT NULL COMMENT '描述',
    status              varchar(32)  NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT/PUBLISHED/ON_SALE/SUSPENDED/OFF_SALE',
    default_image_url   text                  DEFAULT NULL COMMENT '主图',
    badge               varchar(32)           DEFAULT NULL COMMENT '角标：热销/新品/大件/高客单',
    rating              decimal(3,2) NOT NULL DEFAULT 5.00 COMMENT '评分',
    review_count        int          NOT NULL DEFAULT 0 COMMENT '评价数',
    sold_count          int          NOT NULL DEFAULT 0 COMMENT '销量',
    tags_json           json                  DEFAULT NULL COMMENT '卖点标签',
    images_json         json                  DEFAULT NULL COMMENT '图片列表',
    specs_json          json                  DEFAULT NULL COMMENT '规格参数',
    colors_json         json                  DEFAULT NULL COMMENT '颜色选项',
    sizes_json          json                  DEFAULT NULL COMMENT '尺寸/规格选项',
    create_by           varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time         datetime              DEFAULT NULL COMMENT '创建时间',
    update_by           varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time         datetime              DEFAULT NULL COMMENT '更新时间',
    remark              varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (spu_id),
    UNIQUE KEY uq_product_spu_code (spu_code),
    KEY idx_spu_shop_status (shop_id, status),
    KEY idx_spu_category (category_key),
    CONSTRAINT fk_spu_shop FOREIGN KEY (shop_id) REFERENCES shop (shop_id),
    CONSTRAINT ck_product_spu_status CHECK (status IN ('DRAFT','PUBLISHED','ON_SALE','SUSPENDED','OFF_SALE'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '商品标准单元';

DROP TABLE IF EXISTS product_sku;
CREATE TABLE product_sku (
    sku_id                bigint       NOT NULL AUTO_INCREMENT COMMENT 'SKU 主键',
    spu_id                bigint       NOT NULL COMMENT '所属 SPU',
    sku_code              varchar(64)  NOT NULL COMMENT 'SKU 编码',
    barcode               varchar(128)          DEFAULT NULL COMMENT '条码',
    attributes_json       json                  DEFAULT NULL COMMENT '销售属性',
    weight_grams          int                   DEFAULT NULL COMMENT '重量(克)',
    length_mm             int                   DEFAULT NULL COMMENT '长(mm)',
    width_mm              int                   DEFAULT NULL COMMENT '宽(mm)',
    height_mm             int                   DEFAULT NULL COMMENT '高(mm)',
    origin_country_code   char(2)               DEFAULT NULL COMMENT '原产国',
    hs_code               varchar(32)           DEFAULT NULL COMMENT 'HS 编码',
    material              varchar(255)          DEFAULT NULL COMMENT '材质',
    battery_type          varchar(64)           DEFAULT NULL COMMENT '电池类型',
    dangerous_goods_flag  tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否危险品',
    declared_name         varchar(255)          DEFAULT NULL COMMENT '申报品名',
    declared_value_amount bigint                DEFAULT NULL COMMENT '申报价值',
    declared_currency_code char(3)              DEFAULT NULL COMMENT '申报币种',
    status                varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/INACTIVE/FROZEN',
    create_by             varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time           datetime              DEFAULT NULL COMMENT '创建时间',
    update_by             varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time           datetime              DEFAULT NULL COMMENT '更新时间',
    remark                varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (sku_id),
    UNIQUE KEY uq_product_sku_code (sku_code),
    KEY idx_product_sku_spu (spu_id),
    CONSTRAINT fk_product_sku_spu FOREIGN KEY (spu_id) REFERENCES product_spu (spu_id),
    CONSTRAINT ck_product_sku_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'FROZEN'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '可售库存单元';

DROP TABLE IF EXISTS sku_price;
CREATE TABLE sku_price (
    price_id       bigint      NOT NULL AUTO_INCREMENT COMMENT '价格主键',
    sku_id         bigint      NOT NULL COMMENT 'SKU',
    country_code   char(2)     NOT NULL COMMENT '国家/地区',
    channel_code   varchar(64) NOT NULL DEFAULT 'DIRECT' COMMENT '渠道',
    currency_code  char(3)     NOT NULL COMMENT '币种',
    list_amount    bigint      NOT NULL COMMENT '划线价',
    sale_amount    bigint      NOT NULL COMMENT '销售价',
    effective_from datetime    NOT NULL COMMENT '生效时间',
    effective_to   datetime             DEFAULT NULL COMMENT '失效时间',
    status         varchar(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/INACTIVE',
    create_by      varchar(64)          DEFAULT '' COMMENT '创建者',
    create_time    datetime             DEFAULT NULL COMMENT '创建时间',
    update_by      varchar(64)          DEFAULT '' COMMENT '更新者',
    update_time    datetime             DEFAULT NULL COMMENT '更新时间',
    remark         varchar(500)         DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (price_id),
    KEY idx_sku_price_lookup (sku_id, country_code, channel_code, currency_code, effective_from),
    CONSTRAINT fk_sku_price_sku FOREIGN KEY (sku_id) REFERENCES product_sku (sku_id),
    CONSTRAINT ck_sku_price_amount CHECK (list_amount >= 0 AND sale_amount >= 0),
    CONSTRAINT ck_sku_price_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '区域/渠道/币种价格';

DROP TABLE IF EXISTS channel_listing;
CREATE TABLE channel_listing (
    listing_id          bigint       NOT NULL AUTO_INCREMENT COMMENT '映射主键',
    channel_code        varchar(64)  NOT NULL COMMENT '渠道编码',
    external_product_id varchar(128) NOT NULL COMMENT '外部商品 ID',
    external_sku_id     varchar(128)          DEFAULT NULL COMMENT '外部 SKU ID',
    sku_id              bigint                DEFAULT NULL COMMENT '内部 SKU',
    listing_status      varchar(32)  NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT/PENDING/LIVE/PAUSED/CLOSED',
    payload_json        json                  DEFAULT NULL COMMENT '渠道报文',
    last_sync_time      datetime              DEFAULT NULL COMMENT '最近同步',
    create_by           varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time         datetime              DEFAULT NULL COMMENT '创建时间',
    update_by           varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time         datetime              DEFAULT NULL COMMENT '更新时间',
    remark              varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (listing_id),
    UNIQUE KEY uq_channel_listing_external (channel_code, external_product_id, external_sku_id),
    KEY idx_channel_listing_sku (sku_id),
    CONSTRAINT fk_channel_listing_sku FOREIGN KEY (sku_id) REFERENCES product_sku (sku_id),
    CONSTRAINT ck_channel_listing_status CHECK (listing_status IN ('DRAFT','PENDING','LIVE','PAUSED','CLOSED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '渠道商品映射';

-- ------------------------------------------------------------
-- 3. 客户、购物车、营销
-- ------------------------------------------------------------
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
    customer_id  bigint       NOT NULL AUTO_INCREMENT COMMENT '客户主键',
    customer_no  varchar(64)  NOT NULL COMMENT '客户编号',
    email        varchar(320)          DEFAULT NULL COMMENT '邮箱',
    password     varchar(100)          DEFAULT NULL COMMENT '密码（BCrypt）',
    phone        varchar(64)           DEFAULT NULL COMMENT '手机号',
    display_name varchar(255)          DEFAULT NULL COMMENT '昵称',
    country_code char(2)               DEFAULT NULL COMMENT '国家',
    locale       varchar(16)           DEFAULT NULL COMMENT '语言',
    status       varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/INACTIVE/BLOCKED',
    create_by    varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time  datetime              DEFAULT NULL COMMENT '创建时间',
    update_by    varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time  datetime              DEFAULT NULL COMMENT '更新时间',
    remark       varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (customer_id),
    UNIQUE KEY uq_customer_no (customer_no),
    UNIQUE KEY uq_customer_email (email),
    CONSTRAINT ck_customer_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'BLOCKED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '买家';

DROP TABLE IF EXISTS customer_address;
CREATE TABLE customer_address (
    address_id      bigint       NOT NULL AUTO_INCREMENT COMMENT '地址主键',
    customer_id     bigint       NOT NULL COMMENT '客户',
    recipient_name  varchar(255) NOT NULL COMMENT '收件人',
    recipient_phone varchar(64)           DEFAULT NULL COMMENT '电话',
    country_code    char(2)      NOT NULL COMMENT '国家',
    state_code      varchar(64)           DEFAULT NULL COMMENT '州/省',
    city            varchar(128) NOT NULL COMMENT '城市',
    district        varchar(128)          DEFAULT NULL COMMENT '区县',
    address_line1   varchar(255) NOT NULL COMMENT '地址 1',
    address_line2   varchar(255)          DEFAULT NULL COMMENT '地址 2',
    postal_code     varchar(32)           DEFAULT NULL COMMENT '邮编',
    is_default      tinyint(1)   NOT NULL DEFAULT 0 COMMENT '默认地址',
    create_by       varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time     datetime              DEFAULT NULL COMMENT '创建时间',
    update_by       varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time     datetime              DEFAULT NULL COMMENT '更新时间',
    remark          varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (address_id),
    KEY idx_address_customer (customer_id),
    CONSTRAINT fk_address_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '收货地址';

DROP TABLE IF EXISTS cart;
CREATE TABLE cart (
    cart_id       bigint      NOT NULL AUTO_INCREMENT COMMENT '购物车',
    customer_id   bigint      NOT NULL COMMENT '客户',
    country_code  char(2)              DEFAULT NULL COMMENT '结算国家',
    channel_code  varchar(64) NOT NULL DEFAULT 'DIRECT' COMMENT '渠道',
    currency_code char(3)     NOT NULL DEFAULT 'USD' COMMENT '币种',
    create_by     varchar(64)          DEFAULT '' COMMENT '创建者',
    create_time   datetime             DEFAULT NULL COMMENT '创建时间',
    update_by     varchar(64)          DEFAULT '' COMMENT '更新者',
    update_time   datetime             DEFAULT NULL COMMENT '更新时间',
    remark        varchar(500)         DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (cart_id),
    UNIQUE KEY uq_cart_customer (customer_id),
    CONSTRAINT fk_cart_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '购物车';

DROP TABLE IF EXISTS cart_item;
CREATE TABLE cart_item (
    cart_item_id bigint      NOT NULL AUTO_INCREMENT COMMENT '明细',
    cart_id      bigint      NOT NULL COMMENT '购物车',
    sku_id       bigint      NOT NULL COMMENT 'SKU',
    sku_code     varchar(64)          DEFAULT NULL COMMENT 'SKU 编码',
    quantity     int         NOT NULL COMMENT '数量',
    selected     tinyint(1)  NOT NULL DEFAULT 1 COMMENT '选中',
    create_by    varchar(64)          DEFAULT '' COMMENT '创建者',
    create_time  datetime             DEFAULT NULL COMMENT '创建时间',
    update_by    varchar(64)          DEFAULT '' COMMENT '更新者',
    update_time  datetime             DEFAULT NULL COMMENT '更新时间',
    remark       varchar(500)         DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (cart_item_id),
    UNIQUE KEY uq_cart_item_sku (cart_id, sku_id),
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES cart (cart_id),
    CONSTRAINT fk_cart_item_sku FOREIGN KEY (sku_id) REFERENCES product_sku (sku_id),
    CONSTRAINT ck_cart_item_qty CHECK (quantity > 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '购物车明细';

DROP TABLE IF EXISTS promotion;
CREATE TABLE promotion (
    promotion_id     bigint       NOT NULL AUTO_INCREMENT COMMENT '活动主键',
    shop_id          bigint                DEFAULT NULL COMMENT '店铺（空为平台活动）',
    promotion_code   varchar(64)  NOT NULL COMMENT '活动编码',
    promotion_name   varchar(128) NOT NULL COMMENT '活动名称',
    promotion_type   varchar(32)  NOT NULL COMMENT '类型：DISCOUNT/FULL_REDUCTION/FLASH_SALE',
    scope_type       varchar(32)  NOT NULL DEFAULT 'ALL' COMMENT '范围：ALL/CATEGORY/SKU',
    scope_value      varchar(255)          DEFAULT NULL COMMENT '范围值',
    discount_value   bigint       NOT NULL DEFAULT 0 COMMENT '折扣值（百分比或立减金额）',
    threshold_amount bigint       NOT NULL DEFAULT 0 COMMENT '门槛金额',
    currency_code    char(3)      NOT NULL DEFAULT 'USD' COMMENT '币种',
    start_time       datetime     NOT NULL COMMENT '开始时间',
    end_time         datetime     NOT NULL COMMENT '结束时间',
    status           varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/PAUSED/ENDED',
    create_by        varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time      datetime              DEFAULT NULL COMMENT '创建时间',
    update_by        varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time      datetime              DEFAULT NULL COMMENT '更新时间',
    remark           varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (promotion_id),
    UNIQUE KEY uq_promotion_code (promotion_code),
    KEY idx_promotion_shop (shop_id, status),
    CONSTRAINT ck_promotion_type CHECK (promotion_type IN ('DISCOUNT','FULL_REDUCTION','FLASH_SALE')),
    CONSTRAINT ck_promotion_status CHECK (status IN ('ACTIVE','PAUSED','ENDED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '促销活动';

DROP TABLE IF EXISTS coupon;
CREATE TABLE coupon (
    coupon_id      bigint       NOT NULL AUTO_INCREMENT COMMENT '优惠券',
    shop_id        bigint                DEFAULT NULL COMMENT '店铺（空为平台券）',
    coupon_code    varchar(64)  NOT NULL COMMENT '券码',
    coupon_name    varchar(128) NOT NULL COMMENT '名称',
    coupon_type    varchar(32)  NOT NULL COMMENT 'FIXED/PERCENT',
    discount_value bigint       NOT NULL COMMENT '固定金额或百分比',
    min_amount     bigint       NOT NULL DEFAULT 0 COMMENT '最低消费',
    currency_code  char(3)      NOT NULL DEFAULT 'USD' COMMENT '币种',
    country_code   char(2)               DEFAULT NULL COMMENT '适用国家',
    start_time     datetime     NOT NULL COMMENT '开始时间',
    end_time       datetime     NOT NULL COMMENT '结束时间',
    total_qty      int          NOT NULL DEFAULT 0 COMMENT '发放量（0 不限）',
    used_qty       int          NOT NULL DEFAULT 0 COMMENT '已使用',
    per_user_limit int          NOT NULL DEFAULT 1 COMMENT '单用户限用',
    status         varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/INACTIVE',
    create_by      varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time    datetime              DEFAULT NULL COMMENT '创建时间',
    update_by      varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time    datetime              DEFAULT NULL COMMENT '更新时间',
    remark         varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (coupon_id),
    UNIQUE KEY uq_coupon_code (coupon_code),
    CONSTRAINT ck_coupon_type CHECK (coupon_type IN ('FIXED', 'PERCENT')),
    CONSTRAINT ck_coupon_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '优惠券';

-- ------------------------------------------------------------
-- 4. 订单、支付、售后
-- ------------------------------------------------------------
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    order_id           bigint       NOT NULL AUTO_INCREMENT COMMENT '订单主键',
    order_no           varchar(64)  NOT NULL COMMENT '订单号',
    shop_id            bigint       NOT NULL DEFAULT 1 COMMENT '店铺',
    customer_id        bigint                DEFAULT NULL COMMENT '客户',
    channel_code       varchar(64)  NOT NULL DEFAULT 'DIRECT' COMMENT '渠道',
    external_order_id  varchar(128)          DEFAULT NULL COMMENT '外部订单号',
    country_code       char(2)      NOT NULL COMMENT '下单国家',
    currency_code      char(3)      NOT NULL COMMENT '币种',
    subtotal_amount    bigint       NOT NULL DEFAULT 0 COMMENT '小计',
    discount_amount    bigint       NOT NULL DEFAULT 0 COMMENT '优惠',
    shipping_amount    bigint       NOT NULL DEFAULT 0 COMMENT '运费',
    tax_amount         bigint       NOT NULL DEFAULT 0 COMMENT '税费',
    total_amount       bigint       NOT NULL DEFAULT 0 COMMENT '应付总额',
    order_status       varchar(32)  NOT NULL DEFAULT 'CREATED' COMMENT '订单主状态',
    payment_status     varchar(32)  NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态',
    fulfillment_status varchar(32)  NOT NULL DEFAULT 'UNFULFILLED' COMMENT '履约状态',
    after_sale_status  varchar(32)  NOT NULL DEFAULT 'NONE' COMMENT '售后状态',
    coupon_code        varchar(64)           DEFAULT NULL COMMENT '优惠券',
    placed_at          datetime              DEFAULT NULL COMMENT '下单时间',
    paid_at            datetime              DEFAULT NULL COMMENT '支付时间',
    cancelled_at       datetime              DEFAULT NULL COMMENT '取消时间',
    completed_at       datetime              DEFAULT NULL COMMENT '完成时间',
    cancel_reason      varchar(255)          DEFAULT NULL COMMENT '取消原因',
    create_by          varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time        datetime              DEFAULT NULL COMMENT '创建时间',
    update_by          varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time        datetime              DEFAULT NULL COMMENT '更新时间',
    remark             varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (order_id),
    UNIQUE KEY uq_orders_no (order_no),
    UNIQUE KEY uq_orders_external (channel_code, external_order_id),
    KEY idx_orders_shop_status (shop_id, order_status, create_time),
    KEY idx_orders_customer (customer_id),
    CONSTRAINT fk_orders_shop FOREIGN KEY (shop_id) REFERENCES shop (shop_id),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT ck_orders_order_status CHECK (order_status IN ('CREATED','PENDING_PAYMENT','PAID','CONFIRMED',
                                'FULFILLING','SHIPPED','IN_TRANSIT','DELIVERED','COMPLETED','CANCELLED')),
    CONSTRAINT ck_orders_payment_status CHECK (payment_status IN ('UNPAID','AUTHORIZED','PARTIALLY_PAID','PAID',
                                  'REFUNDING','PARTIALLY_REFUNDED','REFUNDED','FAILED')),
    CONSTRAINT ck_orders_fulfillment_status CHECK (fulfillment_status IN ('UNFULFILLED','PARTIALLY_FULFILLED','FULFILLED','RETURNED')),
    CONSTRAINT ck_orders_after_sale_status CHECK (after_sale_status IN ('NONE','REQUESTED','PROCESSING','RESOLVED','REJECTED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单';

DROP TABLE IF EXISTS order_items;
CREATE TABLE order_items (
    order_item_id     bigint      NOT NULL AUTO_INCREMENT COMMENT '明细',
    order_id          bigint      NOT NULL COMMENT '订单',
    sku_id            bigint               DEFAULT NULL COMMENT 'SKU',
    sku_code          varchar(64) NOT NULL COMMENT 'SKU 编码',
    quantity          int         NOT NULL COMMENT '数量',
    unit_price_amount bigint      NOT NULL COMMENT '单价',
    discount_amount   bigint      NOT NULL DEFAULT 0 COMMENT '优惠',
    tax_amount        bigint      NOT NULL DEFAULT 0 COMMENT '税费',
    line_total_amount bigint      NOT NULL COMMENT '明细总额',
    currency_code     char(3)     NOT NULL COMMENT '币种',
    create_time       datetime             DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (order_item_id),
    KEY idx_order_items_order (order_id),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT ck_order_items_qty CHECK (quantity > 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单明细';

DROP TABLE IF EXISTS order_item_snapshots;
CREATE TABLE order_item_snapshots (
    snapshot_id            bigint       NOT NULL AUTO_INCREMENT COMMENT '快照',
    order_item_id          bigint       NOT NULL COMMENT '订单明细',
    product_name           varchar(255) NOT NULL COMMENT '商品名称',
    sku_code               varchar(64)  NOT NULL COMMENT 'SKU 编码',
    attributes_json        json                  DEFAULT NULL COMMENT '属性快照',
    image_url              text                  DEFAULT NULL COMMENT '图片',
    origin_country_code    char(2)               DEFAULT NULL COMMENT '原产国',
    hs_code                varchar(32)           DEFAULT NULL COMMENT 'HS 编码',
    declared_name          varchar(255)          DEFAULT NULL COMMENT '申报品名',
    declared_value_amount  bigint                DEFAULT NULL COMMENT '申报价值',
    declared_currency_code char(3)               DEFAULT NULL COMMENT '申报币种',
    unit_price_amount      bigint       NOT NULL COMMENT '下单单价',
    currency_code          char(3)      NOT NULL COMMENT '币种',
    create_time            datetime              DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (snapshot_id),
    UNIQUE KEY uq_item_snapshot_item (order_item_id),
    CONSTRAINT fk_item_snapshot_item FOREIGN KEY (order_item_id) REFERENCES order_items (order_item_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '明细快照（不可变）';

DROP TABLE IF EXISTS order_snapshots;
CREATE TABLE order_snapshots (
    snapshot_id           bigint      NOT NULL AUTO_INCREMENT COMMENT '快照',
    order_id              bigint      NOT NULL COMMENT '订单',
    buyer_name            varchar(255)         DEFAULT NULL COMMENT '买家',
    buyer_email           varchar(320)         DEFAULT NULL COMMENT '买家邮箱',
    recipient_name        varchar(255) NOT NULL COMMENT '收件人',
    recipient_phone       varchar(64)          DEFAULT NULL COMMENT '电话',
    country_code          char(2)     NOT NULL COMMENT '国家',
    state_code            varchar(64)          DEFAULT NULL COMMENT '州/省',
    city                  varchar(128) NOT NULL COMMENT '城市',
    district              varchar(128)         DEFAULT NULL COMMENT '区县',
    address_line1         varchar(255) NOT NULL COMMENT '地址 1',
    address_line2         varchar(255)         DEFAULT NULL COMMENT '地址 2',
    postal_code           varchar(32)          DEFAULT NULL COMMENT '邮编',
    tax_rule_version      varchar(128)         DEFAULT NULL COMMENT '税率版本',
    exchange_rate_version varchar(128)         DEFAULT NULL COMMENT '汇率版本',
    exchange_rate         decimal(30,12)       DEFAULT NULL COMMENT '汇率',
    raw_address_json      json                 DEFAULT NULL COMMENT '原始地址',
    create_time           datetime             DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (snapshot_id),
    UNIQUE KEY uq_order_snapshot_order (order_id),
    CONSTRAINT fk_order_snapshot_order FOREIGN KEY (order_id) REFERENCES orders (order_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单快照（不可变）';

DROP TABLE IF EXISTS order_state_histories;
CREATE TABLE order_state_histories (
    history_id    bigint      NOT NULL AUTO_INCREMENT COMMENT '历史',
    order_id      bigint      NOT NULL COMMENT '订单',
    from_status   varchar(32)          DEFAULT NULL COMMENT '原状态',
    to_status     varchar(32) NOT NULL COMMENT '新状态',
    status_type   varchar(32) NOT NULL COMMENT 'ORDER/PAYMENT/FULFILLMENT/AFTER_SALE',
    reason        varchar(255)         DEFAULT NULL COMMENT '原因',
    operator_type varchar(32) NOT NULL DEFAULT 'SYSTEM' COMMENT 'SYSTEM/USER/CHANNEL',
    operator_id   varchar(128)         DEFAULT NULL COMMENT '操作人',
    occurred_at   datetime    NOT NULL COMMENT '发生时间',
    create_time   datetime             DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (history_id),
    KEY idx_history_order (order_id, occurred_at),
    CONSTRAINT fk_history_order FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT ck_history_type CHECK (status_type IN ('ORDER','PAYMENT','FULFILLMENT','AFTER_SALE'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单状态历史';

DROP TABLE IF EXISTS payments;
CREATE TABLE payments (
    payment_id          bigint       NOT NULL AUTO_INCREMENT COMMENT '支付单',
    payment_no          varchar(64)  NOT NULL COMMENT '支付单号',
    order_id            bigint       NOT NULL COMMENT '订单',
    provider            varchar(64)  NOT NULL COMMENT '渠道',
    provider_payment_id varchar(128)          DEFAULT NULL COMMENT '渠道支付号',
    method              varchar(32)           DEFAULT NULL COMMENT '方式',
    amount              bigint       NOT NULL COMMENT '金额',
    currency_code       char(3)      NOT NULL COMMENT '币种',
    payment_status      varchar(32)  NOT NULL DEFAULT 'INITIATED' COMMENT '状态',
    idempotency_key     varchar(128) NOT NULL COMMENT '幂等键',
    paid_at             datetime              DEFAULT NULL COMMENT '支付时间',
    expired_at          datetime              DEFAULT NULL COMMENT '过期时间',
    create_by           varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time         datetime              DEFAULT NULL COMMENT '创建时间',
    update_by           varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time         datetime              DEFAULT NULL COMMENT '更新时间',
    remark              varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (payment_id),
    UNIQUE KEY uq_payments_no (payment_no),
    UNIQUE KEY uq_payments_idem (idempotency_key),
    KEY idx_payments_order (order_id),
    CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT ck_payments_status CHECK (payment_status IN ('INITIATED','PENDING','AUTHORIZED','SUCCEEDED',
                                  'FAILED','CANCELLED','EXPIRED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '支付单';

DROP TABLE IF EXISTS payment_events;
CREATE TABLE payment_events (
    payment_event_id  bigint       NOT NULL AUTO_INCREMENT COMMENT '事件',
    payment_id        bigint       NOT NULL COMMENT '支付单',
    provider          varchar(64)  NOT NULL COMMENT '渠道',
    provider_event_id varchar(128) NOT NULL COMMENT '渠道事件 ID',
    event_type        varchar(64)  NOT NULL COMMENT '事件类型',
    event_status      varchar(32)           DEFAULT NULL COMMENT '事件状态',
    payload_json      json                  DEFAULT NULL COMMENT '原始报文',
    received_at       datetime     NOT NULL COMMENT '接收时间',
    processed_at      datetime              DEFAULT NULL COMMENT '处理时间',
    PRIMARY KEY (payment_event_id),
    UNIQUE KEY uq_payment_event (provider, provider_event_id),
    KEY idx_payment_events_payment (payment_id),
    CONSTRAINT fk_payment_events_payment FOREIGN KEY (payment_id) REFERENCES payments (payment_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '支付回调事件';

DROP TABLE IF EXISTS refunds;
CREATE TABLE refunds (
    refund_id          bigint       NOT NULL AUTO_INCREMENT COMMENT '退款单',
    refund_no          varchar(64)  NOT NULL COMMENT '退款单号',
    order_id           bigint       NOT NULL COMMENT '订单',
    payment_id         bigint                DEFAULT NULL COMMENT '支付单',
    amount             bigint       NOT NULL COMMENT '金额',
    currency_code      char(3)      NOT NULL COMMENT '币种',
    refund_status      varchar(32)  NOT NULL DEFAULT 'REQUESTED' COMMENT '状态',
    reason             varchar(255)          DEFAULT NULL COMMENT '原因',
    provider_refund_id varchar(128)          DEFAULT NULL COMMENT '渠道退款号',
    requested_at       datetime              DEFAULT NULL COMMENT '申请时间',
    completed_at       datetime              DEFAULT NULL COMMENT '完成时间',
    create_by          varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time        datetime              DEFAULT NULL COMMENT '创建时间',
    update_by          varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time        datetime              DEFAULT NULL COMMENT '更新时间',
    remark             varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (refund_id),
    UNIQUE KEY uq_refunds_no (refund_no),
    KEY idx_refunds_order (order_id),
    CONSTRAINT fk_refunds_order FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT fk_refunds_payment FOREIGN KEY (payment_id) REFERENCES payments (payment_id),
    CONSTRAINT ck_refunds_status CHECK (refund_status IN ('REQUESTED','APPROVED','PROCESSING','SUCCEEDED','FAILED','REJECTED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '退款单';

-- ------------------------------------------------------------
-- 5. 库存、履约、物流
-- ------------------------------------------------------------
DROP TABLE IF EXISTS inventory;
CREATE TABLE inventory (
    inventory_id   bigint      NOT NULL AUTO_INCREMENT COMMENT '库存',
    shop_id        bigint      NOT NULL DEFAULT 1 COMMENT '店铺',
    sku_id         bigint      NOT NULL COMMENT 'SKU',
    warehouse_code varchar(64) NOT NULL COMMENT '仓库',
    country_code   char(2)              DEFAULT NULL COMMENT '仓库国家',
    total_qty      int         NOT NULL DEFAULT 0 COMMENT '总库存',
    locked_qty     int         NOT NULL DEFAULT 0 COMMENT '锁定',
    safety_qty     int         NOT NULL DEFAULT 0 COMMENT '安全库存',
    version        int         NOT NULL DEFAULT 0 COMMENT '乐观锁',
    create_by      varchar(64)          DEFAULT '' COMMENT '创建者',
    create_time    datetime             DEFAULT NULL COMMENT '创建时间',
    update_by      varchar(64)          DEFAULT '' COMMENT '更新者',
    update_time    datetime             DEFAULT NULL COMMENT '更新时间',
    remark         varchar(500)         DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (inventory_id),
    UNIQUE KEY uq_inventory_sku_wh (sku_id, warehouse_code),
    KEY idx_inventory_shop (shop_id),
    CONSTRAINT fk_inventory_shop FOREIGN KEY (shop_id) REFERENCES shop (shop_id),
    CONSTRAINT fk_inventory_sku FOREIGN KEY (sku_id) REFERENCES product_sku (sku_id),
    CONSTRAINT ck_inventory_qty CHECK (total_qty >= 0 AND locked_qty >= 0 AND locked_qty <= total_qty)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '多仓库存';

DROP TABLE IF EXISTS inventory_transaction;
CREATE TABLE inventory_transaction (
    tx_id          bigint      NOT NULL AUTO_INCREMENT COMMENT '流水',
    inventory_id   bigint      NOT NULL COMMENT '库存',
    sku_id         bigint      NOT NULL COMMENT 'SKU',
    warehouse_code varchar(64) NOT NULL COMMENT '仓库',
    tx_type        varchar(32) NOT NULL COMMENT 'RESERVE/RELEASE/DEDUCT/RESTORE/ADJUST',
    quantity       int         NOT NULL COMMENT '数量',
    before_qty     int         NOT NULL COMMENT '变动前可售',
    after_qty      int         NOT NULL COMMENT '变动后可售',
    biz_type       varchar(32)          DEFAULT NULL COMMENT '业务类型',
    biz_no         varchar(64)          DEFAULT NULL COMMENT '业务单号',
    operator_id    varchar(128)         DEFAULT NULL COMMENT '操作人',
    create_time    datetime             DEFAULT NULL COMMENT '创建时间',
    remark         varchar(500)         DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (tx_id),
    KEY idx_inv_tx_sku (sku_id, warehouse_code, create_time),
    KEY idx_inv_tx_biz (biz_type, biz_no),
    CONSTRAINT fk_inv_tx_inventory FOREIGN KEY (inventory_id) REFERENCES inventory (inventory_id),
    CONSTRAINT ck_inv_tx_type CHECK (tx_type IN ('RESERVE','RELEASE','DEDUCT','RESTORE','ADJUST'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '库存流水';

DROP TABLE IF EXISTS fulfillment_orders;
CREATE TABLE fulfillment_orders (
    fulfillment_id     bigint       NOT NULL AUTO_INCREMENT COMMENT '履约单',
    fulfillment_no     varchar(64)  NOT NULL COMMENT '履约单号',
    shop_id            bigint       NOT NULL DEFAULT 1 COMMENT '店铺',
    order_id           bigint       NOT NULL COMMENT '订单',
    warehouse_code     varchar(64)  NOT NULL COMMENT '仓库',
    fulfillment_status varchar(32)  NOT NULL DEFAULT 'CREATED' COMMENT '状态',
    allocation_reason  varchar(255)          DEFAULT NULL COMMENT '分配原因',
    estimated_ship_at  datetime              DEFAULT NULL COMMENT '预计发货',
    actual_ship_at     datetime              DEFAULT NULL COMMENT '实际发货',
    completed_at       datetime              DEFAULT NULL COMMENT '完成时间',
    create_by          varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time        datetime              DEFAULT NULL COMMENT '创建时间',
    update_by          varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time        datetime              DEFAULT NULL COMMENT '更新时间',
    remark             varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (fulfillment_id),
    UNIQUE KEY uq_fulfillment_no (fulfillment_no),
    KEY idx_fulfillment_order (order_id),
    KEY idx_fulfillment_shop (shop_id, fulfillment_status),
    CONSTRAINT fk_fulfillment_order FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT ck_fulfillment_status CHECK (fulfillment_status IN ('CREATED','ALLOCATED','PICKING','PACKED','SHIPPED','DELIVERED','CANCELLED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '履约单';

DROP TABLE IF EXISTS fulfillment_items;
CREATE TABLE fulfillment_items (
    fulfillment_item_id bigint      NOT NULL AUTO_INCREMENT COMMENT '履约明细',
    fulfillment_id      bigint      NOT NULL COMMENT '履约单',
    order_item_id       bigint      NOT NULL COMMENT '订单明细',
    quantity            int         NOT NULL COMMENT '数量',
    create_time         datetime             DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (fulfillment_item_id),
    KEY idx_fulfillment_items (fulfillment_id),
    CONSTRAINT fk_fulfillment_items_ful FOREIGN KEY (fulfillment_id) REFERENCES fulfillment_orders (fulfillment_id),
    CONSTRAINT fk_fulfillment_items_item FOREIGN KEY (order_item_id) REFERENCES order_items (order_item_id),
    CONSTRAINT ck_fulfillment_items_qty CHECK (quantity > 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '履约明细';

DROP TABLE IF EXISTS shipments;
CREATE TABLE shipments (
    shipment_id     bigint       NOT NULL AUTO_INCREMENT COMMENT '运单',
    shipment_no     varchar(64)  NOT NULL COMMENT '运单号',
    fulfillment_id  bigint       NOT NULL COMMENT '履约单',
    carrier_code    varchar(64)  NOT NULL COMMENT '承运商',
    tracking_no     varchar(128)          DEFAULT NULL COMMENT '物流单号',
    shipment_status varchar(32)  NOT NULL DEFAULT 'CREATED' COMMENT '状态',
    shipped_at      datetime              DEFAULT NULL COMMENT '发货时间',
    delivered_at    datetime              DEFAULT NULL COMMENT '签收时间',
    create_by       varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time     datetime              DEFAULT NULL COMMENT '创建时间',
    update_by       varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time     datetime              DEFAULT NULL COMMENT '更新时间',
    remark          varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (shipment_id),
    UNIQUE KEY uq_shipments_no (shipment_no),
    KEY idx_shipments_fulfillment (fulfillment_id),
    CONSTRAINT fk_shipments_fulfillment FOREIGN KEY (fulfillment_id) REFERENCES fulfillment_orders (fulfillment_id),
    CONSTRAINT ck_shipments_status CHECK (shipment_status IN ('CREATED','LABEL_PRINTED','IN_TRANSIT','DELIVERED','EXCEPTION','RETURNED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '运单';

DROP TABLE IF EXISTS tracking_events;
CREATE TABLE tracking_events (
    tracking_event_id bigint       NOT NULL AUTO_INCREMENT COMMENT '轨迹',
    shipment_id       bigint       NOT NULL COMMENT '运单',
    event_code        varchar(64)  NOT NULL COMMENT '事件编码',
    event_description varchar(255)          DEFAULT NULL COMMENT '描述',
    location          varchar(255)          DEFAULT NULL COMMENT '位置',
    occurred_at       datetime     NOT NULL COMMENT '发生时间',
    raw_payload_json  json                  DEFAULT NULL COMMENT '原始报文',
    received_at       datetime     NOT NULL COMMENT '接收时间',
    PRIMARY KEY (tracking_event_id),
    KEY idx_tracking_shipment (shipment_id, occurred_at),
    CONSTRAINT fk_tracking_shipment FOREIGN KEY (shipment_id) REFERENCES shipments (shipment_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '物流轨迹';

-- ------------------------------------------------------------
-- 6. 税务、汇率、本地消息
-- ------------------------------------------------------------
DROP TABLE IF EXISTS tax_rule;
CREATE TABLE tax_rule (
    rule_id        bigint       NOT NULL AUTO_INCREMENT COMMENT '税率规则',
    country_code   char(2)      NOT NULL COMMENT '国家',
    category_id    bigint                DEFAULT NULL COMMENT '类目（空为通用）',
    tax_type       varchar(32)  NOT NULL DEFAULT 'VAT' COMMENT 'VAT/GST/DUTY',
    rule_name      varchar(128)          DEFAULT NULL COMMENT '名称',
    rate           decimal(9,6) NOT NULL COMMENT '税率',
    version        varchar(64)  NOT NULL COMMENT '版本',
    effective_from datetime     NOT NULL COMMENT '生效',
    effective_to   datetime              DEFAULT NULL COMMENT '失效',
    status         varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
    create_by      varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time    datetime              DEFAULT NULL COMMENT '创建时间',
    update_by      varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time    datetime              DEFAULT NULL COMMENT '更新时间',
    remark         varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (rule_id),
    KEY idx_tax_rule_lookup (country_code, category_id, effective_from),
    CONSTRAINT ck_tax_rule_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '税率规则';

DROP TABLE IF EXISTS exchange_rate;
CREATE TABLE exchange_rate (
    rate_id        bigint       NOT NULL AUTO_INCREMENT COMMENT '汇率',
    base_currency  char(3)      NOT NULL COMMENT '基准币种',
    quote_currency char(3)      NOT NULL COMMENT '报价币种',
    rate           decimal(30,12) NOT NULL COMMENT '汇率',
    version        varchar(64)  NOT NULL COMMENT '版本',
    effective_from datetime     NOT NULL COMMENT '生效',
    effective_to   datetime              DEFAULT NULL COMMENT '失效',
    status         varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
    create_by      varchar(64)           DEFAULT '' COMMENT '创建者',
    create_time    datetime              DEFAULT NULL COMMENT '创建时间',
    update_by      varchar(64)           DEFAULT '' COMMENT '更新者',
    update_time    datetime              DEFAULT NULL COMMENT '更新时间',
    remark         varchar(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (rate_id),
    KEY idx_exchange_rate_lookup (base_currency, quote_currency, effective_from),
    CONSTRAINT ck_exchange_rate_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '汇率';

DROP TABLE IF EXISTS local_message;
CREATE TABLE local_message (
    message_id     bigint       NOT NULL AUTO_INCREMENT COMMENT '消息',
    message_key    varchar(128) NOT NULL COMMENT '幂等键',
    topic          varchar(128) NOT NULL COMMENT '主题',
    event_type     varchar(64)  NOT NULL COMMENT '事件类型',
    aggregate_type varchar(64)           DEFAULT NULL COMMENT '聚合类型',
    aggregate_id   varchar(64)           DEFAULT NULL COMMENT '聚合 ID',
    payload_json   json                  DEFAULT NULL COMMENT '消息体',
    message_status varchar(32)  NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING/SENT/FAILED',
    retry_count    int          NOT NULL DEFAULT 0 COMMENT '重试次数',
    next_retry_at  datetime              DEFAULT NULL COMMENT '下次重试',
    sent_at        datetime              DEFAULT NULL COMMENT '投递时间',
    last_error     varchar(500)          DEFAULT NULL COMMENT '错误',
    create_time    datetime              DEFAULT NULL COMMENT '创建时间',
    update_time    datetime              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (message_id),
    UNIQUE KEY uq_local_message_key (message_key),
    KEY idx_local_message_status (message_status, next_retry_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '本地消息表';

SET FOREIGN_KEY_CHECKS = 1;
