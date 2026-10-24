package com.ruoyi.commerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.Inventory;
import com.ruoyi.commerce.domain.InventoryTransaction;
import com.ruoyi.commerce.mapper.InventoryMapper;
import com.ruoyi.commerce.mapper.InventoryTransactionMapper;
import com.ruoyi.commerce.service.IInventoryService;

/**
 * 库存服务实现。
 *
 * @author ruoyi
 */
@Service
public class InventoryServiceImpl implements IInventoryService
{
    /** 非 Web 环境（如定时任务）的操作人 */
    private static final String SYSTEM_OPERATOR = "system";

    /** 订单业务类型 */
    private static final String BIZ_TYPE_ORDER = "ORDER";

    /** 手工业务类型 */
    private static final String BIZ_TYPE_MANUAL = "MANUAL";

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private InventoryTransactionMapper transactionMapper;

    @Override
    public List<Inventory> selectInventoryList(Inventory inventory)
    {
        return inventoryMapper.selectList(inventory);
    }

    @Override
    public Inventory selectInventoryById(Long inventoryId)
    {
        return inventoryMapper.selectById(inventoryId);
    }

    @Override
    public List<Inventory> selectInventoryBySkuId(Long skuId)
    {
        Inventory query = new Inventory();
        query.setSkuId(skuId);
        return inventoryMapper.selectList(query);
    }

    @Override
    public List<InventoryTransaction> selectTransactionList(InventoryTransaction transaction)
    {
        return transactionMapper.selectList(transaction);
    }

