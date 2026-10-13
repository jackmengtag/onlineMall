package com.ruoyi.commerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.commerce.domain.OrderStateHistory;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.dto.CreateOrderRequest;
import com.ruoyi.commerce.service.IOrderService;

/**
 * 订单控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController
{
    @Autowired
    private IOrderService orderService;

    @RequiresPermissions("commerce:order:list")
    @GetMapping("/list")
    public TableDataInfo list(Orders orders)
    {
        startPage();
        List<Orders> list = orderService.selectOrdersList(orders);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:order:query")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable Long orderId)
    {
        Orders orders = orderService.selectOrdersById(orderId);
        if (orders != null)
        {
            orders.setItems(orderService.selectOrderItems(orderId));
        }
        return success(orders);
    }

    @RequiresPermissions("commerce:order:query")
    @GetMapping(value = "/items/{orderId}")
    public AjaxResult items(@PathVariable Long orderId)
    {
        return success(orderService.selectOrderItems(orderId));
    }

    @RequiresPermissions("commerce:order:query")
    @GetMapping(value = "/history/{orderId}")
    public AjaxResult history(@PathVariable Long orderId)
    {
        List<OrderStateHistory> list = orderService.selectStateHistories(orderId);
        return success(list);
    }

    @RequiresPermissions("commerce:order:add")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public AjaxResult create(@Validated @RequestBody CreateOrderRequest request)
    {
        return success(orderService.createOrder(request));
    }

    @RequiresPermissions("commerce:order:edit")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Orders orders)
    {
        return toAjax(orderService.updateOrders(orders));
    }

    @RequiresPermissions("commerce:order:remove")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(orderService.deleteOrdersByIds(orderIds));
    }

    @RequiresPermissions("commerce:order:edit")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{orderId}")
    public AjaxResult cancel(@PathVariable Long orderId, @RequestParam(value = "reason", required = false) String reason)
    {
        return toAjax(orderService.cancelOrder(orderId, reason));
    }
}
