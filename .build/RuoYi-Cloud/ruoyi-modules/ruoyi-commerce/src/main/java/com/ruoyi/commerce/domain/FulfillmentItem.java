package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 履约明细 fulfillment_items
 *
 * @author ruoyi
 */
public class FulfillmentItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 履约明细主键 */
    private Long fulfillmentItemId;

    /** 履约单主键 */
    private Long fulfillmentId;

    /** 订单明细主键 */
    private Long orderItemId;

    /** 履约数量 */
    private Integer quantity;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    public Long getFulfillmentItemId()
    {
        return fulfillmentItemId;
    }

    public void setFulfillmentItemId(Long fulfillmentItemId)
    {
        this.fulfillmentItemId = fulfillmentItemId;
    }

    public Long getFulfillmentId()
    {
        return fulfillmentId;
    }

    public void setFulfillmentId(Long fulfillmentId)
    {
        this.fulfillmentId = fulfillmentId;
    }

    public Long getOrderItemId()
    {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId)
    {
        this.orderItemId = orderItemId;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }
}
