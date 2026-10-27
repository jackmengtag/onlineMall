package com.ruoyi.commerce.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.Orders;

/**
 * 订单 Mapper。
 *
 * @author ruoyi
 */
public interface OrdersMapper
{
    List<Orders> selectOrderList(Orders orders);

    Orders selectOrderById(@Param("orderId") Long orderId);

    Orders selectOrderByNo(@Param("orderNo") String orderNo);

    Orders selectExternal(@Param("channelCode") String channelCode, @Param("externalOrderId") String externalOrderId);

    int insertOrders(Orders orders);

    int updateOrders(Orders orders);

    int updateStatus(@Param("orderId") Long orderId, @Param("fromStatus") String fromStatus,
            @Param("toStatus") String toStatus, @Param("updateBy") String updateBy);

    int updatePaymentStatus(@Param("orderId") Long orderId, @Param("paymentStatus") String paymentStatus,
            @Param("updateBy") String updateBy);

    int updateFulfillmentStatus(@Param("orderId") Long orderId, @Param("fulfillmentStatus") String fulfillmentStatus,
            @Param("updateBy") String updateBy);

    int updateAfterSaleStatus(@Param("orderId") Long orderId, @Param("afterSaleStatus") String afterSaleStatus,
            @Param("updateBy") String updateBy);

    int updateExternalOrderId(@Param("orderId") Long orderId, @Param("externalOrderId") String externalOrderId);

    int markPaidAt(@Param("orderId") Long orderId);

    int markCancelled(@Param("orderId") Long orderId, @Param("reason") String reason);

    int markCompletedAt(@Param("orderId") Long orderId);

    Long selectTodaySalesAmount(@Param("shopId") Long shopId);

    int countTodayOrders(@Param("shopId") Long shopId);

    int countTodayPaid(@Param("shopId") Long shopId);

    int countPendingFulfillment(@Param("shopId") Long shopId);

    List<Map<String, Object>> countByStatus(@Param("shopId") Long shopId);

    List<Map<String, Object>> selectDailySales(@Param("shopId") Long shopId, @Param("days") int days);
}
