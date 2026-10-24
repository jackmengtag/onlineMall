package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.Inventory;
import com.ruoyi.commerce.domain.InventoryTransaction;

/**
 * 库存服务。
 */
public interface IInventoryService
{
    List<Inventory> selectInventoryList(Inventory inventory);

    Inventory selectInventoryById(Long inventoryId);

    List<Inventory> selectInventoryBySkuId(Long skuId);

    List<InventoryTransaction> selectTransactionList(InventoryTransaction transaction);

    int insertInventory(Inventory inventory);

    String reserve(Long skuId, int quantity, String bizNo);

    void release(Long skuId, String warehouseCode, int quantity, String bizNo);

    void deduct(Long skuId, String warehouseCode, int quantity, String bizNo);

    void restore(Long skuId, String warehouseCode, int quantity, String bizNo);

    void adjust(Long skuId, String warehouseCode, int delta, String remark);
}
