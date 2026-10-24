package com.ruoyi.commerce.controller.storefront;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;

/**
 * 买家端购物车。
 *
 * @author ruoyi
 */
@Anonymous
@RestController
@RequestMapping("/api/storefront/cart")
public class StorefrontCartController extends StorefrontBaseController
{
    @GetMapping
    public Map<String, Object> list(HttpServletRequest request)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        java.util.List<Map<String, Object>> list = storefrontService.cart(customerId);
        return ok(list, list.size());
    }

    @PostMapping
    public Map<String, Object> add(HttpServletRequest request, @RequestBody Map<String, Object> body)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        try
        {
            Long skuId = Convert.toLong(body.get("skuId"));
            Integer quantity = Convert.toInt(body.get("quantity"), 1);
            return ok(storefrontService.addCart(customerId, skuId, quantity));
        }
        catch (ServiceException e)
        {
            return fail(400, e.getMessage());
        }
    }

    @PutMapping
    public Map<String, Object> update(HttpServletRequest request, @RequestBody Map<String, Object> body)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        try
        {
            Long cartItemId = Convert.toLong(body.get("cartItemId"));
            Integer quantity = Convert.toInt(body.get("quantity"));
            Boolean selected = Convert.toBool(body.get("selected"));
            return ok(storefrontService.updateCart(customerId, cartItemId, quantity, selected));
        }
        catch (ServiceException e)
        {
            return fail(400, e.getMessage());
        }
    }

    @DeleteMapping("/{cartItemId}")
    public Map<String, Object> remove(HttpServletRequest request, @PathVariable Long cartItemId)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        try
        {
            return ok(storefrontService.removeCart(customerId, cartItemId));
        }
        catch (ServiceException e)
        {
            return fail(400, e.getMessage());
        }
    }
}
