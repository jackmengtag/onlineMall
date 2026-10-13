package com.ruoyi.commerce.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.commerce.domain.SkuPrice;

/**
 * SKU 价格数据层
 *
 * @author ruoyi
 */
public interface SkuPriceMapper
{
    /**
     * 查询价格
     */
    SkuPrice selectSkuPriceById(Long priceId);

    /**
     * 查询价格列表
     */
    List<SkuPrice> selectSkuPriceList(SkuPrice skuPrice);

    /**
     * 新增价格
     */
    int insertSkuPrice(SkuPrice skuPrice);

    /**
     * 修改价格
     */
    int updateSkuPrice(SkuPrice skuPrice);

    /**
     * 删除价格
     */
    int deleteSkuPriceById(Long priceId);

    /**
     * 批量删除价格
     */
    int deleteSkuPriceByIds(Long[] priceIds);

    /**
     * 查询同一价格维度下有效期存在重叠的记录（区间 [from, to) 相交，effective_to 为 NULL 表示无穷大）
     *
     * @param skuId           SKU 主键
     * @param countryCode     国家二字码
     * @param channelCode     渠道编码
     * @param currencyCode    币种三字码
     * @param effectiveFrom   生效开始时间
     * @param effectiveTo     生效结束时间
     * @param excludePriceId  需排除的价格主键（修改场景）
     * @return 重叠的价格记录
     */
    List<SkuPrice> selectOverlappingPrices(@Param("skuId") Long skuId, @Param("countryCode") String countryCode,
            @Param("channelCode") String channelCode, @Param("currencyCode") String currencyCode,
            @Param("effectiveFrom") Date effectiveFrom, @Param("effectiveTo") Date effectiveTo,
            @Param("excludePriceId") Long excludePriceId);

    /**
     * 查询当前生效的销售价
     *
     * @param skuId        SKU 主键
     * @param countryCode  国家二字码
     * @param channelCode  渠道编码
     * @param currencyCode 币种三字码
     * @return 当前生效的价格记录
     */
    SkuPrice selectSalePrice(@Param("skuId") Long skuId, @Param("countryCode") String countryCode,
            @Param("channelCode") String channelCode, @Param("currencyCode") String currencyCode);
}
