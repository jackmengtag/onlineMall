package com.ruoyi.commerce.service;

import java.util.Map;

/**
 * 运营看板服务。
 *
 * @author ruoyi
 */
public interface IDashboardService
{
    /**
     * 平台/店铺运营概览。
     *
     * @param shopId 店铺 ID，为空表示全平台
     */
    Map<String, Object> summary(Long shopId);

    /**
     * 店铺经营报表。
     */
    Map<String, Object> shopReport(Long shopId, int days);
}
