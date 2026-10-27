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
import com.ruoyi.commerce.domain.Promotion;
import com.ruoyi.commerce.service.IPromotionService;

/**
 * 运营后台促销活动。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/promotion")
public class PromotionAdminController extends BaseController
{
    @Autowired
    private IPromotionService promotionService;

    @RequiresPermissions("commerce:admin:promotion:list")
    @GetMapping("/list")
    public TableDataInfo list(Promotion promotion)
    {
        startPage();
        List<Promotion> list = promotionService.selectPromotionList(promotion);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:admin:promotion:query")
    @GetMapping("/{promotionId}")
    public AjaxResult getInfo(@PathVariable Long promotionId)
    {
        return success(promotionService.selectPromotionById(promotionId));
    }

    @RequiresPermissions("commerce:admin:promotion:add")
    @Log(title = "促销活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Promotion promotion)
    {
        promotion.setCreateBy(SecurityUtils.getUsername());
        return toAjax(promotionService.insertPromotion(promotion));
    }

    @RequiresPermissions("commerce:admin:promotion:edit")
    @Log(title = "促销活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Promotion promotion)
    {
        promotion.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(promotionService.updatePromotion(promotion));
    }

    @RequiresPermissions("commerce:admin:promotion:remove")
    @Log(title = "促销活动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{promotionIds}")
    public AjaxResult remove(@PathVariable Long[] promotionIds)
    {
        return toAjax(promotionService.deletePromotionByIds(promotionIds));
    }
}
