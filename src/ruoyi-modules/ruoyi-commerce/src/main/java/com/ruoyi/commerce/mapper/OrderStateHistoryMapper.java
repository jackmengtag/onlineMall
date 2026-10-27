package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.OrderStateHistory;

/**
 * 订单状态历史 Mapper。
 *
 * @author ruoyi
 */
public interface OrderStateHistoryMapper
{
    List<OrderStateHistory> selectByOrderId(@Param("orderId") Long orderId);

    int insertHistory(OrderStateHistory history);
}
