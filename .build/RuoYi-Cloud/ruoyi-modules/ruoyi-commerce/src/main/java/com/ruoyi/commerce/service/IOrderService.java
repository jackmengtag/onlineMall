package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.OrderItem;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.dto.CreateOrderRequest;

/**
 * 订单服务
 *
 * <p>对应文档第 5.1、5.2 节：下单流程、订单状态机与订单快照。
 * 订单是核心聚合根，四个正交状态由订单服务统一维护并通过状态机校验流转。</p>
 *
 * @author ruoyi
 */
public interface IOrderService
{
    /**
     * 下单：生成订单主记录、明细、快照，锁定库存并生成支付单，订单进入待支付状态
     *
     * @param request 下单请求
     * @return 订单
     */
    Orders createOrder(CreateOrderRequest request);

    /**
     * 查询订单
     */
    Orders selectOrdersById(Long orderId);

    /**
     * 按订单号查询
     */
    Orders selectOrdersByNo(String orderNo);

    /**
     * 查询订单列表
     */
    List<Orders> selectOrdersList(Orders orders);

    /**
     * 查询订单明细
     */
    List<OrderItem> selectOrderItems(Long orderId);

    /**
     * 查询订单状态历史
     */
    List<com.ruoyi.commerce.domain.OrderStateHistory> selectStateHistories(Long orderId);

    /**
     * 新增订单
     */
    int insertOrders(Orders orders);

    /**
     * 修改订单
     */
    int updateOrders(Orders orders);

    /**
     * 批量删除订单
     */
    int deleteOrdersByIds(Long[] orderIds);

    /**
     * 状态流转（状态机校验 + 写状态历史 + 更新订单四正交状态）
     *
     * @param orderId      订单主键
     * @param statusType   ORDER / PAYMENT / FULFILLMENT / AFTER_SALE
     * @param toStatus     目标状态
     * @param reason       变更原因
     * @param operatorType 操作者类型
     * @param operatorId   操作者标识
     * @return 影响行数
     */
    int changeStatus(Long orderId, String statusType, String toStatus, String reason, String operatorType, String operatorId);

    /**
     * 支付成功后推进订单，支付回调幂等由支付域保证
     */
    int markOrderPaid(Long orderId, String paymentNo);

    /**
     * 订单进入履约中
     */
    int markOrderFulfilling(Long orderId);

    /**
     * 订单已发货
     */
    int markOrderShipped(Long orderId);

    /**
     * 订单已签收
     */
    int markOrderDelivered(Long orderId);

    /**
     * 订单完成
     */
    int markOrderCompleted(Long orderId);

    /**
     * 取消订单（释放锁定库存）
     */
    int cancelOrder(Long orderId, String reason);
}
