package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 运单 shipments。
 *
 * @author ruoyi
 */
public class Shipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 运单主键 */
    private Long shipmentId;

    /** 运单号 */
    private String shipmentNo;

    /** 履约单 */
    private Long fulfillmentId;

    /** 承运商 */
    private String carrierCode;

    /** 物流单号 */
    private String trackingNo;

    /** 运单状态 */
    private String shipmentStatus;

    /** 发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shippedAt;

    /** 签收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveredAt;

    /** 订单（联查字段） */
    private Long orderId;

    /** 店铺（联查字段） */
    private Long shopId;

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

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("shipmentId", getShipmentId())
            .append("shipmentNo", getShipmentNo())
            .append("fulfillmentId", getFulfillmentId())
            .append("carrierCode", getCarrierCode())
            .append("trackingNo", getTrackingNo())
            .append("shipmentStatus", getShipmentStatus())
            .append("shippedAt", getShippedAt())
            .append("deliveredAt", getDeliveredAt())
            .append("orderId", getOrderId())
            .append("shopId", getShopId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
