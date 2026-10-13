package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单快照 order_snapshots
 *
 * <p>对应文档第 5.1 节：下单时复制买家、收件地址、税费规则版本与汇率版本快照，保证历史订单可追溯。</p>
 *
 * @author ruoyi
 */
public class OrderSnapshot implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 快照主键 */
    private Long snapshotId;

    /** 订单主键 */
    private Long orderId;

    /** 买家名称 */
    private String buyerName;

    /** 买家邮箱 */
    private String buyerEmail;

    /** 收件人 */
    private String recipientName;

    /** 收件人电话 */
    private String recipientPhone;

    /** 国家/地区 */
    private String countryCode;

    /** 州/省 */
    private String stateCode;

    /** 城市 */
    private String city;

    /** 区/县 */
    private String district;

    /** 地址行 1 */
    private String addressLine1;

    /** 地址行 2 */
    private String addressLine2;

    /** 邮编 */
    private String postalCode;

    /** 税费规则版本 */
    private String taxRuleVersion;

    /** 汇率版本 */
    private String exchangeRateVersion;

    /** 汇率 */
    private BigDecimal exchangeRate;

    /** 地址原始报文（JSON） */
    private String rawAddressJson;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    public Long getSnapshotId()
    {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId)
    {
        this.snapshotId = snapshotId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getBuyerName()
    {
        return buyerName;
    }

    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail()
    {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail)
    {
        this.buyerEmail = buyerEmail;
    }

    public String getRecipientName()
    {
        return recipientName;
    }

    public void setRecipientName(String recipientName)
    {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone()
    {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone)
    {
        this.recipientPhone = recipientPhone;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getStateCode()
    {
        return stateCode;
    }

    public void setStateCode(String stateCode)
    {
        this.stateCode = stateCode;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getDistrict()
    {
        return district;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public String getAddressLine1()
    {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1)
    {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2()
    {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2)
    {
        this.addressLine2 = addressLine2;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getTaxRuleVersion()
    {
        return taxRuleVersion;
    }

    public void setTaxRuleVersion(String taxRuleVersion)
    {
        this.taxRuleVersion = taxRuleVersion;
    }

    public String getExchangeRateVersion()
    {
        return exchangeRateVersion;
    }

    public void setExchangeRateVersion(String exchangeRateVersion)
    {
        this.exchangeRateVersion = exchangeRateVersion;
    }

    public BigDecimal getExchangeRate()
    {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate)
    {
        this.exchangeRate = exchangeRate;
    }

    public String getRawAddressJson()
    {
        return rawAddressJson;
    }

    public void setRawAddressJson(String rawAddressJson)
    {
        this.rawAddressJson = rawAddressJson;
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
