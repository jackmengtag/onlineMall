package com.ruoyi.commerce.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.commerce.domain.Coupon;
import com.ruoyi.commerce.mapper.CouponMapper;
import com.ruoyi.commerce.service.ICouponService;

/**
 * 优惠券服务实现。
 *
 * @author ruoyi
 */
@Service
public class CouponServiceImpl implements ICouponService
{
    private static final String STATUS_ACTIVE = "ACTIVE";

    private static final String TYPE_FIXED = "FIXED";

    private static final String TYPE_PERCENT = "PERCENT";

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public List<Coupon> selectCouponList(Coupon coupon)
    {
        return couponMapper.selectCouponList(coupon);
    }

    @Override
    public Coupon selectCouponById(Long couponId)
    {
        return couponMapper.selectCouponById(couponId);
    }

    @Override
    public int insertCoupon(Coupon coupon)
    {
        if (coupon == null || StringUtils.isEmpty(coupon.getCouponCode()))
        {
            throw new ServiceException("优惠券编码不能为空");
        }
        if (StringUtils.isEmpty(coupon.getStatus()))
        {
            coupon.setStatus(STATUS_ACTIVE);
        }
        if (StringUtils.isEmpty(coupon.getCurrencyCode()))
        {
            coupon.setCurrencyCode("USD");
        }
        if (coupon.getUsedQty() == null)
        {
            coupon.setUsedQty(0);
        }
        if (coupon.getTotalQty() == null)
        {
            coupon.setTotalQty(0);
        }
        if (coupon.getPerUserLimit() == null)
        {
            coupon.setPerUserLimit(1);
        }
        if (coupon.getMinAmount() == null)
        {
            coupon.setMinAmount(0L);
        }
        return couponMapper.insertCoupon(coupon);
    }

    @Override
    public int updateCoupon(Coupon coupon)
    {
        return couponMapper.updateCoupon(coupon);
    }

    @Override
    public int deleteCouponByIds(Long[] couponIds)
    {
        return couponMapper.deleteCouponByIds(couponIds);
    }

    @Override
    public Coupon validateCoupon(String couponCode, long amount, String countryCode, String currencyCode)
    {
        Coupon coupon = StringUtils.isEmpty(couponCode) ? null : couponMapper.selectByCode(couponCode);
        if (coupon == null)
        {
            throw new ServiceException("优惠券不存在：" + couponCode);
        }
        if (!STATUS_ACTIVE.equals(coupon.getStatus()))
        {
            throw new ServiceException("优惠券已停用");
        }
        Date now = new Date();
        if (coupon.getStartTime() != null && now.before(coupon.getStartTime()))
        {
            throw new ServiceException("优惠券尚未生效");
        }
        if (coupon.getEndTime() != null && now.after(coupon.getEndTime()))
        {
            throw new ServiceException("优惠券已过期");
        }
        if (coupon.getTotalQty() != null && coupon.getTotalQty() > 0
                && coupon.getUsedQty() != null && coupon.getUsedQty() >= coupon.getTotalQty())
        {
            throw new ServiceException("优惠券已领完");
        }
        if (coupon.getMinAmount() != null && amount < coupon.getMinAmount())
        {
            throw new ServiceException("订单金额未达到优惠券使用门槛");
        }
        if (StringUtils.isNotEmpty(coupon.getCountryCode()) && StringUtils.isNotEmpty(countryCode)
                && !coupon.getCountryCode().equalsIgnoreCase(countryCode))
        {
            throw new ServiceException("优惠券不适用于当前国家/地区");
        }
        if (StringUtils.isNotEmpty(coupon.getCurrencyCode()) && StringUtils.isNotEmpty(currencyCode)
                && !coupon.getCurrencyCode().equalsIgnoreCase(currencyCode))
        {
            throw new ServiceException("优惠券币种与订单币种不一致");
        }
        return coupon;
    }

    @Override
    public long calculateDiscount(Coupon coupon, long amount)
    {
        if (coupon == null || coupon.getDiscountValue() == null || amount <= 0)
        {
            return 0L;
        }
        long discount;
        if (TYPE_FIXED.equals(coupon.getCouponType()))
        {
            discount = coupon.getDiscountValue();
        }
        else if (TYPE_PERCENT.equals(coupon.getCouponType()))
        {
            discount = amount * coupon.getDiscountValue() / 100L;
        }
        else
        {
            return 0L;
        }
        if (discount < 0)
        {
            return 0L;
        }
        return Math.min(discount, amount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markUsed(String couponCode)
    {
        Coupon coupon = StringUtils.isEmpty(couponCode) ? null : couponMapper.selectByCode(couponCode);
        if (coupon == null)
        {
            return;
        }
        if (couponMapper.incrementUsed(coupon.getCouponId()) == 0)
        {
            throw new ServiceException("优惠券已领完");
        }
    }
}
