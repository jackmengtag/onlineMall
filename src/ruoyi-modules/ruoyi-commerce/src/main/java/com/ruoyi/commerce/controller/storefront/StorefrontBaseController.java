package com.ruoyi.commerce.controller.storefront;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruoyi.commerce.service.IStorefrontService;

/**
 * 买家端控制器基类。
 *
 * @author ruoyi
 */
public class StorefrontBaseController
{
    protected static final String TOKEN_HEADER = "X-Customer-Token";

    @Autowired
    protected IStorefrontService storefrontService;

    /**
     * 从请求头解析买家 ID，未登录返回 null。
     */
    protected Long currentCustomerId(HttpServletRequest request)
    {
        return storefrontService.resolveCustomerId(request.getHeader(TOKEN_HEADER));
    }

    protected Map<String, Object> ok(Object data)
    {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 0);
        result.put("data", data);
        return result;
    }

    protected Map<String, Object> ok(Object data, long total)
    {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 0);
        result.put("data", data);
        result.put("total", total);
        return result;
    }

    protected Map<String, Object> fail(int code, String message)
    {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", code);
        result.put("message", message);
        return result;
    }

    protected Map<String, Object> unauthorized()
    {
        return fail(401, "请先登录");
    }
}
