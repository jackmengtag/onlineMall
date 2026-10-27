package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 履约明细 fulfillment_items（不可变记录）。
 *
 * @author ruoyi
 */
public class FulfillmentItem
{
    /** 履约明细主键 */
    private Long fulfillmentItemId;

    /** 履约单 */
    private Long fulfillmentId;

    /** 订单明细 */
    private Long orderItemId;

    /** 数量 */
    private Integer quantity;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 订单（联查字段） */
    private Long orderId;

    /** SKU（联查字段） */
    private Long skuId;

    /** SKU 编码（联查字段） */
    private String skuCode;

    /** 商品名称（联查字段） */
    private String productName;

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

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getSkuId()
    {
        return skuId;
    }

    public void setSkuId(Long skuId)
    {
        this.skuId = skuId;
    }

    public String getSkuCode()
    {
        return skuCode;
    }

    public void setSkuCode(String skuCode)
    {
        this.skuCode = skuCode;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }
}
