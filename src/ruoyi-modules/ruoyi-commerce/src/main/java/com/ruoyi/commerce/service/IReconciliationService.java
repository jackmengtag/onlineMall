package com.ruoyi.commerce.service;

import java.util.List;
import java.util.Map;

/**
 * 对账服务。
 *
 * @author ruoyi
 */
public interface IReconciliationService
{
    /**
     * 指定日期对账汇总（按支付渠道）。
     */
    Map<String, Object> reconciliation(String date);

    /**
     * 指定日期对账明细。
     */
    List<Map<String, Object>> selectDetails(String date);
}
