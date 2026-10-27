package com.ruoyi.commerce.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.commerce.domain.Shop;
import com.ruoyi.commerce.domain.ShopSettlement;

/**
 * 店铺中心服务。
 *
 * @author ruoyi
 */
public interface IShopCenterService
{
    /**
     * 店铺工作台汇总（订单数/销售额/待发货/退款/商品数/近 7 日趋势）。
     */
    Map<String, Object> dashboard(Long shopId);

    /**
     * 店铺资料。
     */
    Shop profile(Long shopId);

    /**
     * 更新店铺资料（仅资料字段）。
     */
    int updateProfile(Shop shop);

    /**
     * 本店订单去重客户列表。
     */
    List<Map<String, Object>> customers(Long shopId, String keyword);

    List<ShopSettlement> settlements(Long shopId, String status);

    /**
     * 按账期生成结算单。
     */
    ShopSettlement generateSettlement(Long shopId, Date periodStart, Date periodEnd);

    /**
     * 标记结算单已打款。
     */
    int markSettlementPaid(Long shopId, Long settlementId);
}
