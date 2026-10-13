package com.ruoyi.commerce.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.Inventory;
import com.ruoyi.commerce.domain.InventoryTransaction;
import com.ruoyi.commerce.mapper.InventoryMapper;
import com.ruoyi.commerce.mapper.InventoryTransactionMapper;
import com.ruoyi.commerce.service.IInventoryService;

/**
 * 库存服务实现
 *
 * <p>对应文档 领域全景-库存中心与第 10.2 节：锁定、释放、扣减、回补均使用条件更新保证原子性，
 * 每次变更写入库存流水，防止超卖。</p>
 *
 * @author ruoyi
 */
@Service
public class InventoryServiceImpl implements IInventoryService
{
    /** 库存可用状态 */
    private static final String INVENTORY_STATUS_ACTIVE = "ACTIVE";

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private InventoryTransactionMapper inventoryTransactionMapper;

    @Override
    @Transactional
    public boolean lock(Long skuId, String warehouseCode, int quantity, String bizNo)
    {
        if (quantity <= 0)
        {
            return false;
        }
        ensureInventoryRow(skuId, warehouseCode);
        int rows = inventoryMapper.lockStock(skuId, warehouseCode, quantity);
        if (rows > 0)
        {
            recordTransaction(skuId, warehouseCode, CommerceConstants.INVENTORY_LOCK, quantity, bizNo, "锁定库存");
        }
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean unlock(Long skuId, String warehouseCode, int quantity, String bizNo)
    {
        if (quantity <= 0)
        {
            return false;
        }
        int rows = inventoryMapper.unlockStock(skuId, warehouseCode, quantity);
        if (rows > 0)
        {
            recordTransaction(skuId, warehouseCode, CommerceConstants.INVENTORY_UNLOCK, quantity, bizNo, "释放锁定库存");
        }
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean deduct(Long skuId, String warehouseCode, int quantity, String bizNo)
    {
        if (quantity <= 0)
        {
            return false;
        }
        int rows = inventoryMapper.deductStock(skuId, warehouseCode, quantity);
        if (rows > 0)
        {
            recordTransaction(skuId, warehouseCode, CommerceConstants.INVENTORY_DEDUCT, quantity, bizNo, "扣减库存");
        }
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean restock(Long skuId, String warehouseCode, int quantity, String bizNo)
    {
        if (quantity <= 0)
        {
            return false;
        }
        ensureInventoryRow(skuId, warehouseCode);
        int rows = inventoryMapper.restockStock(skuId, warehouseCode, quantity);
        if (rows > 0)
        {
            recordTransaction(skuId, warehouseCode, CommerceConstants.INVENTORY_RESTOCK, quantity, bizNo, "回补库存");
        }
        return rows > 0;
    }

    @Override
    public int getAvailable(Long skuId, String warehouseCode)
    {
        Inventory inventory = inventoryMapper.selectInventory(skuId, warehouseCode);
        if (inventory == null)
        {
            return 0;
        }
        int total = inventory.getTotalQuantity() == null ? 0 : inventory.getTotalQuantity();
        int locked = inventory.getLockedQuantity() == null ? 0 : inventory.getLockedQuantity();
        return total - locked;
    }

    @Override
    public String selectWarehouse(Long skuId, int quantity)
    {
        return inventoryMapper.selectAvailableWarehouse(skuId, quantity);
    }

    @Override
    public Inventory selectInventory(Long skuId, String warehouseCode)
    {
        return inventoryMapper.selectInventory(skuId, warehouseCode);
    }

    @Override
    public List<Inventory> selectInventoryList(Inventory inventory)
    {
        return inventoryMapper.selectInventoryList(inventory);
    }

    @Override
    public List<InventoryTransaction> selectTransactionList(Long skuId, String warehouseCode)
    {
        return inventoryTransactionMapper.selectTransactionList(skuId, warehouseCode);
    }

    @Override
    @Transactional
    public int adjust(Long skuId, String warehouseCode, int totalQuantity, String remark)
    {
        if (totalQuantity < 0)
        {
            throw new ServiceException("总库存不能为负数");
        }
        Inventory inventory = inventoryMapper.selectInventory(skuId, warehouseCode);
        int locked = 0;
        if (inventory == null)
        {
            ensureInventoryRow(skuId, warehouseCode);
        }
        else
        {
            locked = inventory.getLockedQuantity() == null ? 0 : inventory.getLockedQuantity();
        }
        if (totalQuantity < locked)
        {
            throw new ServiceException("总库存不能小于锁定库存");
        }
        int currentTotal = inventory == null || inventory.getTotalQuantity() == null ? 0 : inventory.getTotalQuantity();
        int rows = inventoryMapper.updateTotalQuantity(skuId, warehouseCode, totalQuantity);
        if (rows > 0)
        {
            recordTransaction(skuId, warehouseCode, CommerceConstants.INVENTORY_ADJUST, totalQuantity - currentTotal, null, remark);
        }
        return rows;
    }

    /**
     * 库存行不存在时自动建仓库存行（数量默认 0）
     */
    private void ensureInventoryRow(Long skuId, String warehouseCode)
    {
        Inventory exist = inventoryMapper.selectInventory(skuId, warehouseCode);
        if (exist != null)
        {
            return;
        }
        Inventory inventory = new Inventory();
        inventory.setSkuId(skuId);
        inventory.setWarehouseCode(warehouseCode);
        inventory.setTotalQuantity(0);
        inventory.setLockedQuantity(0);
        inventory.setSafetyStock(0);
        inventory.setStatus(INVENTORY_STATUS_ACTIVE);
        inventoryMapper.insertInventory(inventory);
    }

    /**
     * 写入库存流水
     */
    private void recordTransaction(Long skuId, String warehouseCode, String bizType, int quantity, String bizNo, String remark)
    {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setSkuId(skuId);
        transaction.setWarehouseCode(warehouseCode);
        transaction.setBizType(bizType);
        transaction.setQuantity(quantity);
        transaction.setBizNo(bizNo);
        transaction.setRemark(remark);
        inventoryTransactionMapper.insertTransaction(transaction);
    }
}
