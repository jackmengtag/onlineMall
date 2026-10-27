package com.ruoyi.commerce.controller.shop;

import com.ruoyi.common.security.utils.SecurityUtils;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.commerce.domain.Coupon;
import com.ruoyi.commerce.domain.Promotion;
import com.ruoyi.commerce.service.ICouponService;
import com.ruoyi.commerce.service.IPromotionService;
import com.ruoyi.commerce.service.IShopService;

/**
 * 店铺中心营销（促销、优惠券）。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/shop/marketing")
public class ShopMarketingController extends BaseController
{
    @Autowired
    private IPromotionService promotionService;

    @Autowired
    private ICouponService couponService;

    @Autowired
    private IShopService shopService;

    @RequiresPermissions("commerce:shop:marketing:list")
    @GetMapping("/promotion/list")
    public TableDataInfo promotionList(Promotion promotion)
    {
        promotion.setShopId(currentShopId());
        startPage();
        List<Promotion> list = promotionService.selectPromotionList(promotion);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:shop:marketing:add")
    @Log(title = "店铺促销", businessType = BusinessType.INSERT)
    @PostMapping("/promotion")
    public AjaxResult addPromotion(@RequestBody Promotion promotion)
    {
        promotion.setShopId(currentShopId());
        promotion.setCreateBy(SecurityUtils.getUsername());
        return toAjax(promotionService.insertPromotion(promotion));
    }

    @RequiresPermissions("commerce:shop:marketing:edit")
    @Log(title = "店铺促销", businessType = BusinessType.UPDATE)
    @PutMapping("/promotion")
    public AjaxResult editPromotion(@RequestBody Promotion promotion)
    {
        Promotion existing = promotion.getPromotionId() == null ? null
                : promotionService.selectPromotionById(promotion.getPromotionId());
        if (existing == null || !currentShopId().equals(existing.getShopId()))
        {
            throw new ServiceException("无权操作该促销活动");
        }
        promotion.setShopId(currentShopId());
        promotion.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(promotionService.updatePromotion(promotion));
    }

    @RequiresPermissions("commerce:shop:marketing:remove")
    @Log(title = "店铺促销", businessType = BusinessType.DELETE)
    @DeleteMapping("/promotion/{promotionIds}")
    public AjaxResult removePromotion(@PathVariable Long[] promotionIds)
    {
        Long shopId = currentShopId();
        for (Long promotionId : promotionIds)
        {
            Promotion existing = promotionService.selectPromotionById(promotionId);
            if (existing == null || !shopId.equals(existing.getShopId()))
            {
                throw new ServiceException("无权操作该促销活动");
            }
        }
        return toAjax(promotionService.deletePromotionByIds(promotionIds));
    }

    @RequiresPermissions("commerce:shop:marketing:list")
    @GetMapping("/coupon/list")
    public TableDataInfo couponList(Coupon coupon)
    {
        coupon.setShopId(currentShopId());
        startPage();
        List<Coupon> list = couponService.selectCouponList(coupon);
        return getDataTable(list);
    }

    private Long currentShopId()
    {
        Long shopId = shopService.currentShopId();
        if (shopId == null)
        {
            throw new ServiceException("当前账号未绑定店铺");
        }
        return shopId;
    }
}
