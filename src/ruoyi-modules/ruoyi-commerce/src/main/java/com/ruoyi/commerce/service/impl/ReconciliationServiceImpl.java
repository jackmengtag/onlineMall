package com.ruoyi.commerce.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.mapper.PaymentMapper;
import com.ruoyi.commerce.service.IReconciliationService;

/**
 * 对账服务实现。
 *
 * @author ruoyi
 */
@Service
public class ReconciliationServiceImpl implements IReconciliationService
{
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String STATUS_MATCHED = "MATCHED";
    private static final String STATUS_AMOUNT_MISMATCH = "AMOUNT_MISMATCH";
    private static final String STATUS_ORDER_NOT_PAID = "ORDER_NOT_PAID";
    private static final String STATUS_PAYMENT_NOT_SUCCEEDED = "PAYMENT_NOT_SUCCEEDED";
    private static final String STATUS_ORDER_MISSING = "ORDER_MISSING";
    private static final String STATUS_PENDING = "PENDING";

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public Map<String, Object> reconciliation(String date)
    {
        String statDate = normalizeDate(date);
        List<Map<String, Object>> rows = paymentMapper.selectReconciliationRows(statDate);

        long channelAmount = 0L;
        long systemAmount = 0L;
        int succeededCount = 0;
        int matchedCount = 0;
        int mismatchCount = 0;
        Set<Long> orderIds = new HashSet<>();
        Set<Long> paidOrderIds = new HashSet<>();
        Map<String, Map<String, Object>> providerSummary = new LinkedHashMap<>();

        for (Map<String, Object> row : rows)
        {
            String provider = row.get("provider") == null ? "UNKNOWN" : String.valueOf(row.get("provider"));
            String paymentStatus = row.get("paymentStatus") == null ? "" : String.valueOf(row.get("paymentStatus"));
            String orderPaymentStatus = row.get("orderPaymentStatus") == null
                    ? "" : String.valueOf(row.get("orderPaymentStatus"));
            Long orderId = toLongObject(row.get("orderId"));
            long paymentAmount = toLong(row.get("channelAmount"));
            long orderAmount = toLong(row.get("systemAmount"));
            boolean succeeded = CommerceConstants.PAYMENT_SUCCEEDED.equals(paymentStatus);
            boolean orderPaid = CommerceConstants.PAY_PAID.equals(orderPaymentStatus)
                    || CommerceConstants.PAY_PARTIALLY_REFUNDED.equals(orderPaymentStatus);

            if (orderId != null)
            {
                orderIds.add(orderId);
                if (orderPaid)
                {
                    paidOrderIds.add(orderId);
                }
            }
            if (succeeded)
            {
                succeededCount++;
                channelAmount += paymentAmount;
            }
            if (orderPaid && orderId != null)
            {
                systemAmount += orderAmount;
            }

            Map<String, Object> providerRow = providerSummary.computeIfAbsent(provider, key -> {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("provider", key);
                item.put("paymentCount", 0);
                item.put("channelAmount", 0L);
                item.put("systemAmount", 0L);
                return item;
            });
            providerRow.put("paymentCount", ((Integer) providerRow.get("paymentCount")) + 1);
            if (succeeded)
            {
                providerRow.put("channelAmount", ((Long) providerRow.get("channelAmount")) + paymentAmount);
            }
            if (orderPaid && orderId != null)
            {
                providerRow.put("systemAmount", ((Long) providerRow.get("systemAmount")) + orderAmount);
            }

            String status = resolveStatus(row, succeeded, orderPaid, orderId);
            if (STATUS_MATCHED.equals(status))
            {
                matchedCount++;
            }
            else if (!STATUS_PENDING.equals(status))
            {
                mismatchCount++;
            }
        }

        for (Map<String, Object> providerRow : providerSummary.values())
        {
            long channel = (Long) providerRow.get("channelAmount");
            long system = (Long) providerRow.get("systemAmount");
            providerRow.put("differenceAmount", channel - system);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("date", statDate);
        result.put("paymentCount", rows.size());
        result.put("succeededPaymentCount", succeededCount);
        result.put("orderCount", orderIds.size());
        result.put("paidOrderCount", paidOrderIds.size());
        result.put("channelAmount", channelAmount);
        result.put("systemAmount", systemAmount);
        result.put("differenceAmount", channelAmount - systemAmount);
        result.put("matchedCount", matchedCount);
        result.put("mismatchCount", mismatchCount);
        result.put("providerSummary", new ArrayList<>(providerSummary.values()));
        return result;
    }

    @Override
    public List<Map<String, Object>> selectDetails(String date)
    {
        String statDate = normalizeDate(date);
        List<Map<String, Object>> rows = paymentMapper.selectReconciliationRows(statDate);
        List<Map<String, Object>> details = new ArrayList<>();
        for (Map<String, Object> row : rows)
        {
            String paymentStatus = row.get("paymentStatus") == null ? "" : String.valueOf(row.get("paymentStatus"));
            String orderPaymentStatus = row.get("orderPaymentStatus") == null
                    ? "" : String.valueOf(row.get("orderPaymentStatus"));
            Long orderId = toLongObject(row.get("orderId"));
            boolean succeeded = CommerceConstants.PAYMENT_SUCCEEDED.equals(paymentStatus);
            boolean orderPaid = CommerceConstants.PAY_PAID.equals(orderPaymentStatus)
                    || CommerceConstants.PAY_PARTIALLY_REFUNDED.equals(orderPaymentStatus);

            long channelAmount = succeeded ? toLong(row.get("channelAmount")) : 0L;
            long systemAmount = orderPaid && orderId != null ? toLong(row.get("systemAmount")) : 0L;

            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("paymentNo", row.get("paymentNo"));
            detail.put("orderNo", row.get("orderNo"));
            detail.put("provider", row.get("provider"));
            detail.put("currencyCode", row.get("currencyCode"));
            detail.put("systemAmount", systemAmount);
            detail.put("channelAmount", channelAmount);
            detail.put("difference", channelAmount - systemAmount);
            detail.put("status", resolveStatus(row, succeeded, orderPaid, orderId));
            detail.put("paymentStatus", paymentStatus);
            detail.put("orderPaymentStatus", orderPaymentStatus);
            details.add(detail);
        }
        return details;
    }

    private String resolveStatus(Map<String, Object> row, boolean succeeded, boolean orderPaid, Long orderId)
    {
        if (orderId == null)
        {
            return STATUS_ORDER_MISSING;
        }
        if (succeeded && !orderPaid)
        {
            return STATUS_ORDER_NOT_PAID;
        }
        if (!succeeded && orderPaid)
        {
            return STATUS_PAYMENT_NOT_SUCCEEDED;
        }
        if (succeeded && orderPaid)
        {
            long channel = toLong(row.get("channelAmount"));
            long system = toLong(row.get("systemAmount"));
            return channel == system ? STATUS_MATCHED : STATUS_AMOUNT_MISMATCH;
        }
        return STATUS_PENDING;
    }

    private static String normalizeDate(String date)
    {
        if (StringUtils.isEmpty(date))
        {
            return LocalDate.now().format(DATE_FORMATTER);
        }
        try
        {
            return LocalDate.parse(date, DATE_FORMATTER).format(DATE_FORMATTER);
        }
        catch (DateTimeParseException e)
        {
            throw new ServiceException("日期格式应为 yyyy-MM-dd");
        }
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

    private static Long toLongObject(Object value)
    {
        if (value == null)
        {
            return null;
        }
        if (value instanceof Number)
        {
            return ((Number) value).longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }
}