    @Override
    public int insertInventory(Inventory inventory)
    {
        if (inventory == null || inventory.getSkuId() == null || StringUtils.isEmpty(inventory.getWarehouseCode()))
        {
            throw new ServiceException("SKU 与仓库不能为空");
        }
        Inventory exists = inventoryMapper.selectBySkuAndWarehouse(inventory.getSkuId(), inventory.getWarehouseCode());
        if (exists != null)
        {
            throw new ServiceException("该 SKU 在仓库 " + inventory.getWarehouseCode() + " 的库存已存在");
        }
        if (inventory.getShopId() == null)
        {
            inventory.setShopId(1L);
        }
        if (inventory.getTotalQty() == null)
        {
            inventory.setTotalQty(0);
        }
        if (inventory.getLockedQty() == null)
        {
            inventory.setLockedQty(0);
        }
        if (inventory.getSafetyQty() == null)
        {
            inventory.setSafetyQty(0);
        }
        inventory.setVersion(0);
        inventory.setCreateBy(currentOperator());
        return inventoryMapper.insertInventory(inventory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String reserve(Long skuId, int quantity, String bizNo)
    {
        if (skuId == null || quantity <= 0)
        {
            throw new ServiceException("锁定库存参数不合法");
        }
        String operator = currentOperator();
        List<Inventory> candidates = inventoryMapper.selectAvailableBySkuId(skuId);
        if (candidates == null || candidates.isEmpty())
        {
            throw new ServiceException("SKU " + skuId + " 无可用库存");
        }
        for (Inventory candidate : candidates)
        {
            int available = candidate.getAvailableQty();
            if (available < quantity)
            {
                continue;
            }
            int rows = inventoryMapper.reserveQty(candidate.getInventoryId(), quantity, operator);
            if (rows == 0)
            {
                continue;
            }
            Inventory updated = inventoryMapper.selectById(candidate.getInventoryId());
            writeTransaction(updated, CommerceConstants.INV_RESERVE, quantity, available,
                    updated.getAvailableQty(), BIZ_TYPE_ORDER, bizNo, operator, null);
            return candidate.getWarehouseCode();
        }
        throw new ServiceException("SKU " + skuId + " 可用库存不足，需锁定 " + quantity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void release(Long skuId, String warehouseCode, int quantity, String bizNo)
    {
        Inventory inventory = requireInventory(skuId, warehouseCode);
        if (quantity <= 0)
        {
            throw new ServiceException("释放库存数量必须大于 0");
        }
        String operator = currentOperator();
        int before = inventory.getAvailableQty();
        int rows = inventoryMapper.releaseQty(inventory.getInventoryId(), quantity, operator);
        if (rows == 0)
        {
            throw new ServiceException("释放库存失败，锁定库存不足");
        }
        Inventory updated = inventoryMapper.selectById(inventory.getInventoryId());
        writeTransaction(updated, CommerceConstants.INV_RELEASE, quantity, before,
                updated.getAvailableQty(), BIZ_TYPE_ORDER, bizNo, operator, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deduct(Long skuId, String warehouseCode, int quantity, String bizNo)
    {
        Inventory inventory = requireInventory(skuId, warehouseCode);
        if (quantity <= 0)
        {
            throw new ServiceException("扣减库存数量必须大于 0");
        }
        String operator = currentOperator();
        int before = inventory.getAvailableQty();
        int rows = inventoryMapper.deductQty(inventory.getInventoryId(), quantity, operator);
        if (rows == 0)
        {
            throw new ServiceException("扣减库存失败，锁定库存或总库存不足");
        }
        Inventory updated = inventoryMapper.selectById(inventory.getInventoryId());
        writeTransaction(updated, CommerceConstants.INV_DEDUCT, quantity, before,
                updated.getAvailableQty(), BIZ_TYPE_ORDER, bizNo, operator, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restore(Long skuId, String warehouseCode, int quantity, String bizNo)
    {
        Inventory inventory = requireInventory(skuId, warehouseCode);
        if (quantity <= 0)
        {
            throw new ServiceException("恢复库存数量必须大于 0");
        }
        String operator = currentOperator();
        int before = inventory.getAvailableQty();
        int rows = inventoryMapper.restoreQty(inventory.getInventoryId(), quantity, operator);
        if (rows == 0)
        {
            throw new ServiceException("恢复库存失败");
        }
        Inventory updated = inventoryMapper.selectById(inventory.getInventoryId());
        writeTransaction(updated, CommerceConstants.INV_RESTORE, quantity, before,
                updated.getAvailableQty(), BIZ_TYPE_ORDER, bizNo, operator, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjust(Long skuId, String warehouseCode, int delta, String remark)
    {
        Inventory inventory = requireInventory(skuId, warehouseCode);
        if (delta == 0)
        {
            throw new ServiceException("调整数量不能为 0");
        }
        int currentTotal = inventory.getTotalQty() == null ? 0 : inventory.getTotalQty();
        int locked = inventory.getLockedQty() == null ? 0 : inventory.getLockedQty();
        int targetTotal = currentTotal + delta;
        if (targetTotal < 0)
        {
            throw new ServiceException("调整后总库存不能小于 0");
        }
        if (targetTotal < locked)
        {
            throw new ServiceException("调整后总库存不能小于锁定库存");
        }
        String operator = currentOperator();
        int before = inventory.getAvailableQty();
        int rows = inventoryMapper.adjustQty(inventory.getInventoryId(), targetTotal, operator);
        if (rows == 0)
        {
            throw new ServiceException("库存调整失败，记录可能已变更");
        }
        Inventory updated = inventoryMapper.selectById(inventory.getInventoryId());
        writeTransaction(updated, CommerceConstants.INV_ADJUST, delta, before,
                updated.getAvailableQty(), BIZ_TYPE_MANUAL, null, operator, remark);
    }

    private Inventory requireInventory(Long skuId, String warehouseCode)
    {
        if (skuId == null || StringUtils.isEmpty(warehouseCode))
        {
            throw new ServiceException("SKU 与仓库不能为空");
        }
        Inventory inventory = inventoryMapper.selectBySkuAndWarehouse(skuId, warehouseCode);
        if (inventory == null)
        {
            throw new ServiceException("未找到 SKU " + skuId + " 在仓库 " + warehouseCode + " 的库存");
        }
        return inventory;
    }

    private void writeTransaction(Inventory inventory, String txType, int quantity, int beforeQty, int afterQty,
            String bizType, String bizNo, String operator, String remark)
    {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setInventoryId(inventory.getInventoryId());
        transaction.setSkuId(inventory.getSkuId());
        transaction.setWarehouseCode(inventory.getWarehouseCode());
        transaction.setTxType(txType);
        transaction.setQuantity(quantity);
        transaction.setBeforeQty(beforeQty);
        transaction.setAfterQty(afterQty);
        transaction.setBizType(bizType);
        transaction.setBizNo(bizNo);
        transaction.setOperatorId(operator);
        transaction.setRemark(remark);
        transactionMapper.insertTransaction(transaction);
    }

    /**
     * 当前登录用户；非 Web 环境回退 system。
     */
    private String currentOperator()
    {
        try
        {
            String username = SecurityUtils.getUsername();
            return StringUtils.isEmpty(username) ? SYSTEM_OPERATOR : username;
        }
        catch (Exception e)
        {
            return SYSTEM_OPERATOR;
        }
    }
}
