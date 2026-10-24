package com.ruoyi.commerce.service.impl;

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

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.commerce.domain.AuditLog;
import com.ruoyi.commerce.domain.ChannelConfig;
import com.ruoyi.commerce.domain.PlatformConfig;
import com.ruoyi.commerce.domain.Shop;
import com.ruoyi.commerce.mapper.AnalyticsMapper;
import com.ruoyi.commerce.mapper.AuditLogMapper;
import com.ruoyi.commerce.mapper.ChannelConfigMapper;
import com.ruoyi.commerce.mapper.PlatformConfigMapper;
import com.ruoyi.commerce.service.IPlatformService;
import com.ruoyi.commerce.service.IShopService;

/**
 * 平台中心服务实现。
 *
 * @author ruoyi
 */
@Service
public class PlatformServiceImpl implements IPlatformService
{
    @Autowired
    private IShopService shopService;

    @Autowired
    private AnalyticsMapper analyticsMapper;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private ChannelConfigMapper channelConfigMapper;

    @Autowired
    private PlatformConfigMapper platformConfigMapper;

    @Override
    public Map<String, Object> dashboard()
    {
        Map<String, Object> summary = analyticsMapper.selectPlatformSummary();
        Map<String, Object> result = new LinkedHashMap<>();
        if (summary != null)
        {
            result.putAll(summary);
        }
        result.put("trend", fillTrend(analyticsMapper.selectPlatformTrend()));
        result.put("shopRanking", analyticsMapper.selectShopRanking());
        return result;
    }

    @Override
    public List<Shop> shops(Shop shop)
    {
        return shopService.selectShopList(shop);
    }

    @Override
    public Shop shop(Long shopId)
    {
        Shop shop = shopService.selectShopById(shopId);
        if (shop == null)
        {
            throw new ServiceException("店铺不存在");
        }
        return shop;
    }

    @Override
    public int createShop(Shop shop)
    {
        int rows = shopService.insertShop(shop);
        record(shop.getShopId(), "SHOP", "CREATE", "SHOP", String.valueOf(shop.getShopId()),
                "新增店铺 " + shop.getShopName());
        return rows;
    }

    @Override
    public int updateShop(Shop shop)
    {
        int rows = shopService.updateShop(shop);
        record(shop.getShopId(), "SHOP", "UPDATE", "SHOP", String.valueOf(shop.getShopId()),
                "更新店铺 " + shop.getShopName());
        return rows;
    }

    @Override
    public int deleteShops(Long[] shopIds)
    {
        int rows = shopService.deleteShopByIds(shopIds);
        if (shopIds != null)
        {
            for (Long shopId : shopIds)
            {
                record(shopId, "SHOP", "DELETE", "SHOP", String.valueOf(shopId), "删除店铺");
            }
        }
        return rows;
    }

    @Override
    public int changeStatus(Long shopId, String status)
    {
        int rows = shopService.changeStatus(shopId, status);
        record(shopId, "SHOP", "STATUS", "SHOP", String.valueOf(shopId), "店铺状态调整为 " + status);
        return rows;
    }

    @Override
    public List<Map<String, Object>> users(String userName, Long shopId)
    {
        return analyticsMapper.selectPlatformUsers(userName, shopId);
    }

    @Override
    public List<AuditLog> audits(Long shopId, String module, Date beginTime, Date endTime)
    {
        return auditLogMapper.selectList(shopId, module, beginTime, endTime);
    }

    @Override
    public void record(AuditLog auditLog)
    {
        if (auditLog == null)
        {
            return;
        }
        if (StringUtils.isEmpty(auditLog.getOperator()))
        {
            try
            {
                auditLog.setOperator(SecurityUtils.getUsername());
            }
            catch (Exception e)
            {
                auditLog.setOperator("system");
            }
        }
        if (StringUtils.isEmpty(auditLog.getIp()))
        {
            auditLog.setIp(IpUtils.getIpAddr());
        }
        if (StringUtils.isEmpty(auditLog.getResult()))
        {
            auditLog.setResult("SUCCESS");
        }
        auditLogMapper.insert(auditLog);
    }

    @Override
    public List<ChannelConfig> channels(ChannelConfig config)
    {
        return channelConfigMapper.selectList(config);
    }

