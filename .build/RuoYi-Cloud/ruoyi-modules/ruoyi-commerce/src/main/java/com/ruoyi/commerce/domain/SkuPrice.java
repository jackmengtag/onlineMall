package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;

/**
 * SKU 价格 sku_price
 *
 * <p>同一 (sku_id, country_code, channel_code, currency_code) 下的生效区间不得重叠，
 * 对应文档 8.1 节“价格有效期使用排他约束防止重叠”，MySQL 无排他约束，改由服务层校验。</p>
 *
 * @author ruoyi
 */
public class SkuPrice implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 价格主键 */
    @Excel(name = "价格主键", cellType = ColumnType.NUMERIC)
    private Long priceId;

    /** SKU 主键 */
    private Long skuId;

    /** 国家二字码 */
    private String countryCode;

    /** 渠道编码 */
    private String channelCode;

    /** 币种三字码 */
    private String currencyCode;

    /** 划线价（最小货币单位） */
    private Long listAmount;

    /** 销售价（最小货币单位） */
    private Long saleAmount;

    /** 生效开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveFrom;

    /** 生效结束时间（NULL 表示无穷大） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveTo;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

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

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }
}
