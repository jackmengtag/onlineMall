package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.OrderItemSnapshot;

/**
 * 订单明细快照 Mapper。
 *
 * @author ruoyi
 */
public interface OrderItemSnapshotMapper
{
    List<OrderItemSnapshot> selectByOrderId(@Param("orderId") Long orderId);

    int insertOrderItemSnapshot(OrderItemSnapshot snapshot);
}
