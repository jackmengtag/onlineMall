package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.OrderItem;

/**
 * 订单明细数据层
 *
 * @author ruoyi
 */
public interface OrderItemMapper
{
    /**
     * 按订单查询明细
     */
    List<OrderItem> selectOrderItemsByOrderId(Long orderId);

    /**
     * 新增订单明细
     */
    int insertOrderItem(OrderItem orderItem);

    /**
     * 批量新增订单明细
     */
    int insertOrderItemBatch(List<OrderItem> orderItems);

    /**
     * 按订单删除明细
     */
    int deleteOrderItemsByOrderId(Long orderId);
}
