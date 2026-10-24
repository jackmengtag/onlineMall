package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 渠道配置 channel_config。
 *
 * @author ruoyi
 */
public class ChannelConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 渠道配置主键 */
    private Long channelConfigId;

    /** 渠道编码 */
    private String channelCode;

    /** 渠道名称 */
    private String channelName;

    /** 类型：DIRECT/PLATFORM/SOCIAL */
    private String channelType;

    /** 国家/地区 */
    private String countryCode;

    /** 币种 */
    private String currencyCode;

    /** 接口地址 */
    private String endpoint;

    /** 应用 Key */
    private String appKey;

    /** 状态：ACTIVE/PAUSED/DISABLED */
    private String status;

    /** 最近同步时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastSyncTime;

    public Long getChannelConfigId()
    {
        return channelConfigId;
    }

    public void setChannelConfigId(Long channelConfigId)
    {
        this.channelConfigId = channelConfigId;
    }

    public String getChannelCode()
    {
        return channelCode;
    }

    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public String getChannelType()
    {
        return channelType;
    }

    public void setChannelType(String channelType)
    {
        this.channelType = channelType;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public String getEndpoint()
    {
        return endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    public String getAppKey()
    {
        return appKey;
    }

    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getLastSyncTime()
    {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime)
    {
        this.lastSyncTime = lastSyncTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("channelConfigId", getChannelConfigId())
            .append("channelCode", getChannelCode())
            .append("channelName", getChannelName())
            .append("channelType", getChannelType())
            .append("countryCode", getCountryCode())
            .append("currencyCode", getCurrencyCode())
            .append("status", getStatus())
            .append("lastSyncTime", getLastSyncTime())
            .toString();
    }
}
