package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;

/**
 * 库存流水 inventory_transaction
 *
 * <p>对应文档 领域全景-库存中心：记录每一次锁定、释放、扣减、回补与调整，
 * biz_type 取值 LOCK/UNLOCK/DEDUCT/RESTOCK/ADJUST。</p>
 *
 * @author ruoyi
 */
public class InventoryTransaction implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 流水主键 */
    private Long transactionId;

    /** SKU 主键 */
    @Excel(name = "SKU主键", cellType = ColumnType.NUMERIC)
    private Long skuId;

    /** 仓库编码 */
    @Excel(name = "仓库编码")
    private String warehouseCode;

    /** 业务类型 LOCK/UNLOCK/DEDUCT/RESTOCK/ADJUST */
    @Excel(name = "业务类型")
    private String bizType;

    /** 变更数量 */
    @Excel(name = "变更数量", cellType = ColumnType.NUMERIC)
    private Integer quantity;

    /** 业务单号 */
    @Excel(name = "业务单号")
    private String bizNo;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    public Long getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(Long transactionId)
    {
        this.transactionId = transactionId;
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

    public String getBizType()
    {
        return bizType;
    }

    public void setBizType(String bizType)
    {
        this.bizType = bizType;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public String getBizNo()
    {
        return bizNo;
    }

    public void setBizNo(String bizNo)
    {
        this.bizNo = bizNo;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
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
