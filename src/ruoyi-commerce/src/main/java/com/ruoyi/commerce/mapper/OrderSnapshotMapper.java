package com.ruoyi.commerce.mapper;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.OrderSnapshot;

/**
 * 订单快照 Mapper。
 *
 * @author ruoyi
 */
public interface OrderSnapshotMapper
{
    OrderSnapshot selectByOrderId(@Param("orderId") Long orderId);

    int insertOrderSnapshot(OrderSnapshot snapshot);
}
