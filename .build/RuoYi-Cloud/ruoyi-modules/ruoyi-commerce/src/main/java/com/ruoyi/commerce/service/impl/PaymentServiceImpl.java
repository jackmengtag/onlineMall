package com.ruoyi.commerce.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.Payment;
import com.ruoyi.commerce.domain.PaymentEvent;
import com.ruoyi.commerce.domain.Refund;
import com.ruoyi.commerce.domain.dto.PaymentCallbackRequest;
import com.ruoyi.commerce.mapper.PaymentEventMapper;
import com.ruoyi.commerce.mapper.PaymentMapper;
import com.ruoyi.commerce.mapper.RefundMapper;
import com.ruoyi.commerce.service.IFulfillmentService;
import com.ruoyi.commerce.service.IInventoryService;
import com.ruoyi.commerce.service.ILocalMessageService;
import com.ruoyi.commerce.service.IOrderService;
import com.ruoyi.commerce.service.IPaymentService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 支付服务实现
 *
 * <p>对应文档第 5.3 节：支付结果以服务端回调为准，回调幂等；支付成功推进订单并触发履约生成；
 * 第 10.2 节约束：退款金额不得超过订单实付金额。</p>
 *
 * @author ruoyi
 */
@Service
public class PaymentServiceImpl implements IPaymentService
{
    /** 支付超时时间（毫秒），30 分钟 */
    private static final long PAYMENT_EXPIRE_MILLIS = 30 * 60 * 1000L;

    /** 默认支付服务商 */
    private static final String DEFAULT_PROVIDER = "DEFAULT";

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PaymentEventMapper paymentEventMapper;

    @Autowired
    private RefundMapper refundMapper;

    @Lazy
    @Autowired
    private IOrderService orderService;

    @Lazy
    @Autowired
    private IFulfillmentService fulfillmentService;

    @Lazy
    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private ILocalMessageService localMessageService;

    @Override
    public Payment createPayment(Orders order)
    {
        Payment payment = new Payment();
        payment.setPaymentNo(CommerceNoUtils.paymentNo());
        payment.setOrderId(order.getOrderId());
        payment.setProvider(DEFAULT_PROVIDER);
        payment.setAmount(order.getTotalAmount());
        payment.setCurrencyCode(order.getCurrencyCode());
        payment.setPaymentStatus(CommerceConstants.PAYMENT_INITIATED);
        payment.setIdempotencyKey(CommerceNoUtils.idempotencyKey("PAY", order.getOrderNo()));
        payment.setExpiredAt(new Date(System.currentTimeMillis() + PAYMENT_EXPIRE_MILLIS));
        paymentMapper.insertPayment(payment);
        return payment;
    }

    @Override
    public Payment selectPaymentById(Long paymentId)
    {
        return paymentMapper.selectPaymentById(paymentId);
    }

    @Override
    public Payment selectPaymentByNo(String paymentNo)
    {
        return paymentMapper.selectPaymentByNo(paymentNo);
    }

    @Override
    public List<Payment> selectPaymentList(Payment payment)
    {
        return paymentMapper.selectPaymentList(payment);
    }

    @Override
    public List<PaymentEvent> selectPaymentEvents(Long paymentId)
    {
        return paymentEventMapper.selectPaymentEvents(paymentId);
    }

