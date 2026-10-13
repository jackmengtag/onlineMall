package com.ruoyi.commerce.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.commerce.domain.Inventory;

/**
 * 库存数据层
 *
 * <p>锁定、释放、扣减均使用条件更新（where 中带数量校验），由数据库保证原子性，防止超卖。</p>
 *
 * @author ruoyi
 */
public interface InventoryMapper
{
    /**
     * 锁定库存：需满足总库存 - 已锁定库存 >= 数量
     */
    int lockStock(@Param("skuId") Long skuId, @Param("warehouseCode") String warehouseCode, @Param("quantity") int quantity);

    /**
     * 释放锁定库存：需满足已锁定库存 >= 数量
     */
    int unlockStock(@Param("skuId") Long skuId, @Param("warehouseCode") String warehouseCode, @Param("quantity") int quantity);

    /**
     * 扣减库存：锁定转实际扣减，需满足已锁定库存 >= 数量且总库存 >= 数量
     */
    int deductStock(@Param("skuId") Long skuId, @Param("warehouseCode") String warehouseCode, @Param("quantity") int quantity);

    /**
     * 回补库存：退货入库
     */
    int restockStock(@Param("skuId") Long skuId, @Param("warehouseCode") String warehouseCode, @Param("quantity") int quantity);

    /**
     * 直接设置总库存（库存调整）
     */
    int updateTotalQuantity(@Param("skuId") Long skuId, @Param("warehouseCode") String warehouseCode, @Param("totalQuantity") int totalQuantity);

    /**
     * 查询单条库存
     */
    Inventory selectInventory(@Param("skuId") Long skuId, @Param("warehouseCode") String warehouseCode);

    /**
     * 选择有可用库存的仓库（按可用量降序）
     */
    String selectAvailableWarehouse(@Param("skuId") Long skuId, @Param("quantity") int quantity);

    /**
     * 查询库存列表
     */
    List<Inventory> selectInventoryList(Inventory inventory);

    /**
     * 新增库存行
     */
    int insertInventory(Inventory inventory);
}
