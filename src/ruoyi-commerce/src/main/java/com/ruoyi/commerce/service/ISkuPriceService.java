package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.SkuPrice;

/**
 * 区域价格服务。
 */
public interface ISkuPriceService
{
    List<SkuPrice> selectSkuPriceList(SkuPrice price);

    SkuPrice selectSkuPriceById(Long priceId);

    int insertSkuPrice(SkuPrice price);

    int updateSkuPrice(SkuPrice price);

    int deleteSkuPriceByIds(Long[] priceIds);

    SkuPrice resolvePrice(Long skuId, String countryCode, String channelCode, String currencyCode);
}
