package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.Payment;
import com.ruoyi.commerce.domain.Refund;
import com.ruoyi.commerce.domain.dto.PaymentCallbackRequest;

/**
 * 支付服务
 *
 * <p>对应文档第 5.3 节：支付单独立建模，一个订单允许多次支付尝试；支付结果以服务端回调为准，
 * 回调必须幂等；支付事件表保存外部回调原始报文；覆盖支付失败、超时、退款与拒付等异常分支。</p>
 *
 * @author ruoyi
 */
public interface IPaymentService
{
    /**
     * 创建支付单（下单时）
     *
     * @param order 订单
     * @return 支付单
     */
    Payment createPayment(Orders order);

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
     * 查询支付事件
     */
    List<com.ruoyi.commerce.domain.PaymentEvent> selectPaymentEvents(Long paymentId);

    /**
     * 处理支付回调（幂等：同一服务商事件 ID 重复回调不影响结果）
     *
     * @param request 回调请求
     * @return 是否首次处理成功
     */
    boolean handleCallback(PaymentCallbackRequest request);

    /**
     * 标记支付失败
     */
    int markPaymentFailed(Long paymentId, String reason);

    /**
     * 创建退款单
     */
    Refund createRefund(Refund refund);

    /**
     * 查询退款单
     */
    Refund selectRefundById(Long refundId);

    /**
     * 查询退款单列表
     */
    List<Refund> selectRefundList(Refund refund);

    /**
     * 退款成功（推进订单退款状态并回补库存）
     */
    int completeRefund(Long refundId, String providerRefundId);

    /**
     * 退款失败
     */
    int failRefund(Long refundId, String reason);
}
