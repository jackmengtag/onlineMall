package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单明细 order_items
 *
 * @author ruoyi
 */
public class OrderItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 订单明细主键 */
    private Long orderItemId;

    /** 订单主键 */
    private Long orderId;

    /** SKU 主键 */
    private Long skuId;

    /** SKU 编码 */
    private String skuCode;

    /** 数量 */
    private Integer quantity;

    /** 单价（最小货币单位） */
    private Long unitPriceAmount;

    /** 明细优惠（最小货币单位） */
    private Long discountAmount;

    /** 明细税费（最小货币单位） */
    private Long taxAmount;

    /** 行小计（最小货币单位） */
    private Long lineTotalAmount;

    /** 币种 */
    private String currencyCode;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    public Long getOrderItemId()
    {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId)
    {
        this.orderItemId = orderItemId;
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

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Long getUnitPriceAmount()
    {
        return unitPriceAmount;
    }

    public void setUnitPriceAmount(Long unitPriceAmount)
    {
        this.unitPriceAmount = unitPriceAmount;
    }

    public Long getDiscountAmount()
    {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public Long getTaxAmount()
    {
        return taxAmount;
    }

    public void setTaxAmount(Long taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    public Long getLineTotalAmount()
    {
        return lineTotalAmount;
    }

    public void setLineTotalAmount(Long lineTotalAmount)
    {
        this.lineTotalAmount = lineTotalAmount;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
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
