package com.ruoyi.commerce.mapper;

import java.util.List;

import com.ruoyi.commerce.domain.ChannelConfig;

/**
 * 渠道配置 Mapper。
 *
 * @author ruoyi
 */
public interface ChannelConfigMapper
{
    List<ChannelConfig> selectList(ChannelConfig config);

    ChannelConfig selectById(Long channelConfigId);

    ChannelConfig selectByCode(String channelCode);

    int insert(ChannelConfig config);

    int update(ChannelConfig config);

    int deleteByIds(Long[] channelConfigIds);
}
