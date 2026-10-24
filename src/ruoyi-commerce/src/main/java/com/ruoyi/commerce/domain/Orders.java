package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单 orders。
 *
 * @author ruoyi
 */
public class Orders extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private String orderNo;
    private Long shopId;
    private Long customerId;
    private String channelCode;
    private String externalOrderId;
    private String countryCode;
    private String currencyCode;
    private Long subtotalAmount;
    private Long discountAmount;
    private Long shippingAmount;
    private Long taxAmount;
    private Long totalAmount;
    private String orderStatus;
    private String paymentStatus;
    private String fulfillmentStatus;
    private String afterSaleStatus;
    private String couponCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date placedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paidAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelledAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completedAt;

    private String cancelReason;

    /** 展示用：客户名称（非数据库字段） */
    private transient String customerName;

    /** 展示用：店铺名称（非数据库字段） */
    private transient String shopName;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getShopId() { return shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getChannelCode() { return channelCode; }
    public void setChannelCode(String channelCode) { this.channelCode = channelCode; }
    public String getExternalOrderId() { return externalOrderId; }
    public void setExternalOrderId(String externalOrderId) { this.externalOrderId = externalOrderId; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Long getSubtotalAmount() { return subtotalAmount; }
    public void setSubtotalAmount(Long subtotalAmount) { this.subtotalAmount = subtotalAmount; }
    public Long getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Long discountAmount) { this.discountAmount = discountAmount; }
    public Long getShippingAmount() { return shippingAmount; }
    public void setShippingAmount(Long shippingAmount) { this.shippingAmount = shippingAmount; }
    public Long getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Long taxAmount) { this.taxAmount = taxAmount; }
    public Long getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Long totalAmount) { this.totalAmount = totalAmount; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getFulfillmentStatus() { return fulfillmentStatus; }
    public void setFulfillmentStatus(String fulfillmentStatus) { this.fulfillmentStatus = fulfillmentStatus; }
    public String getAfterSaleStatus() { return afterSaleStatus; }
    public void setAfterSaleStatus(String afterSaleStatus) { this.afterSaleStatus = afterSaleStatus; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public Date getPlacedAt() { return placedAt; }
    public void setPlacedAt(Date placedAt) { this.placedAt = placedAt; }
    public Date getPaidAt() { return paidAt; }
    public void setPaidAt(Date paidAt) { this.paidAt = paidAt; }
    public Date getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(Date cancelledAt) { this.cancelledAt = cancelledAt; }
    public Date getCompletedAt() { return completedAt; }
    public void setCompletedAt(Date completedAt) { this.completedAt = completedAt; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
}
