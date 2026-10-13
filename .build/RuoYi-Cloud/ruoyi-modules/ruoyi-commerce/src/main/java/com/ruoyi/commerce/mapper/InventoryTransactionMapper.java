package com.ruoyi.commerce.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.commerce.domain.InventoryTransaction;

/**
 * 库存流水数据层
 *
 * @author ruoyi
 */
public interface InventoryTransactionMapper
{
    /**
     * 查询库存流水
     */
    List<InventoryTransaction> selectTransactionList(@Param("skuId") Long skuId, @Param("warehouseCode") String warehouseCode);

    /**
     * 新增库存流水
     */
    int insertTransaction(InventoryTransaction transaction);
}
