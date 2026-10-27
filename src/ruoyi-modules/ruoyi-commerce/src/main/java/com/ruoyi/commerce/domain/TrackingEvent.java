package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 物流轨迹 tracking_events（不可变记录）。
 *
 * @author ruoyi
 */
public class TrackingEvent
{
    /** 轨迹主键 */
    private Long trackingEventId;

    /** 运单 */
    private Long shipmentId;

    /** 事件编码 */
    private String eventCode;

    /** 事件描述 */
    private String eventDescription;

    /** 位置 */
    private String location;

    /** 发生时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date occurredAt;

    /** 原始报文 */
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
