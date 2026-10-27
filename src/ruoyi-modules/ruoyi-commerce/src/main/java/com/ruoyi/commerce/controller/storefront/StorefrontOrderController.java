package com.ruoyi.commerce.controller.storefront;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.commerce.domain.dto.CreateOrderRequest;

/**
 * 买家端订单。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/storefront/orders")
public class StorefrontOrderController extends StorefrontBaseController
{
    @GetMapping
    public Map<String, Object> list(HttpServletRequest request,
            @RequestParam(required = false) String status)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        Map<String, Object> result = storefrontService.orders(customerId, status);
        return ok(result.get("data"), Convert.toLong(result.get("total"), 0L));
    }

    @GetMapping("/{orderNo}")
    public Map<String, Object> detail(HttpServletRequest request, @PathVariable String orderNo)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        try
        {
            return ok(storefrontService.order(customerId, orderNo));
        }
        catch (ServiceException e)
        {
            return fail(404, e.getMessage());
        }
    }

    @PostMapping
    public Map<String, Object> create(HttpServletRequest request, @RequestBody CreateOrderRequest body)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        try
        {
            return ok(storefrontService.createOrder(customerId, body));
        }
        catch (ServiceException e)
        {
            return fail(400, e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/pay")
    public Map<String, Object> pay(HttpServletRequest request, @PathVariable String orderNo)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        try
        {
            return ok(storefrontService.pay(customerId, orderNo));
        }
        catch (ServiceException e)
        {
            return fail(400, e.getMessage());
        }
    }
}
