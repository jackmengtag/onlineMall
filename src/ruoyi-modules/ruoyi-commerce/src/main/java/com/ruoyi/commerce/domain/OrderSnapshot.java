package com.ruoyi.commerce.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单快照 order_snapshots（不可变）。
 *
 * @author ruoyi
 */
public class OrderSnapshot
{
    private Long snapshotId;
    private Long orderId;
    private String buyerName;
    private String buyerEmail;
    private String recipientName;
    private String recipientPhone;
    private String countryCode;
    private String stateCode;
    private String city;
    private String district;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String taxRuleVersion;
    private String exchangeRateVersion;
    private BigDecimal exchangeRate;
    private String rawAddressJson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getSnapshotId() { return snapshotId; }
    public void setSnapshotId(Long snapshotId) { this.snapshotId = snapshotId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getStateCode() { return stateCode; }
    public void setStateCode(String stateCode) { this.stateCode = stateCode; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getTaxRuleVersion() { return taxRuleVersion; }
    public void setTaxRuleVersion(String taxRuleVersion) { this.taxRuleVersion = taxRuleVersion; }
    public String getExchangeRateVersion() { return exchangeRateVersion; }
    public void setExchangeRateVersion(String exchangeRateVersion) { this.exchangeRateVersion = exchangeRateVersion; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public String getRawAddressJson() { return rawAddressJson; }
    public void setRawAddressJson(String rawAddressJson) { this.rawAddressJson = rawAddressJson; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
