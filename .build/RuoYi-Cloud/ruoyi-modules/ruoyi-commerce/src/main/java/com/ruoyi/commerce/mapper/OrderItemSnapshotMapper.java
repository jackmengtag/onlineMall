package com.ruoyi.commerce.mapper;

import com.ruoyi.commerce.domain.OrderItemSnapshot;

/**
 * 订单明细快照数据层
 *
 * @author ruoyi
 */
public interface OrderItemSnapshotMapper
{
    /**
     * 新增明细快照
     */
    int insertOrderItemSnapshot(OrderItemSnapshot orderItemSnapshot);

    /**
     * 按订单明细查询快照
     */
    OrderItemSnapshot selectSnapshotByOrderItemId(Long orderItemId);
}
