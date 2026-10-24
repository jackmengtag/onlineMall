package com.ruoyi.commerce.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.Payment;

/**
 * 支付单 Mapper。
 *
 * @author ruoyi
 */
public interface PaymentMapper
{
    List<Payment> selectPaymentList(Payment payment);

    Payment selectPaymentById(@Param("paymentId") Long paymentId);

    Payment selectByNo(@Param("paymentNo") String paymentNo);

    List<Payment> selectByOrderId(@Param("orderId") Long orderId);

    Payment selectByIdempotencyKey(@Param("idempotencyKey") String idempotencyKey);

    int insertPayment(Payment payment);

    int updatePayment(Payment payment);

    /**
     * 对账明细（支付单 + 订单）。
     */
    List<Map<String, Object>> selectReconciliationRows(@Param("statDate") String statDate);
}
