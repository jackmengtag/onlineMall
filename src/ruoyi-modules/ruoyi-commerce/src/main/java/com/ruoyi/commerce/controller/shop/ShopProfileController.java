package com.ruoyi.commerce.controller.shop;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.commerce.domain.Shop;
import com.ruoyi.commerce.service.IShopCenterService;
import com.ruoyi.commerce.service.IShopService;

/**
 * 店铺资料。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/shop/profile")
public class ShopProfileController extends BaseController
{
    @Autowired
    private IShopService shopService;

    @Autowired
    private IShopCenterService shopCenterService;

    @RequiresPermissions("commerce:shop:profile:view")
    @GetMapping
    public AjaxResult getInfo()
    {
        Long shopId = shopService.currentShopId();
        if (shopId == null)
        {
            throw new ServiceException("当前账号未绑定店铺");
        }
        return success(shopCenterService.profile(shopId));
    }

    @RequiresPermissions("commerce:shop:profile:edit")
    @Log(title = "店铺资料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Shop shop)
    {
        Long shopId = shopService.currentShopId();
        if (shopId == null)
        {
            throw new ServiceException("当前账号未绑定店铺");
        }
        shop.setShopId(shopId);
        return toAjax(shopCenterService.updateProfile(shop));
    }
}
