package com.ruoyi.commerce.constant;

/**
 * 跨境电商平台通用常量
 *
 * <p>对应文档《跨境电商平台架构设计文档》第 5.2 节订单状态机与第 8 章数据库模型，
 * 集中定义各域状态取值、状态类型、业务类型与默认配置，避免魔法字符串散落。</p>
 *
 * @author ruoyi
 */
public class CommerceConstants
{
    private CommerceConstants()
    {
    }

    /** 默认渠道：独立站 */
    public static final String DEFAULT_CHANNEL = "DIRECT";

    /** 默认仓库（阶段 0-3 单仓履约） */
    public static final String DEFAULT_WAREHOUSE = "DEFAULT";

    /** 系统操作者 */
    public static final String OPERATOR_SYSTEM = "SYSTEM";

    /** 人工操作者 */
    public static final String OPERATOR_MANUAL = "MANUAL";

    // ------------------------------------------------------------------
    // 状态类型（order_state_histories.status_type）
    // ------------------------------------------------------------------

    /** 订单主状态 */
    public static final String STATUS_TYPE_ORDER = "ORDER";

    /** 支付状态 */
    public static final String STATUS_TYPE_PAYMENT = "PAYMENT";

    /** 履约状态 */
    public static final String STATUS_TYPE_FULFILLMENT = "FULFILLMENT";

    /** 售后状态 */
    public static final String STATUS_TYPE_AFTER_SALE = "AFTER_SALE";

    // ------------------------------------------------------------------
    // 订单主状态 order_status
    // ------------------------------------------------------------------

    public static final String ORDER_CREATED = "CREATED";
    public static final String ORDER_PENDING_PAYMENT = "PENDING_PAYMENT";
    public static final String ORDER_PAID = "PAID";
    public static final String ORDER_CONFIRMED = "CONFIRMED";
    public static final String ORDER_FULFILLING = "FULFILLING";
    public static final String ORDER_SHIPPED = "SHIPPED";
    public static final String ORDER_IN_TRANSIT = "IN_TRANSIT";
    public static final String ORDER_DELIVERED = "DELIVERED";
    public static final String ORDER_COMPLETED = "COMPLETED";
    public static final String ORDER_CANCELLED = "CANCELLED";

    // ------------------------------------------------------------------
    // 支付状态 payment_status
    // ------------------------------------------------------------------

    public static final String PAY_UNPAID = "UNPAID";
    public static final String PAY_AUTHORIZED = "AUTHORIZED";
    public static final String PAY_PARTIALLY_PAID = "PARTIALLY_PAID";
    public static final String PAY_PAID = "PAID";
    public static final String PAY_REFUNDING = "REFUNDING";
    public static final String PAY_PARTIALLY_REFUNDED = "PARTIALLY_REFUNDED";
    public static final String PAY_REFUNDED = "REFUNDED";
    public static final String PAY_FAILED = "FAILED";

    // ------------------------------------------------------------------
    // 履约状态 fulfillment_status
    // ------------------------------------------------------------------

    public static final String FULFILL_UNFULFILLED = "UNFULFILLED";
    public static final String FULFILL_PARTIALLY_FULFILLED = "PARTIALLY_FULFILLED";
    public static final String FULFILL_FULFILLED = "FULFILLED";
    public static final String FULFILL_RETURNED = "RETURNED";

    // ------------------------------------------------------------------
    // 售后状态 after_sale_status
    // ------------------------------------------------------------------

    public static final String AFTER_SALE_NONE = "NONE";
    public static final String AFTER_SALE_REQUESTED = "REQUESTED";
    public static final String AFTER_SALE_PROCESSING = "PROCESSING";
    public static final String AFTER_SALE_RESOLVED = "RESOLVED";
    public static final String AFTER_SALE_REJECTED = "REJECTED";

    // ------------------------------------------------------------------
    // 支付单状态 payments.payment_status
    // ------------------------------------------------------------------

    public static final String PAYMENT_INITIATED = "INITIATED";
    public static final String PAYMENT_PENDING = "PENDING";
    public static final String PAYMENT_AUTHORIZED = "AUTHORIZED";
    public static final String PAYMENT_SUCCEEDED = "SUCCEEDED";
    public static final String PAYMENT_FAILED = "FAILED";
    public static final String PAYMENT_CANCELLED = "CANCELLED";
    public static final String PAYMENT_EXPIRED = "EXPIRED";

    // ------------------------------------------------------------------
    // 退款状态 refunds.refund_status
    // ------------------------------------------------------------------

    public static final String REFUND_REQUESTED = "REQUESTED";
    public static final String REFUND_APPROVED = "APPROVED";
    public static final String REFUND_PROCESSING = "PROCESSING";
    public static final String REFUND_SUCCEEDED = "SUCCEEDED";
    public static final String REFUND_FAILED = "FAILED";
    public static final String REFUND_REJECTED = "REJECTED";

    // ------------------------------------------------------------------
    // 履约单状态 fulfillment_orders.fulfillment_status
    // ------------------------------------------------------------------

    public static final String FULFILLMENT_CREATED = "CREATED";
    public static final String FULFILLMENT_ALLOCATED = "ALLOCATED";
    public static final String FULFILLMENT_PICKING = "PICKING";
    public static final String FULFILLMENT_PACKED = "PACKED";
    public static final String FULFILLMENT_SHIPPED = "SHIPPED";
    public static final String FULFILLMENT_DELIVERED = "DELIVERED";
    public static final String FULFILLMENT_CANCELLED = "CANCELLED";

    // ------------------------------------------------------------------
    // 运单状态 shipments.shipment_status
    // ------------------------------------------------------------------

    public static final String SHIPMENT_CREATED = "CREATED";
    public static final String SHIPMENT_LABEL_PRINTED = "LABEL_PRINTED";
    public static final String SHIPMENT_IN_TRANSIT = "IN_TRANSIT";
    public static final String SHIPMENT_DELIVERED = "DELIVERED";
    public static final String SHIPMENT_EXCEPTION = "EXCEPTION";
    public static final String SHIPMENT_RETURNED = "RETURNED";

    // ------------------------------------------------------------------
    // 库存变更业务类型 inventory_transaction.biz_type
    // ------------------------------------------------------------------

    public static final String INVENTORY_LOCK = "LOCK";
    public static final String INVENTORY_UNLOCK = "UNLOCK";
    public static final String INVENTORY_DEDUCT = "DEDUCT";
    public static final String INVENTORY_RESTOCK = "RESTOCK";
    public static final String INVENTORY_ADJUST = "ADJUST";

    // ------------------------------------------------------------------
    // 本地消息 topic（跨领域事件）
    // ------------------------------------------------------------------

    public static final String TOPIC_ORDER_CREATED = "commerce.order.created";
    public static final String TOPIC_ORDER_PAID = "commerce.order.paid";
    public static final String TOPIC_ORDER_CANCELLED = "commerce.order.cancelled";
    public static final String TOPIC_ORDER_SHIPPED = "commerce.order.shipped";
    public static final String TOPIC_ORDER_DELIVERED = "commerce.order.delivered";
    public static final String TOPIC_PAYMENT_SUCCEEDED = "commerce.payment.succeeded";
    public static final String TOPIC_FULFILLMENT_CREATED = "commerce.fulfillment.created";
    public static final String TOPIC_REFUND_SUCCEEDED = "commerce.refund.succeeded";
}
