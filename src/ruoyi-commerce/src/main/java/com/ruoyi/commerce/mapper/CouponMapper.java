package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.Coupon;

/**
 * 优惠券 Mapper。
 *
 * @author ruoyi
 */
public interface CouponMapper
{
    List<Coupon> selectCouponList(Coupon coupon);

    Coupon selectCouponById(@Param("couponId") Long couponId);

    Coupon selectByCode(@Param("couponCode") String couponCode);

    int insertCoupon(Coupon coupon);

    int updateCoupon(Coupon coupon);

    int deleteCouponByIds(Long[] couponIds);

    int incrementUsed(@Param("couponId") Long couponId);
}
