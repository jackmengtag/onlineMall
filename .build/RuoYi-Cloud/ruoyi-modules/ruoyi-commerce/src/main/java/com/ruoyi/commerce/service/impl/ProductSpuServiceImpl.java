package com.ruoyi.commerce.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.ProductSpu;
import com.ruoyi.commerce.mapper.ProductSpuMapper;
import com.ruoyi.commerce.service.IProductSpuService;

/**
 * 商品 SPU 服务实现
 *
 * @author ruoyi
 */
@Service
public class ProductSpuServiceImpl implements IProductSpuService
{
    @Autowired
    private ProductSpuMapper productSpuMapper;

    @Override
    public ProductSpu selectProductSpuById(Long spuId)
    {
        return productSpuMapper.selectProductSpuById(spuId);
    }

    @Override
    public List<ProductSpu> selectProductSpuList(ProductSpu productSpu)
    {
        return productSpuMapper.selectProductSpuList(productSpu);
    }

    @Override
    public int insertProductSpu(ProductSpu productSpu)
    {
        if (StringUtils.isEmpty(productSpu.getStatus()))
        {
            productSpu.setStatus("DRAFT");
        }
        return productSpuMapper.insertProductSpu(productSpu);
    }

    @Override
    public int updateProductSpu(ProductSpu productSpu)
    {
        return productSpuMapper.updateProductSpu(productSpu);
    }

    @Override
    public int deleteProductSpuByIds(Long[] spuIds)
    {
        return productSpuMapper.deleteProductSpuByIds(spuIds);
    }

    @Override
    public boolean checkSpuCodeUnique(ProductSpu productSpu)
    {
        Long spuId = productSpu.getSpuId() == null ? -1L : productSpu.getSpuId();
        ProductSpu info = productSpuMapper.selectProductSpuByCode(productSpu.getSpuCode());
        return info == null || info.getSpuId().longValue() == spuId.longValue();
    }
}
