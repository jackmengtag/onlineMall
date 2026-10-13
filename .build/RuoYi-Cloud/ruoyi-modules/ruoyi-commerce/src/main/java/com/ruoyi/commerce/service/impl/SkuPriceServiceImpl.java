package com.ruoyi.commerce.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.commerce.domain.SkuPrice;
import com.ruoyi.commerce.mapper.SkuPriceMapper;
import com.ruoyi.commerce.service.ISkuPriceService;

/**
 * SKU 价格服务实现
 *
 * <p>新增/修改前校验同一价格维度下的生效区间是否重叠，重叠则抛出业务异常，
 * 对应文档 8.1 节“价格有效期使用排他约束防止重叠”在服务层的实现。</p>
 *
 * @author ruoyi
 */
@Service
public class SkuPriceServiceImpl implements ISkuPriceService
{
    @Autowired
    private SkuPriceMapper skuPriceMapper;

    @Override
    public SkuPrice selectSkuPriceById(Long priceId)
    {
        return skuPriceMapper.selectSkuPriceById(priceId);
    }

    @Override
    public List<SkuPrice> selectSkuPriceList(SkuPrice skuPrice)
    {
        return skuPriceMapper.selectSkuPriceList(skuPrice);
    }

    @Override
    public int insertSkuPrice(SkuPrice skuPrice)
    {
        checkPricePeriodOverlap(skuPrice, null);
        return skuPriceMapper.insertSkuPrice(skuPrice);
    }

    @Override
    public int updateSkuPrice(SkuPrice skuPrice)
    {
        checkPricePeriodOverlap(skuPrice, skuPrice.getPriceId());
        return skuPriceMapper.updateSkuPrice(skuPrice);
    }

    @Override
    public int deleteSkuPriceByIds(Long[] priceIds)
    {
        return skuPriceMapper.deleteSkuPriceByIds(priceIds);
    }

    @Override
    public SkuPrice selectSalePrice(Long skuId, String countryCode, String channelCode, String currencyCode)
    {
        return skuPriceMapper.selectSalePrice(skuId, countryCode, channelCode, currencyCode);
    }

    /**
     * 校验价格有效期是否重叠
     *
     * @param skuPrice       待校验的价格
     * @param excludePriceId 需排除的价格主键（修改场景）
     */
    private void checkPricePeriodOverlap(SkuPrice skuPrice, Long excludePriceId)
    {
        List<SkuPrice> overlaps = skuPriceMapper.selectOverlappingPrices(skuPrice.getSkuId(),
                skuPrice.getCountryCode(), skuPrice.getChannelCode(), skuPrice.getCurrencyCode(),
                skuPrice.getEffectiveFrom(), skuPrice.getEffectiveTo(), excludePriceId);
        if (overlaps != null && !overlaps.isEmpty())
        {
            throw new ServiceException("价格有效期存在重叠，请调整生效区间");
        }
    }
}
