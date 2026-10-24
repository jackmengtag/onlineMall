package com.ruoyi.commerce.controller.storefront;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;

/**
 * 买家端商品目录。
 *
 * @author ruoyi
 */
@Anonymous
@RestController
@RequestMapping("/api/storefront")
public class StorefrontCatalogController extends StorefrontBaseController
{
    @GetMapping("/categories")
    public Map<String, Object> categories()
    {
        java.util.List<Map<String, Object>> list = storefrontService.categories();
        return ok(list, list.size());
    }

    @GetMapping("/products")
    public Map<String, Object> products(@RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "8") Integer size)
    {
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("category", category);
        query.put("q", q);
        query.put("sort", sort);
        query.put("page", page);
        query.put("size", size);
        Map<String, Object> result = storefrontService.products(query);
        return ok(result.get("data"), Convert.toLong(result.get("total"), 0L));
    }

    @GetMapping("/products/{id}")
    public Map<String, Object> product(@PathVariable Long id)
    {
        try
        {
            return ok(storefrontService.product(id));
        }
        catch (ServiceException e)
        {
            return fail(404, e.getMessage());
        }
    }
}
