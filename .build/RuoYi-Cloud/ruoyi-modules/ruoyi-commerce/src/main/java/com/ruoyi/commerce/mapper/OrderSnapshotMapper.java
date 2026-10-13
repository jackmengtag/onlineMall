package com.ruoyi.commerce.mapper;

import com.ruoyi.commerce.domain.OrderSnapshot;

/**
 * 订单快照数据层
 *
 * @author ruoyi
 */
public interface OrderSnapshotMapper
{
    /**
     * 新增订单快照
     */
    int insertOrderSnapshot(OrderSnapshot orderSnapshot);

    /**
     * 按订单查询快照
     */
    OrderSnapshot selectSnapshotByOrderId(Long orderId);
}
