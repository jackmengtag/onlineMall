package com.ruoyi.commerce.controller.storefront;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.exception.ServiceException;

/**
 * 买家端收货地址。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/storefront/addresses")
public class StorefrontAddressController extends StorefrontBaseController
{
    @GetMapping
    public Map<String, Object> list(HttpServletRequest request)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        java.util.List<Map<String, Object>> list = storefrontService.addresses(customerId);
        return ok(list, list.size());
    }

    @PostMapping
    public Map<String, Object> save(HttpServletRequest request, @RequestBody Map<String, Object> body)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        try
        {
            return ok(storefrontService.saveAddress(customerId, body));
        }
        catch (ServiceException e)
        {
            return fail(400, e.getMessage());
        }
    }
}
