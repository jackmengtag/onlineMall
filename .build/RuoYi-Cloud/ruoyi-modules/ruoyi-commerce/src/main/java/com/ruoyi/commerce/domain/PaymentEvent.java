package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;

/**
 * 支付事件 payment_events
 *
 * <p>对应文档第 5.3 节：保存外部支付回调原始报文，便于追溯与重放；
 * 以 provider + provider_event_id 作为回调幂等去重键。</p>
 *
 * @author ruoyi
 */
public class PaymentEvent implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 支付事件主键 */
    private Long paymentEventId;

    /** 支付单主键 */
    private Long paymentId;

    /** 支付服务商 */
    @Excel(name = "支付服务商")
    private String provider;

    /** 服务商事件 ID */
    private String providerEventId;

    /** 事件类型 */
    @Excel(name = "事件类型")
    private String eventType;

    /** 事件状态 */
    private String eventStatus;

    /** 回调原始报文(JSON) */
    private String payloadJson;

    /** 接收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receivedAt;

    /** 处理完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date processedAt;

    public Long getPaymentEventId()
    {
        return paymentEventId;
    }

    public void setPaymentEventId(Long paymentEventId)
    {
        this.paymentEventId = paymentEventId;
    }

    public Long getPaymentId()
    {
        return paymentId;
    }

    public void setPaymentId(Long paymentId)
    {
        this.paymentId = paymentId;
    }

    public String getProvider()
    {
        return provider;
    }

    public void setProvider(String provider)
    {
        this.provider = provider;
    }

    public String getProviderEventId()
    {
        return providerEventId;
    }

    public void setProviderEventId(String providerEventId)
    {
        this.providerEventId = providerEventId;
    }

    public String getEventType()
    {
        return eventType;
    }

    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    public String getEventStatus()
    {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus)
    {
        this.eventStatus = eventStatus;
    }

    public String getPayloadJson()
    {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson)
    {
        this.payloadJson = payloadJson;
    }

    public Date getReceivedAt()
    {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt)
    {
        this.receivedAt = receivedAt;
    }

    public Date getProcessedAt()
    {
        return processedAt;
    }

    public void setProcessedAt(Date processedAt)
    {
        this.processedAt = processedAt;
    }
}
