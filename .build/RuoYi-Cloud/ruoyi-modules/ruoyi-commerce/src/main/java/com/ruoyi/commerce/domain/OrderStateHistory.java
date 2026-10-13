package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单状态历史 order_state_histories
 *
 * <p>对应文档第 5.2 节：每次状态流转均落库记录，非法流转必须拒绝并记录。</p>
 *
 * @author ruoyi
 */
public class OrderStateHistory implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 历史主键 */
    private Long historyId;

    /** 订单主键 */
    private Long orderId;

    /** 原状态 */
    private String fromStatus;

    /** 目标状态 */
    private String toStatus;

    /** 状态类型 ORDER / PAYMENT / FULFILLMENT / AFTER_SALE */
    private String statusType;

    /** 变更原因 */
    private String reason;

    /** 操作者类型 */
    private String operatorType;

    /** 操作者标识 */
    private String operatorId;

    /** 发生时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date occurredAt;

    public Long getHistoryId()
    {
        return historyId;
    }

    public void setHistoryId(Long historyId)
    {
        this.historyId = historyId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getFromStatus()
    {
        return fromStatus;
    }

    public void setFromStatus(String fromStatus)
    {
        this.fromStatus = fromStatus;
    }

    public String getToStatus()
    {
        return toStatus;
    }

    public void setToStatus(String toStatus)
    {
        this.toStatus = toStatus;
    }

    public String getStatusType()
    {
        return statusType;
    }

    public void setStatusType(String statusType)
    {
        this.statusType = statusType;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getOperatorType()
    {
        return operatorType;
    }

    public void setOperatorType(String operatorType)
    {
        this.operatorType = operatorType;
    }

    public String getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(String operatorId)
    {
        this.operatorId = operatorId;
    }

    public Date getOccurredAt()
    {
        return occurredAt;
    }

    public void setOccurredAt(Date occurredAt)
    {
        this.occurredAt = occurredAt;
    }
}
