package com.ruoyi.commerce.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 多仓库存 inventory。
 *
 * @author ruoyi
 */
public class Inventory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 库存主键 */
    private Long inventoryId;

    /** 店铺 */
    private Long shopId;

    /** SKU */
    private Long skuId;

    /** 仓库编码 */
    private String warehouseCode;

    /** 仓库国家 */
    private String countryCode;

    /** 总库存 */
    private Integer totalQty;

    /** 锁定库存 */
    private Integer lockedQty;

    /** 安全库存 */
    private Integer safetyQty;

    /** 乐观锁版本 */
    private Integer version;

    /** SKU 编码（联查字段） */
    private String skuCode;

    /** 商品名称（联查字段） */
    private String spuName;

    public Long getInventoryId()
    {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId)
    {
        this.inventoryId = inventoryId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Long getSkuId()
    {
        return skuId;
    }

    public void setSkuId(Long skuId)
    {
        this.skuId = skuId;
    }

    public String getWarehouseCode()
    {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode)
    {
        this.warehouseCode = warehouseCode;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public Integer getTotalQty()
    {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty)
    {
        this.totalQty = totalQty;
    }

    public Integer getLockedQty()
    {
        return lockedQty;
    }

    public void setLockedQty(Integer lockedQty)
    {
        this.lockedQty = lockedQty;
    }

    public Integer getSafetyQty()
    {
        return safetyQty;
    }

    public void setSafetyQty(Integer safetyQty)
    {
        this.safetyQty = safetyQty;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public String getSkuCode()
    {
        return skuCode;
    }

    public void setSkuCode(String skuCode)
    {
        this.skuCode = skuCode;
    }

    public String getSpuName()
    {
        return spuName;
    }

    public void setSpuName(String spuName)
    {
        this.spuName = spuName;
    }

    /**
     * 可售库存 = 总库存 - 锁定库存。
     */
    public Integer getAvailableQty()
    {
        int total = totalQty == null ? 0 : totalQty;
        int locked = lockedQty == null ? 0 : lockedQty;
        return total - locked;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("inventoryId", getInventoryId())
            .append("shopId", getShopId())
            .append("skuId", getSkuId())
            .append("warehouseCode", getWarehouseCode())
            .append("countryCode", getCountryCode())
            .append("totalQty", getTotalQty())
            .append("lockedQty", getLockedQty())
            .append("safetyQty", getSafetyQty())
            .append("version", getVersion())
            .append("skuCode", getSkuCode())
            .append("spuName", getSpuName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
