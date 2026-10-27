package com.ruoyi.commerce.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 店铺结算单 shop_settlement。
 *
 * @author ruoyi
 */
public class ShopSettlement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 结算单主键 */
    private Long settlementId;

    /** 结算单号 */
    private String settlementNo;

    /** 店铺 */
    private Long shopId;

    /** 账期开始 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date periodStart;

    /** 账期结束 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date periodEnd;

    /** 订单数 */
    private Integer orderCount;

    /** 成交总额 */
    private Long grossAmount;

    /** 平台佣金 */
    private Long commissionAmount;

    /** 应结金额 */
    private Long netAmount;

    /** 状态：PENDING/PAID */
    private String status;

    /** 打款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paidAt;

    /** 店铺名称（展示用） */
    private transient String shopName;

    public Long getSettlementId()
    {
        return settlementId;
    }

    public void setSettlementId(Long settlementId)
    {
        this.settlementId = settlementId;
    }

    public String getSettlementNo()
    {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo)
    {
        this.settlementNo = settlementNo;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Date getPeriodStart()
    {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart)
    {
        this.periodStart = periodStart;
    }

    public Date getPeriodEnd()
    {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd)
    {
        this.periodEnd = periodEnd;
    }

    public Integer getOrderCount()
    {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount)
    {
        this.orderCount = orderCount;
    }

    public Long getGrossAmount()
    {
        return grossAmount;
    }

    public void setGrossAmount(Long grossAmount)
    {
        this.grossAmount = grossAmount;
    }

    public Long getCommissionAmount()
    {
        return commissionAmount;
    }

    public void setCommissionAmount(Long commissionAmount)
    {
        this.commissionAmount = commissionAmount;
    }

    public Long getNetAmount()
    {
        return netAmount;
    }

    public void setNetAmount(Long netAmount)
    {
        this.netAmount = netAmount;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getPaidAt()
    {
        return paidAt;
    }

    public void setPaidAt(Date paidAt)
    {
        this.paidAt = paidAt;
    }

    public String getShopName()
    {
        return shopName;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("settlementId", getSettlementId())
            .append("settlementNo", getSettlementNo())
            .append("shopId", getShopId())
            .append("periodStart", getPeriodStart())
            .append("periodEnd", getPeriodEnd())
            .append("orderCount", getOrderCount())
            .append("grossAmount", getGrossAmount())
            .append("commissionAmount", getCommissionAmount())
            .append("netAmount", getNetAmount())
            .append("status", getStatus())
            .append("paidAt", getPaidAt())
            .toString();
    }
}
