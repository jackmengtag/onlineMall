package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 履约单 fulfillment_orders。
 *
 * @author ruoyi
 */
public class FulfillmentOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 履约单主键 */
    private Long fulfillmentId;

    /** 履约单号 */
    private String fulfillmentNo;

    /** 店铺 */
    private Long shopId;

    /** 订单 */
    private Long orderId;

    /** 仓库编码 */
    private String warehouseCode;

    /** 履约状态 */
    private String fulfillmentStatus;

    /** 分配原因 */
    private String allocationReason;

    /** 预计发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date estimatedShipAt;

    /** 实际发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualShipAt;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completedAt;

    public Long getFulfillmentId()
    {
        return fulfillmentId;
    }

    public void setFulfillmentId(Long fulfillmentId)
    {
        this.fulfillmentId = fulfillmentId;
    }

    public String getFulfillmentNo()
    {
        return fulfillmentNo;
    }

    public void setFulfillmentNo(String fulfillmentNo)
    {
        this.fulfillmentNo = fulfillmentNo;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getWarehouseCode()
    {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode)
    {
        this.warehouseCode = warehouseCode;
    }

    public String getFulfillmentStatus()
    {
        return fulfillmentStatus;
    }

    public void setFulfillmentStatus(String fulfillmentStatus)
    {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    public String getAllocationReason()
    {
        return allocationReason;
    }

    public void setAllocationReason(String allocationReason)
    {
        this.allocationReason = allocationReason;
    }

    public Date getEstimatedShipAt()
    {
        return estimatedShipAt;
    }

    public void setEstimatedShipAt(Date estimatedShipAt)
    {
        this.estimatedShipAt = estimatedShipAt;
    }

    public Date getActualShipAt()
    {
        return actualShipAt;
    }

    public void setActualShipAt(Date actualShipAt)
    {
        this.actualShipAt = actualShipAt;
    }

    public Date getCompletedAt()
    {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt)
    {
        this.completedAt = completedAt;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("fulfillmentId", getFulfillmentId())
            .append("fulfillmentNo", getFulfillmentNo())
            .append("shopId", getShopId())
            .append("orderId", getOrderId())
            .append("warehouseCode", getWarehouseCode())
            .append("fulfillmentStatus", getFulfillmentStatus())
            .append("allocationReason", getAllocationReason())
            .append("estimatedShipAt", getEstimatedShipAt())
            .append("actualShipAt", getActualShipAt())
            .append("completedAt", getCompletedAt())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
