package com.ruoyi.commerce.controller.admin;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.commerce.service.IReconciliationService;

/**
 * 运营后台对账。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/reconciliation")
public class ReconciliationAdminController extends BaseController
{
    @Autowired
    private IReconciliationService reconciliationService;

    @RequiresPermissions("commerce:admin:reconciliation:view")
    @GetMapping("/summary")
    public AjaxResult summary(@RequestParam(required = false) String date)
    {
        return success(reconciliationService.reconciliation(date));
    }

    @RequiresPermissions("commerce:admin:reconciliation:view")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String date)
    {
        return getDataTable(reconciliationService.selectDetails(date));
    }
}
