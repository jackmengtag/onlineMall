package com.ruoyi.commerce.mapper;

import java.util.List;

import com.ruoyi.commerce.domain.InventoryTransaction;

/**
 * 库存流水 Mapper。
 *
 * @author ruoyi
 */
public interface InventoryTransactionMapper
{
    List<InventoryTransaction> selectList(InventoryTransaction transaction);

    int insertTransaction(InventoryTransaction transaction);
}
