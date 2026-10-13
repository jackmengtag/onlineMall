package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.ProductSpu;

/**
 * 商品 SPU 数据层
 *
 * @author ruoyi
 */
public interface ProductSpuMapper
{
    /**
     * 查询 SPU
     */
    ProductSpu selectProductSpuById(Long spuId);

    /**
     * 按编码查询 SPU
     */
    ProductSpu selectProductSpuByCode(String spuCode);

    /**
     * 查询 SPU 列表
     */
    List<ProductSpu> selectProductSpuList(ProductSpu productSpu);

    /**
     * 新增 SPU
     */
    int insertProductSpu(ProductSpu productSpu);

    /**
     * 修改 SPU
     */
    int updateProductSpu(ProductSpu productSpu);

    /**
     * 删除 SPU
     */
    int deleteProductSpuById(Long spuId);

    /**
     * 批量删除 SPU
     */
    int deleteProductSpuByIds(Long[] spuIds);
}
