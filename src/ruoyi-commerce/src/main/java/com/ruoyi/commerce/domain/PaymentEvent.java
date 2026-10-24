package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 支付回调事件 payment_events（不可变）。
 *
 * @author ruoyi
 */
public class PaymentEvent
{
    private Long paymentEventId;
    private Long paymentId;
    private String provider;
    private String providerEventId;
    private String eventType;
    private String eventStatus;
    private String payloadJson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receivedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date processedAt;

    public Long getPaymentEventId() { return paymentEventId; }
    public void setPaymentEventId(Long paymentEventId) { this.paymentEventId = paymentEventId; }
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getProviderEventId() { return providerEventId; }
    public void setProviderEventId(String providerEventId) { this.providerEventId = providerEventId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getEventStatus() { return eventStatus; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }
    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
    public Date getReceivedAt() { return receivedAt; }
    public void setReceivedAt(Date receivedAt) { this.receivedAt = receivedAt; }
    public Date getProcessedAt() { return processedAt; }
    public void setProcessedAt(Date processedAt) { this.processedAt = processedAt; }
}
