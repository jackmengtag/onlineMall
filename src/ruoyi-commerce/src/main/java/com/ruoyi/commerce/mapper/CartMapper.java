package com.ruoyi.commerce.mapper;

import com.ruoyi.commerce.domain.Cart;

/**
 * 购物车数据层。
 *
 * @author ruoyi
 */
public interface CartMapper
{
    Cart selectByCustomerId(Long customerId);

    Cart selectCartById(Long cartId);

    int insertCart(Cart cart);

    int updateCart(Cart cart);
}
