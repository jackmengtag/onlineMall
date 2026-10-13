package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.OrderStateHistory;

/**
 * 订单状态历史数据层
 *
 * @author ruoyi
 */
public interface OrderStateHistoryMapper
{
    /**
     * 新增状态历史
     */
    int insertOrderStateHistory(OrderStateHistory orderStateHistory);

    /**
     * 按订单查询状态历史
     */
    List<OrderStateHistory> selectStateHistoriesByOrderId(Long orderId);
}
