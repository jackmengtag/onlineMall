package com.ruoyi.commerce.statemachine;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.commerce.constant.CommerceConstants;

/**
 * 订单状态机
 *
 * <p>对应文档第 5.2 节，订单需独立维护四个正交状态：order_status、payment_status、
 * fulfillment_status、after_sale_status。本状态机负责约束合法流转，非法流转必须拒绝并记录。</p>
 *
 * @author ruoyi
 */
public final class OrderStateMachine
{
    /** 订单主状态流转图 */
    private static final Map<String, Set<String>> ORDER_TRANSITIONS;

    /** 支付状态流转图 */
    private static final Map<String, Set<String>> PAYMENT_TRANSITIONS;

    /** 履约状态流转图 */
    private static final Map<String, Set<String>> FULFILLMENT_TRANSITIONS;

    /** 售后状态流转图 */
    private static final Map<String, Set<String>> AFTER_SALE_TRANSITIONS;

    static
    {
        Map<String, Set<String>> order = new HashMap<>();
        order.put(CommerceConstants.ORDER_CREATED, setOf(CommerceConstants.ORDER_PENDING_PAYMENT, CommerceConstants.ORDER_CANCELLED));
        order.put(CommerceConstants.ORDER_PENDING_PAYMENT, setOf(CommerceConstants.ORDER_PAID, CommerceConstants.ORDER_CANCELLED));
        order.put(CommerceConstants.ORDER_PAID, setOf(CommerceConstants.ORDER_CONFIRMED, CommerceConstants.ORDER_CANCELLED));
        order.put(CommerceConstants.ORDER_CONFIRMED, setOf(CommerceConstants.ORDER_FULFILLING, CommerceConstants.ORDER_CANCELLED));
        order.put(CommerceConstants.ORDER_FULFILLING, setOf(CommerceConstants.ORDER_SHIPPED));
        order.put(CommerceConstants.ORDER_SHIPPED, setOf(CommerceConstants.ORDER_IN_TRANSIT, CommerceConstants.ORDER_DELIVERED));
        order.put(CommerceConstants.ORDER_IN_TRANSIT, setOf(CommerceConstants.ORDER_DELIVERED));
        order.put(CommerceConstants.ORDER_DELIVERED, setOf(CommerceConstants.ORDER_COMPLETED));
        order.put(CommerceConstants.ORDER_COMPLETED, Collections.emptySet());
        order.put(CommerceConstants.ORDER_CANCELLED, Collections.emptySet());
        ORDER_TRANSITIONS = Collections.unmodifiableMap(order);

        Map<String, Set<String>> payment = new HashMap<>();
        payment.put(CommerceConstants.PAY_UNPAID, setOf(CommerceConstants.PAY_AUTHORIZED, CommerceConstants.PAY_PARTIALLY_PAID,
                CommerceConstants.PAY_PAID, CommerceConstants.PAY_FAILED));
        payment.put(CommerceConstants.PAY_AUTHORIZED, setOf(CommerceConstants.PAY_PAID, CommerceConstants.PAY_FAILED));
        payment.put(CommerceConstants.PAY_PARTIALLY_PAID, setOf(CommerceConstants.PAY_PAID, CommerceConstants.PAY_REFUNDING));
        payment.put(CommerceConstants.PAY_PAID, setOf(CommerceConstants.PAY_REFUNDING, CommerceConstants.PAY_PARTIALLY_REFUNDED,
                CommerceConstants.PAY_REFUNDED));
        payment.put(CommerceConstants.PAY_REFUNDING, setOf(CommerceConstants.PAY_PARTIALLY_REFUNDED,
                CommerceConstants.PAY_REFUNDED, CommerceConstants.PAY_PAID));
        payment.put(CommerceConstants.PAY_PARTIALLY_REFUNDED, setOf(CommerceConstants.PAY_REFUNDED));
        payment.put(CommerceConstants.PAY_REFUNDED, Collections.emptySet());
        payment.put(CommerceConstants.PAY_FAILED, setOf(CommerceConstants.PAY_UNPAID));
        PAYMENT_TRANSITIONS = Collections.unmodifiableMap(payment);

        Map<String, Set<String>> fulfillment = new HashMap<>();
        fulfillment.put(CommerceConstants.FULFILL_UNFULFILLED, setOf(CommerceConstants.FULFILL_PARTIALLY_FULFILLED,
                CommerceConstants.FULFILL_FULFILLED));
        fulfillment.put(CommerceConstants.FULFILL_PARTIALLY_FULFILLED, setOf(CommerceConstants.FULFILL_FULFILLED,
                CommerceConstants.FULFILL_RETURNED));
        fulfillment.put(CommerceConstants.FULFILL_FULFILLED, setOf(CommerceConstants.FULFILL_RETURNED));
        fulfillment.put(CommerceConstants.FULFILL_RETURNED, Collections.emptySet());
        FULFILLMENT_TRANSITIONS = Collections.unmodifiableMap(fulfillment);

        Map<String, Set<String>> afterSale = new HashMap<>();
        afterSale.put(CommerceConstants.AFTER_SALE_NONE, setOf(CommerceConstants.AFTER_SALE_REQUESTED));
        afterSale.put(CommerceConstants.AFTER_SALE_REQUESTED, setOf(CommerceConstants.AFTER_SALE_PROCESSING,
                CommerceConstants.AFTER_SALE_REJECTED, CommerceConstants.AFTER_SALE_RESOLVED));
        afterSale.put(CommerceConstants.AFTER_SALE_PROCESSING, setOf(CommerceConstants.AFTER_SALE_RESOLVED,
                CommerceConstants.AFTER_SALE_REJECTED));
        afterSale.put(CommerceConstants.AFTER_SALE_RESOLVED, Collections.emptySet());
        afterSale.put(CommerceConstants.AFTER_SALE_REJECTED, setOf(CommerceConstants.AFTER_SALE_REQUESTED));
        AFTER_SALE_TRANSITIONS = Collections.unmodifiableMap(afterSale);
    }

