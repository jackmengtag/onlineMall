package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.Inventory;

/**
 * 库存服务
 *
 * <p>对应文档 领域全景-库存中心：可分库存、锁定、扣减、回补与跨仓调拨；
 * 以及第 10.2 节关键业务约束：锁定扣减分离，防止超卖。</p>
 *
 * @author ruoyi
 */
public interface IInventoryService
{
    /**
     * 锁定库存（下单时）
     *
     * @param skuId         SKU 主键
     * @param warehouseCode 仓库编码
     * @param quantity      数量
     * @param bizNo         业务单号（订单号）
     * @return 是否锁定成功
     */
    boolean lock(Long skuId, String warehouseCode, int quantity, String bizNo);

    /**
     * 释放锁定库存（订单取消/超时）
     */
    boolean unlock(Long skuId, String warehouseCode, int quantity, String bizNo);

    /**
     * 扣减库存（发货时，锁定转为实际扣减）
     */
    boolean deduct(Long skuId, String warehouseCode, int quantity, String bizNo);

    /**
     * 回补库存（退货入库）
     */
    boolean restock(Long skuId, String warehouseCode, int quantity, String bizNo);

    /**
     * 查询可用库存（总量 - 锁定）
     */
    int getAvailable(Long skuId, String warehouseCode);

    /**
     * 选择有可用库存的仓库（按可用量降序）
     *
     * @return 仓库编码，无可用库存返回 null
     */
    String selectWarehouse(Long skuId, int quantity);

    /**
     * 查询单条库存
     */
    Inventory selectInventory(Long skuId, String warehouseCode);

    /**
     * 查询库存列表
     */
    List<Inventory> selectInventoryList(Inventory inventory);

    /**
     * 查询库存流水
     */
    List<com.ruoyi.commerce.domain.InventoryTransaction> selectTransactionList(Long skuId, String warehouseCode);

    /**
     * 库存调整（人工盘点/初始化）
     */
    int adjust(Long skuId, String warehouseCode, int totalQuantity, String remark);
}
