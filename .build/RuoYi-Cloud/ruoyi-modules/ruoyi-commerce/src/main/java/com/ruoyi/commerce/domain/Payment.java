package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;

/**
 * 支付单 payments
 *
 * <p>对应文档第 5.3 节：支付单独立建模，一个订单允许多次支付尝试；
 * payment_status 取值 INITIATED/PENDING/AUTHORIZED/SUCCEEDED/FAILED/CANCELLED/EXPIRED。</p>
 *
 * @author ruoyi
 */
public class Payment implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 支付单主键 */
    @Excel(name = "支付单主键", cellType = ColumnType.NUMERIC)
    private Long paymentId;

    /** 支付单号 */
    @Excel(name = "支付单号")
    private String paymentNo;

    /** 订单主键 */
    private Long orderId;

    /** 支付服务商 */
    @Excel(name = "支付服务商")
    private String provider;

    /** 服务商支付 ID */
    private String providerPaymentId;

    /** 支付方式 */
    private String method;

    /** 支付金额（最小货币单位） */
    @Excel(name = "支付金额", cellType = ColumnType.NUMERIC)
    private Long amount;

    /** 货币代码 */
    private String currencyCode;

    /** 支付状态 INITIATED/PENDING/AUTHORIZED/SUCCEEDED/FAILED/CANCELLED/EXPIRED */
    @Excel(name = "支付状态")
    private String paymentStatus;

    /** 幂等键 */
    private String idempotencyKey;

    /** 支付完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paidAt;

    /** 支付超时时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiredAt;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getPaymentId()
    {
        return paymentId;
    }

    public void setPaymentId(Long paymentId)
    {
        this.paymentId = paymentId;
    }

    public String getPaymentNo()
    {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo)
    {
        this.paymentNo = paymentNo;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getProvider()
    {
        return provider;
    }

    public void setProvider(String provider)
    {
        this.provider = provider;
    }

    public String getProviderPaymentId()
    {
        return providerPaymentId;
    }

    public void setProviderPaymentId(String providerPaymentId)
    {
        this.providerPaymentId = providerPaymentId;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public Long getAmount()
    {
        return amount;
    }

    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public String getPaymentStatus()
    {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }

    public String getIdempotencyKey()
    {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey)
    {
        this.idempotencyKey = idempotencyKey;
    }

    public Date getPaidAt()
    {
        return paidAt;
    }

    public void setPaidAt(Date paidAt)
    {
        this.paidAt = paidAt;
    }

    public Date getExpiredAt()
    {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt)
    {
        this.expiredAt = expiredAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