    private OrderStateMachine()
    {
    }

    /**
     * 判断状态是否可流转
     *
     * @param statusType 状态类型 ORDER / PAYMENT / FULFILLMENT / AFTER_SALE
     * @param fromStatus 原状态
     * @param toStatus   目标状态
     * @return 是否合法
     */
    public static boolean canTransfer(String statusType, String fromStatus, String toStatus)
    {
        if (fromStatus == null || fromStatus.equals(toStatus))
        {
            // 相同状态视为幂等，允许
            return fromStatus == null || fromStatus.equals(toStatus);
        }
        Set<String> next = nextStatuses(statusType, fromStatus);
        return next.contains(toStatus);
    }

    /**
     * 校验状态流转合法性，非法时抛出业务异常
     *
     * @param statusType 状态类型
     * @param fromStatus 原状态
     * @param toStatus   目标状态
     */
    public static void assertTransfer(String statusType, String fromStatus, String toStatus)
    {
        if (!canTransfer(statusType, fromStatus, toStatus))
        {
            throw new ServiceException(String.format("非法的%s状态流转：%s -> %s", statusType, fromStatus, toStatus));
        }
    }

    /**
     * 查询指定状态的合法后继状态
     *
     * @param statusType 状态类型
     * @param fromStatus 原状态
     * @return 后继状态集合
     */
    public static Set<String> nextStatuses(String statusType, String fromStatus)
    {
        Map<String, Set<String>> transitions = transitionsOf(statusType);
        return transitions.getOrDefault(fromStatus, Collections.emptySet());
    }

    private static Map<String, Set<String>> transitionsOf(String statusType)
    {
        switch (statusType)
        {
            case CommerceConstants.STATUS_TYPE_ORDER:
                return ORDER_TRANSITIONS;
            case CommerceConstants.STATUS_TYPE_PAYMENT:
                return PAYMENT_TRANSITIONS;
            case CommerceConstants.STATUS_TYPE_FULFILLMENT:
                return FULFILLMENT_TRANSITIONS;
            case CommerceConstants.STATUS_TYPE_AFTER_SALE:
                return AFTER_SALE_TRANSITIONS;
            default:
                throw new ServiceException("未知的状态类型：" + statusType);
        }
    }

    private static Set<String> setOf(String... values)
    {
        Set<String> set = new LinkedHashSet<>();
        Collections.addAll(set, values);
        return set;
    }
}
