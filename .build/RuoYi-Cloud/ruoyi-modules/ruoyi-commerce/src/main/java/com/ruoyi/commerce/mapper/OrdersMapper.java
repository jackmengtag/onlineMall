package com.ruoyi.commerce.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.commerce.domain.Orders;

/**
 * 订单主档数据层
 *
 * @author ruoyi
 */
public interface OrdersMapper
{
    /**
     * 查询订单
     */
    Orders selectOrdersById(Long orderId);

    /**
     * 按订单号查询订单
     */
    Orders selectOrdersByNo(String orderNo);

    /**
     * 查询订单列表
     */
    List<Orders> selectOrdersList(Orders orders);

    /**
     * 新增订单
     */
    int insertOrders(Orders orders);

    /**
     * 修改订单
     */
    int updateOrders(Orders orders);

    /**
     * 按状态类型更新订单对应的正交状态列
     *
     * @param orderId      订单主键
     * @param statusType   ORDER / PAYMENT / FULFILLMENT / AFTER_SALE
     * @param toStatus     目标状态
     * @param paidAt       支付时间（可空）
     * @param cancelledAt  取消时间（可空）
     * @param completedAt  完成时间（可空）
     * @return 影响行数
     */
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("statusType") String statusType,
            @Param("toStatus") String toStatus, @Param("paidAt") Date paidAt,
            @Param("cancelledAt") Date cancelledAt, @Param("completedAt") Date completedAt);

    /**
     * 删除订单
     */
    int deleteOrdersById(Long orderId);

    /**
     * 批量删除订单
     */
    int deleteOrdersByIds(Long[] orderIds);
}
