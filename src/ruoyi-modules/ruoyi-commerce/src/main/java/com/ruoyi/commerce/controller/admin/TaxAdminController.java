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
import com.ruoyi.commerce.domain.TaxRule;
import com.ruoyi.commerce.service.ITaxService;

/**
 * 运营后台税率。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/tax")
public class TaxAdminController extends BaseController
{
    @Autowired
    private ITaxService taxService;

    @RequiresPermissions("commerce:admin:tax:list")
    @GetMapping("/list")
    public TableDataInfo list(TaxRule rule)
    {
        startPage();
        List<TaxRule> list = taxService.selectTaxRuleList(rule);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:admin:tax:query")
    @GetMapping("/{ruleId}")
    public AjaxResult getInfo(@PathVariable Long ruleId)
    {
        return success(taxService.selectTaxRuleById(ruleId));
    }

    @RequiresPermissions("commerce:admin:tax:add")
    @Log(title = "税率管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaxRule rule)
    {
        rule.setCreateBy(SecurityUtils.getUsername());
        return toAjax(taxService.insertTaxRule(rule));
    }

    @RequiresPermissions("commerce:admin:tax:edit")
    @Log(title = "税率管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaxRule rule)
    {
        rule.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(taxService.updateTaxRule(rule));
    }

    @RequiresPermissions("commerce:admin:tax:remove")
    @Log(title = "税率管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ruleIds}")
    public AjaxResult remove(@PathVariable Long[] ruleIds)
    {
        return toAjax(taxService.deleteTaxRuleByIds(ruleIds));
    }
}
