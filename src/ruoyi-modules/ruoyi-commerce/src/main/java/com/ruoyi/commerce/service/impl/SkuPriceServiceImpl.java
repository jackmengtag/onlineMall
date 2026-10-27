package com.ruoyi.commerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.SkuPrice;
import com.ruoyi.commerce.mapper.SkuPriceMapper;
import com.ruoyi.commerce.service.ISkuPriceService;

/**
 * 区域/渠道/币种价格服务实现。
 *
 * <p>同一 (sku, country, channel, currency) 下的生效区间不得重叠，由服务层校验。</p>
 *
 * @author ruoyi
 */
@Service
public class SkuPriceServiceImpl implements ISkuPriceService
{
    @Autowired
    private SkuPriceMapper skuPriceMapper;

    @Override
    public List<SkuPrice> selectSkuPriceList(SkuPrice price)
    {
        return skuPriceMapper.selectSkuPriceList(price);
    }

    @Override
    public SkuPrice selectSkuPriceById(Long priceId)
    {
        return skuPriceMapper.selectSkuPriceById(priceId);
    }

    @Override
    public int insertSkuPrice(SkuPrice price)
    {
        fillDefaults(price);
        validate(price, null);
        return skuPriceMapper.insertSkuPrice(price);
    }

    @Override
    public int updateSkuPrice(SkuPrice price)
    {
        if (price.getPriceId() == null)
        {
            throw new ServiceException("价格主键不能为空");
        }
        SkuPrice existing = skuPriceMapper.selectSkuPriceById(price.getPriceId());
        if (existing == null)
        {
            throw new ServiceException("价格记录不存在");
        }
        SkuPrice merged = mergeForCheck(price, existing);
        fillDefaults(merged);
        validate(merged, price.getPriceId());
        return skuPriceMapper.updateSkuPrice(price);
    }

    @Override
    public int deleteSkuPriceByIds(Long[] priceIds)
    {
        return skuPriceMapper.deleteSkuPriceByIds(priceIds);
    }

    @Override
    public SkuPrice resolvePrice(Long skuId, String countryCode, String channelCode, String currencyCode)
    {
        SkuPrice price = skuPriceMapper.selectEffectivePrice(skuId, countryCode, channelCode, currencyCode);
        if (price == null)
        {
            // 跨市场兼容：目标国家未单独配置价格时，回退到同币种的任意国家有效价格
            price = skuPriceMapper.selectEffectivePrice(skuId, null, channelCode, currencyCode);
        }
        if (price == null)
        {
            throw new ServiceException("未找到匹配的有效销售价");
        }
        return price;
    }

    private void fillDefaults(SkuPrice price)
    {
        if (StringUtils.isEmpty(price.getChannelCode()))
        {
            price.setChannelCode(CommerceConstants.DEFAULT_CHANNEL);
        }
        if (StringUtils.isEmpty(price.getStatus()))
        {
            price.setStatus("ACTIVE");
        }
    }

    /**
     * 校验金额、时间区间与区间重叠。
     */
    private void validate(SkuPrice price, Long excludePriceId)
    {
        if (price.getSkuId() == null)
        {
            throw new ServiceException("SKU 不能为空");
        }
        if (StringUtils.isEmpty(price.getCountryCode()) || StringUtils.isEmpty(price.getCurrencyCode()))
        {
            throw new ServiceException("国家与币种不能为空");
        }
        if (price.getListAmount() == null || price.getSaleAmount() == null)
        {
            throw new ServiceException("划线价与销售价不能为空");
        }
        if (price.getListAmount() < 0 || price.getSaleAmount() < 0)
        {
            throw new ServiceException("价格不能为负数");
        }
        if (price.getSaleAmount() > price.getListAmount())
        {
            throw new ServiceException("销售价不能高于划线价");
        }
        if (price.getEffectiveFrom() == null)
        {
            throw new ServiceException("生效开始时间不能为空");
        }
        if (price.getEffectiveTo() != null && !price.getEffectiveTo().after(price.getEffectiveFrom()))
        {
            throw new ServiceException("失效时间必须晚于生效时间");
        }
        price.setPriceId(excludePriceId);
        int overlap = skuPriceMapper.countOverlap(price);
        price.setPriceId(null);
        if (overlap > 0)
        {
            throw new ServiceException("同一维度下价格有效期存在重叠，请调整生效区间");
        }
    }

    /**
     * 修改时以数据库现有值补全未传字段，保证校验基于最终数据。
     */
    private SkuPrice mergeForCheck(SkuPrice price, SkuPrice existing)
    {
        SkuPrice merged = new SkuPrice();
        merged.setSkuId(price.getSkuId() != null ? price.getSkuId() : existing.getSkuId());
        merged.setCountryCode(price.getCountryCode() != null ? price.getCountryCode() : existing.getCountryCode());
        merged.setChannelCode(price.getChannelCode() != null ? price.getChannelCode() : existing.getChannelCode());
        merged.setCurrencyCode(price.getCurrencyCode() != null ? price.getCurrencyCode() : existing.getCurrencyCode());
        merged.setListAmount(price.getListAmount() != null ? price.getListAmount() : existing.getListAmount());
        merged.setSaleAmount(price.getSaleAmount() != null ? price.getSaleAmount() : existing.getSaleAmount());
        merged.setEffectiveFrom(price.getEffectiveFrom() != null ? price.getEffectiveFrom() : existing.getEffectiveFrom());
        merged.setEffectiveTo(price.getEffectiveTo() != null ? price.getEffectiveTo() : existing.getEffectiveTo());
        merged.setStatus(price.getStatus() != null ? price.getStatus() : existing.getStatus());
        return merged;
    }
}
