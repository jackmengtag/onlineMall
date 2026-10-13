package com.ruoyi.commerce.domain.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;

/**
 * 支付回调请求
 *
 * <p>对应文档第 5.3 节：支付结果以服务端回调为准，回调必须幂等，重复回调不影响结果；
 * 支付事件表保存外部回调原始报文，便于追溯与重放。</p>
 *
 * @author ruoyi
 */
public class PaymentCallbackRequest implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 支付服务商 */
    @NotBlank(message = "支付服务商不能为空")
    private String provider;

    /** 服务商事件 ID（幂等去重键） */
    @NotBlank(message = "服务商事件 ID 不能为空")
    private String providerEventId;

    /** 支付单号 */
    private String paymentNo;

    /** 服务商支付 ID */
    private String providerPaymentId;

    /** 事件类型，如 PAYMENT_SUCCESS / PAYMENT_FAILED */
    @NotBlank(message = "事件类型不能为空")
    private String eventType;

    /** 事件状态 */
    private String eventStatus;

    /** 回调原始报文(JSON) */
    private String payloadJson;

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

    public String getPaymentNo()
    {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo)
    {
        this.paymentNo = paymentNo;
    }

    public String getProviderPaymentId()
    {
        return providerPaymentId;
    }

    public void setProviderPaymentId(String providerPaymentId)
    {
        this.providerPaymentId = providerPaymentId;
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
}
