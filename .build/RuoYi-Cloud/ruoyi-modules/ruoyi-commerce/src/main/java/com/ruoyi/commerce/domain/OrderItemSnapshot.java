package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单明细快照 order_item_snapshots
 *
 * <p>对应文档第 5.1 节：下单时复制商品、价格与申报信息快照，保证后续商品信息变更不影响历史订单。</p>
 *
 * @author ruoyi
 */
public class OrderItemSnapshot implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 快照主键 */
    private Long snapshotId;

    /** 订单明细主键 */
    private Long orderItemId;

    /** 商品名称 */
    private String productName;

    /** SKU 编码 */
    private String skuCode;

    /** 属性快照（JSON） */
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

    /** 单价（最小货币单位） */
    private Long unitPriceAmount;

    /** 币种 */
    private String currencyCode;

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

    public Long getOrderItemId()
    {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId)
    {
        this.orderItemId = orderItemId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getSkuCode()
    {
        return skuCode;
    }

    public void setSkuCode(String skuCode)
    {
        this.skuCode = skuCode;
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

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }
}
