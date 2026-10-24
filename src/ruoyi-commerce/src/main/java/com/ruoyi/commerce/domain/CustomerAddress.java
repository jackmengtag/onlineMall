package com.ruoyi.commerce.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 收货地址 customer_address。
 *
 * @author ruoyi
 */
public class CustomerAddress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地址主键 */
    private Long addressId;

    /** 客户 */
    private Long customerId;

    /** 收件人 */
    private String recipientName;

    /** 电话 */
    private String recipientPhone;

    /** 国家 */
    private String countryCode;

    /** 州/省 */
    private String stateCode;

    /** 城市 */
    private String city;

    /** 区县 */
    private String district;

    /** 地址 1 */
    private String addressLine1;

    /** 地址 2 */
    private String addressLine2;

    /** 邮编 */
    private String postalCode;

    /** 默认地址 */
    private Boolean isDefault;

    public Long getAddressId()
    {
        return addressId;
    }

    public void setAddressId(Long addressId)
    {
        this.addressId = addressId;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
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

    public Boolean getIsDefault()
    {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault)
    {
        this.isDefault = isDefault;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("addressId", getAddressId())
            .append("customerId", getCustomerId())
            .append("recipientName", getRecipientName())
            .append("countryCode", getCountryCode())
            .append("city", getCity())
            .append("isDefault", getIsDefault())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
