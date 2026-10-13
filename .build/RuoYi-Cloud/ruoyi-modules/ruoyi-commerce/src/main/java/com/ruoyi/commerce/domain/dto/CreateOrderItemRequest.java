package com.ruoyi.commerce.domain.dto;

import java.io.Serializable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 下单明细请求
 *
 * <p>下单时由渠道/商品域解析出的商品与价格信息，用于生成订单明细及其快照。</p>
 *
 * @author ruoyi
 */
public class CreateOrderItemRequest implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** SKU 主键 */
    private Long skuId;

    /** SKU 编码 */
    @NotBlank(message = "SKU 编码不能为空")
    private String skuCode;

    /** 商品名称（快照用） */
    @NotBlank(message = "商品名称不能为空")
    private String productName;

    /** 数量 */
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于 0")
    private Integer quantity;

    /** 单价（最小货币单位） */
    @NotNull(message = "单价不能为空")
    private Long unitPriceAmount;

    /** 明细优惠 */
    private Long discountAmount = 0L;

    /** 明细税费 */
    private Long taxAmount = 0L;

    /** 属性快照(JSON) */
    private String attributesJson;

    /** 图片快照 */
    private String imageUrl;

    /** 原产国 */
    private String originCountryCode;

    /** HS 编码 */
    private String hsCode;

    /** 申报品名 */
    private String declaredName;

    /** 申报价值 */
    private Long declaredValueAmount;

    /** 申报币种 */
    private String declaredCurrencyCode;

    public Long getSkuId()
    {
        return skuId;
    }

    public void setSkuId(Long skuId)
    {
        this.skuId = skuId;
    }

    public String getSkuCode()
    {
        return skuCode;
    }

    public void setSkuCode(String skuCode)
    {
        this.skuCode = skuCode;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Long getUnitPriceAmount()
    {
        return unitPriceAmount;
    }

    public void setUnitPriceAmount(Long unitPriceAmount)
    {
        this.unitPriceAmount = unitPriceAmount;
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

    public String getAttributesJson()
    {
        return attributesJson;
    }

    public void setAttributesJson(String attributesJson)
    {
        this.attributesJson = attributesJson;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getOriginCountryCode()
    {
        return originCountryCode;
    }

    public void setOriginCountryCode(String originCountryCode)
    {
        this.originCountryCode = originCountryCode;
    }

    public String getHsCode()
    {
        return hsCode;
    }

    public void setHsCode(String hsCode)
    {
        this.hsCode = hsCode;
    }

    public String getDeclaredName()
    {
        return declaredName;
    }

    public void setDeclaredName(String declaredName)
    {
        this.declaredName = declaredName;
    }

    public Long getDeclaredValueAmount()
    {
        return declaredValueAmount;
    }

    public void setDeclaredValueAmount(Long declaredValueAmount)
    {
        this.declaredValueAmount = declaredValueAmount;
    }

    public String getDeclaredCurrencyCode()
    {
        return declaredCurrencyCode;
    }

    public void setDeclaredCurrencyCode(String declaredCurrencyCode)
    {
        this.declaredCurrencyCode = declaredCurrencyCode;
    }
}
