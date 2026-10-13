package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.Payment;

/**
 * 支付单数据层
 *
 * @author ruoyi
 */
public interface PaymentMapper
{
    /**
     * 查询支付单
     */
    Payment selectPaymentById(Long paymentId);

    /**
     * 按支付单号查询
     */
    Payment selectPaymentByNo(String paymentNo);

    /**
     * 查询支付单列表
     */
    List<Payment> selectPaymentList(Payment payment);

    /**
     * 新增支付单
     */
    int insertPayment(Payment payment);

    /**
     * 修改支付单
     */
    int updatePayment(Payment payment);

    /**
     * 统计订单已成功支付金额
     */
    Long selectPaidAmountByOrderId(Long orderId);
}
