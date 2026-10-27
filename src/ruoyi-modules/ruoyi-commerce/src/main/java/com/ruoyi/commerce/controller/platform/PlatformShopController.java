package com.ruoyi.commerce.controller.platform;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.commerce.domain.Shop;
import com.ruoyi.commerce.service.IPlatformService;

/**
 * 平台店铺管理。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/platform/shop")
public class PlatformShopController extends BaseController
{
    @Autowired
    private IPlatformService platformService;

    @RequiresPermissions("commerce:platform:shop:list")
    @GetMapping("/list")
    public TableDataInfo list(Shop shop)
    {
        return getDataTable(platformService.shops(shop));
    }

    @RequiresPermissions("commerce:platform:shop:query")
    @GetMapping("/{shopId}")
    public AjaxResult getInfo(@PathVariable Long shopId)
    {
        return success(platformService.shop(shopId));
    }

    @RequiresPermissions("commerce:platform:shop:add")
    @Log(title = "平台店铺", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Shop shop)
    {
        return toAjax(platformService.createShop(shop));
    }

    @RequiresPermissions("commerce:platform:shop:edit")
    @Log(title = "平台店铺", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Shop shop)
    {
        return toAjax(platformService.updateShop(shop));
    }

    @RequiresPermissions("commerce:platform:shop:remove")
    @Log(title = "平台店铺", businessType = BusinessType.DELETE)
    @DeleteMapping("/{shopIds}")
    public AjaxResult remove(@PathVariable Long[] shopIds)
    {
        return toAjax(platformService.deleteShops(shopIds));
    }

    @RequiresPermissions("commerce:platform:shop:status")
    @Log(title = "平台店铺", businessType = BusinessType.UPDATE)
    @PostMapping("/status")
    public AjaxResult changeStatus(@RequestParam Long shopId, @RequestParam String status)
    {
        return toAjax(platformService.changeStatus(shopId, status));
    }
}
