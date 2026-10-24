package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.Shop;

/**
 * 店铺 Mapper。
 *
 * @author ruoyi
 */
public interface ShopMapper
{
    List<Shop> selectShopList(Shop shop);

    Shop selectShopById(Long shopId);

    Shop selectByCode(String shopCode);

    int insertShop(Shop shop);

    int updateShop(Shop shop);

    int deleteShopByIds(Long[] shopIds);

    int updateStatus(@Param("shopId") Long shopId, @Param("status") String status);
}
