package com.ruoyi.commerce.controller.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.commerce.service.IShopCenterService;
import com.ruoyi.commerce.service.IShopService;

/**
 * 店铺客户。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/shop/customer")
public class ShopCustomerController extends BaseController
{
    @Autowired
    private IShopService shopService;

    @Autowired
    private IShopCenterService shopCenterService;

    @PreAuthorize("@ss.hasPermi('commerce:shop:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String keyword)
    {
        return getDataTable(shopCenterService.customers(shopService.currentShopId(), keyword));
    }
}
