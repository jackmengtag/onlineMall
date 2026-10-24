package com.ruoyi.commerce.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 购物车 cart。
 *
 * @author ruoyi
 */
public class Cart extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 购物车主键 */
    private Long cartId;

    /** 客户 */
    private Long customerId;

    /** 结算国家 */
    private String countryCode;

    /** 渠道 */
    private String channelCode;

    /** 币种 */
    private String currencyCode;

    public Long getCartId()
    {
        return cartId;
    }

    public void setCartId(Long cartId)
    {
        this.cartId = cartId;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
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

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("cartId", getCartId())
            .append("customerId", getCustomerId())
            .append("countryCode", getCountryCode())
            .append("channelCode", getChannelCode())
            .append("currencyCode", getCurrencyCode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
