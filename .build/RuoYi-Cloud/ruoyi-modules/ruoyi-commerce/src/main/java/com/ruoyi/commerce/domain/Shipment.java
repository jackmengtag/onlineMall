package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 运单 shipments
 *
 * @author ruoyi
 */
public class Shipment implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 运单主键 */
    private Long shipmentId;

    /** 运单号 */
    private String shipmentNo;

    /** 履约单主键 */
    private Long fulfillmentId;

    /** 承运商编码 */
    private String carrierCode;

    /** 物流追踪号 */
    private String trackingNo;

    /** 运单状态 CREATED/LABEL_PRINTED/IN_TRANSIT/DELIVERED/EXCEPTION/RETURNED */
    private String shipmentStatus;

    /** 发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shippedAt;

    /** 签收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveredAt;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getShipmentId()
    {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId)
    {
        this.shipmentId = shipmentId;
    }

    public String getShipmentNo()
    {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo)
    {
        this.shipmentNo = shipmentNo;
    }

    public Long getFulfillmentId()
    {
        return fulfillmentId;
    }

    public void setFulfillmentId(Long fulfillmentId)
    {
        this.fulfillmentId = fulfillmentId;
    }

    public String getCarrierCode()
    {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode)
    {
        this.carrierCode = carrierCode;
    }

    public String getTrackingNo()
    {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo)
    {
        this.trackingNo = trackingNo;
    }

    public String getShipmentStatus()
    {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus)
    {
        this.shipmentStatus = shipmentStatus;
    }

    public Date getShippedAt()
    {
        return shippedAt;
    }

    public void setShippedAt(Date shippedAt)
    {
        this.shippedAt = shippedAt;
    }

    public Date getDeliveredAt()
    {
        return deliveredAt;
    }

    public void setDeliveredAt(Date deliveredAt)
    {
        this.deliveredAt = deliveredAt;
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