    @Override
    public ChannelConfig channel(Long channelConfigId)
    {
        ChannelConfig config = channelConfigMapper.selectById(channelConfigId);
        if (config == null)
        {
            throw new ServiceException("渠道配置不存在");
        }
        return config;
    }

    @Override
    public int createChannel(ChannelConfig config)
    {
        if (StringUtils.isEmpty(config.getChannelCode()) || StringUtils.isEmpty(config.getChannelName())
                || StringUtils.isEmpty(config.getChannelType()))
        {
            throw new ServiceException("渠道编码、名称、类型不能为空");
        }
        if (channelConfigMapper.selectByCode(config.getChannelCode()) != null)
        {
            throw new ServiceException("渠道编码已存在");
        }
        if (StringUtils.isEmpty(config.getCurrencyCode()))
        {
            config.setCurrencyCode("USD");
        }
        if (StringUtils.isEmpty(config.getStatus()))
        {
            config.setStatus("ACTIVE");
        }
        config.setCreateBy(SecurityUtils.getUsername());
        int rows = channelConfigMapper.insert(config);
        record(null, "CHANNEL", "CREATE", "CHANNEL", String.valueOf(config.getChannelConfigId()),
                "新增渠道 " + config.getChannelName());
        return rows;
    }

    @Override
    public int updateChannel(ChannelConfig config)
    {
        if (config.getChannelConfigId() == null)
        {
            throw new ServiceException("渠道配置 ID 不能为空");
        }
        if (StringUtils.isNotEmpty(config.getChannelCode()))
        {
            ChannelConfig exist = channelConfigMapper.selectByCode(config.getChannelCode());
            if (exist != null && !exist.getChannelConfigId().equals(config.getChannelConfigId()))
            {
                throw new ServiceException("渠道编码已存在");
            }
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        int rows = channelConfigMapper.update(config);
        record(null, "CHANNEL", "UPDATE", "CHANNEL", String.valueOf(config.getChannelConfigId()),
                "更新渠道 " + config.getChannelName());
        return rows;
    }

    @Override
    public int deleteChannels(Long[] channelConfigIds)
    {
        if (channelConfigIds == null || channelConfigIds.length == 0)
        {
            throw new ServiceException("请选择要删除的渠道配置");
        }
        int rows = channelConfigMapper.deleteByIds(channelConfigIds);
        for (Long id : channelConfigIds)
        {
            record(null, "CHANNEL", "DELETE", "CHANNEL", String.valueOf(id), "删除渠道配置");
        }
        return rows;
    }

    @Override
    public List<PlatformConfig> configs(PlatformConfig config)
    {
        return platformConfigMapper.selectList(config);
    }

    @Override
    public int updateConfigs(List<PlatformConfig> configs)
    {
        if (configs == null || configs.isEmpty())
        {
            return 0;
        }
        int rows = 0;
        for (PlatformConfig config : configs)
        {
            if (StringUtils.isEmpty(config.getConfigKey()))
            {
                continue;
            }
            config.setUpdateBy(SecurityUtils.getUsername());
            if (config.getConfigId() == null)
            {
                PlatformConfig exist = platformConfigMapper.selectByKey(config.getConfigKey());
                if (exist != null)
                {
                    config.setConfigId(exist.getConfigId());
                    rows += platformConfigMapper.update(config);
                }
                else
                {
                    config.setCreateBy(SecurityUtils.getUsername());
                    if (StringUtils.isEmpty(config.getConfigGroup()))
                    {
                        config.setConfigGroup("GENERAL");
                    }
                    rows += platformConfigMapper.insert(config);
                }
            }
            else
            {
                rows += platformConfigMapper.update(config);
            }
        }
        record(null, "CONFIG", "UPDATE", "CONFIG", "platform", "批量更新平台设置 " + configs.size() + " 项");
        return rows;
    }

    private void record(Long shopId, String module, String action, String targetType, String targetId, String detail)
    {
        AuditLog log = new AuditLog();
        log.setShopId(shopId);
        log.setModule(module);
        log.setAction(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setDetail(detail);
        log.setResult("SUCCESS");
        record(log);
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
