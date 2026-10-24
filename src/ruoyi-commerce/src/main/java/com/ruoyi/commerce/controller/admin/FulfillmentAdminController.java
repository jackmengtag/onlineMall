package com.ruoyi.commerce.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.commerce.domain.FulfillmentOrder;
import com.ruoyi.commerce.service.IFulfillmentService;

/**
 * 运营后台 · 履约管理。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/fulfillment")
public class FulfillmentAdminController extends BaseController
{
    @Autowired
    private IFulfillmentService fulfillmentService;

    /**
     * 履约单列表。
     */
    @PreAuthorize("@ss.hasPermi('commerce:admin:fulfillment:list')")
    @GetMapping("/list")
    public TableDataInfo list(FulfillmentOrder fulfillmentOrder)
    {
        startPage();
        List<FulfillmentOrder> list = fulfillmentService.selectFulfillmentList(fulfillmentOrder);
        return getDataTable(list);
    }

    /**
     * 履约单详情。
     */
    @PreAuthorize("@ss.hasPermi('commerce:admin:fulfillment:query')")
    @GetMapping("/{fulfillmentId}")
    public AjaxResult getInfo(@PathVariable Long fulfillmentId)
    {
        return success(fulfillmentService.selectFulfillmentDetail(fulfillmentId));
    }

    /**
     * 订单拆分为履约单。
     */
    @PreAuthorize("@ss.hasPermi('commerce:admin:fulfillment:split')")
    @Log(title = "履约拆单", businessType = BusinessType.INSERT)
    @PostMapping("/split/{orderId}")
    public AjaxResult split(@PathVariable Long orderId)
    {
        return success(fulfillmentService.splitOrder(orderId));
    }

    /**
     * 变更履约状态。
     */
    @PreAuthorize("@ss.hasPermi('commerce:admin:fulfillment:edit')")
    @Log(title = "履约管理", businessType = BusinessType.UPDATE)
    @PostMapping("/status")
    public AjaxResult changeStatus(@RequestParam("fulfillmentId") Long fulfillmentId,
            @RequestParam("toStatus") String toStatus)
    {
        fulfillmentService.changeStatus(fulfillmentId, toStatus);
        return success();
    }

    /**
     * 履约单发货。
     */
    @PreAuthorize("@ss.hasPermi('commerce:admin:fulfillment:ship')")
    @Log(title = "履约发货", businessType = BusinessType.UPDATE)
    @PostMapping("/ship")
    public AjaxResult ship(@RequestParam("fulfillmentId") Long fulfillmentId,
            @RequestParam("carrierCode") String carrierCode,
            @RequestParam(value = "trackingNo", required = false) String trackingNo)
    {
        return success(fulfillmentService.ship(fulfillmentId, carrierCode, trackingNo));
    }
}
