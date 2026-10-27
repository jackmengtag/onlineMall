package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.Coupon;

/**
 * 优惠券服务。
 */
public interface ICouponService
{
    List<Coupon> selectCouponList(Coupon coupon);

    Coupon selectCouponById(Long couponId);

    int insertCoupon(Coupon coupon);

    int updateCoupon(Coupon coupon);

    int deleteCouponByIds(Long[] couponIds);

    Coupon validateCoupon(String couponCode, long amount, String countryCode, String currencyCode);

    long calculateDiscount(Coupon coupon, long amount);

    void markUsed(String couponCode);
}
