package com.ruoyi.commerce.mapper;

import java.util.List;

import com.ruoyi.commerce.domain.ShopUser;

/**
 * 店铺成员 Mapper。
 *
 * @author ruoyi
 */
public interface ShopUserMapper
{
    /**
     * 查询用户绑定的店铺 ID。
     */
    Long selectShopIdByUserId(Long userId);

    List<ShopUser> selectByShopId(Long shopId);

    int insert(ShopUser shopUser);

    int delete(Long id);
}
