package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.ProductSku;

/**
 * 商品 SKU 数据层。
 *
 * @author ruoyi
 */
public interface ProductSkuMapper
{
    ProductSku selectProductSkuById(Long skuId);

    ProductSku selectProductSkuByCode(String skuCode);

    List<ProductSku> selectProductSkuList(ProductSku productSku);

    List<ProductSku> selectProductSkuBySpuId(Long spuId);

    int insertProductSku(ProductSku productSku);

    int updateProductSku(ProductSku productSku);

    int deleteProductSkuById(Long skuId);

    int deleteProductSkuByIds(Long[] skuIds);

    /**
     * 按 SPU 删除 SKU。
     */
    int deleteSkuBySpuId(@Param("spuId") Long spuId);

    /**
     * 统计可售库存（total_qty - locked_qty）。
     */
    int countAvailableQty(@Param("skuId") Long skuId, @Param("countryCode") String countryCode);
}
