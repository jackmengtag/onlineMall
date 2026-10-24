package com.ruoyi.commerce.controller.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.commerce.domain.Refund;
import com.ruoyi.commerce.service.IRefundService;

/**
 * 运营后台退款售后。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/refund")
public class RefundAdminController extends BaseController
{
    @Autowired
    private IRefundService refundService;

    @PreAuthorize("@ss.hasPermi('commerce:admin:refund:list')")
    @GetMapping("/list")
    public TableDataInfo list(Refund refund, @RequestParam(required = false) Date beginTime,
            @RequestParam(required = false) Date endTime)
    {
        if (beginTime != null)
        {
            refund.getParams().put("beginTime", beginTime);
        }
        if (endTime != null)
        {
            refund.getParams().put("endTime", endTime);
        }
        startPage();
        List<Refund> list = refundService.selectRefundList(refund);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:refund:query')")
    @GetMapping("/{refundId}")
    public AjaxResult getInfo(@PathVariable Long refundId)
    {
        return success(refundService.selectRefundById(refundId));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:refund:add')")
    @Log(title = "退款售后", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Refund refund)
    {
        return success(refundService.applyRefund(refund));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:refund:edit')")
    @Log(title = "退款售后", businessType = BusinessType.UPDATE)
    @PostMapping("/approve/{refundId}")
    public AjaxResult approve(@PathVariable Long refundId)
    {
        refundService.approve(refundId);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:refund:edit')")
    @Log(title = "退款售后", businessType = BusinessType.UPDATE)
    @PostMapping("/reject/{refundId}")
    public AjaxResult reject(@PathVariable Long refundId, @RequestParam(required = false) String reason)
    {
        refundService.reject(refundId, reason);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:refund:edit')")
    @Log(title = "退款售后", businessType = BusinessType.UPDATE)
    @PostMapping("/complete/{refundId}")
    public AjaxResult complete(@PathVariable Long refundId)
    {
        refundService.complete(refundId);
        return success();
    }
}
