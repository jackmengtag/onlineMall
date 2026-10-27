package com.ruoyi.commerce.controller.storefront;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.core.exception.ServiceException;

/**
 * 买家端售后。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/storefront/aftersales")
public class StorefrontAfterSaleController extends StorefrontBaseController
{
    @GetMapping
    public Map<String, Object> list(HttpServletRequest request)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        java.util.List<Map<String, Object>> list = storefrontService.aftersales(customerId);
        return ok(list, list.size());
    }

    @PostMapping
    public Map<String, Object> apply(HttpServletRequest request, @RequestBody Map<String, Object> body)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        try
        {
            String orderNo = Convert.toStr(body.get("orderNo"));
            Long amount = Convert.toLong(body.get("amount"));
            String reason = Convert.toStr(body.get("reason"));
            String type = Convert.toStr(body.get("type"));
            return ok(storefrontService.applyAftersale(customerId, orderNo, amount, reason, type));
        }
        catch (ServiceException e)
        {
            return fail(400, e.getMessage());
        }
    }
}
