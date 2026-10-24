package com.ruoyi.commerce.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 渠道商品映射 channel_listing。
 *
 * @author ruoyi
 */
public class ChannelListing extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 映射主键 */
    private Long listingId;

    /** 渠道编码 */
    private String channelCode;

    /** 外部商品 ID */
    private String externalProductId;

    /** 外部 SKU ID */
    private String externalSkuId;

    /** 内部 SKU */
    private Long skuId;

    /** 状态 DRAFT/PENDING/LIVE/PAUSED/CLOSED */
    private String listingStatus;

    /** 渠道报文 JSON */
    private String payloadJson;

    /** 最近同步时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastSyncTime;

    /** SKU 编码（联查，非表字段） */
    private String skuCode;

    public Long getListingId()
    {
        return listingId;
    }

    public void setListingId(Long listingId)
    {
        this.listingId = listingId;
    }

    public String getChannelCode()
    {
        return channelCode;
    }

    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
    }

    public String getExternalProductId()
    {
        return externalProductId;
    }

    public void setExternalProductId(String externalProductId)
    {
        this.externalProductId = externalProductId;
    }

    public String getExternalSkuId()
    {
        return externalSkuId;
    }

    public void setExternalSkuId(String externalSkuId)
    {
        this.externalSkuId = externalSkuId;
    }

    public Long getSkuId()
    {
        return skuId;
    }

    public void setSkuId(Long skuId)
    {
        this.skuId = skuId;
    }

    public String getListingStatus()
    {
        return listingStatus;
    }

    public void setListingStatus(String listingStatus)
    {
        this.listingStatus = listingStatus;
    }

    public String getPayloadJson()
    {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson)
    {
        this.payloadJson = payloadJson;
    }

    public Date getLastSyncTime()
    {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime)
    {
        this.lastSyncTime = lastSyncTime;
    }

    public String getSkuCode()
    {
        return skuCode;
    }

    public void setSkuCode(String skuCode)
    {
        this.skuCode = skuCode;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("listingId", getListingId())
            .append("channelCode", getChannelCode())
            .append("externalProductId", getExternalProductId())
            .append("externalSkuId", getExternalSkuId())
            .append("skuId", getSkuId())
            .append("listingStatus", getListingStatus())
            .append("lastSyncTime", getLastSyncTime())
            .toString();
    }
}
