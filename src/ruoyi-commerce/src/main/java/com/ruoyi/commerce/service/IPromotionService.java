package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.Promotion;

/**
 * 促销活动服务。
 *
 * @author ruoyi
 */
public interface IPromotionService
{
    List<Promotion> selectPromotionList(Promotion promotion);

    Promotion selectPromotionById(Long promotionId);

    int insertPromotion(Promotion promotion);

    int updatePromotion(Promotion promotion);

    int deletePromotionByIds(Long[] promotionIds);
}
