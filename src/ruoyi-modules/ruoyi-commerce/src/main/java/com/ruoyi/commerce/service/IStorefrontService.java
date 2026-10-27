package com.ruoyi.commerce.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.commerce.domain.dto.CreateOrderRequest;

/**
 * 买家端（Storefront）服务。
 *
 * @author ruoyi
 */
public interface IStorefrontService
{
    /**
     * 商品类目（含 all/全部商品）。
     */
    List<Map<String, Object>> categories();

    /**
     * 商品分页查询，返回 { data, total, page, size }。
     */
    Map<String, Object> products(Map<String, Object> query);

    /**
     * 商品详情。
     */
    Map<String, Object> product(Long spuId);

    /**
     * 买家登录，返回 { token, customerId, displayName, email }。
     */
    Map<String, Object> login(String email, String password);

    /**
     * 退出登录。
     */
    void logout(String token);

    /**
     * 解析买家 token 对应的 customerId，无效返回 null。
     */
    Long resolveCustomerId(String token);

    /**
     * 买家账户信息。
     */
    Map<String, Object> account(Long customerId);

    /**
     * 购物车明细。
     */
    List<Map<String, Object>> cart(Long customerId);

    Map<String, Object> addCart(Long customerId, Long skuId, Integer quantity);

    Map<String, Object> updateCart(Long customerId, Long cartItemId, Integer quantity, Boolean selected);

    int removeCart(Long customerId, Long cartItemId);

    /**
     * 订单列表，返回 { data, total }。
     */
    Map<String, Object> orders(Long customerId, String status);

    /**
     * 订单详情（订单、明细、快照、历史）。
     */
    Map<String, Object> order(Long customerId, String orderNo);

    /**
     * 买家下单。
     */
    Map<String, Object> createOrder(Long customerId, CreateOrderRequest request);

    /**
     * 模拟支付成功，返回 { orderNo, status, paymentStatus }。
     */
    Map<String, Object> pay(Long customerId, String orderNo);

    /**
     * 提交售后（退款）申请，返回 { no, status }。
     */
    Map<String, Object> applyAftersale(Long customerId, String orderNo, Long amount, String reason, String type);

    /**
     * 买家的售后列表。
     */
    List<Map<String, Object>> aftersales(Long customerId);

    List<Map<String, Object>> addresses(Long customerId);

    Map<String, Object> saveAddress(Long customerId, Map<String, Object> address);
}
