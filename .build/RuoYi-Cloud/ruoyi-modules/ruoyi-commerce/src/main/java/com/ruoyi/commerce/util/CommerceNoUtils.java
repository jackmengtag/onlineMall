package com.ruoyi.commerce.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import com.ruoyi.common.core.utils.DateUtils;

/**
 * 跨境电商业务单号生成工具
 *
 * <p>单号规则：业务前缀 + yyyyMMddHHmmss + 4 位随机数，例如 ORD202610131030001234。</p>
 *
 * @author ruoyi
 */
public class CommerceNoUtils
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /** 订单号前缀 */
    public static final String PREFIX_ORDER = "ORD";
    /** 支付单号前缀 */
    public static final String PREFIX_PAYMENT = "PAY";
    /** 退款单号前缀 */
    public static final String PREFIX_REFUND = "REF";
    /** 履约单号前缀 */
    public static final String PREFIX_FULFILLMENT = "FUL";
    /** 运单号前缀 */
    public static final String PREFIX_SHIPMENT = "SHP";
    /** 客户编号前缀 */
    public static final String PREFIX_CUSTOMER = "CUS";
    /** 本地消息编号前缀 */
    public static final String PREFIX_MESSAGE = "MSG";
    /** SPU 编码前缀 */
    public static final String PREFIX_SPU = "SPU";
    /** SKU 编码前缀 */
    public static final String PREFIX_SKU = "SKU";

    private CommerceNoUtils()
    {
    }

    /**
     * 生成业务单号
     *
     * @param prefix 前缀
     * @return 单号
     */
    public static String generate(String prefix)
    {
        return prefix + LocalDateTime.now().format(FORMATTER) + randomSuffix();
    }

    /** 生成订单号 */
    public static String orderNo()
    {
        return generate(PREFIX_ORDER);
    }

    /** 生成支付单号 */
    public static String paymentNo()
    {
        return generate(PREFIX_PAYMENT);
    }

    /** 生成退款单号 */
    public static String refundNo()
    {
        return generate(PREFIX_REFUND);
    }

    /** 生成履约单号 */
    public static String fulfillmentNo()
    {
        return generate(PREFIX_FULFILLMENT);
    }

    /** 生成运单号 */
    public static String shipmentNo()
    {
        return generate(PREFIX_SHIPMENT);
    }

    /** 生成客户编号 */
    public static String customerNo()
    {
        return generate(PREFIX_CUSTOMER);
    }

    /** 生成本地消息编号 */
    public static String messageNo()
    {
        return generate(PREFIX_MESSAGE);
    }

    /**
     * 生成幂等键，用于下单、支付回调、退款等场景
     *
     * @param bizType 业务类型
     * @param bizKey  业务键
     * @return 幂等键
     */
    public static String idempotencyKey(String bizType, String bizKey)
    {
        return bizType + ":" + bizKey + ":" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + randomSuffix();
    }

    private static String randomSuffix()
    {
        return String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }
}
