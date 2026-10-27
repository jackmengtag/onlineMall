package com.ruoyi.commerce.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.Shop;
import com.ruoyi.commerce.mapper.ShopMapper;
import com.ruoyi.commerce.mapper.ShopUserMapper;
import com.ruoyi.commerce.service.IShopService;

/**
 * 店铺与数据权限服务实现。
 *
 * @author ruoyi
 */
@Service
public class ShopServiceImpl implements IShopService
{
    private static final List<String> SHOP_STATUS = Arrays.asList("ACTIVE", "SUSPENDED", "CLOSED");

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopUserMapper shopUserMapper;

    @Override
    public List<Shop> selectShopList(Shop shop)
    {
        return shopMapper.selectShopList(shop);
    }

    @Override
    public Shop selectShopById(Long shopId)
    {
        return shopMapper.selectShopById(shopId);
    }

    @Override
    public int insertShop(Shop shop)
    {
        if (StringUtils.isEmpty(shop.getShopCode()))
        {
            throw new ServiceException("店铺编码不能为空");
        }
        if (StringUtils.isEmpty(shop.getShopName()))
        {
            throw new ServiceException("店铺名称不能为空");
        }
        if (shopMapper.selectByCode(shop.getShopCode()) != null)
        {
            throw new ServiceException("店铺编码已存在");
        }
        if (StringUtils.isEmpty(shop.getCurrencyCode()))
        {
            shop.setCurrencyCode("USD");
        }
        if (StringUtils.isEmpty(shop.getShopType()))
        {
            shop.setShopType("SELF");
        }
        if (shop.getCommissionRate() == null)
        {
            shop.setCommissionRate(new BigDecimal("0.0500"));
        }
        if (StringUtils.isEmpty(shop.getStatus()))
        {
            shop.setStatus("ACTIVE");
        }
        if (shop.getSettledAmount() == null)
        {
            shop.setSettledAmount(0L);
        }
        if (shop.getPendingAmount() == null)
        {
            shop.setPendingAmount(0L);
        }
        shop.setCreateBy(SecurityUtils.getUsername());
        return shopMapper.insertShop(shop);
    }

    @Override
    public int updateShop(Shop shop)
    {
        if (shop.getShopId() == null)
        {
            throw new ServiceException("店铺 ID 不能为空");
        }
        if (StringUtils.isNotEmpty(shop.getShopCode()))
        {
            Shop exist = shopMapper.selectByCode(shop.getShopCode());
            if (exist != null && !exist.getShopId().equals(shop.getShopId()))
            {
                throw new ServiceException("店铺编码已存在");
            }
        }
        shop.setUpdateBy(SecurityUtils.getUsername());
        return shopMapper.updateShop(shop);
    }

    @Override
    public int deleteShopByIds(Long[] shopIds)
    {
        if (shopIds == null || shopIds.length == 0)
        {
            throw new ServiceException("请选择要删除的店铺");
        }
        return shopMapper.deleteShopByIds(shopIds);
    }

    @Override
    public int changeStatus(Long shopId, String status)
    {
        if (shopId == null)
        {
            throw new ServiceException("店铺 ID 不能为空");
        }
        if (!SHOP_STATUS.contains(status))
        {
            throw new ServiceException("不支持的店铺状态：" + status);
        }
        return shopMapper.updateStatus(shopId, status);
    }

    @Override
    public Long currentShopId()
    {
        if (isPlatformAdmin())
        {
            return null;
        }
        Long userId = SecurityUtils.getUserId();
        Long shopId = shopUserMapper.selectShopIdByUserId(userId);
        if (shopId == null)
        {
            throw new ServiceException("当前账号未绑定店铺");
        }
        return shopId;
    }

    @Override
    public boolean isPlatformAdmin()
    {
        Long userId = SecurityUtils.getUserId();
        if (Long.valueOf(1L).equals(userId))
        {
            return true;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return loginUser != null && loginUser.getSysUser() != null && loginUser.getSysUser().isAdmin();
    }
}
