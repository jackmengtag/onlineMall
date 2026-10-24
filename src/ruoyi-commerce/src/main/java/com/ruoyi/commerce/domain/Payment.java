package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 支付单 payments。
 *
 * @author ruoyi
 */
public class Payment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long paymentId;
    private String paymentNo;
    private Long orderId;
    private String provider;
    private String providerPaymentId;
    private String method;
    private Long amount;
    private String currencyCode;
    private String paymentStatus;
    private String idempotencyKey;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paidAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiredAt;

    /** 展示用：订单号（非数据库字段） */
    private transient String orderNo;

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public String getPaymentNo() { return paymentNo; }
    public void setPaymentNo(String paymentNo) { this.paymentNo = paymentNo; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getProviderPaymentId() { return providerPaymentId; }
    public void setProviderPaymentId(String providerPaymentId) { this.providerPaymentId = providerPaymentId; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public Date getPaidAt() { return paidAt; }
    public void setPaidAt(Date paidAt) { this.paidAt = paidAt; }
    public Date getExpiredAt() { return expiredAt; }
    public void setExpiredAt(Date expiredAt) { this.expiredAt = expiredAt; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
}
