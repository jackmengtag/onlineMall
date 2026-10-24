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
import com.ruoyi.commerce.domain.OrderStateHistory;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.dto.CreateOrderRequest;
import com.ruoyi.commerce.domain.dto.ExternalOrderRequest;
import com.ruoyi.commerce.domain.vo.OrderDetailVo;
import com.ruoyi.commerce.service.IOrderService;

/**
 * 运营后台订单。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/order")
public class OrderAdminController extends BaseController
{
    @Autowired
    private IOrderService orderService;

    @PreAuthorize("@ss.hasPermi('commerce:admin:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(Orders orders, @RequestParam(required = false) Date beginTime,
            @RequestParam(required = false) Date endTime)
    {
        if (beginTime != null)
        {
            orders.getParams().put("beginTime", beginTime);
        }
        if (endTime != null)
        {
            orders.getParams().put("endTime", endTime);
        }
        startPage();
        List<Orders> list = orderService.selectOrderList(orders);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:order:query')")
    @GetMapping("/{orderId}")
    public AjaxResult getInfo(@PathVariable Long orderId)
    {
        OrderDetailVo detail = orderService.selectOrderDetail(orderId);
        return success(detail);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:order:add')")
    @Log(title = "订单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CreateOrderRequest request)
    {
        return success(orderService.createOrder(request));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:order:edit')")
    @Log(title = "订单管理", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{orderId}")
    public AjaxResult cancel(@PathVariable Long orderId, @RequestParam(required = false) String reason)
    {
        orderService.cancelOrder(orderId, reason);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:order:edit')")
    @Log(title = "订单管理", businessType = BusinessType.UPDATE)
    @PostMapping("/complete/{orderId}")
    public AjaxResult complete(@PathVariable Long orderId)
    {
        orderService.completeOrder(orderId);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:order:query')")
    @GetMapping("/history/{orderId}")
    public AjaxResult history(@PathVariable Long orderId)
    {
        List<OrderStateHistory> histories = orderService.selectHistories(orderId);
        return success(histories);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:order:add')")
    @Log(title = "订单管理", businessType = BusinessType.INSERT)
    @PostMapping("/external")
    public AjaxResult external(@RequestBody ExternalOrderRequest request)
    {
        return success(orderService.importExternalOrder(request));
    }
}
