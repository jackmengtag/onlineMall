package com.ruoyi.commerce.controller.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.commerce.service.IShopCenterService;
import com.ruoyi.commerce.service.IShopService;

/**
 * 店铺工作台。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/shop/dashboard")
public class ShopDashboardController extends BaseController
{
    @Autowired
    private IShopService shopService;

    @Autowired
    private IShopCenterService shopCenterService;

    @PreAuthorize("@ss.hasPermi('commerce:shop:dashboard:view')")
    @GetMapping("/summary")
    public AjaxResult summary()
    {
        return success(shopCenterService.dashboard(shopService.currentShopId()));
    }
}
