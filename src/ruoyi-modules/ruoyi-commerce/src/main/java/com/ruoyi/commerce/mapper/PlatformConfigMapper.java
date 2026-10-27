package com.ruoyi.commerce.mapper;

import java.util.List;

import com.ruoyi.commerce.domain.PlatformConfig;

/**
 * 平台设置 Mapper。
 *
 * @author ruoyi
 */
public interface PlatformConfigMapper
{
    List<PlatformConfig> selectList(PlatformConfig config);

    PlatformConfig selectByKey(String configKey);

    int insert(PlatformConfig config);

    int update(PlatformConfig config);
}
