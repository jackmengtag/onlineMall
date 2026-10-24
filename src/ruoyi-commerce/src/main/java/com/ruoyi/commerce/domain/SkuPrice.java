package com.ruoyi.commerce.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 区域/渠道/币种价格 sku_price。
 *
 * @author ruoyi
 */
public class SkuPrice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 价格主键 */
    private Long priceId;

    /** SKU 主键 */
    private Long skuId;

    /** 国家/地区 */
    private String countryCode;

    /** 渠道 */
    private String channelCode;

    /** 币种 */
    private String currencyCode;

    /** 划线价（最小货币单位） */
    private Long listAmount;

    /** 销售价（最小货币单位） */
    private Long saleAmount;

    /** 生效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveFrom;

    /** 失效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveTo;

    /** 状态 ACTIVE/INACTIVE */
    private String status;

    /** SKU 编码（联查，非表字段） */
    private String skuCode;

    /** SPU 名称（联查，非表字段） */
    private String spuName;

    public Long getPriceId()
    {
        return priceId;
    }

    public void setPriceId(Long priceId)
    {
        this.priceId = priceId;
    }

    public Long getSkuId()
    {
        return skuId;
    }

    public void setSkuId(Long skuId)
    {
        this.skuId = skuId;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getChannelCode()
    {
        return channelCode;
    }

    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public Long getListAmount()
    {
        return listAmount;
    }

    public void setListAmount(Long listAmount)
    {
        this.listAmount = listAmount;
    }

    public Long getSaleAmount()
    {
        return saleAmount;
    }

    public void setSaleAmount(Long saleAmount)
    {
        this.saleAmount = saleAmount;
    }

    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }

    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }

    public Date getEffectiveTo()
    {
        return effectiveTo;
    }

    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getSkuCode()
    {
        return skuCode;
    }

    public void setSkuCode(String skuCode)
    {
        this.skuCode = skuCode;
    }

    public String getSpuName()
    {
        return spuName;
    }

    public void setSpuName(String spuName)
    {
        this.spuName = spuName;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("priceId", getPriceId())
            .append("skuId", getSkuId())
            .append("countryCode", getCountryCode())
            .append("channelCode", getChannelCode())
            .append("currencyCode", getCurrencyCode())
            .append("saleAmount", getSaleAmount())
            .append("effectiveFrom", getEffectiveFrom())
            .append("effectiveTo", getEffectiveTo())
            .append("status", getStatus())
            .toString();
    }
}
