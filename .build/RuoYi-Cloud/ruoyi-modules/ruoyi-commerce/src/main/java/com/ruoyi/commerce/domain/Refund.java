package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;

/**
 * 退款单 refunds
 *
 * <p>对应文档第 5.3、10.2 节：退款金额不得超过订单实付金额；
 * refund_status 取值 REQUESTED/APPROVED/PROCESSING/SUCCEEDED/FAILED/REJECTED。</p>
 *
 * @author ruoyi
 */
public class Refund implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 退款单主键 */
    @Excel(name = "退款单主键", cellType = ColumnType.NUMERIC)
    private Long refundId;

    /** 退款单号 */
    @Excel(name = "退款单号")
    private String refundNo;

    /** 订单主键 */
    private Long orderId;

    /** 支付单主键 */
    private Long paymentId;

    /** 退款金额（最小货币单位） */
    @Excel(name = "退款金额", cellType = ColumnType.NUMERIC)
    private Long amount;

    /** 货币代码 */
    private String currencyCode;

    /** 退款状态 REQUESTED/APPROVED/PROCESSING/SUCCEEDED/FAILED/REJECTED */
    @Excel(name = "退款状态")
    private String refundStatus;

    /** 退款原因 */
    private String reason;

    /** 服务商退款 ID */
    private String providerRefundId;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date requestedAt;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completedAt;

    public Long getRefundId()
    {
        return refundId;
    }

    public void setRefundId(Long refundId)
    {
        this.refundId = refundId;
    }

    public String getRefundNo()
    {
        return refundNo;
    }

    public void setRefundNo(String refundNo)
    {
        this.refundNo = refundNo;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getPaymentId()
    {
        return paymentId;
    }

    public void setPaymentId(Long paymentId)
    {
        this.paymentId = paymentId;
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

    public String getRefundStatus()
    {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus)
    {
        this.refundStatus = refundStatus;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getProviderRefundId()
    {
        return providerRefundId;
    }

    public void setProviderRefundId(String providerRefundId)
    {
        this.providerRefundId = providerRefundId;
    }

    public Date getRequestedAt()
    {
        return requestedAt;
    }

    public void setRequestedAt(Date requestedAt)
    {
        this.requestedAt = requestedAt;
    }

    public Date getCompletedAt()
    {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt)
    {
        this.completedAt = completedAt;
    }
}
