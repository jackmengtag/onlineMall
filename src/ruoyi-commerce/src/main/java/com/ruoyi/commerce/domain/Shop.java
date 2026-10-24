package com.ruoyi.commerce.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 店铺 shop。
 *
 * @author ruoyi
 */
public class Shop extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 店铺主键 */
    private Long shopId;

    /** 店铺编码 */
    private String shopCode;

    /** 店铺名称 */
    private String shopName;

    /** 店铺 Logo */
    private String logoUrl;

    /** 联系人 */
    private String contactName;

    /** 联系邮箱 */
    private String contactEmail;

    /** 国家/地区 */
    private String countryCode;

    /** 结算币种 */
    private String currencyCode;

    /** 类型：SELF 自营 / THIRD_PARTY 三方 */
    private String shopType;

    /** 平台佣金比例 */
    private BigDecimal commissionRate;

    /** 状态：ACTIVE/SUSPENDED/CLOSED */
    private String status;

    /** 已结算金额（最小货币单位） */
    private Long settledAmount;

    /** 待结算金额（最小货币单位） */
    private Long pendingAmount;

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public String getShopCode()
    {
        return shopCode;
    }

    public void setShopCode(String shopCode)
    {
        this.shopCode = shopCode;
    }

    public String getShopName()
    {
        return shopName;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }

    public String getLogoUrl()
    {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl)
    {
        this.logoUrl = logoUrl;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getContactEmail()
    {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public String getShopType()
    {
        return shopType;
    }

    public void setShopType(String shopType)
    {
        this.shopType = shopType;
    }

    public BigDecimal getCommissionRate()
    {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate)
    {
        this.commissionRate = commissionRate;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Long getSettledAmount()
    {
        return settledAmount;
    }

    public void setSettledAmount(Long settledAmount)
    {
        this.settledAmount = settledAmount;
    }

    public Long getPendingAmount()
    {
        return pendingAmount;
    }

    public void setPendingAmount(Long pendingAmount)
    {
        this.pendingAmount = pendingAmount;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("shopId", getShopId())
            .append("shopCode", getShopCode())
            .append("shopName", getShopName())
            .append("countryCode", getCountryCode())
            .append("currencyCode", getCurrencyCode())
            .append("shopType", getShopType())
            .append("commissionRate", getCommissionRate())
            .append("status", getStatus())
            .append("settledAmount", getSettledAmount())
            .append("pendingAmount", getPendingAmount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
