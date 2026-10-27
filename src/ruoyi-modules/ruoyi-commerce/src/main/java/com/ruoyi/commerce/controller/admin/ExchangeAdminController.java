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
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.commerce.domain.ExchangeRate;
import com.ruoyi.commerce.service.IExchangeRateService;

/**
 * 运营后台汇率。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/exchange")
public class ExchangeAdminController extends BaseController
{
    @Autowired
    private IExchangeRateService exchangeRateService;

    @RequiresPermissions("commerce:admin:exchange:list")
    @GetMapping("/list")
    public TableDataInfo list(ExchangeRate rate)
    {
        startPage();
        List<ExchangeRate> list = exchangeRateService.selectExchangeRateList(rate);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:admin:exchange:query")
    @GetMapping("/{rateId}")
    public AjaxResult getInfo(@PathVariable Long rateId)
    {
        return success(exchangeRateService.selectExchangeRateById(rateId));
    }

    @RequiresPermissions("commerce:admin:exchange:add")
    @Log(title = "汇率管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExchangeRate rate)
    {
        rate.setCreateBy(SecurityUtils.getUsername());
        return toAjax(exchangeRateService.insertExchangeRate(rate));
    }

    @RequiresPermissions("commerce:admin:exchange:edit")
    @Log(title = "汇率管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExchangeRate rate)
    {
        rate.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(exchangeRateService.updateExchangeRate(rate));
    }

    @RequiresPermissions("commerce:admin:exchange:remove")
    @Log(title = "汇率管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{rateIds}")
    public AjaxResult remove(@PathVariable Long[] rateIds)
    {
        return toAjax(exchangeRateService.deleteExchangeRateByIds(rateIds));
    }
}
