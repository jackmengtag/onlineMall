package com.ruoyi.commerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.Cart;
import com.ruoyi.commerce.domain.CartItem;
import com.ruoyi.commerce.domain.ProductSku;
import com.ruoyi.commerce.domain.ProductSpu;
import com.ruoyi.commerce.domain.SkuPrice;
import com.ruoyi.commerce.mapper.CartItemMapper;
import com.ruoyi.commerce.mapper.CartMapper;
import com.ruoyi.commerce.mapper.ProductSkuMapper;
import com.ruoyi.commerce.mapper.ProductSpuMapper;
import com.ruoyi.commerce.mapper.SkuPriceMapper;
import com.ruoyi.commerce.service.ICartService;

/**
 * 购物车服务实现。
 *
 * @author ruoyi
 */
@Service
public class CartServiceImpl implements ICartService
{
    private static final String SKU_ACTIVE = "ACTIVE";

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductSpuMapper productSpuMapper;

    @Autowired
    private SkuPriceMapper skuPriceMapper;

    @Override
    public Cart selectCartByCustomerId(Long customerId)
    {
        return cartMapper.selectByCustomerId(customerId);
    }

    @Override
    public List<CartItem> selectCartItems(Long cartId)
    {
        List<CartItem> items = cartItemMapper.selectByCartId(cartId);
        Cart cart = cartMapper.selectCartById(cartId);
        for (CartItem item : items)
        {
            enrich(item, cart);
        }
        return items;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItem addItem(Long customerId, Long skuId, Integer quantity)
    {
        if (customerId == null)
        {
            throw new ServiceException("客户不能为空");
        }
        if (skuId == null)
        {
            throw new ServiceException("SKU 不能为空");
        }
        int qty = quantity == null ? 1 : quantity;
        if (qty <= 0)
        {
            throw new ServiceException("购买数量必须大于 0");
        }
        ProductSku sku = productSkuMapper.selectProductSkuById(skuId);
        if (sku == null)
        {
            throw new ServiceException("SKU 不存在");
        }
        if (!SKU_ACTIVE.equals(sku.getStatus()))
        {
            throw new ServiceException("SKU 当前不可售");
        }
        Cart cart = cartMapper.selectByCustomerId(customerId);
        if (cart == null)
        {
            cart = new Cart();
            cart.setCustomerId(customerId);
            cart.setChannelCode(CommerceConstants.DEFAULT_CHANNEL);
            cart.setCurrencyCode("USD");
            cartMapper.insertCart(cart);
        }
        CartItem item = cartItemMapper.selectByCartAndSku(cart.getCartId(), skuId);
        if (item != null)
        {
            int current = item.getQuantity() == null ? 0 : item.getQuantity();
            item.setQuantity(current + qty);
            cartItemMapper.updateCartItem(item);
        }
        else
        {
            item = new CartItem();
            item.setCartId(cart.getCartId());
            item.setSkuId(skuId);
            item.setSkuCode(sku.getSkuCode());
            item.setQuantity(qty);
            item.setSelected(Boolean.TRUE);
            cartItemMapper.insertCartItem(item);
        }
        return enrich(item, cart);
    }

    @Override
    public int updateItem(CartItem item)
    {
        if (item.getCartItemId() == null)
        {
            throw new ServiceException("购物车明细主键不能为空");
        }
        CartItem existing = cartItemMapper.selectCartItemById(item.getCartItemId());
        if (existing == null)
        {
            throw new ServiceException("购物车明细不存在");
        }
        if (item.getQuantity() != null && item.getQuantity() <= 0)
        {
            throw new ServiceException("购买数量必须大于 0");
        }
        return cartItemMapper.updateCartItem(item);
    }

    @Override
    public int removeItem(Long cartItemId)
    {
        return cartItemMapper.deleteCartItemById(cartItemId);
    }

    @Override
    public int clearCart(Long customerId)
    {
        Cart cart = cartMapper.selectByCustomerId(customerId);
        if (cart == null)
        {
            return 0;
        }
        return cartItemMapper.deleteByCartId(cart.getCartId());
    }

    /**
     * 补全商品名称、图片与当前有效单价。
     */
    private CartItem enrich(CartItem item, Cart cart)
    {
        ProductSku sku = productSkuMapper.selectProductSkuById(item.getSkuId());
        if (sku != null)
        {
            if (StringUtils.isEmpty(item.getSkuCode()))
            {
                item.setSkuCode(sku.getSkuCode());
            }
            ProductSpu spu = productSpuMapper.selectProductSpuById(sku.getSpuId());
            if (spu != null)
            {
                item.setProductName(spu.getDefaultName());
                item.setImageUrl(spu.getDefaultImageUrl());
            }
        }
        String countryCode = cart == null ? null : cart.getCountryCode();
        String channelCode = cart == null ? CommerceConstants.DEFAULT_CHANNEL : cart.getChannelCode();
        String currencyCode = cart == null ? null : cart.getCurrencyCode();
        SkuPrice price = skuPriceMapper.selectEffectivePrice(item.getSkuId(), countryCode, channelCode, currencyCode);
        if (price != null)
        {
            item.setUnitPriceAmount(price.getSaleAmount());
            item.setCurrencyCode(price.getCurrencyCode());
        }
        else if (StringUtils.isEmpty(item.getCurrencyCode()) && currencyCode != null)
        {
            item.setCurrencyCode(currencyCode);
        }
        return item;
    }
}
