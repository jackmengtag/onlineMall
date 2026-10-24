package com.ruoyi.commerce.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.commerce.domain.Shop;
import com.ruoyi.commerce.domain.ShopSettlement;
import com.ruoyi.commerce.mapper.AnalyticsMapper;
import com.ruoyi.commerce.mapper.ShopMapper;
import com.ruoyi.commerce.mapper.ShopSettlementMapper;
import com.ruoyi.commerce.service.IShopCenterService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 店铺中心服务实现。
 *
 * @author ruoyi
 */
@Service
public class ShopCenterServiceImpl implements IShopCenterService
{
    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopSettlementMapper settlementMapper;

    @Autowired
    private AnalyticsMapper analyticsMapper;

    @Override
    public Map<String, Object> dashboard(Long shopId)
    {
        Map<String, Object> summary = analyticsMapper.selectShopSummary(shopId);
        Map<String, Object> result = new LinkedHashMap<>();
        if (summary != null)
        {
            result.putAll(summary);
        }
        result.put("shopId", shopId);
        result.put("trend", fillTrend(analyticsMapper.selectShopTrend(shopId)));
        return result;
    }

    @Override
    public Shop profile(Long shopId)
    {
        if (shopId == null)
        {
            throw new ServiceException("当前账号未绑定店铺");
        }
        Shop shop = shopMapper.selectShopById(shopId);
        if (shop == null)
        {
            throw new ServiceException("店铺不存在");
        }
        return shop;
    }

    @Override
    public int updateProfile(Shop shop)
    {
        if (shop == null || shop.getShopId() == null)
        {
            throw new ServiceException("店铺 ID 不能为空");
        }
        if (StringUtils.isNotEmpty(shop.getShopName()) && StringUtils.isEmpty(shop.getShopName().trim()))
        {
            throw new ServiceException("店铺名称不能为空");
        }
        // 资料接口不允许修改状态与佣金
        shop.setStatus(null);
        shop.setCommissionRate(null);
        shop.setSettledAmount(null);
        shop.setPendingAmount(null);
        shop.setUpdateBy(SecurityUtils.getUsername());
        return shopMapper.updateShop(shop);
    }

    @Override
    public List<Map<String, Object>> customers(Long shopId, String keyword)
    {
        return analyticsMapper.selectShopCustomers(shopId, keyword);
    }

    @Override
    public List<ShopSettlement> settlements(Long shopId, String status)
    {
        return settlementMapper.selectList(shopId, status);
    }

    @Override
    public ShopSettlement generateSettlement(Long shopId, Date periodStart, Date periodEnd)
    {
        if (shopId == null)
        {
            throw new ServiceException("当前账号未绑定店铺");
        }
        if (periodStart == null || periodEnd == null)
        {
            throw new ServiceException("账期开始与结束日期不能为空");
        }
        if (periodStart.after(periodEnd))
        {
            throw new ServiceException("账期开始日期不能晚于结束日期");
        }
        Shop shop = shopMapper.selectShopById(shopId);
        if (shop == null)
        {
            throw new ServiceException("店铺不存在");
        }
        Map<String, Object> summary = analyticsMapper.selectShopPeriodSummary(shopId, periodStart, periodEnd);
        int orderCount = summary == null ? 0 : Convert.toInt(summary.get("orderCount"), 0);
        long gross = summary == null ? 0L : Convert.toLong(summary.get("grossAmount"), 0L);
        BigDecimal rate = shop.getCommissionRate() == null ? BigDecimal.ZERO : shop.getCommissionRate();
        long commission = BigDecimal.valueOf(gross).multiply(rate).setScale(0, RoundingMode.HALF_UP).longValue();
        long net = gross - commission;

        ShopSettlement settlement = new ShopSettlement();
        settlement.setSettlementNo(CommerceNoUtils.settlementNo());
        settlement.setShopId(shopId);
        settlement.setPeriodStart(periodStart);
        settlement.setPeriodEnd(periodEnd);
        settlement.setOrderCount(orderCount);
        settlement.setGrossAmount(gross);
        settlement.setCommissionAmount(commission);
        settlement.setNetAmount(net);
        settlement.setStatus("PENDING");
        settlement.setCreateBy(SecurityUtils.getUsername());
        settlementMapper.insert(settlement);
        return settlement;
    }

    @Override
    public int markSettlementPaid(Long shopId, Long settlementId)
    {
        if (settlementId == null)
        {
            throw new ServiceException("结算单 ID 不能为空");
        }
        ShopSettlement settlement = settlementMapper.selectById(settlementId);
        if (settlement == null)
        {
            throw new ServiceException("结算单不存在");
        }
        if (shopId != null && !shopId.equals(settlement.getShopId()))
        {
            throw new ServiceException("无权操作该结算单");
        }
        if ("PAID".equals(settlement.getStatus()))
        {
            return 0;
        }
        return settlementMapper.paid(settlementId);
    }

    /**
     * 补零近 7 日趋势。
     */
    private List<Map<String, Object>> fillTrend(List<Map<String, Object>> rows)
    {
        Map<String, Map<String, Object>> byDay = new HashMap<>();
        if (rows != null)
        {
            for (Map<String, Object> row : rows)
            {
                Object d = row.get("d");
                if (d != null)
                {
                    byDay.put(String.valueOf(d), row);
                }
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        for (int i = 0; i < 7; i++)
        {
            String day = format.format(calendar.getTime());
            Map<String, Object> row = byDay.get(day);
            if (row == null)
            {
                row = new LinkedHashMap<>();
                row.put("d", day);
                row.put("orders", 0);
                row.put("amount", 0);
            }
            result.add(row);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }
}
