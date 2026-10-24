package com.ruoyi.commerce.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.commerce.domain.Coupon;
import com.ruoyi.commerce.service.ICouponService;

/**
 * 运营后台优惠券。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/coupon")
public class CouponAdminController extends BaseController
{
    @Autowired
    private ICouponService couponService;

    @PreAuthorize("@ss.hasPermi('commerce:admin:coupon:list')")
    @GetMapping("/list")
    public TableDataInfo list(Coupon coupon)
    {
        startPage();
        List<Coupon> list = couponService.selectCouponList(coupon);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:coupon:query')")
    @GetMapping("/{couponId}")
    public AjaxResult getInfo(@PathVariable Long couponId)
    {
        return success(couponService.selectCouponById(couponId));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:coupon:add')")
    @Log(title = "优惠券", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Coupon coupon)
    {
        coupon.setCreateBy(getUsername());
        return toAjax(couponService.insertCoupon(coupon));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:coupon:edit')")
    @Log(title = "优惠券", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Coupon coupon)
    {
        coupon.setUpdateBy(getUsername());
        return toAjax(couponService.updateCoupon(coupon));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:coupon:remove')")
    @Log(title = "优惠券", businessType = BusinessType.DELETE)
    @DeleteMapping("/{couponIds}")
    public AjaxResult remove(@PathVariable Long[] couponIds)
    {
        return toAjax(couponService.deleteCouponByIds(couponIds));
    }
}
