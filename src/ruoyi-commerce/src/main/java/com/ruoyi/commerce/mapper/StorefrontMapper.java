package com.ruoyi.commerce.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 买家端只读聚合 Mapper。
 *
 * <p>自定义 SQL 联查 product_spu/product_sku/sku_price/inventory/channel_listing/orders/order_items，
 * 不依赖其他业务 Mapper。</p>
 *
 * @author ruoyi
 */
public interface StorefrontMapper
{
    List<Map<String, Object>> selectCategories();

    List<Map<String, Object>> selectSpuList(Map<String, Object> params);

    Long countSpu(Map<String, Object> params);

    Map<String, Object> selectSpuById(Long spuId);

    List<Map<String, Object>> selectSkuRows(@Param("spuIds") List<Long> spuIds);

    List<Map<String, Object>> selectInventoryRows(@Param("spuIds") List<Long> spuIds);

    List<Map<String, Object>> selectChannelRows(@Param("spuIds") List<Long> spuIds);

    Map<String, Object> selectSkuInfo(Long skuId);

    List<Map<String, Object>> selectOrders(@Param("customerId") Long customerId, @Param("status") String status);

    List<Map<String, Object>> selectOrderItems(@Param("orderIds") List<Long> orderIds);

    Map<String, Object> selectOrderBrief(@Param("orderNo") String orderNo);

    Map<String, Object> selectLatestPayment(Long orderId);

    Long selectCartItemCustomerId(Long cartItemId);
}
