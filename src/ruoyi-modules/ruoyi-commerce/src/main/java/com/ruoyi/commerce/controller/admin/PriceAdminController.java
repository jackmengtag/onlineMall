package com.ruoyi.commerce.controller.admin;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.commerce.domain.SkuPrice;
import com.ruoyi.commerce.service.ISkuPriceService;

/**
 * 运营后台-定价管理。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/price")
public class PriceAdminController extends BaseController
{
    @Autowired
    private ISkuPriceService skuPriceService;

    @RequiresPermissions("commerce:admin:price:list")
    @GetMapping("/list")
    public TableDataInfo list(SkuPrice price)
    {
        startPage();
        List<SkuPrice> list = skuPriceService.selectSkuPriceList(price);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:admin:price:query")
    @GetMapping("/{priceId}")
    public AjaxResult getInfo(@PathVariable Long priceId)
    {
        return success(skuPriceService.selectSkuPriceById(priceId));
    }

    @RequiresPermissions("commerce:admin:price:add")
    @Log(title = "价格管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SkuPrice price)
    {
        price.setCreateBy(SecurityUtils.getUsername());
        return toAjax(skuPriceService.insertSkuPrice(price));
    }

    @RequiresPermissions("commerce:admin:price:edit")
    @Log(title = "价格管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SkuPrice price)
    {
        price.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(skuPriceService.updateSkuPrice(price));
    }

    @RequiresPermissions("commerce:admin:price:remove")
    @Log(title = "价格管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{priceIds}")
    public AjaxResult remove(@PathVariable Long[] priceIds)
    {
        return toAjax(skuPriceService.deleteSkuPriceByIds(priceIds));
    }

    @RequiresPermissions("commerce:admin:price:query")
    @GetMapping("/resolve")
    public AjaxResult resolve(@RequestParam Long skuId,
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String channelCode,
            @RequestParam(required = false) String currencyCode)
    {
        return success(skuPriceService.resolvePrice(skuId, countryCode, channelCode, currencyCode));
    }
}
