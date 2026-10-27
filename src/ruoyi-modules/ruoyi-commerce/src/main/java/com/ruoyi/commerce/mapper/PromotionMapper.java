package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.Promotion;

/**
 * 促销活动 Mapper。
 *
 * @author ruoyi
 */
public interface PromotionMapper
{
    List<Promotion> selectPromotionList(Promotion promotion);

    Promotion selectPromotionById(@Param("promotionId") Long promotionId);

    int insertPromotion(Promotion promotion);

    int updatePromotion(Promotion promotion);

    int deletePromotionByIds(Long[] promotionIds);
}
