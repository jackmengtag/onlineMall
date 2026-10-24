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

    @PreAuthorize("@ss.hasPermi('commerce:admin:tax:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaxRule rule)
    {
        startPage();
        List<TaxRule> list = taxService.selectTaxRuleList(rule);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:tax:query')")
    @GetMapping("/{ruleId}")
    public AjaxResult getInfo(@PathVariable Long ruleId)
    {
        return success(taxService.selectTaxRuleById(ruleId));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:tax:add')")
    @Log(title = "税率管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaxRule rule)
    {
        rule.setCreateBy(getUsername());
        return toAjax(taxService.insertTaxRule(rule));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:tax:edit')")
    @Log(title = "税率管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaxRule rule)
    {
        rule.setUpdateBy(getUsername());
        return toAjax(taxService.updateTaxRule(rule));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:tax:remove')")
    @Log(title = "税率管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ruleIds}")
    public AjaxResult remove(@PathVariable Long[] ruleIds)
    {
        return toAjax(taxService.deleteTaxRuleByIds(ruleIds));
    }
}
