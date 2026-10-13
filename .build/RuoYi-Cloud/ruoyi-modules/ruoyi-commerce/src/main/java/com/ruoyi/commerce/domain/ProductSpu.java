package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;

/**
 * 商品标准单元主档 product_spu
 *
 * @author ruoyi
 */
public class ProductSpu implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** SPU 主键 */
    @Excel(name = "SPU主键", cellType = ColumnType.NUMERIC)
    private Long spuId;

    /** SPU 编码 */
    @Excel(name = "SPU编码")
    private String spuCode;

    /** 品牌 ID */
    private Long brandId;

    /** 类目 ID */
    private Long categoryId;

    /** 默认名称 */
    @Excel(name = "默认名称")
    private String defaultName;

    /** 默认描述 */
    private String defaultDescription;

    /** 状态 DRAFT/PUBLISHED/ON_SALE/SUSPENDED/OFF_SALE */
    @Excel(name = "状态")
    private String status;

    /** 默认主图 */
    private String defaultImageUrl;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getSpuId()
    {
        return spuId;
    }

    public void setSpuId(Long spuId)
    {
        this.spuId = spuId;
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

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getDefaultName()
    {
        return defaultName;
    }

    public void setDefaultName(String defaultName)
    {
        this.defaultName = defaultName;
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
