package com.ruoyi.commerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.commerce.domain.Inventory;
import com.ruoyi.commerce.domain.InventoryTransaction;
import com.ruoyi.commerce.service.IInventoryService;

/**
 * 库存控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController extends BaseController
{
    @Autowired
    private IInventoryService inventoryService;

    @RequiresPermissions("commerce:inventory:list")
    @GetMapping("/list")
    public TableDataInfo list(Inventory inventory)
    {
        startPage();
        List<Inventory> list = inventoryService.selectInventoryList(inventory);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:inventory:query")
    @GetMapping("/{skuId}/{warehouseCode}")
    public AjaxResult getInfo(@PathVariable Long skuId, @PathVariable String warehouseCode)
    {
        return success(inventoryService.selectInventory(skuId, warehouseCode));
    }

    @RequiresPermissions("commerce:inventory:edit")
    @Log(title = "库存调整", businessType = BusinessType.UPDATE)
    @PostMapping("/adjust")
    public AjaxResult adjust(@RequestParam Long skuId, @RequestParam String warehouseCode,
                             @RequestParam Integer totalQuantity, @RequestParam(required = false) String remark)
    {
        return toAjax(inventoryService.adjust(skuId, warehouseCode, totalQuantity, remark));
    }

    @RequiresPermissions("commerce:inventory:list")
    @GetMapping("/transactions")
    public TableDataInfo transactions(@RequestParam(required = false) Long skuId,
                                      @RequestParam(required = false) String warehouseCode)
    {
        startPage();
        List<InventoryTransaction> list = inventoryService.selectTransactionList(skuId, warehouseCode);
        return getDataTable(list);
    }
}
