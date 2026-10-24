package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.Inventory;

/**
 * 库存 Mapper。
 *
 * @author ruoyi
 */
public interface InventoryMapper
{
    List<Inventory> selectList(Inventory inventory);

    Inventory selectById(Long inventoryId);

    Inventory selectBySkuAndWarehouse(@Param("skuId") Long skuId, @Param("warehouseCode") String warehouseCode);

    List<Inventory> selectAvailableBySkuId(Long skuId);

    int insertInventory(Inventory inventory);

    int reserveQty(@Param("inventoryId") Long inventoryId, @Param("quantity") int quantity,
            @Param("updateBy") String updateBy);

    int releaseQty(@Param("inventoryId") Long inventoryId, @Param("quantity") int quantity,
            @Param("updateBy") String updateBy);

    int deductQty(@Param("inventoryId") Long inventoryId, @Param("quantity") int quantity,
            @Param("updateBy") String updateBy);

    int restoreQty(@Param("inventoryId") Long inventoryId, @Param("quantity") int quantity,
            @Param("updateBy") String updateBy);

    int adjustQty(@Param("inventoryId") Long inventoryId, @Param("totalQty") int totalQty,
            @Param("updateBy") String updateBy);
}
