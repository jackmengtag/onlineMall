package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.Shop;

/**
 * 店铺与数据权限服务。
 */
public interface IShopService
{
    List<Shop> selectShopList(Shop shop);

    Shop selectShopById(Long shopId);

    int insertShop(Shop shop);

    int updateShop(Shop shop);

    int deleteShopByIds(Long[] shopIds);

    int changeStatus(Long shopId, String status);

    /**
     * 当前登录用户所属店铺 ID；平台管理员返回 null（表示可见全部店铺）。
     */
    Long currentShopId();

    /**
     * 当前登录用户是否平台管理员。
     */
    boolean isPlatformAdmin();
}
