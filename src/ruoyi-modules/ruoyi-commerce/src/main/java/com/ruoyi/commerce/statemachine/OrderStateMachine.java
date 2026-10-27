package com.ruoyi.commerce.statemachine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.commerce.constant.CommerceConstants;

/**
 * 订单主状态机（BR-005：非法流转必须拒绝并记录）。
 *
 * @author ruoyi
 */
public class OrderStateMachine
{
    private static final Map<String, Set<String>> TRANSITIONS = new HashMap<>();

    static
    {
        allow(CommerceConstants.ORDER_CREATED, CommerceConstants.ORDER_PENDING_PAYMENT, CommerceConstants.ORDER_CANCELLED);
        allow(CommerceConstants.ORDER_PENDING_PAYMENT, CommerceConstants.ORDER_PAID, CommerceConstants.ORDER_CANCELLED);
        allow(CommerceConstants.ORDER_PAID, CommerceConstants.ORDER_CONFIRMED, CommerceConstants.ORDER_CANCELLED);
        allow(CommerceConstants.ORDER_CONFIRMED, CommerceConstants.ORDER_FULFILLING, CommerceConstants.ORDER_CANCELLED);
        allow(CommerceConstants.ORDER_FULFILLING, CommerceConstants.ORDER_SHIPPED, CommerceConstants.ORDER_CANCELLED);
        allow(CommerceConstants.ORDER_SHIPPED, CommerceConstants.ORDER_IN_TRANSIT, CommerceConstants.ORDER_DELIVERED);
        allow(CommerceConstants.ORDER_IN_TRANSIT, CommerceConstants.ORDER_DELIVERED);
        allow(CommerceConstants.ORDER_DELIVERED, CommerceConstants.ORDER_COMPLETED);
        allow(CommerceConstants.ORDER_COMPLETED);
        allow(CommerceConstants.ORDER_CANCELLED);
    }

    private OrderStateMachine()
    {
    }

    private static void allow(String from, String... targets)
    {
        TRANSITIONS.put(from, new HashSet<>(Arrays.asList(targets)));
    }

    public static boolean canTransition(String from, String to)
    {
        if (from == null || to == null)
        {
            return false;
        }
        if (from.equals(to))
        {
            return true;
        }
        Set<String> targets = TRANSITIONS.get(from);
        return targets != null && targets.contains(to);
    }

    public static void validate(String from, String to)
    {
        if (!canTransition(from, to))
        {
            throw new ServiceException("订单状态不允许从 " + from + " 流转到 " + to);
        }
    }

    public static boolean cancellable(String status)
    {
        return Arrays.asList(CommerceConstants.ORDER_CREATED, CommerceConstants.ORDER_PENDING_PAYMENT,
                CommerceConstants.ORDER_PAID, CommerceConstants.ORDER_CONFIRMED,
                CommerceConstants.ORDER_FULFILLING).contains(status);
    }
}
