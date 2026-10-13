package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 履约单 fulfillment_orders
 *
 * <p>对应文档第 5.4 节：订单按仓库可用库存拆分为一个或多个履约单，
 * 履约单驱动仓库拣货、打包、发货并生成运单。</p>
 *
 * @author ruoyi
 */
public class FulfillmentOrder implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 履约单主键 */
    private Long fulfillmentId;

    /** 履约单号 */
    private String fulfillmentNo;

    /** 订单主键 */
    private Long orderId;

    /** 仓库编码 */
    private String warehouseCode;

    /** 履约状态 CREATED/ALLOCATED/PICKING/PACKED/SHIPPED/DELIVERED/CANCELLED */
    private String fulfillmentStatus;

    /** 拆分原因 */
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

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

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
