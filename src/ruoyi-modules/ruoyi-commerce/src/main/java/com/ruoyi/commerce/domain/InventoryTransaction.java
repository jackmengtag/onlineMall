package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 库存流水 inventory_transaction（不可变记录）。
 *
 * @author ruoyi
 */
public class InventoryTransaction
{
    /** 流水主键 */
    private Long txId;

    /** 库存主键 */
    private Long inventoryId;

    /** SKU */
    private Long skuId;

    /** 仓库编码 */
    private String warehouseCode;

    /** 流水类型：RESERVE/RELEASE/DEDUCT/RESTORE/ADJUST */
    private String txType;

    /** 变动数量 */
    private Integer quantity;

    /** 变动前可售 */
    private Integer beforeQty;

    /** 变动后可售 */
    private Integer afterQty;

    /** 业务类型 */
    private String bizType;

    /** 业务单号 */
    private String bizNo;

    /** 操作人 */
    private String operatorId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 备注 */
    private String remark;

    /** SKU 编码（联查字段） */
    private String skuCode;

    public Long getTxId()
    {
        return txId;
    }

    public void setTxId(Long txId)
    {
        this.txId = txId;
    }

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

    public String getTxType()
    {
        return txType;
    }

    public void setTxType(String txType)
    {
        this.txType = txType;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Integer getBeforeQty()
    {
        return beforeQty;
    }

    public void setBeforeQty(Integer beforeQty)
    {
        this.beforeQty = beforeQty;
    }

    public Integer getAfterQty()
    {
        return afterQty;
    }

    public void setAfterQty(Integer afterQty)
    {
        this.afterQty = afterQty;
    }

    public String getBizType()
    {
        return bizType;
    }

    public void setBizType(String bizType)
    {
        this.bizType = bizType;
    }

    public String getBizNo()
    {
        return bizNo;
    }

    public void setBizNo(String bizNo)
    {
        this.bizNo = bizNo;
    }

    public String getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(String operatorId)
    {
        this.operatorId = operatorId;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getSkuCode()
    {
        return skuCode;
    }

    public void setSkuCode(String skuCode)
    {
        this.skuCode = skuCode;
    }
}
