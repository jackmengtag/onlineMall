package com.ruoyi.commerce.controller.shop;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.commerce.service.IShopCenterService;
import com.ruoyi.commerce.service.IShopService;

/**
 * 店铺结算单。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/shop/settlement")
public class ShopSettlementController extends BaseController
{
    @Autowired
    private IShopService shopService;

    @Autowired
    private IShopCenterService shopCenterService;

    @RequiresPermissions("commerce:shop:settlement:list")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String status)
    {
        return getDataTable(shopCenterService.settlements(shopService.currentShopId(), status));
    }

    @RequiresPermissions("commerce:shop:settlement:generate")
    @Log(title = "店铺结算", businessType = BusinessType.INSERT)
    @PostMapping("/generate")
    public AjaxResult generate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodStart,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodEnd)
    {
        return success(shopCenterService.generateSettlement(shopService.currentShopId(), periodStart, periodEnd));
    }

    @RequiresPermissions("commerce:shop:settlement:pay")
    @Log(title = "店铺结算", businessType = BusinessType.UPDATE)
    @PostMapping("/pay/{settlementId}")
    public AjaxResult pay(@PathVariable Long settlementId)
    {
        return toAjax(shopCenterService.markSettlementPaid(shopService.currentShopId(), settlementId));
    }
}
