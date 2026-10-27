package com.ruoyi.commerce.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.commerce.domain.AuditLog;
import com.ruoyi.commerce.domain.ChannelConfig;
import com.ruoyi.commerce.domain.PlatformConfig;
import com.ruoyi.commerce.domain.Shop;

/**
 * 平台中心服务。
 *
 * @author ruoyi
 */
public interface IPlatformService
{
    /**
     * 平台驾驶舱（GMV、订单数、店铺数、活跃店铺、渠道数、近 7 日趋势、店铺排行）。
     */
    Map<String, Object> dashboard();

    List<Shop> shops(Shop shop);

    Shop shop(Long shopId);

    int createShop(Shop shop);

    int updateShop(Shop shop);

    int deleteShops(Long[] shopIds);

    int changeStatus(Long shopId, String status);

    /**
     * 平台用户及其店铺绑定。
     */
    List<Map<String, Object>> users(String userName, Long shopId);

    List<AuditLog> audits(Long shopId, String module, Date beginTime, Date endTime);

    /**
     * 记录审计日志。
     */
    void record(AuditLog auditLog);

    List<ChannelConfig> channels(ChannelConfig config);

    ChannelConfig channel(Long channelConfigId);

    int createChannel(ChannelConfig config);

    int updateChannel(ChannelConfig config);

    int deleteChannels(Long[] channelConfigIds);

    List<PlatformConfig> configs(PlatformConfig config);

    int updateConfigs(List<PlatformConfig> configs);
}
