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

    @PreAuthorize("@ss.hasPermi('commerce:admin:exchange:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExchangeRate rate)
    {
        startPage();
        List<ExchangeRate> list = exchangeRateService.selectExchangeRateList(rate);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:exchange:query')")
    @GetMapping("/{rateId}")
    public AjaxResult getInfo(@PathVariable Long rateId)
    {
        return success(exchangeRateService.selectExchangeRateById(rateId));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:exchange:add')")
    @Log(title = "汇率管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExchangeRate rate)
    {
        rate.setCreateBy(getUsername());
        return toAjax(exchangeRateService.insertExchangeRate(rate));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:exchange:edit')")
    @Log(title = "汇率管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExchangeRate rate)
    {
        rate.setUpdateBy(getUsername());
        return toAjax(exchangeRateService.updateExchangeRate(rate));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:exchange:remove')")
    @Log(title = "汇率管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{rateIds}")
    public AjaxResult remove(@PathVariable Long[] rateIds)
    {
        return toAjax(exchangeRateService.deleteExchangeRateByIds(rateIds));
    }
}
