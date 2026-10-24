package com.ruoyi.commerce.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.Refund;

/**
 * 退款单 Mapper（买家端售后写入 + 运营后台售后管理）。
 *
 * @author ruoyi
 */
public interface RefundMapper
{
    List<Refund> selectRefundList(Refund refund);

    Refund selectRefundById(Long refundId);

    /**
     * 订单已占用退款金额（REQUESTED/APPROVED/PROCESSING/SUCCEEDED）。
     */
    Long sumRefundedByOrderId(Long orderId);

    /**
     * 新增退款单，同时兼容 Refund 实体与 Map 入参。
     */
    int insertRefund(Object refund);

    int updateRefund(Refund refund);

    List<Refund> selectByOrderId(Long orderId);

    /**
     * 买家售后列表（按客户订单聚合）。
     */
    List<Map<String, Object>> selectRefundsByCustomer(Long customerId);

    int updateOrderAfterSaleStatus(@Param("orderId") Long orderId, @Param("status") String status);

    /**
     * 近 days 天成功退款金额（按店铺）。
     */
    Long sumRefundedAmount(@Param("shopId") Long shopId, @Param("days") Integer days);
}
