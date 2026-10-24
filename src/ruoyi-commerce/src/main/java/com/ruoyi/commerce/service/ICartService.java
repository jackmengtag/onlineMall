package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.Cart;
import com.ruoyi.commerce.domain.CartItem;

/**
 * 购物车服务。
 */
public interface ICartService
{
    Cart selectCartByCustomerId(Long customerId);

    List<CartItem> selectCartItems(Long cartId);

    CartItem addItem(Long customerId, Long skuId, Integer quantity);

    int updateItem(CartItem item);

    int removeItem(Long cartItemId);

    int clearCart(Long customerId);
}
