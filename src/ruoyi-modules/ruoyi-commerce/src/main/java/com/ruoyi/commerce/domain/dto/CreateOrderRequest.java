package com.ruoyi.commerce.domain.dto;

import java.util.List;

/**
 * 创建订单请求。
 */
public class CreateOrderRequest
{
    private Long customerId;
    private Long addressId;
    private Long shopId;
    private String countryCode;
    private String channelCode;
    private String currencyCode;
    private String couponCode;
    private Long shippingAmount;
    private String remark;
    private List<CreateOrderItemRequest> items;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }
    public Long getShopId() { return shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getChannelCode() { return channelCode; }
    public void setChannelCode(String channelCode) { this.channelCode = channelCode; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public Long getShippingAmount() { return shippingAmount; }
    public void setShippingAmount(Long shippingAmount) { this.shippingAmount = shippingAmount; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public List<CreateOrderItemRequest> getItems() { return items; }
    public void setItems(List<CreateOrderItemRequest> items) { this.items = items; }
}