    @Override
    @Transactional
    public boolean handleCallback(PaymentCallbackRequest request)
    {
        Payment payment = paymentMapper.selectPaymentByNo(request.getPaymentNo());
        if (payment == null)
        {
            throw new ServiceException("支付单不存在");
        }

        // 幂等：同一服务商事件 ID 已处理过则直接返回
        PaymentEvent exists = paymentEventMapper.selectByProviderAndEventId(request.getProvider(), request.getProviderEventId());
        if (exists != null)
        {
            return false;
        }

        PaymentEvent event = new PaymentEvent();
        event.setPaymentId(payment.getPaymentId());
        event.setProvider(request.getProvider());
        event.setProviderEventId(request.getProviderEventId());
        event.setEventType(request.getEventType());
        event.setEventStatus(request.getEventStatus());
        event.setPayloadJson(StringUtils.isEmpty(request.getPayloadJson()) ? "{}" : request.getPayloadJson());
        paymentEventMapper.insertPaymentEvent(event);

        if (isSuccessEvent(request.getEventType()))
        {
            Payment update = new Payment();
            update.setPaymentId(payment.getPaymentId());
            update.setPaymentStatus(CommerceConstants.PAYMENT_SUCCEEDED);
            update.setPaidAt(DateUtils.getNowDate());
            update.setProviderPaymentId(request.getProviderPaymentId());
            paymentMapper.updatePayment(update);

            orderService.markOrderPaid(payment.getOrderId(), payment.getPaymentNo());
            localMessageService.publish(CommerceConstants.TOPIC_PAYMENT_SUCCEEDED, payment.getPaymentNo(), request);

            Orders order = orderService.selectOrdersById(payment.getOrderId());
            fulfillmentService.createFulfillmentForOrder(order);
        }
        else
        {
            Payment update = new Payment();
            update.setPaymentId(payment.getPaymentId());
            update.setPaymentStatus(CommerceConstants.PAYMENT_FAILED);
            paymentMapper.updatePayment(update);
        }

        PaymentEvent processed = new PaymentEvent();
        processed.setPaymentEventId(event.getPaymentEventId());
        processed.setProcessedAt(DateUtils.getNowDate());
        paymentEventMapper.updatePaymentEvent(processed);
        return true;
    }

    @Override
    public int markPaymentFailed(Long paymentId, String reason)
    {
        Payment update = new Payment();
        update.setPaymentId(paymentId);
        update.setPaymentStatus(CommerceConstants.PAYMENT_FAILED);
        return paymentMapper.updatePayment(update);
    }

    @Override
    @Transactional
    public Refund createRefund(Refund refund)
    {
        if (StringUtils.isEmpty(refund.getRefundNo()))
        {
            refund.setRefundNo(CommerceNoUtils.refundNo());
        }
        if (StringUtils.isEmpty(refund.getRefundStatus()))
        {
            refund.setRefundStatus(CommerceConstants.REFUND_REQUESTED);
        }
        Long paidAmount = paymentMapper.selectPaidAmountByOrderId(refund.getOrderId());
        Long refundedAmount = refundMapper.selectRefundedAmountByOrderId(refund.getOrderId());
        long paid = paidAmount == null ? 0L : paidAmount;
        long refunded = refundedAmount == null ? 0L : refundedAmount;
        long amount = refund.getAmount() == null ? 0L : refund.getAmount();
        if (amount + refunded > paid)
        {
            throw new ServiceException("退款金额超过订单实付金额");
        }
        refundMapper.insertRefund(refund);
        return refund;
    }

    @Override
    public Refund selectRefundById(Long refundId)
    {
        return refundMapper.selectRefundById(refundId);
    }

    @Override
    public List<Refund> selectRefundList(Refund refund)
    {
        return refundMapper.selectRefundList(refund);
    }

    @Override
    @Transactional
    public int completeRefund(Long refundId, String providerRefundId)
    {
        Refund refund = refundMapper.selectRefundById(refundId);
        if (refund == null)
        {
            throw new ServiceException("退款单不存在");
        }
        Refund update = new Refund();
        update.setRefundId(refundId);
        update.setRefundStatus(CommerceConstants.REFUND_SUCCEEDED);
        update.setProviderRefundId(providerRefundId);
        update.setCompletedAt(DateUtils.getNowDate());
        int rows = refundMapper.updateRefund(update);

        orderService.changeStatus(refund.getOrderId(), CommerceConstants.STATUS_TYPE_PAYMENT,
                CommerceConstants.PAY_REFUNDED, "退款完成", CommerceConstants.OPERATOR_SYSTEM, null);
        localMessageService.publish(CommerceConstants.TOPIC_REFUND_SUCCEEDED, refund.getRefundNo(), refund);
        return rows;
    }

    @Override
    public int failRefund(Long refundId, String reason)
    {
        Refund update = new Refund();
        update.setRefundId(refundId);
        update.setRefundStatus(CommerceConstants.REFUND_FAILED);
        update.setReason(reason);
        return refundMapper.updateRefund(update);
    }

    /**
     * 事件类型是否为支付成功
     */
    private boolean isSuccessEvent(String eventType)
    {
        if (StringUtils.isEmpty(eventType))
        {
            return false;
        }
        String upper = eventType.toUpperCase();
        return upper.contains("SUCCESS") || upper.contains("SUCCEEDED") || upper.contains("PAID");
    }
}
