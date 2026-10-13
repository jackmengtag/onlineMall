package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 物流轨迹事件 tracking_events
 *
 * <p>occurredAt 为承运商回传的事件发生时间（客户端传入），receivedAt 为系统落库时间。</p>
 *
 * @author ruoyi
 */
public class TrackingEvent implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 轨迹事件主键 */
    private Long trackingEventId;

    /** 运单主键 */
    private Long shipmentId;

    /** 事件编码 */
    private String eventCode;

    /** 事件描述 */
    private String eventDescription;

    /** 发生地点 */
    private String location;

    /** 事件发生时间（客户端传入） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date occurredAt;

    /** 原始报文(JSON) */
    private String rawPayloadJson;

    /** 接收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receivedAt;

    public Long getTrackingEventId()
    {
        return trackingEventId;
    }

    public void setTrackingEventId(Long trackingEventId)
    {
        this.trackingEventId = trackingEventId;
    }

    public Long getShipmentId()
    {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId)
    {
        this.shipmentId = shipmentId;
    }

    public String getEventCode()
    {
        return eventCode;
    }

    public void setEventCode(String eventCode)
    {
        this.eventCode = eventCode;
    }

    public String getEventDescription()
    {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription)
    {
        this.eventDescription = eventDescription;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Date getOccurredAt()
    {
        return occurredAt;
    }

    public void setOccurredAt(Date occurredAt)
    {
        this.occurredAt = occurredAt;
    }

    public String getRawPayloadJson()
    {
        return rawPayloadJson;
    }

    public void setRawPayloadJson(String rawPayloadJson)
    {
        this.rawPayloadJson = rawPayloadJson;
    }

    public Date getReceivedAt()
    {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt)
    {
        this.receivedAt = receivedAt;
    }
}
