package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.ProductSku;

/**
 * 商品 SKU 服务
 *
 * @author ruoyi
 */
public interface IProductSkuService
{
    ProductSku selectProductSkuById(Long skuId);

    List<ProductSku> selectProductSkuList(ProductSku productSku);

    int insertProductSku(ProductSku productSku);

    int updateProductSku(ProductSku productSku);

    int deleteProductSkuByIds(Long[] skuIds);

    boolean checkSkuCodeUnique(ProductSku productSku);
}
