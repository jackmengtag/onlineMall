package com.ruoyi.commerce.controller.shop;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.commerce.domain.Inventory;
import com.ruoyi.commerce.service.IInventoryService;
import com.ruoyi.commerce.service.IShopService;

/**
 * 店铺中心 · 库存查询。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/shop/inventory")
public class ShopInventoryController extends BaseController
{
    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private IShopService shopService;

    /**
     * 当前店铺库存列表。
     */
    @RequiresPermissions("commerce:shop:product:list")
    @GetMapping("/list")
    public TableDataInfo list(Inventory inventory)
    {
        Long shopId = shopService.currentShopId();
        if (shopId == null)
        {
            throw new ServiceException("当前用户未绑定店铺");
        }
        inventory.setShopId(shopId);
        startPage();
        List<Inventory> list = inventoryService.selectInventoryList(inventory);
        return getDataTable(list);
    }
}
