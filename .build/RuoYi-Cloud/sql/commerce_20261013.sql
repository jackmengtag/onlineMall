-- ============================================================================
-- 跨境电商平台 · 交易闭环建表脚本（MySQL 8 版）
-- 对应文档：《跨境电商平台架构设计文档》第 7、8、9 章
--
-- 说明：
--   1. 原文档基于 PostgreSQL 15+，统一使用 commerce schema；此处按若依技术栈
--      （MySQL 8）转写，使用独立数据库 `ry-commerce` 对应文档中的 commerce schema。
--   2. 类型映射：JSONB -> json，TIMESTAMPTZ -> datetime，BIGINT IDENTITY -> AUTO_INCREMENT，
--      NUMERIC -> decimal，CHAR(n) -> char(n)。
--   3. 排他约束（btree_gist + EXCLUDE）MySQL 不支持，价格有效期防重叠改由服务层
--      校验实现（见 sku_price 表注释），并保留普通索引加速区间查询。
--   4. 金额一律使用最小货币单位整数（BIGINT）保存，币种 CHAR(3)，国家 CHAR(2)。
--   5. 索引创建在生产环境建议使用 ALGORITHM=INPLACE，大表变更参见文档第 11 章
--      Expand / Migrate / Transition / Contract 策略。
-- ============================================================================

CREATE DATABASE IF NOT EXISTS `ry-commerce` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `ry-commerce`;
SET NAMES utf8mb4;

-- ------------------------------------------------------------
-- 1. 商品域
-- ------------------------------------------------------------

