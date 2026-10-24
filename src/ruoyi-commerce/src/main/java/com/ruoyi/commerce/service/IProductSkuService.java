package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.ProductSku;

/**
 * 商品 SKU 服务。
 */
public interface IProductSkuService
{
    List<ProductSku> selectProductSkuList(ProductSku sku);

    List<ProductSku> selectSkuBySpuId(Long spuId);

    ProductSku selectProductSkuById(Long skuId);

    int insertProductSku(ProductSku sku);

    int updateProductSku(ProductSku sku);

    int deleteProductSkuByIds(Long[] skuIds);
}
