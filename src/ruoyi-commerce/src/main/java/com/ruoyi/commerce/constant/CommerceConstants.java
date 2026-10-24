package com.ruoyi.commerce.constant;

/**
 * 跨境电商平台常量。
 *
 * @author ruoyi
 */
public class CommerceConstants
{
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

    public static final String PAY_UNPAID = "UNPAID";
    public static final String PAY_PARTIALLY_PAID = "PARTIALLY_PAID";
    public static final String PAY_PAID = "PAID";
    public static final String PAY_REFUNDING = "REFUNDING";
    public static final String PAY_PARTIALLY_REFUNDED = "PARTIALLY_REFUNDED";
    public static final String PAY_REFUNDED = "REFUNDED";
    public static final String PAY_FAILED = "FAILED";

    public static final String PAYMENT_SUCCEEDED = "SUCCEEDED";
    public static final String PAYMENT_INITIATED = "INITIATED";
    public static final String PAYMENT_FAILED = "FAILED";

    public static final String FULFILL_UNFULFILLED = "UNFULFILLED";
    public static final String FULFILL_PARTIALLY = "PARTIALLY_FULFILLED";
    public static final String FULFILL_FULFILLED = "FULFILLED";
    public static final String FULFILL_RETURNED = "RETURNED";

    public static final String AFTER_SALE_NONE = "NONE";
    public static final String AFTER_SALE_REQUESTED = "REQUESTED";
    public static final String AFTER_SALE_PROCESSING = "PROCESSING";
    public static final String AFTER_SALE_RESOLVED = "RESOLVED";
    public static final String AFTER_SALE_REJECTED = "REJECTED";

    public static final String FUL_ORDER_CREATED = "CREATED";
    public static final String FUL_ORDER_ALLOCATED = "ALLOCATED";
    public static final String FUL_ORDER_PICKING = "PICKING";
    public static final String FUL_ORDER_PACKED = "PACKED";
    public static final String FUL_ORDER_SHIPPED = "SHIPPED";
    public static final String FUL_ORDER_DELIVERED = "DELIVERED";
    public static final String FUL_ORDER_CANCELLED = "CANCELLED";

    public static final String SHIPMENT_CREATED = "CREATED";
    public static final String SHIPMENT_IN_TRANSIT = "IN_TRANSIT";
    public static final String SHIPMENT_DELIVERED = "DELIVERED";
    public static final String SHIPMENT_EXCEPTION = "EXCEPTION";
    public static final String SHIPMENT_RETURNED = "RETURNED";

    public static final String REFUND_REQUESTED = "REQUESTED";
    public static final String REFUND_APPROVED = "APPROVED";
    public static final String REFUND_PROCESSING = "PROCESSING";
    public static final String REFUND_SUCCEEDED = "SUCCEEDED";
    public static final String REFUND_FAILED = "FAILED";
    public static final String REFUND_REJECTED = "REJECTED";

    public static final String SPU_DRAFT = "DRAFT";
    public static final String SPU_PUBLISHED = "PUBLISHED";
    public static final String SPU_ON_SALE = "ON_SALE";
    public static final String SPU_SUSPENDED = "SUSPENDED";
    public static final String SPU_OFF_SALE = "OFF_SALE";

    public static final String INV_RESERVE = "RESERVE";
    public static final String INV_RELEASE = "RELEASE";
    public static final String INV_DEDUCT = "DEDUCT";
    public static final String INV_RESTORE = "RESTORE";
    public static final String INV_ADJUST = "ADJUST";

    public static final String MESSAGE_PENDING = "PENDING";
    public static final String MESSAGE_SENT = "SENT";
    public static final String MESSAGE_FAILED = "FAILED";

    public static final String DEFAULT_CHANNEL = "DIRECT";
    public static final String DEFAULT_WAREHOUSE = "US-WEST";

    public static final String TOPIC_ORDER = "commerce.order";
    public static final String TOPIC_PAYMENT = "commerce.payment";
    public static final String TOPIC_INVENTORY = "commerce.inventory";
    public static final String TOPIC_FULFILLMENT = "commerce.fulfillment";
    public static final String TOPIC_AFTER_SALE = "commerce.after-sale";

    /** 买家登录令牌 Redis 前缀 */
    public static final String CUSTOMER_TOKEN_PREFIX = "commerce:customer:token:";

    private CommerceConstants()
    {
    }
}
