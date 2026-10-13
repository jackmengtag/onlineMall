package com.ruoyi.commerce.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.ProductSku;
import com.ruoyi.commerce.mapper.ProductSkuMapper;
import com.ruoyi.commerce.service.IProductSkuService;

/**
 * 商品 SKU 服务实现
 *
 * @author ruoyi
 */
@Service
public class ProductSkuServiceImpl implements IProductSkuService
{
    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Override
    public ProductSku selectProductSkuById(Long skuId)
    {
        return productSkuMapper.selectProductSkuById(skuId);
    }

    @Override
    public List<ProductSku> selectProductSkuList(ProductSku productSku)
    {
        return productSkuMapper.selectProductSkuList(productSku);
    }

    @Override
    public int insertProductSku(ProductSku productSku)
    {
        if (StringUtils.isEmpty(productSku.getStatus()))
        {
            productSku.setStatus("ACTIVE");
        }
        if (StringUtils.isEmpty(productSku.getAttributesJson()))
        {
            productSku.setAttributesJson("{}");
        }
        if (productSku.getDangerousGoodsFlag() == null)
        {
            productSku.setDangerousGoodsFlag(Boolean.FALSE);
        }
        return productSkuMapper.insertProductSku(productSku);
    }

    @Override
    public int updateProductSku(ProductSku productSku)
    {
        return productSkuMapper.updateProductSku(productSku);
    }

    @Override
    public int deleteProductSkuByIds(Long[] skuIds)
    {
        return productSkuMapper.deleteProductSkuByIds(skuIds);
    }

    @Override
    public boolean checkSkuCodeUnique(ProductSku productSku)
    {
        Long skuId = productSku.getSkuId() == null ? -1L : productSku.getSkuId();
        ProductSku info = productSkuMapper.selectProductSkuByCode(productSku.getSkuCode());
        return info == null || info.getSkuId().longValue() == skuId.longValue();
    }
}
