package com.ruoyi.commerce.controller.shop;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.vo.OrderDetailVo;
import com.ruoyi.commerce.service.IOrderService;
import com.ruoyi.commerce.service.IShopService;

/**
 * 店铺中心订单。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/shop/order")
public class ShopOrderController extends BaseController
{
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IShopService shopService;

    @RequiresPermissions("commerce:shop:order:list")
    @GetMapping("/list")
    public TableDataInfo list(Orders orders, @RequestParam(required = false) Date beginTime,
            @RequestParam(required = false) Date endTime)
    {
        orders.setShopId(currentShopId());
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

    @RequiresPermissions("commerce:shop:order:query")
    @GetMapping("/{orderId}")
    public AjaxResult getInfo(@PathVariable Long orderId)
    {
        OrderDetailVo detail = orderService.selectOrderDetail(orderId);
        if (detail == null || detail.getOrder() == null
                || !currentShopId().equals(detail.getOrder().getShopId()))
        {
            throw new ServiceException("无权访问该订单");
        }
        return success(detail);
    }

    @RequiresPermissions("commerce:shop:order:edit")
    @Log(title = "店铺订单", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{orderId}")
    public AjaxResult cancel(@PathVariable Long orderId, @RequestParam(required = false) String reason)
    {
        Orders order = orderService.selectOrderById(orderId);
        if (order == null || !currentShopId().equals(order.getShopId()))
        {
            throw new ServiceException("无权操作该订单");
        }
        orderService.cancelOrder(orderId, reason);
        return success();
    }

    private Long currentShopId()
    {
        Long shopId = shopService.currentShopId();
        if (shopId == null)
        {
            throw new ServiceException("当前账号未绑定店铺");
        }
        return shopId;
    }
}
