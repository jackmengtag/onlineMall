package com.ruoyi.commerce.domain;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品标准单元 product_spu。
 *
 * @author ruoyi
 */
public class ProductSpu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** SPU 主键 */
    private Long spuId;

    /** 所属店铺 */
    private Long shopId;

    /** SPU 编码 */
    private String spuCode;

    /** 品牌 ID */
    private Long brandId;

    /** 品牌名称 */
    private String brandName;

    /** 类目 ID */
    private Long categoryId;

    /** 类目键（前台筛选） */
    private String categoryKey;

    /** 类目名称 */
    private String categoryName;

    /** 商品名称 */
    private String defaultName;

    /** 英文名称 */
    private String nameEn;

    /** 描述 */
    private String defaultDescription;

    /** 状态 DRAFT/PUBLISHED/ON_SALE/SUSPENDED/OFF_SALE */
    private String status;

    /** 主图 */
    private String defaultImageUrl;

    /** 角标 */
    private String badge;

    /** 评分 */
    private BigDecimal rating;

    /** 评价数 */
    private Integer reviewCount;

    /** 销量 */
    private Integer soldCount;

    /** 卖点标签 JSON */
    private String tagsJson;

    /** 图片列表 JSON */
    private String imagesJson;

    /** 规格参数 JSON */
    private String specsJson;

    /** 颜色选项 JSON */
    private String colorsJson;

    /** 尺寸/规格选项 JSON */
    private String sizesJson;

    /** SKU 数量（列表联查，非表字段） */
    private Integer skuCount;

    /** SKU 列表（级联维护，非表字段） */
    private List<ProductSku> skus;

    public Long getSpuId()
    {
        return spuId;
    }

    public void setSpuId(Long spuId)
    {
        this.spuId = spuId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public String getSpuCode()
    {
        return spuCode;
    }

    public void setSpuCode(String spuCode)
    {
        this.spuCode = spuCode;
    }

    public Long getBrandId()
    {
        return brandId;
    }

    public void setBrandId(Long brandId)
    {
        this.brandId = brandId;
    }

    public String getBrandName()
    {
        return brandName;
    }

    public void setBrandName(String brandName)
    {
        this.brandName = brandName;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategoryKey()
    {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey)
    {
        this.categoryKey = categoryKey;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getDefaultName()
    {
        return defaultName;
    }

    public void setDefaultName(String defaultName)
    {
        this.defaultName = defaultName;
    }

    public String getNameEn()
    {
        return nameEn;
    }

    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }

    public String getDefaultDescription()
    {
        return defaultDescription;
    }

    public void setDefaultDescription(String defaultDescription)
    {
        this.defaultDescription = defaultDescription;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDefaultImageUrl()
    {
        return defaultImageUrl;
    }

    public void setDefaultImageUrl(String defaultImageUrl)
    {
        this.defaultImageUrl = defaultImageUrl;
    }

    public String getBadge()
    {
        return badge;
    }

    public void setBadge(String badge)
    {
        this.badge = badge;
    }

    public BigDecimal getRating()
    {
        return rating;
    }

    public void setRating(BigDecimal rating)
    {
        this.rating = rating;
    }

    public Integer getReviewCount()
    {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount)
    {
        this.reviewCount = reviewCount;
    }

    public Integer getSoldCount()
    {
        return soldCount;
    }

    public void setSoldCount(Integer soldCount)
    {
        this.soldCount = soldCount;
    }

    public String getTagsJson()
    {
        return tagsJson;
    }

    public void setTagsJson(String tagsJson)
    {
        this.tagsJson = tagsJson;
    }

    public String getImagesJson()
    {
        return imagesJson;
    }

    public void setImagesJson(String imagesJson)
    {
        this.imagesJson = imagesJson;
    }

    public String getSpecsJson()
    {
        return specsJson;
    }

    public void setSpecsJson(String specsJson)
    {
        this.specsJson = specsJson;
    }

    public String getColorsJson()
    {
        return colorsJson;
    }

    public void setColorsJson(String colorsJson)
    {
        this.colorsJson = colorsJson;
    }

    public String getSizesJson()
    {
        return sizesJson;
    }

    public void setSizesJson(String sizesJson)
    {
        this.sizesJson = sizesJson;
    }

    public Integer getSkuCount()
    {
        return skuCount;
    }

    public void setSkuCount(Integer skuCount)
    {
        this.skuCount = skuCount;
    }

    public List<ProductSku> getSkus()
    {
        return skus;
    }

    public void setSkus(List<ProductSku> skus)
    {
        this.skus = skus;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("spuId", getSpuId())
            .append("shopId", getShopId())
            .append("spuCode", getSpuCode())
            .append("defaultName", getDefaultName())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
