package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;

/**
 * 库存 inventory
 *
 * <p>对应文档 领域全景-库存中心与第 10.2 节：可分库存 = total_quantity - locked_quantity，
 * 锁定与扣减分离，通过条件更新防止超卖。</p>
 *
 * @author ruoyi
 */
public class Inventory implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 库存主键 */
    @Excel(name = "库存主键", cellType = ColumnType.NUMERIC)
    private Long inventoryId;

    /** SKU 主键 */
    @Excel(name = "SKU主键", cellType = ColumnType.NUMERIC)
    private Long skuId;

    /** 仓库编码 */
    @Excel(name = "仓库编码")
    private String warehouseCode;

    /** 总库存 */
    @Excel(name = "总库存", cellType = ColumnType.NUMERIC)
    private Integer totalQuantity;

    /** 锁定库存 */
    @Excel(name = "锁定库存", cellType = ColumnType.NUMERIC)
    private Integer lockedQuantity;

    /** 安全库存 */
    private Integer safetyStock;

    /** 状态 ACTIVE/FROZEN */
    @Excel(name = "状态")
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getInventoryId()
    {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId)
    {
        this.inventoryId = inventoryId;
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

    public Integer getTotalQuantity()
    {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity)
    {
        this.totalQuantity = totalQuantity;
    }

    public Integer getLockedQuantity()
    {
        return lockedQuantity;
    }

    public void setLockedQuantity(Integer lockedQuantity)
    {
        this.lockedQuantity = lockedQuantity;
    }

    public Integer getSafetyStock()
    {
        return safetyStock;
    }

    public void setSafetyStock(Integer safetyStock)
    {
        this.safetyStock = safetyStock;
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
