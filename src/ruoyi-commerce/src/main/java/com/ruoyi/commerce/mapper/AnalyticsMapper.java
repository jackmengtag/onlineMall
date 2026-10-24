package com.ruoyi.commerce.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 店铺中心 / 平台中心统计聚合 Mapper。
 *
 * @author ruoyi
 */
public interface AnalyticsMapper
{
    Map<String, Object> selectShopSummary(@Param("shopId") Long shopId);

    Map<String, Object> selectShopPeriodSummary(@Param("shopId") Long shopId, @Param("beginTime") Date beginTime,
            @Param("endTime") Date endTime);

    List<Map<String, Object>> selectShopTrend(@Param("shopId") Long shopId);

    List<Map<String, Object>> selectShopCustomers(@Param("shopId") Long shopId, @Param("keyword") String keyword);

    Map<String, Object> selectPlatformSummary();

    List<Map<String, Object>> selectPlatformTrend();

    List<Map<String, Object>> selectShopRanking();

    List<Map<String, Object>> selectPlatformUsers(@Param("userName") String userName, @Param("shopId") Long shopId);
}
