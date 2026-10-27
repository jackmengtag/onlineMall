package com.ruoyi.commerce.controller.admin;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.commerce.domain.Inventory;
import com.ruoyi.commerce.domain.InventoryTransaction;
import com.ruoyi.commerce.service.IInventoryService;

/**
 * 运营后台 · 库存管理。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/inventory")
public class InventoryAdminController extends BaseController
{
    @Autowired
    private IInventoryService inventoryService;

    /**
     * 库存列表。
     */
    @RequiresPermissions("commerce:admin:inventory:list")
    @GetMapping("/list")
    public TableDataInfo list(Inventory inventory)
    {
        startPage();
        List<Inventory> list = inventoryService.selectInventoryList(inventory);
        return getDataTable(list);
    }

    /**
     * 库存详情。
     */
    @RequiresPermissions("commerce:admin:inventory:query")
    @GetMapping("/{inventoryId}")
    public AjaxResult getInfo(@PathVariable Long inventoryId)
    {
        return success(inventoryService.selectInventoryById(inventoryId));
    }

    /**
     * 库存流水。
     */
    @RequiresPermissions("commerce:admin:inventory:list")
    @GetMapping("/transactions")
    public TableDataInfo transactions(InventoryTransaction transaction)
    {
        startPage();
        List<InventoryTransaction> list = inventoryService.selectTransactionList(transaction);
        return getDataTable(list);
    }

    /**
     * 新增库存。
     */
    @RequiresPermissions("commerce:admin:inventory:add")
    @Log(title = "库存管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Inventory inventory)
    {
        return toAjax(inventoryService.insertInventory(inventory));
    }

    /**
     * 手工调整库存。
     */
    @RequiresPermissions("commerce:admin:inventory:adjust")
    @Log(title = "库存调整", businessType = BusinessType.UPDATE)
    @PostMapping("/adjust")
    public AjaxResult adjust(@RequestParam("skuId") Long skuId,
            @RequestParam("warehouseCode") String warehouseCode,
            @RequestParam("delta") Integer delta,
            @RequestParam(value = "remark", required = false) String remark)
    {
        inventoryService.adjust(skuId, warehouseCode, delta == null ? 0 : delta, remark);
        return success();
    }
}