DROP TABLE IF EXISTS product_spu;
CREATE TABLE product_spu (
    spu_id              bigint       NOT NULL AUTO_INCREMENT COMMENT 'SPU 主键',
    spu_code            varchar(64)  NOT NULL COMMENT 'SPU 编码',
    brand_id            bigint                DEFAULT NULL COMMENT '品牌 ID',
    category_id         bigint                DEFAULT NULL COMMENT '类目 ID',
    default_name        varchar(255) NOT NULL COMMENT '默认名称',
    default_description text                  DEFAULT NULL COMMENT '默认描述',
    status              varchar(32)  NOT NULL DEFAULT 'DRAFT' COMMENT '状态',
    default_image_url   text                  DEFAULT NULL COMMENT '默认主图',
    created_at          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (spu_id),
    UNIQUE KEY uq_product_spu_code (spu_code),
    KEY idx_product_spu_category (category_id),
    KEY idx_product_spu_brand (brand_id),
    CONSTRAINT ck_product_spu_status
        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ON_SALE', 'SUSPENDED', 'OFF_SALE'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '商品标准单元主档';

DROP TABLE IF EXISTS product_sku;
CREATE TABLE product_sku (
    sku_id               bigint       NOT NULL AUTO_INCREMENT COMMENT 'SKU 主键',
    spu_id               bigint       NOT NULL COMMENT '所属 SPU',
    sku_code             varchar(64)  NOT NULL COMMENT 'SKU 编码',
    barcode              varchar(128)          DEFAULT NULL COMMENT '条码',
    attributes_json      json         NOT NULL COMMENT '销售属性',
    weight_grams         int                   DEFAULT NULL COMMENT '重量(克)',
    length_mm            int                   DEFAULT NULL COMMENT '长(mm)',
    width_mm             int                   DEFAULT NULL COMMENT '宽(mm)',
    height_mm            int                   DEFAULT NULL COMMENT '高(mm)',
    origin_country_code  char(2)               DEFAULT NULL COMMENT '原产国',
    hs_code              varchar(32)           DEFAULT NULL COMMENT 'HS 编码',
    material             varchar(255)          DEFAULT NULL COMMENT '材质',
    battery_type         varchar(64)           DEFAULT NULL COMMENT '电池类型',
    dangerous_goods_flag tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否危险品',
    status               varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
    created_at           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (sku_id),
    UNIQUE KEY uq_product_sku_code (sku_code),
    KEY idx_product_sku_spu (spu_id),
    CONSTRAINT fk_product_sku_spu
        FOREIGN KEY (spu_id) REFERENCES product_spu (spu_id),
    CONSTRAINT ck_product_sku_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'FROZEN'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '可售库存单元';

DROP TABLE IF EXISTS sku_price;
CREATE TABLE sku_price (
    price_id       bigint       NOT NULL AUTO_INCREMENT COMMENT '价格主键',
    sku_id         bigint       NOT NULL COMMENT 'SKU 主键',
    country_code   char(2)      NOT NULL COMMENT '国家/地区',
    channel_code   varchar(64)  NOT NULL DEFAULT 'DIRECT' COMMENT '渠道',
    currency_code  char(3)      NOT NULL COMMENT '币种',
    list_amount    bigint       NOT NULL COMMENT '划线价(最小货币单位)',
    sale_amount    bigint       NOT NULL COMMENT '销售价(最小货币单位)',
    effective_from datetime     NOT NULL COMMENT '生效时间',
    effective_to   datetime              DEFAULT NULL COMMENT '失效时间(NULL 表示长期有效)',
    created_at     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (price_id),
    KEY idx_sku_price_lookup (sku_id, country_code, channel_code, currency_code, effective_from),
    CONSTRAINT fk_sku_price_sku
        FOREIGN KEY (sku_id) REFERENCES product_sku (sku_id),
    CONSTRAINT ck_sku_price_amount
        CHECK (list_amount >= 0 AND sale_amount >= 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '区域/渠道/币种价格(有效期防重叠由服务层校验)';

DROP TABLE IF EXISTS channel_listing;
CREATE TABLE channel_listing (
    listing_id          bigint       NOT NULL AUTO_INCREMENT COMMENT '映射主键',
    channel_code        varchar(64)  NOT NULL COMMENT '渠道编码',
    external_product_id varchar(128) NOT NULL COMMENT '外部商品 ID',
    external_sku_id     varchar(128)          DEFAULT NULL COMMENT '外部 SKU ID',
    sku_id              bigint                DEFAULT NULL COMMENT '内部 SKU 主键',
    listing_status      varchar(32)  NOT NULL DEFAULT 'DRAFT' COMMENT '映射状态',
    payload_json        json         NOT NULL COMMENT '外部原始报文',
    created_at          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (listing_id),
    UNIQUE KEY uq_channel_listing_external (channel_code, external_product_id, external_sku_id),
    KEY idx_channel_listing_sku (sku_id),
    CONSTRAINT fk_channel_listing_sku
        FOREIGN KEY (sku_id) REFERENCES product_sku (sku_id),
    CONSTRAINT ck_channel_listing_status
        CHECK (listing_status IN ('DRAFT', 'PENDING', 'LIVE', 'PAUSED', 'CLOSED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '渠道商品映射';

-- ------------------------------------------------------------
-- 2. 客户域
-- ------------------------------------------------------------

DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
    customer_id  bigint       NOT NULL AUTO_INCREMENT COMMENT '客户主键',
    customer_no  varchar(64)  NOT NULL COMMENT '客户编号',
    email        varchar(320)          DEFAULT NULL COMMENT '邮箱',
    phone        varchar(64)           DEFAULT NULL COMMENT '手机号',
    display_name varchar(255)          DEFAULT NULL COMMENT '显示名称',
    country_code char(2)               DEFAULT NULL COMMENT '国家/地区',
    status       varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
    created_at   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (customer_id),
    UNIQUE KEY uq_customer_no (customer_no),
    KEY idx_customer_email (email),
    CONSTRAINT ck_customer_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'BLOCKED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '客户档案';

DROP TABLE IF EXISTS customer_address;
CREATE TABLE customer_address (
    address_id      bigint       NOT NULL AUTO_INCREMENT COMMENT '地址主键',
    customer_id     bigint       NOT NULL COMMENT '客户主键',
    recipient_name  varchar(255) NOT NULL COMMENT '收件人',
    recipient_phone varchar(64)           DEFAULT NULL COMMENT '收件人电话',
    country_code    char(2)      NOT NULL COMMENT '国家/地区',
    state_code      varchar(64)           DEFAULT NULL COMMENT '州/省',
    city            varchar(128) NOT NULL COMMENT '城市',
    district        varchar(128)          DEFAULT NULL COMMENT '区/县',
    address_line1   varchar(255) NOT NULL COMMENT '地址行 1',
    address_line2   varchar(255)          DEFAULT NULL COMMENT '地址行 2',
    postal_code     varchar(32)           DEFAULT NULL COMMENT '邮编',
    is_default      tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否默认地址',
    created_at      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (address_id),
    KEY idx_customer_address_customer (customer_id),
    CONSTRAINT fk_customer_address_customer
        FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '收货地址';

-- ------------------------------------------------------------
-- 3. 订单域
-- ------------------------------------------------------------

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    order_id           bigint      NOT NULL AUTO_INCREMENT COMMENT '订单主键',
    order_no           varchar(64) NOT NULL COMMENT '订单编号',
    customer_id        bigint               DEFAULT NULL COMMENT '客户主键',
    channel_code       varchar(64) NOT NULL DEFAULT 'DIRECT' COMMENT '渠道编码',
    external_order_id  varchar(128)         DEFAULT NULL COMMENT '外部订单号',
    country_code       char(2)     NOT NULL COMMENT '国家/地区',
    currency_code      char(3)     NOT NULL COMMENT '币种',
    subtotal_amount    bigint      NOT NULL DEFAULT 0 COMMENT '商品小计',
    discount_amount    bigint      NOT NULL DEFAULT 0 COMMENT '优惠金额',
    shipping_amount    bigint      NOT NULL DEFAULT 0 COMMENT '运费',
    tax_amount         bigint      NOT NULL DEFAULT 0 COMMENT '税费',
    total_amount       bigint      NOT NULL DEFAULT 0 COMMENT '应付总额',
    order_status       varchar(32) NOT NULL DEFAULT 'CREATED' COMMENT '订单主状态',
    payment_status     varchar(32) NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态',
    fulfillment_status varchar(32) NOT NULL DEFAULT 'UNFULFILLED' COMMENT '履约状态',
    after_sale_status  varchar(32) NOT NULL DEFAULT 'NONE' COMMENT '售后状态',
    placed_at          datetime             DEFAULT NULL COMMENT '下单时间',
    paid_at            datetime             DEFAULT NULL COMMENT '支付时间',
    cancelled_at       datetime             DEFAULT NULL COMMENT '取消时间',
    completed_at       datetime             DEFAULT NULL COMMENT '完成时间',
    created_at         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (order_id),
    UNIQUE KEY uq_orders_order_no (order_no),
    UNIQUE KEY uq_orders_external_order (channel_code, external_order_id),
    KEY idx_orders_customer (customer_id),
    KEY idx_orders_status (order_status, created_at),
    KEY idx_orders_placed_at (placed_at),
    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT ck_orders_order_status
        CHECK (order_status IN ('CREATED','PENDING_PAYMENT','PAID','CONFIRMED',
                                'FULFILLING','SHIPPED','IN_TRANSIT','DELIVERED',
                                'COMPLETED','CANCELLED')),
    CONSTRAINT ck_orders_payment_status
        CHECK (payment_status IN ('UNPAID','AUTHORIZED','PARTIALLY_PAID','PAID',
                                  'REFUNDING','PARTIALLY_REFUNDED','REFUNDED','FAILED')),
    CONSTRAINT ck_orders_fulfillment_status
        CHECK (fulfillment_status IN ('UNFULFILLED','PARTIALLY_FULFILLED','FULFILLED','RETURNED')),
    CONSTRAINT ck_orders_after_sale_status
        CHECK (after_sale_status IN ('NONE','REQUESTED','PROCESSING','RESOLVED','REJECTED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单主表';

DROP TABLE IF EXISTS order_items;
CREATE TABLE order_items (
    order_item_id     bigint      NOT NULL AUTO_INCREMENT COMMENT '明细主键',
    order_id          bigint      NOT NULL COMMENT '订单主键',
    sku_id            bigint               DEFAULT NULL COMMENT 'SKU 主键',
    sku_code          varchar(64) NOT NULL COMMENT 'SKU 编码',
    quantity          int         NOT NULL COMMENT '数量',
    unit_price_amount bigint      NOT NULL COMMENT '单价',
    discount_amount   bigint      NOT NULL DEFAULT 0 COMMENT '明细优惠',
    tax_amount        bigint      NOT NULL DEFAULT 0 COMMENT '明细税费',
    line_total_amount bigint      NOT NULL COMMENT '行小计',
    currency_code     char(3)     NOT NULL COMMENT '币种',
    created_at        datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (order_item_id),
    KEY idx_order_items_order (order_id),
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT ck_order_items_quantity
        CHECK (quantity > 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单明细';

DROP TABLE IF EXISTS order_item_snapshots;
CREATE TABLE order_item_snapshots (
    snapshot_id            bigint       NOT NULL AUTO_INCREMENT COMMENT '快照主键',
    order_item_id          bigint       NOT NULL COMMENT '订单明细主键',
    product_name           varchar(255) NOT NULL COMMENT '商品名称',
    sku_code               varchar(64)  NOT NULL COMMENT 'SKU 编码',
    attributes_json        json         NOT NULL COMMENT '属性快照',
    image_url              text                  DEFAULT NULL COMMENT '图片快照',
    origin_country_code    char(2)               DEFAULT NULL COMMENT '原产国',
    hs_code                varchar(32)           DEFAULT NULL COMMENT 'HS 编码',
    declared_name          varchar(255)          DEFAULT NULL COMMENT '申报品名',
    declared_value_amount  bigint                DEFAULT NULL COMMENT '申报价值',
    declared_currency_code char(3)               DEFAULT NULL COMMENT '申报币种',
    unit_price_amount      bigint       NOT NULL COMMENT '下单单价',
    currency_code          char(3)      NOT NULL COMMENT '币种',
    created_at             datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (snapshot_id),
    UNIQUE KEY uq_order_item_snapshots_item (order_item_id),
    CONSTRAINT fk_order_item_snapshots_item
        FOREIGN KEY (order_item_id) REFERENCES order_items (order_item_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '明细商品与价格快照';

DROP TABLE IF EXISTS order_snapshots;
CREATE TABLE order_snapshots (
    snapshot_id           bigint         NOT NULL AUTO_INCREMENT COMMENT '快照主键',
    order_id              bigint         NOT NULL COMMENT '订单主键',
    buyer_name            varchar(255)            DEFAULT NULL COMMENT '买家名称',
    buyer_email           varchar(320)            DEFAULT NULL COMMENT '买家邮箱',
    recipient_name        varchar(255)   NOT NULL COMMENT '收件人',
    recipient_phone       varchar(64)             DEFAULT NULL COMMENT '收件人电话',
    country_code          char(2)        NOT NULL COMMENT '国家/地区',
    state_code            varchar(64)             DEFAULT NULL COMMENT '州/省',
    city                  varchar(128)   NOT NULL COMMENT '城市',
    district              varchar(128)            DEFAULT NULL COMMENT '区/县',
    address_line1         varchar(255)   NOT NULL COMMENT '地址行 1',
    address_line2         varchar(255)            DEFAULT NULL COMMENT '地址行 2',
    postal_code           varchar(32)             DEFAULT NULL COMMENT '邮编',
    tax_rule_version      varchar(128)            DEFAULT NULL COMMENT '税费规则版本',
    exchange_rate_version varchar(128)            DEFAULT NULL COMMENT '汇率版本',
    exchange_rate         decimal(30, 12)         DEFAULT NULL COMMENT '汇率',
    raw_address_json      json           NOT NULL COMMENT '地址原始报文',
    created_at            datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (snapshot_id),
    UNIQUE KEY uq_order_snapshots_order (order_id),
    CONSTRAINT fk_order_snapshots_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单地址、税费与汇率快照';

DROP TABLE IF EXISTS order_state_histories;
CREATE TABLE order_state_histories (
    history_id    bigint      NOT NULL AUTO_INCREMENT COMMENT '历史主键',
    order_id      bigint      NOT NULL COMMENT '订单主键',
    from_status   varchar(32)          DEFAULT NULL COMMENT '原状态',
    to_status     varchar(32) NOT NULL COMMENT '新状态',
    status_type   varchar(32) NOT NULL COMMENT '状态类型',
    reason        varchar(255)         DEFAULT NULL COMMENT '变更原因',
    operator_type varchar(32) NOT NULL DEFAULT 'SYSTEM' COMMENT '操作者类型',
    operator_id   varchar(128)         DEFAULT NULL COMMENT '操作者标识',
    occurred_at   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
    PRIMARY KEY (history_id),
    KEY idx_order_state_histories (order_id, occurred_at),
    CONSTRAINT fk_order_state_histories_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT ck_order_state_histories_type
        CHECK (status_type IN ('ORDER','PAYMENT','FULFILLMENT','AFTER_SALE'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单状态变更历史';

-- ------------------------------------------------------------
-- 4. 支付域
-- ------------------------------------------------------------

DROP TABLE IF EXISTS payments;
CREATE TABLE payments (
    payment_id          bigint       NOT NULL AUTO_INCREMENT COMMENT '支付单主键',
    payment_no          varchar(64)  NOT NULL COMMENT '支付单号',
    order_id            bigint       NOT NULL COMMENT '订单主键',
    provider            varchar(64)  NOT NULL COMMENT '支付服务商',
    provider_payment_id varchar(128)          DEFAULT NULL COMMENT '服务商支付 ID',
    method              varchar(32)           DEFAULT NULL COMMENT '支付方式',
    amount              bigint       NOT NULL COMMENT '支付金额',
    currency_code       char(3)      NOT NULL COMMENT '币种',
    payment_status      varchar(32)  NOT NULL DEFAULT 'INITIATED' COMMENT '支付状态',
    idempotency_key     varchar(128) NOT NULL COMMENT '幂等键',
    paid_at             datetime              DEFAULT NULL COMMENT '支付成功时间',
    expired_at          datetime              DEFAULT NULL COMMENT '过期时间',
    created_at          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (payment_id),
    UNIQUE KEY uq_payments_payment_no (payment_no),
    UNIQUE KEY uq_payments_idempotency (idempotency_key),
    KEY idx_payments_order (order_id),
    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT ck_payments_status
        CHECK (payment_status IN ('INITIATED','PENDING','AUTHORIZED','SUCCEEDED',
                                  'FAILED','CANCELLED','EXPIRED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '支付单';

DROP TABLE IF EXISTS payment_events;
CREATE TABLE payment_events (
    payment_event_id  bigint       NOT NULL AUTO_INCREMENT COMMENT '事件主键',
    payment_id        bigint       NOT NULL COMMENT '支付单主键',
    provider          varchar(64)  NOT NULL COMMENT '支付服务商',
    provider_event_id varchar(128) NOT NULL COMMENT '服务商事件 ID',
    event_type        varchar(64)  NOT NULL COMMENT '事件类型',
    event_status      varchar(32)           DEFAULT NULL COMMENT '事件状态',
    payload_json      json         NOT NULL COMMENT '回调原始报文',
    received_at       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '接收时间',
    processed_at      datetime              DEFAULT NULL COMMENT '处理完成时间',
    PRIMARY KEY (payment_event_id),
    UNIQUE KEY uq_payment_events_provider_event (provider, provider_event_id),
    KEY idx_payment_events_payment (payment_id),
    CONSTRAINT fk_payment_events_payment
        FOREIGN KEY (payment_id) REFERENCES payments (payment_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '支付回调事件';

DROP TABLE IF EXISTS refunds;
CREATE TABLE refunds (
    refund_id          bigint      NOT NULL AUTO_INCREMENT COMMENT '退款主键',
    refund_no          varchar(64) NOT NULL COMMENT '退款单号',
    order_id           bigint      NOT NULL COMMENT '订单主键',
    payment_id         bigint               DEFAULT NULL COMMENT '支付单主键',
    amount             bigint      NOT NULL COMMENT '退款金额',
    currency_code      char(3)     NOT NULL COMMENT '币种',
    refund_status      varchar(32) NOT NULL DEFAULT 'REQUESTED' COMMENT '退款状态',
    reason             varchar(255)         DEFAULT NULL COMMENT '退款原因',
    provider_refund_id varchar(128)         DEFAULT NULL COMMENT '服务商退款 ID',
    requested_at       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    completed_at       datetime             DEFAULT NULL COMMENT '完成时间',
    PRIMARY KEY (refund_id),
    UNIQUE KEY uq_refunds_refund_no (refund_no),
    KEY idx_refunds_order (order_id),
    CONSTRAINT fk_refunds_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT fk_refunds_payment
        FOREIGN KEY (payment_id) REFERENCES payments (payment_id),
    CONSTRAINT ck_refunds_status
        CHECK (refund_status IN ('REQUESTED','APPROVED','PROCESSING','SUCCEEDED','FAILED','REJECTED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '退款单';

-- ------------------------------------------------------------
-- 5. 履约与物流域
-- ------------------------------------------------------------

DROP TABLE IF EXISTS fulfillment_orders;
CREATE TABLE fulfillment_orders (
    fulfillment_id     bigint      NOT NULL AUTO_INCREMENT COMMENT '履约单主键',
    fulfillment_no     varchar(64) NOT NULL COMMENT '履约单号',
    order_id           bigint      NOT NULL COMMENT '订单主键',
    warehouse_code     varchar(64) NOT NULL COMMENT '仓库编码',
    fulfillment_status varchar(32) NOT NULL DEFAULT 'CREATED' COMMENT '履约状态',
    allocation_reason  varchar(255)         DEFAULT NULL COMMENT '分配原因',
    estimated_ship_at  datetime             DEFAULT NULL COMMENT '预计发货时间',
    actual_ship_at     datetime             DEFAULT NULL COMMENT '实际发货时间',
    completed_at       datetime             DEFAULT NULL COMMENT '完成时间',
    created_at         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (fulfillment_id),
    UNIQUE KEY uq_fulfillment_orders_no (fulfillment_no),
    KEY idx_fulfillment_orders_order (order_id),
    CONSTRAINT fk_fulfillment_orders_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT ck_fulfillment_orders_status
        CHECK (fulfillment_status IN ('CREATED','ALLOCATED','PICKING','PACKED','SHIPPED','DELIVERED','CANCELLED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '履约单';

DROP TABLE IF EXISTS fulfillment_items;
CREATE TABLE fulfillment_items (
    fulfillment_item_id bigint   NOT NULL AUTO_INCREMENT COMMENT '履约明细主键',
    fulfillment_id      bigint   NOT NULL COMMENT '履约单主键',
    order_item_id       bigint   NOT NULL COMMENT '订单明细主键',
    quantity            int      NOT NULL COMMENT '履约数量',
    created_at          datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (fulfillment_item_id),
    KEY idx_fulfillment_items_fulfillment (fulfillment_id),
    KEY idx_fulfillment_items_order_item (order_item_id),
    CONSTRAINT fk_fulfillment_items_fulfillment
        FOREIGN KEY (fulfillment_id) REFERENCES fulfillment_orders (fulfillment_id),
    CONSTRAINT fk_fulfillment_items_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_items (order_item_id),
    CONSTRAINT ck_fulfillment_items_quantity
        CHECK (quantity > 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '履约明细';

DROP TABLE IF EXISTS shipments;
CREATE TABLE shipments (
    shipment_id     bigint       NOT NULL AUTO_INCREMENT COMMENT '运单主键',
    shipment_no     varchar(64)  NOT NULL COMMENT '运单号',
    fulfillment_id  bigint       NOT NULL COMMENT '履约单主键',
    carrier_code    varchar(64)  NOT NULL COMMENT '承运商编码',
    tracking_no     varchar(128)          DEFAULT NULL COMMENT '物流追踪号',
    shipment_status varchar(32)  NOT NULL DEFAULT 'CREATED' COMMENT '运单状态',
    shipped_at      datetime              DEFAULT NULL COMMENT '发货时间',
    delivered_at    datetime              DEFAULT NULL COMMENT '签收时间',
    created_at      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (shipment_id),
    UNIQUE KEY uq_shipments_shipment_no (shipment_no),
    KEY idx_shipments_fulfillment (fulfillment_id),
    CONSTRAINT fk_shipments_fulfillment
        FOREIGN KEY (fulfillment_id) REFERENCES fulfillment_orders (fulfillment_id),
    CONSTRAINT ck_shipments_status
        CHECK (shipment_status IN ('CREATED','LABEL_PRINTED','IN_TRANSIT','DELIVERED','EXCEPTION','RETURNED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '运单';

DROP TABLE IF EXISTS tracking_events;
CREATE TABLE tracking_events (
    tracking_event_id bigint       NOT NULL AUTO_INCREMENT COMMENT '轨迹事件主键',
    shipment_id       bigint       NOT NULL COMMENT '运单主键',
    event_code        varchar(64)  NOT NULL COMMENT '事件编码',
    event_description varchar(255)          DEFAULT NULL COMMENT '事件描述',
    location          varchar(255)          DEFAULT NULL COMMENT '发生地点',
    occurred_at       datetime     NOT NULL COMMENT '发生时间',
    raw_payload_json  json         NOT NULL COMMENT '原始报文',
    received_at       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '接收时间',
    PRIMARY KEY (tracking_event_id),
    KEY idx_tracking_events_shipment (shipment_id, occurred_at),
    CONSTRAINT fk_tracking_events_shipment
        FOREIGN KEY (shipment_id) REFERENCES shipments (shipment_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '物流轨迹事件';

-- ------------------------------------------------------------
-- 6. 库存域（阶段 0-3 交易闭环必需，对应文档 领域全景-库存中心）
-- ------------------------------------------------------------

DROP TABLE IF EXISTS inventory;
CREATE TABLE inventory (
    inventory_id     bigint      NOT NULL AUTO_INCREMENT COMMENT '库存主键',
    sku_id           bigint      NOT NULL COMMENT 'SKU 主键',
    warehouse_code   varchar(64) NOT NULL COMMENT '仓库编码',
    total_quantity   int         NOT NULL DEFAULT 0 COMMENT '总库存',
    locked_quantity  int         NOT NULL DEFAULT 0 COMMENT '锁定库存',
    safety_stock     int         NOT NULL DEFAULT 0 COMMENT '安全库存',
    status           varchar(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
    created_at       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (inventory_id),
    UNIQUE KEY uq_inventory_sku_warehouse (sku_id, warehouse_code),
    KEY idx_inventory_warehouse (warehouse_code),
    CONSTRAINT ck_inventory_quantity
        CHECK (total_quantity >= 0 AND locked_quantity >= 0 AND locked_quantity <= total_quantity),
    CONSTRAINT ck_inventory_status
        CHECK (status IN ('ACTIVE','FROZEN'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '可分库存(可用=总量-锁定)';

DROP TABLE IF EXISTS inventory_transaction;
CREATE TABLE inventory_transaction (
    transaction_id bigint       NOT NULL AUTO_INCREMENT COMMENT '流水主键',
    sku_id         bigint       NOT NULL COMMENT 'SKU 主键',
    warehouse_code varchar(64)  NOT NULL COMMENT '仓库编码',
    biz_type       varchar(32)  NOT NULL COMMENT '业务类型',
    quantity       int          NOT NULL COMMENT '变更数量(正数)',
    biz_no         varchar(64)           DEFAULT NULL COMMENT '业务单号',
    remark         varchar(255)          DEFAULT NULL COMMENT '备注',
    created_at     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (transaction_id),
    KEY idx_inventory_transaction_sku (sku_id, warehouse_code, created_at),
    KEY idx_inventory_transaction_biz (biz_no),
    CONSTRAINT ck_inventory_transaction_type
        CHECK (biz_type IN ('LOCK','UNLOCK','DEDUCT','RESTOCK','ADJUST'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '库存变更流水';

-- ------------------------------------------------------------
-- 7. 本地消息表（对应文档 10.1 一致性机制：本地消息表）
-- ------------------------------------------------------------

DROP TABLE IF EXISTS local_message;
CREATE TABLE local_message (
    message_id    bigint       NOT NULL AUTO_INCREMENT COMMENT '消息主键',
    message_no    varchar(64)  NOT NULL COMMENT '消息编号',
    topic         varchar(128) NOT NULL COMMENT '消息主题',
    biz_key       varchar(128)          DEFAULT NULL COMMENT '业务键',
    payload_json  json         NOT NULL COMMENT '消息体',
    status        varchar(32)  NOT NULL DEFAULT 'PENDING' COMMENT '投递状态',
    retry_count   int          NOT NULL DEFAULT 0 COMMENT '重试次数',
    next_retry_at datetime              DEFAULT NULL COMMENT '下次重试时间',
    sent_at       datetime              DEFAULT NULL COMMENT '投递成功时间',
    created_at    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (message_id),
    UNIQUE KEY uq_local_message_no (message_no),
    KEY idx_local_message_status (status, next_retry_at),
    KEY idx_local_message_biz (biz_key),
    CONSTRAINT ck_local_message_status
        CHECK (status IN ('PENDING','SENT','FAILED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '本地消息表(业务事务与消息同库落库)';
