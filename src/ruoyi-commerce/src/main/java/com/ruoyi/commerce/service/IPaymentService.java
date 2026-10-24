package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.Payment;
import com.ruoyi.commerce.domain.PaymentEvent;
import com.ruoyi.commerce.domain.dto.PaymentCallbackRequest;

/**
 * 支付服务。
 */
public interface IPaymentService
{
    List<Payment> selectPaymentList(Payment payment);

    Payment selectPaymentById(Long paymentId);

    Payment selectPaymentByNo(String paymentNo);

    Payment createPayment(Long orderId, String provider, String method);

    void handleCallback(PaymentCallbackRequest request);

    List<PaymentEvent> selectPaymentEvents(Long paymentId);

    int mockSuccess(Long paymentId);
}
