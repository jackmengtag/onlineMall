package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.ShopSettlement;

/**
 * 店铺结算单 Mapper。
 *
 * @author ruoyi
 */
public interface ShopSettlementMapper
{
    List<ShopSettlement> selectList(@Param("shopId") Long shopId, @Param("status") String status);

    ShopSettlement selectById(Long settlementId);

    int insert(ShopSettlement settlement);

    int updateStatus(@Param("settlementId") Long settlementId, @Param("status") String status);

    int paid(Long settlementId);
}
