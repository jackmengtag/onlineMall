package com.ruoyi.commerce.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.commerce.service.IDashboardService;

/**
 * 运营看板。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/dashboard")
public class DashboardAdminController extends BaseController
{
    @Autowired
    private IDashboardService dashboardService;

    @PreAuthorize("@ss.hasPermi('commerce:admin:dashboard:view')")
    @GetMapping("/summary")
    public AjaxResult summary(@RequestParam(required = false) Long shopId)
    {
        return success(dashboardService.summary(shopId));
    }
}
