package com.ruoyi.commerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.Promotion;
import com.ruoyi.commerce.mapper.PromotionMapper;
import com.ruoyi.commerce.service.IPromotionService;

/**
 * 促销活动服务实现。
 *
 * @author ruoyi
 */
@Service
public class PromotionServiceImpl implements IPromotionService
{
    private static final String STATUS_ACTIVE = "ACTIVE";

    private static final String SCOPE_ALL = "ALL";

    @Autowired
    private PromotionMapper promotionMapper;

    @Override
    public List<Promotion> selectPromotionList(Promotion promotion)
    {
        return promotionMapper.selectPromotionList(promotion);
    }

    @Override
    public Promotion selectPromotionById(Long promotionId)
    {
        return promotionMapper.selectPromotionById(promotionId);
    }

    @Override
    public int insertPromotion(Promotion promotion)
    {
        if (promotion == null || StringUtils.isEmpty(promotion.getPromotionCode()))
        {
            throw new ServiceException("活动编码不能为空");
        }
        if (StringUtils.isEmpty(promotion.getStatus()))
        {
            promotion.setStatus(STATUS_ACTIVE);
        }
        if (StringUtils.isEmpty(promotion.getScopeType()))
        {
            promotion.setScopeType(SCOPE_ALL);
        }
        if (StringUtils.isEmpty(promotion.getCurrencyCode()))
        {
            promotion.setCurrencyCode("USD");
        }
        if (promotion.getDiscountValue() == null)
        {
            promotion.setDiscountValue(0L);
        }
        if (promotion.getThresholdAmount() == null)
        {
            promotion.setThresholdAmount(0L);
        }
        return promotionMapper.insertPromotion(promotion);
    }

    @Override
    public int updatePromotion(Promotion promotion)
    {
        return promotionMapper.updatePromotion(promotion);
    }

    @Override
    public int deletePromotionByIds(Long[] promotionIds)
    {
        return promotionMapper.deletePromotionByIds(promotionIds);
    }
}
