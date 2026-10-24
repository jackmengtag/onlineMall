package com.ruoyi.commerce.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.commerce.domain.Cart;
import com.ruoyi.commerce.domain.CartItem;
import com.ruoyi.commerce.service.ICartService;

/**
 * 运营后台-购物车。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/cart")
public class CartAdminController extends BaseController
{
    @Autowired
    private ICartService cartService;

    @PreAuthorize("@ss.hasPermi('commerce:admin:customer:query')")
    @GetMapping("/{customerId}")
    public AjaxResult getInfo(@PathVariable Long customerId)
    {
        Cart cart = cartService.selectCartByCustomerId(customerId);
        List<CartItem> items = cart == null ? new ArrayList<CartItem>() : cartService.selectCartItems(cart.getCartId());
        Map<String, Object> data = new HashMap<>();
        data.put("cart", cart);
        data.put("items", items);
        return success(data);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:customer:query')")
    @Log(title = "购物车", businessType = BusinessType.INSERT)
    @PostMapping("/item")
    public AjaxResult addItem(@RequestParam Long customerId, @RequestParam Long skuId,
            @RequestParam(required = false) Integer quantity)
    {
        return success(cartService.addItem(customerId, skuId, quantity));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:customer:query')")
    @Log(title = "购物车", businessType = BusinessType.UPDATE)
    @PutMapping("/item")
    public AjaxResult editItem(@RequestBody CartItem item)
    {
        item.setUpdateBy(getUsername());
        return toAjax(cartService.updateItem(item));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:customer:query')")
    @Log(title = "购物车", businessType = BusinessType.DELETE)
    @DeleteMapping("/item/{cartItemId}")
    public AjaxResult removeItem(@PathVariable Long cartItemId)
    {
        return toAjax(cartService.removeItem(cartItemId));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:customer:query')")
    @Log(title = "购物车", businessType = BusinessType.DELETE)
    @DeleteMapping("/{customerId}")
    public AjaxResult clear(@PathVariable Long customerId)
    {
        return toAjax(cartService.clearCart(customerId));
    }
}
