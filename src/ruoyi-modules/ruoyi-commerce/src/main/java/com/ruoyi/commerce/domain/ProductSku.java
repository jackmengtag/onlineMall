package com.ruoyi.commerce.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 可售库存单元 product_sku。
 *
 * @author ruoyi
 */
public class ProductSku extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** SKU 主键 */
    private Long skuId;

    /** 所属 SPU */
    private Long spuId;

    /** SKU 编码 */
    private String skuCode;

    /** 条码 */
    private String barcode;

    /** 销售属性 JSON */
    private String attributesJson;

    /** 重量（克） */
    private Integer weightGrams;

    /** 长（毫米） */
    private Integer lengthMm;

    /** 宽（毫米） */
    private Integer widthMm;

    /** 高（毫米） */
    private Integer heightMm;

    /** 原产国 */
    private String originCountryCode;

    /** HS 编码 */
    private String hsCode;

    /** 材质 */
    private String material;

    /** 电池类型 */
    private String batteryType;

    /** 是否危险品 */
    private Boolean dangerousGoodsFlag;

    /** 申报品名 */
    private String declaredName;

    /** 申报价值 */
    private Long declaredValueAmount;

    /** 申报币种 */
    private String declaredCurrencyCode;

    /** 状态 ACTIVE/INACTIVE/FROZEN */
    private String status;

    /** SPU 名称（联查，非表字段） */
    private String spuName;

    public Long getSkuId()
    {
        return skuId;
    }

    public void setSkuId(Long skuId)
    {
        this.skuId = skuId;
    }

    public Long getSpuId()
    {
        return spuId;
    }

    public void setSpuId(Long spuId)
    {
        this.spuId = spuId;
    }

    public String getSkuCode()
    {
        return skuCode;
    }

    public void setSkuCode(String skuCode)
    {
        this.skuCode = skuCode;
    }

    public String getBarcode()
    {
        return barcode;
    }

    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }

    public String getAttributesJson()
    {
        return attributesJson;
    }

    public void setAttributesJson(String attributesJson)
    {
        this.attributesJson = attributesJson;
    }

    public Integer getWeightGrams()
    {
        return weightGrams;
    }

    public void setWeightGrams(Integer weightGrams)
    {
        this.weightGrams = weightGrams;
    }

    public Integer getLengthMm()
    {
        return lengthMm;
    }

    public void setLengthMm(Integer lengthMm)
    {
        this.lengthMm = lengthMm;
    }

    public Integer getWidthMm()
    {
        return widthMm;
    }

    public void setWidthMm(Integer widthMm)
    {
        this.widthMm = widthMm;
    }

    public Integer getHeightMm()
    {
        return heightMm;
    }

    public void setHeightMm(Integer heightMm)
    {
        this.heightMm = heightMm;
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

    public String getMaterial()
    {
        return material;
    }

    public void setMaterial(String material)
    {
        this.material = material;
    }

    public String getBatteryType()
    {
        return batteryType;
    }

    public void setBatteryType(String batteryType)
    {
        this.batteryType = batteryType;
    }

    public Boolean getDangerousGoodsFlag()
    {
        return dangerousGoodsFlag;
    }

    public void setDangerousGoodsFlag(Boolean dangerousGoodsFlag)
    {
        this.dangerousGoodsFlag = dangerousGoodsFlag;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
            .append("skuId", getSkuId())
            .append("spuId", getSpuId())
            .append("skuCode", getSkuCode())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
