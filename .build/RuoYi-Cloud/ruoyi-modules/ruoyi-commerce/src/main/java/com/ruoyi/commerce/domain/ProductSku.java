package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;

/**
 * 商品 SKU 单元 product_sku
 *
 * @author ruoyi
 */
public class ProductSku implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** SKU 主键 */
    @Excel(name = "SKU主键", cellType = ColumnType.NUMERIC)
    private Long skuId;

    /** SPU 主键 */
    private Long spuId;

    /** SKU 编码 */
    @Excel(name = "SKU编码")
    private String skuCode;

    /** 条码 */
    private String barcode;

    /** 规格属性 JSON */
    private String attributesJson;

    /** 重量（克） */
    private Integer weightGrams;

    /** 长度（毫米） */
    private Integer lengthMm;

    /** 宽度（毫米） */
    private Integer widthMm;

    /** 高度（毫米） */
    private Integer heightMm;

    /** 原产国二字码 */
    private String originCountryCode;

    /** HS 编码 */
    private String hsCode;

    /** 材质 */
    private String material;

    /** 电池类型 */
    private String batteryType;

    /** 是否危险品 */
    private Boolean dangerousGoodsFlag;

    /** 状态 ACTIVE/INACTIVE/FROZEN */
    @Excel(name = "状态")
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
