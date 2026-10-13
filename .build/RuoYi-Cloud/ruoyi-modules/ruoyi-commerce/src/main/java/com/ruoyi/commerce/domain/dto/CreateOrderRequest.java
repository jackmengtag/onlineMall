package com.ruoyi.commerce.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * 下单请求
 *
 * <p>对应文档第 5.1 节下单流程：提交订单时生成订单主记录，并复制商品、价格、地址、
 * 税费规则与汇率快照；金额一律使用最小货币单位整数。</p>
 *
 * @author ruoyi
 */
public class CreateOrderRequest implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 客户主键（游客下单可为空） */
    private Long customerId;

    /** 渠道编码，默认 DIRECT */
    private String channelCode;

    /** 外部订单号（第三方渠道下单时使用） */
    private String externalOrderId;

    /** 国家/地区 */
    @NotBlank(message = "国家/地区不能为空")
    private String countryCode;

    /** 币种 */
    @NotBlank(message = "币种不能为空")
    private String currencyCode;

    /** 运费（最小货币单位） */
    private Long shippingAmount = 0L;

    /** 优惠金额（最小货币单位） */
    private Long discountAmount = 0L;

    /** 税费（最小货币单位） */
    private Long taxAmount = 0L;

    /** 汇率 */
    private BigDecimal exchangeRate;

    /** 税费规则版本 */
    private String taxRuleVersion;

    /** 汇率版本 */
    private String exchangeRateVersion;

    /** 买家名称 */
    private String buyerName;

    /** 买家邮箱 */
    private String buyerEmail;

    /** 收件人 */
    @NotBlank(message = "收件人不能为空")
    private String recipientName;

    /** 收件人电话 */
    private String recipientPhone;

    /** 州/省 */
    private String stateCode;

    /** 城市 */
    @NotBlank(message = "城市不能为空")
    private String city;

    /** 区/县 */
    private String district;

    /** 地址行 1 */
    @NotBlank(message = "地址行 1 不能为空")
    private String addressLine1;

    /** 地址行 2 */
    private String addressLine2;

    /** 邮编 */
    private String postalCode;

    /** 地址原始报文 */
    private String rawAddressJson;

    /** 订单明细 */
    @Valid
    @NotEmpty(message = "订单明细不能为空")
    private List<CreateOrderItemRequest> items;

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public String getChannelCode()
    {
        return channelCode;
    }

    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
    }

    public String getExternalOrderId()
    {
        return externalOrderId;
    }

    public void setExternalOrderId(String externalOrderId)
    {
        this.externalOrderId = externalOrderId;
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

    public Long getShippingAmount()
    {
        return shippingAmount;
    }

    public void setShippingAmount(Long shippingAmount)
    {
        this.shippingAmount = shippingAmount;
    }

    public Long getDiscountAmount()
    {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public Long getTaxAmount()
    {
        return taxAmount;
    }

    public void setTaxAmount(Long taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getExchangeRate()
    {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate)
    {
        this.exchangeRate = exchangeRate;
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

    public String getRawAddressJson()
    {
        return rawAddressJson;
    }

    public void setRawAddressJson(String rawAddressJson)
    {
        this.rawAddressJson = rawAddressJson;
    }

    public List<CreateOrderItemRequest> getItems()
    {
        return items;
    }

    public void setItems(List<CreateOrderItemRequest> items)
    {
        this.items = items;
    }
}
