package com.ruoyi.commerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.ProductSku;
import com.ruoyi.commerce.mapper.ProductSkuMapper;
import com.ruoyi.commerce.service.IProductSkuService;

/**
 * 商品 SKU 服务实现。
 *
 * @author ruoyi
 */
@Service
public class ProductSkuServiceImpl implements IProductSkuService
{
    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Override
    public List<ProductSku> selectProductSkuList(ProductSku sku)
    {
        return productSkuMapper.selectProductSkuList(sku);
    }

    @Override
    public List<ProductSku> selectSkuBySpuId(Long spuId)
    {
        return productSkuMapper.selectProductSkuBySpuId(spuId);
    }

    @Override
    public ProductSku selectProductSkuById(Long skuId)
    {
        return productSkuMapper.selectProductSkuById(skuId);
    }

    @Override
    public int insertProductSku(ProductSku sku)
    {
        if (sku.getSpuId() == null)
        {
            throw new ServiceException("SKU 所属 SPU 不能为空");
        }
        if (StringUtils.isEmpty(sku.getSkuCode()))
        {
            throw new ServiceException("SKU 编码不能为空");
        }
        ProductSku exists = productSkuMapper.selectProductSkuByCode(sku.getSkuCode());
        if (exists != null)
        {
            throw new ServiceException("SKU 编码'" + sku.getSkuCode() + "'已存在");
        }
        if (StringUtils.isEmpty(sku.getStatus()))
        {
            sku.setStatus("ACTIVE");
        }
        if (sku.getDangerousGoodsFlag() == null)
        {
            sku.setDangerousGoodsFlag(Boolean.FALSE);
        }
        if (StringUtils.isEmpty(sku.getAttributesJson()))
        {
            sku.setAttributesJson("{}");
        }
        return productSkuMapper.insertProductSku(sku);
    }

    @Override
    public int updateProductSku(ProductSku sku)
    {
        if (sku.getSkuId() == null)
        {
            throw new ServiceException("SKU 主键不能为空");
        }
        if (!StringUtils.isEmpty(sku.getSkuCode()))
        {
            ProductSku exists = productSkuMapper.selectProductSkuByCode(sku.getSkuCode());
            if (exists != null && !exists.getSkuId().equals(sku.getSkuId()))
            {
                throw new ServiceException("SKU 编码'" + sku.getSkuCode() + "'已存在");
            }
        }
        return productSkuMapper.updateProductSku(sku);
    }

    @Override
    public int deleteProductSkuByIds(Long[] skuIds)
    {
        return productSkuMapper.deleteProductSkuByIds(skuIds);
    }
}
