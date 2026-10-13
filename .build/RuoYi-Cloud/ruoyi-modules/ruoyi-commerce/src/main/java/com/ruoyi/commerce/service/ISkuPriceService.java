package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.SkuPrice;

/**
 * SKU 价格服务
 *
 * @author ruoyi
 */
public interface ISkuPriceService
{
    SkuPrice selectSkuPriceById(Long priceId);

    List<SkuPrice> selectSkuPriceList(SkuPrice skuPrice);

    int insertSkuPrice(SkuPrice skuPrice);

    int updateSkuPrice(SkuPrice skuPrice);

    int deleteSkuPriceByIds(Long[] priceIds);

    /**
     * 查询当前生效的销售价
     */
    SkuPrice selectSalePrice(Long skuId, String countryCode, String channelCode, String currencyCode);
}
