package com.ruoyi.commerce.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 平台设置 platform_config。
 *
 * @author ruoyi
 */
public class PlatformConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置主键 */
    private Long configId;

    /** 配置键 */
    private String configKey;

    /** 配置值 */
    private String configValue;

    /** 分组 */
    private String configGroup;

    public Long getConfigId()
    {
        return configId;
    }

    public void setConfigId(Long configId)
    {
        this.configId = configId;
    }

    public String getConfigKey()
    {
        return configKey;
    }

    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    public String getConfigValue()
    {
        return configValue;
    }

    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getConfigGroup()
    {
        return configGroup;
    }

    public void setConfigGroup(String configGroup)
    {
        this.configGroup = configGroup;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("configKey", getConfigKey())
            .append("configValue", getConfigValue())
            .append("configGroup", getConfigGroup())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
