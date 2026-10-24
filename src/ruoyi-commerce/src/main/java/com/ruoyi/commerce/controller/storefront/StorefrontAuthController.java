package com.ruoyi.commerce.controller.storefront;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.commerce.domain.dto.StorefrontLoginRequest;

/**
 * 买家端登录与账户。
 *
 * @author ruoyi
 */
@Anonymous
@RestController
@RequestMapping("/api/storefront")
public class StorefrontAuthController extends StorefrontBaseController
{
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody StorefrontLoginRequest request)
    {
        try
        {
            return ok(storefrontService.login(request.getEmail(), request.getPassword()));
        }
        catch (ServiceException e)
        {
            return fail(400, e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request)
    {
        storefrontService.logout(request.getHeader(TOKEN_HEADER));
        return ok(null);
    }

    @GetMapping("/account")
    public Map<String, Object> account(HttpServletRequest request)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        return ok(storefrontService.account(customerId));
    }

    @GetMapping("/session")
    public Map<String, Object> session(HttpServletRequest request)
    {
        Long customerId = currentCustomerId(request);
        if (customerId == null)
        {
            return unauthorized();
        }
        return ok(storefrontService.account(customerId));
    }
}
