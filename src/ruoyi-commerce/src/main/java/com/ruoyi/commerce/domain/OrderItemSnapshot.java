package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单明细快照 order_item_snapshots（不可变）。
 *
 * @author ruoyi
 */
public class OrderItemSnapshot
{
    private Long snapshotId;
    private Long orderItemId;
    private String productName;
    private String skuCode;
    private String attributesJson;
    private String imageUrl;
    private String originCountryCode;
    private String hsCode;
    private String declaredName;
    private Long declaredValueAmount;
    private String declaredCurrencyCode;
    private Long unitPriceAmount;
    private String currencyCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getSnapshotId() { return snapshotId; }
    public void setSnapshotId(Long snapshotId) { this.snapshotId = snapshotId; }
    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }
    public String getAttributesJson() { return attributesJson; }
    public void setAttributesJson(String attributesJson) { this.attributesJson = attributesJson; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getOriginCountryCode() { return originCountryCode; }
    public void setOriginCountryCode(String originCountryCode) { this.originCountryCode = originCountryCode; }
    public String getHsCode() { return hsCode; }
    public void setHsCode(String hsCode) { this.hsCode = hsCode; }
    public String getDeclaredName() { return declaredName; }
    public void setDeclaredName(String declaredName) { this.declaredName = declaredName; }
    public Long getDeclaredValueAmount() { return declaredValueAmount; }
    public void setDeclaredValueAmount(Long declaredValueAmount) { this.declaredValueAmount = declaredValueAmount; }
    public String getDeclaredCurrencyCode() { return declaredCurrencyCode; }
    public void setDeclaredCurrencyCode(String declaredCurrencyCode) { this.declaredCurrencyCode = declaredCurrencyCode; }
    public Long getUnitPriceAmount() { return unitPriceAmount; }
    public void setUnitPriceAmount(Long unitPriceAmount) { this.unitPriceAmount = unitPriceAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
