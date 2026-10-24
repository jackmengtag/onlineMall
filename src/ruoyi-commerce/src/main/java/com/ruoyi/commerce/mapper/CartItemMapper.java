package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.CartItem;

/**
 * 购物车明细数据层。
 *
 * @author ruoyi
 */
public interface CartItemMapper
{
    List<CartItem> selectByCartId(Long cartId);

    CartItem selectByCartAndSku(@Param("cartId") Long cartId, @Param("skuId") Long skuId);

    CartItem selectCartItemById(Long cartItemId);

    int insertCartItem(CartItem cartItem);

    int updateCartItem(CartItem cartItem);

    int deleteCartItemById(Long cartItemId);

    int deleteByCartId(Long cartId);
}
