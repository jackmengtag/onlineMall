package com.ruoyi.commerce.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.Payment;
import com.ruoyi.commerce.domain.PaymentEvent;
import com.ruoyi.commerce.domain.dto.PaymentCallbackRequest;
import com.ruoyi.commerce.mapper.OrdersMapper;
import com.ruoyi.commerce.mapper.PaymentEventMapper;
import com.ruoyi.commerce.mapper.PaymentMapper;
import com.ruoyi.commerce.service.ILocalMessageService;
import com.ruoyi.commerce.service.IOrderService;
import com.ruoyi.commerce.service.IPaymentService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 支付服务实现。
 *
 * @author ruoyi
 */
@Service
public class PaymentServiceImpl implements IPaymentService
{
    private static final String DEFAULT_PROVIDER = "MOCK";

    private static final long PAYMENT_TIMEOUT_MILLIS = 30L * 60L * 1000L;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PaymentEventMapper paymentEventMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ILocalMessageService localMessageService;

    @Autowired
    @Lazy
    private IOrderService orderService;

    @Override
    public List<Payment> selectPaymentList(Payment payment)
    {
        return paymentMapper.selectPaymentList(payment);
    }

    @Override
    public Payment selectPaymentById(Long paymentId)
    {
        return paymentMapper.selectPaymentById(paymentId);
    }

    @Override
    public Payment selectPaymentByNo(String paymentNo)
    {
        return paymentMapper.selectByNo(paymentNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Payment createPayment(Long orderId, String provider, String method)
    {
        Orders order = orderId == null ? null : ordersMapper.selectOrderById(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在：" + orderId);
        }
        String payProvider = StringUtils.isEmpty(provider) ? DEFAULT_PROVIDER : provider;
        String idempotencyKey = "ORDER-" + orderId + "-" + payProvider;
        Payment existing = paymentMapper.selectByIdempotencyKey(idempotencyKey);
        if (existing != null)
        {
            return existing;
        }

        Payment payment = new Payment();
        payment.setPaymentNo(CommerceNoUtils.paymentNo());
        payment.setOrderId(orderId);
        payment.setProvider(payProvider);
        payment.setMethod(method);
        payment.setAmount(order.getTotalAmount());
        payment.setCurrencyCode(order.getCurrencyCode());
        payment.setPaymentStatus(CommerceConstants.PAYMENT_INITIATED);
        payment.setIdempotencyKey(idempotencyKey);
        payment.setExpiredAt(new Date(System.currentTimeMillis() + PAYMENT_TIMEOUT_MILLIS));
        payment.setCreateBy(currentUsername());
        paymentMapper.insertPayment(payment);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("paymentId", payment.getPaymentId());
        payload.put("paymentNo", payment.getPaymentNo());
        payload.put("orderId", orderId);
        payload.put("amount", payment.getAmount());
        localMessageService.record(CommerceConstants.TOPIC_PAYMENT, "PaymentInitiated", "PAYMENT",
                String.valueOf(payment.getPaymentId()), JSON.toJSONString(payload),
                "PAYMENT_INITIATED:" + payment.getPaymentNo());
        return payment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleCallback(PaymentCallbackRequest request)
    {
        if (request == null || StringUtils.isEmpty(request.getProvider())
                || StringUtils.isEmpty(request.getProviderEventId())
                || StringUtils.isEmpty(request.getPaymentNo()))
        {
            throw new ServiceException("支付回调参数不完整");
        }
        if (paymentEventMapper.countByProviderEvent(request.getProvider(), request.getProviderEventId()) > 0)
        {
            return;
        }
        Payment payment = paymentMapper.selectByNo(request.getPaymentNo());
        if (payment == null)
        {
            throw new ServiceException("支付单不存在：" + request.getPaymentNo());
        }
        if (request.getAmount() != null && !request.getAmount().equals(payment.getAmount()))
        {
            throw new ServiceException("支付回调金额与支付单不一致");
        }
        if (StringUtils.isNotEmpty(request.getCurrencyCode())
                && !request.getCurrencyCode().equalsIgnoreCase(payment.getCurrencyCode()))
        {
            throw new ServiceException("支付回调币种与支付单不一致");
        }

        Date now = new Date();
        String eventType = request.getEventType() == null ? "UNKNOWN" : request.getEventType();
        PaymentEvent event = new PaymentEvent();
        event.setPaymentId(payment.getPaymentId());
        event.setProvider(request.getProvider());
        event.setProviderEventId(request.getProviderEventId());
        event.setEventType(eventType);
        event.setEventStatus(eventType);
        event.setPayloadJson(request.getRawPayload());
        event.setReceivedAt(now);
        event.setProcessedAt(now);
        paymentEventMapper.insertPaymentEvent(event);

        String upperType = eventType.toUpperCase();
        boolean succeeded = upperType.contains("SUCCEED") || upperType.contains("SUCCESS")
                || upperType.contains("PAID");
        boolean failed = upperType.contains("FAIL") || upperType.contains("DECLIN");

        if (succeeded)
        {
            if (CommerceConstants.PAYMENT_SUCCEEDED.equals(payment.getPaymentStatus()))
            {
                return;
            }
            payment.setPaymentStatus(CommerceConstants.PAYMENT_SUCCEEDED);
            payment.setProviderPaymentId(request.getProviderEventId());
            payment.setPaidAt(now);
            paymentMapper.updatePayment(payment);
            orderService.markPaid(payment.getOrderId());
        }
        else if (failed)
        {
            if (CommerceConstants.PAYMENT_SUCCEEDED.equals(payment.getPaymentStatus()))
            {
                return;
            }
            payment.setPaymentStatus(CommerceConstants.PAYMENT_FAILED);
            paymentMapper.updatePayment(payment);
            orderService.markPaymentFailed(payment.getOrderId());
        }
    }

    @Override
    public List<PaymentEvent> selectPaymentEvents(Long paymentId)
    {
        return paymentEventMapper.selectByPaymentId(paymentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int mockSuccess(Long paymentId)
    {
        Payment payment = paymentId == null ? null : paymentMapper.selectPaymentById(paymentId);
        if (payment == null)
        {
            throw new ServiceException("支付单不存在：" + paymentId);
        }
        if (CommerceConstants.PAYMENT_SUCCEEDED.equals(payment.getPaymentStatus()))
        {
            return 0;
        }
        Date now = new Date();
        String eventId = "MOCK-" + paymentId;
        if (paymentEventMapper.countByProviderEvent(DEFAULT_PROVIDER, eventId) == 0)
        {
            PaymentEvent event = new PaymentEvent();
            event.setPaymentId(paymentId);
            event.setProvider(DEFAULT_PROVIDER);
            event.setProviderEventId(eventId);
            event.setEventType("PAYMENT_SUCCEEDED");
            event.setEventStatus(CommerceConstants.PAYMENT_SUCCEEDED);
            event.setPayloadJson("{\"mock\":true}");
            event.setReceivedAt(now);
            event.setProcessedAt(now);
            paymentEventMapper.insertPaymentEvent(event);
        }
        payment.setPaymentStatus(CommerceConstants.PAYMENT_SUCCEEDED);
        payment.setProviderPaymentId(eventId);
        payment.setPaidAt(now);
        paymentMapper.updatePayment(payment);
        orderService.markPaid(payment.getOrderId());
        return 1;
    }

    private String currentUsername()
    {
        try
        {
            return SecurityUtils.getUsername();
        }
        catch (Exception e)
        {
            return "system";
        }
    }
}
