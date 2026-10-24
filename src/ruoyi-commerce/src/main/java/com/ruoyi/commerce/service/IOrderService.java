package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.OrderStateHistory;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.dto.CreateOrderRequest;
import com.ruoyi.commerce.domain.dto.ExternalOrderRequest;
import com.ruoyi.commerce.domain.vo.OrderCreateResult;
import com.ruoyi.commerce.domain.vo.OrderDetailVo;

/**
 * 订单服务。
 */
public interface IOrderService
{
    List<Orders> selectOrderList(Orders orders);

    Orders selectOrderById(Long orderId);

    Orders selectOrderByNo(String orderNo);

    OrderDetailVo selectOrderDetail(Long orderId);

    OrderCreateResult createOrder(CreateOrderRequest request);

    Long importExternalOrder(ExternalOrderRequest request);

    void markPaid(Long orderId);

    void markPaymentFailed(Long orderId);

    void markFulfilling(Long orderId);

    void markShipped(Long orderId);

    void markDelivered(Long orderId);

    void completeOrder(Long orderId);

    void cancelOrder(Long orderId, String reason);

    void markRefundResult(Long orderId, long refundAmount, boolean success);

    List<OrderStateHistory> selectHistories(Long orderId);
}
