package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单明细 order_items。
 *
 * @author ruoyi
 */
public class OrderItem
{
    private Long orderItemId;
    private Long orderId;
    private Long skuId;
    private String skuCode;
    private Integer quantity;
    private Long unitPriceAmount;
    private Long discountAmount;
    private Long taxAmount;
    private Long lineTotalAmount;
    private String currencyCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Long getUnitPriceAmount() { return unitPriceAmount; }
    public void setUnitPriceAmount(Long unitPriceAmount) { this.unitPriceAmount = unitPriceAmount; }
    public Long getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Long discountAmount) { this.discountAmount = discountAmount; }
    public Long getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Long taxAmount) { this.taxAmount = taxAmount; }
    public Long getLineTotalAmount() { return lineTotalAmount; }
    public void setLineTotalAmount(Long lineTotalAmount) { this.lineTotalAmount = lineTotalAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
