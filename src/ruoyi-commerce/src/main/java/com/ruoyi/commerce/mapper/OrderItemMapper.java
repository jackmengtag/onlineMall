package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.OrderItem;

/**
 * 订单明细 Mapper。
 *
 * @author ruoyi
 */
public interface OrderItemMapper
{
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);

    int insertOrderItem(OrderItem item);
}
