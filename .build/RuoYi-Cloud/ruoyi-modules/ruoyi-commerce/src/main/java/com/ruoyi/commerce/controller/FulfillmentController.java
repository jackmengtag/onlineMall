package com.ruoyi.commerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.commerce.domain.FulfillmentOrder;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.service.IFulfillmentService;
import com.ruoyi.commerce.service.IOrderService;

/**
 * 履约单控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/fulfillment")
public class FulfillmentController extends BaseController
{
    @Autowired
    private IFulfillmentService fulfillmentService;

    @Autowired
    private IOrderService orderService;

    @RequiresPermissions("commerce:fulfillment:list")
    @GetMapping("/list")
    public TableDataInfo list(FulfillmentOrder fulfillmentOrder)
    {
        startPage();
        List<FulfillmentOrder> list = fulfillmentService.selectFulfillmentList(fulfillmentOrder);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:fulfillment:query")
    @GetMapping("/{fulfillmentId}")
    public AjaxResult getInfo(@PathVariable Long fulfillmentId)
    {
        return success(fulfillmentService.selectFulfillmentById(fulfillmentId));
    }

    @RequiresPermissions("commerce:fulfillment:add")
    @Log(title = "履约拆分", businessType = BusinessType.INSERT)
    @PostMapping("/split/{orderId}")
    public AjaxResult split(@PathVariable Long orderId)
    {
        Orders order = orderService.selectOrdersById(orderId);
        if (order == null)
        {
            return error("订单不存在");
        }
        return success(fulfillmentService.createFulfillmentForOrder(order));
    }

    @RequiresPermissions("commerce:fulfillment:edit")
    @Log(title = "履约单", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult updateStatus(Long fulfillmentId, String toStatus, String reason)
    {
        return toAjax(fulfillmentService.updateFulfillmentStatus(fulfillmentId, toStatus, reason));
    }

    @RequiresPermissions("commerce:fulfillment:query")
    @GetMapping("/items/{fulfillmentId}")
    public AjaxResult items(@PathVariable Long fulfillmentId)
    {
        return success(fulfillmentService.selectFulfillmentItems(fulfillmentId));
    }

    @RequiresPermissions("commerce:fulfillment:edit")
    @Log(title = "履约发货", businessType = BusinessType.UPDATE)
    @PostMapping("/ship")
    public AjaxResult ship(Long fulfillmentId, String carrierCode, String trackingNo)
    {
        return success(fulfillmentService.ship(fulfillmentId, carrierCode, trackingNo));
    }
}
