package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;

/**
 * 渠道商品映射 channel_listing
 *
 * @author ruoyi
 */
public class ChannelListing implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 映射主键 */
    @Excel(name = "映射主键", cellType = ColumnType.NUMERIC)
    private Long listingId;

    /** 渠道编码 */
    @Excel(name = "渠道编码")
    private String channelCode;

    /** 渠道商品 ID */
    private String externalProductId;

    /** 渠道 SKU ID */
    private String externalSkuId;

    /** 内部 SKU 主键 */
    private Long skuId;

    /** 刊登状态 DRAFT/PENDING/LIVE/PAUSED/CLOSED */
    @Excel(name = "刊登状态")
    private String listingStatus;

    /** 渠道原始报文 JSON */
    private String payloadJson;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

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

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
