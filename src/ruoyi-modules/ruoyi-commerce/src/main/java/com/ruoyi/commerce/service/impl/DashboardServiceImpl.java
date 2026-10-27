package com.ruoyi.commerce.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.commerce.mapper.OrdersMapper;
import com.ruoyi.commerce.mapper.RefundMapper;
import com.ruoyi.commerce.service.IDashboardService;

/**
 * 运营看板服务实现。
 *
 * @author ruoyi
 */
@Service
public class DashboardServiceImpl implements IDashboardService
{
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private RefundMapper refundMapper;

    @Override
    public Map<String, Object> summary(Long shopId)
    {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("shopId", shopId);
        data.put("todaySalesAmount", nullToZero(ordersMapper.selectTodaySalesAmount(shopId)));
        data.put("todayOrderCount", ordersMapper.countTodayOrders(shopId));
        data.put("todayPaidCount", ordersMapper.countTodayPaid(shopId));
        data.put("pendingFulfillmentCount", ordersMapper.countPendingFulfillment(shopId));
        data.put("refundAmount", nullToZero(refundMapper.sumRefundedAmount(shopId, 1)));
        data.put("statusDistribution", ordersMapper.countByStatus(shopId));
        data.put("salesTrend", buildTrend(ordersMapper.selectDailySales(shopId, 7), 7));
        return data;
    }

    @Override
    public Map<String, Object> shopReport(Long shopId, int days)
    {
        int span = days <= 0 ? 7 : days;
        List<Map<String, Object>> rows = ordersMapper.selectDailySales(shopId, span);
        long salesAmount = 0L;
        long orderCount = 0L;
        for (Map<String, Object> row : rows)
        {
            salesAmount += toLong(row.get("salesAmount"));
            orderCount += toLong(row.get("orderCount"));
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("shopId", shopId);
        data.put("days", span);
        data.put("salesAmount", salesAmount);
        data.put("orderCount", orderCount);
        data.put("avgOrderAmount", orderCount == 0 ? 0L : salesAmount / orderCount);
        data.put("refundAmount", nullToZero(refundMapper.sumRefundedAmount(shopId, span)));
        data.put("dailyTrend", buildTrend(rows, span));
        return data;
    }

    private List<Map<String, Object>> buildTrend(List<Map<String, Object>> rows, int days)
    {
        Map<String, Map<String, Object>> indexed = new LinkedHashMap<>();
        if (rows != null)
        {
            for (Map<String, Object> row : rows)
            {
                Object statDate = row.get("statDate");
                if (statDate != null)
                {
                    indexed.put(String.valueOf(statDate), row);
                }
            }
        }
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = days - 1; i >= 0; i--)
        {
            String statDate = today.minusDays(i).format(DATE_FORMATTER);
            Map<String, Object> row = indexed.get(statDate);
            Map<String, Object> point = new LinkedHashMap<>();
            point.put("statDate", statDate);
            point.put("salesAmount", row == null ? 0L : toLong(row.get("salesAmount")));
            point.put("orderCount", row == null ? 0L : toLong(row.get("orderCount")));
            trend.add(point);
        }
        return trend;
    }

    private static long toLong(Object value)
    {
        if (value == null)
        {
            return 0L;
        }
        if (value instanceof Number)
        {
            return ((Number) value).longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }

    private static long nullToZero(Long value)
    {
        return value == null ? 0L : value;
    }
}
