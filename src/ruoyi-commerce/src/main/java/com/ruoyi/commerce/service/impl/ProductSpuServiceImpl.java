package com.ruoyi.commerce.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.ProductSku;
import com.ruoyi.commerce.domain.ProductSpu;
import com.ruoyi.commerce.domain.SkuPrice;
import com.ruoyi.commerce.mapper.ProductSkuMapper;
import com.ruoyi.commerce.mapper.ProductSpuMapper;
import com.ruoyi.commerce.mapper.SkuPriceMapper;
import com.ruoyi.commerce.service.IProductSpuService;

/**
 * 商品 SPU 服务实现。
 *
 * @author ruoyi
 */
@Service
public class ProductSpuServiceImpl implements IProductSpuService
{
    /** SPU 合法状态流转 */
    private static final Map<String, List<String>> STATUS_TRANSITIONS = new HashMap<>();

    static
    {
        STATUS_TRANSITIONS.put(CommerceConstants.SPU_DRAFT,
                Arrays.asList(CommerceConstants.SPU_PUBLISHED, CommerceConstants.SPU_OFF_SALE));
        STATUS_TRANSITIONS.put(CommerceConstants.SPU_PUBLISHED,
                Arrays.asList(CommerceConstants.SPU_ON_SALE, CommerceConstants.SPU_OFF_SALE));
        STATUS_TRANSITIONS.put(CommerceConstants.SPU_ON_SALE,
                Arrays.asList(CommerceConstants.SPU_SUSPENDED, CommerceConstants.SPU_OFF_SALE));
        STATUS_TRANSITIONS.put(CommerceConstants.SPU_SUSPENDED,
                Arrays.asList(CommerceConstants.SPU_ON_SALE, CommerceConstants.SPU_OFF_SALE));
        STATUS_TRANSITIONS.put(CommerceConstants.SPU_OFF_SALE,
                Arrays.asList(CommerceConstants.SPU_DRAFT));
    }

    @Autowired
    private ProductSpuMapper productSpuMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private SkuPriceMapper skuPriceMapper;

    @Override
    public List<ProductSpu> selectProductSpuList(ProductSpu spu)
    {
        return productSpuMapper.selectProductSpuList(spu);
    }

    @Override
    public ProductSpu selectProductSpuById(Long spuId)
    {
        return productSpuMapper.selectProductSpuById(spuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertProductSpu(ProductSpu spu)
    {
        if (StringUtils.isEmpty(spu.getSpuCode()))
        {
            throw new ServiceException("SPU 编码不能为空");
        }
        if (StringUtils.isEmpty(spu.getDefaultName()))
        {
            throw new ServiceException("商品名称不能为空");
        }
        ProductSpu exists = productSpuMapper.selectProductSpuByCode(spu.getSpuCode());
        if (exists != null)
        {
            throw new ServiceException("SPU 编码'" + spu.getSpuCode() + "'已存在");
        }
        if (StringUtils.isEmpty(spu.getStatus()))
        {
            spu.setStatus(CommerceConstants.SPU_DRAFT);
        }
        int rows = productSpuMapper.insertProductSpu(spu);
        saveSkus(spu);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProductSpu(ProductSpu spu)
    {
        if (spu.getSpuId() == null)
        {
            throw new ServiceException("SPU 主键不能为空");
        }
        if (!StringUtils.isEmpty(spu.getSpuCode()))
        {
            ProductSpu exists = productSpuMapper.selectProductSpuByCode(spu.getSpuCode());
            if (exists != null && !exists.getSpuId().equals(spu.getSpuId()))
            {
                throw new ServiceException("SPU 编码'" + spu.getSpuCode() + "'已存在");
            }
        }
        int rows = productSpuMapper.updateProductSpu(spu);
        saveSkus(spu);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProductSpuByIds(Long[] spuIds)
    {
        for (Long spuId : spuIds)
        {
            productSkuMapper.deleteSkuBySpuId(spuId);
        }
        return productSpuMapper.deleteProductSpuByIds(spuIds);
    }

    @Override
    public int changeStatus(Long spuId, String status)
    {
        ProductSpu spu = productSpuMapper.selectProductSpuById(spuId);
        if (spu == null)
        {
            throw new ServiceException("商品不存在");
        }
        if (!STATUS_TRANSITIONS.containsKey(status))
        {
            throw new ServiceException("非法的商品状态：" + status);
        }
        String current = spu.getStatus();
        if (status.equals(current))
        {
            return 1;
        }
        List<String> allowed = STATUS_TRANSITIONS.get(current);
        if (allowed == null || !allowed.contains(status))
        {
            throw new ServiceException("商品状态不允许从 " + current + " 流转到 " + status);
        }
        return productSpuMapper.updateStatus(spuId, status, SecurityUtils.getUsername());
    }

    @Override
    public Map<String, Object> selectAvailability(Long skuId, String countryCode, String channelCode,
            String currencyCode)
    {
        Map<String, Object> result = new HashMap<>();
        ProductSku sku = productSkuMapper.selectProductSkuById(skuId);
        result.put("sku", sku);
        if (sku == null)
        {
            result.put("spu", null);
            result.put("price", null);
            result.put("availableQty", 0);
            result.put("available", Boolean.FALSE);
            return result;
        }
        ProductSpu spu = productSpuMapper.selectProductSpuById(sku.getSpuId());
        SkuPrice price = skuPriceMapper.selectEffectivePrice(skuId, countryCode, channelCode, currencyCode);
        int availableQty = productSkuMapper.countAvailableQty(skuId, countryCode);
        boolean available = "ACTIVE".equals(sku.getStatus())
                && spu != null
                && CommerceConstants.SPU_ON_SALE.equals(spu.getStatus())
                && price != null
                && availableQty > 0;
        result.put("spu", spu);
        result.put("price", price);
        result.put("availableQty", availableQty);
        result.put("available", available);
        return result;
    }

    /**
     * 级联保存 SKU：无主键新增，有主键更新。
     */
    private void saveSkus(ProductSpu spu)
    {
        List<ProductSku> skus = spu.getSkus();
        if (skus == null || skus.isEmpty())
        {
            return;
        }
        for (ProductSku sku : skus)
        {
            sku.setSpuId(spu.getSpuId());
            if (StringUtils.isEmpty(sku.getSkuCode()))
            {
                throw new ServiceException("SKU 编码不能为空");
            }
            if (sku.getSkuId() == null)
            {
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
                if (StringUtils.isEmpty(sku.getCreateBy()))
                {
                    sku.setCreateBy(StringUtils.isNotEmpty(spu.getCreateBy()) ? spu.getCreateBy() : spu.getUpdateBy());
                }
                productSkuMapper.insertProductSku(sku);
            }
            else
            {
                sku.setUpdateBy(spu.getUpdateBy());
                productSkuMapper.updateProductSku(sku);
            }
        }
    }
}
