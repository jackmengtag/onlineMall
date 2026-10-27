package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.ProductSpu;

/**
 * 商品 SPU 数据层。
 *
 * @author ruoyi
 */
public interface ProductSpuMapper
{
    ProductSpu selectProductSpuById(Long spuId);

    ProductSpu selectProductSpuByCode(String spuCode);

    List<ProductSpu> selectProductSpuList(ProductSpu productSpu);

    int insertProductSpu(ProductSpu productSpu);

    int updateProductSpu(ProductSpu productSpu);

    int updateStatus(@Param("spuId") Long spuId, @Param("status") String status, @Param("updateBy") String updateBy);

    int deleteProductSpuByIds(Long[] spuIds);
}
