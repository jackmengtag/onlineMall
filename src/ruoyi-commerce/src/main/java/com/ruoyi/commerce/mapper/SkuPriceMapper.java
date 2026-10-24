package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.SkuPrice;

/**
 * SKU 价格数据层。
 *
 * @author ruoyi
 */
public interface SkuPriceMapper
{
    SkuPrice selectSkuPriceById(Long priceId);

    List<SkuPrice> selectSkuPriceList(SkuPrice skuPrice);

    int insertSkuPrice(SkuPrice skuPrice);

    int updateSkuPrice(SkuPrice skuPrice);

    int deleteSkuPriceById(Long priceId);

    int deleteSkuPriceByIds(Long[] priceIds);

    /**
     * 统计同一价格维度下生效区间重叠的记录数。
     */
    int countOverlap(SkuPrice skuPrice);

    /**
     * 查询当前生效价格。
     */
    SkuPrice selectEffectivePrice(@Param("skuId") Long skuId, @Param("countryCode") String countryCode,
            @Param("channelCode") String channelCode, @Param("currencyCode") String currencyCode);
}
