package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.ProductSku;

/**
 * 商品 SKU 数据层
 *
 * @author ruoyi
 */
public interface ProductSkuMapper
{
    /**
     * 查询 SKU
     */
    ProductSku selectProductSkuById(Long skuId);

    /**
     * 按编码查询 SKU
     */
    ProductSku selectProductSkuByCode(String skuCode);

    /**
     * 查询 SKU 列表
     */
    List<ProductSku> selectProductSkuList(ProductSku productSku);

    /**
     * 新增 SKU
     */
    int insertProductSku(ProductSku productSku);

    /**
     * 修改 SKU
     */
    int updateProductSku(ProductSku productSku);

    /**
     * 删除 SKU
     */
    int deleteProductSkuById(Long skuId);

    /**
     * 批量删除 SKU
     */
    int deleteProductSkuByIds(Long[] skuIds);
}
