package com.ruoyi.commerce.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.commerce.domain.ProductSpu;

/**
 * 商品 SPU 服务。
 */
public interface IProductSpuService
{
    List<ProductSpu> selectProductSpuList(ProductSpu spu);

    ProductSpu selectProductSpuById(Long spuId);

    int insertProductSpu(ProductSpu spu);

    int updateProductSpu(ProductSpu spu);

    int deleteProductSpuByIds(Long[] spuIds);

    int changeStatus(Long spuId, String status);

    Map<String, Object> selectAvailability(Long skuId, String countryCode, String channelCode, String currencyCode);
}
