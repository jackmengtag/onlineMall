package com.ruoyi.commerce.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 购物车明细 cart_item。
 *
 * @author ruoyi
 */
public class CartItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细主键 */
    private Long cartItemId;

    /** 购物车 */
    private Long cartId;

    /** SKU */
    private Long skuId;

    /** SKU 编码 */
    private String skuCode;

    /** 数量 */
    private Integer quantity;

    /** 选中 */
    private Boolean selected;

    /** 商品名称（展示字段） */
    private String productName;

    /** 图片（展示字段） */
    private String imageUrl;

    /** 单价（展示字段） */
    private Long unitPriceAmount;

    /** 币种（展示字段） */
    private String currencyCode;

    public Long getCartItemId()
    {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId)
    {
        this.cartItemId = cartItemId;
    }

    public Long getCartId()
    {
        return cartId;
    }

    public void setCartId(Long cartId)
    {
        this.cartId = cartId;
    }

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

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Boolean getSelected()
    {
        return selected;
    }

    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public Long getUnitPriceAmount()
    {
        return unitPriceAmount;
    }

    public void setUnitPriceAmount(Long unitPriceAmount)
    {
        this.unitPriceAmount = unitPriceAmount;
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
            .append("cartItemId", getCartItemId())
            .append("cartId", getCartId())
            .append("skuId", getSkuId())
            .append("skuCode", getSkuCode())
            .append("quantity", getQuantity())
            .append("selected", getSelected())
            .toString();
    }
}
