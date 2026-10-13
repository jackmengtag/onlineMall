package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.ProductSpu;

/**
 * 商品 SPU 服务
 *
 * @author ruoyi
 */
public interface IProductSpuService
{
    ProductSpu selectProductSpuById(Long spuId);

    List<ProductSpu> selectProductSpuList(ProductSpu productSpu);

    int insertProductSpu(ProductSpu productSpu);

    int updateProductSpu(ProductSpu productSpu);

    int deleteProductSpuByIds(Long[] spuIds);

    boolean checkSpuCodeUnique(ProductSpu productSpu);
}
