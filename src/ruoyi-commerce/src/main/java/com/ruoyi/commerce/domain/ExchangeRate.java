package com.ruoyi.commerce.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 汇率 exchange_rate。
 *
 * @author ruoyi
 */
public class ExchangeRate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long rateId;
    private String baseCurrency;
    private String quoteCurrency;
    private BigDecimal rate;
    private String version;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveFrom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveTo;

    private String status;

    public Long getRateId() { return rateId; }
    public void setRateId(Long rateId) { this.rateId = rateId; }
    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }
    public String getQuoteCurrency() { return quoteCurrency; }
    public void setQuoteCurrency(String quoteCurrency) { this.quoteCurrency = quoteCurrency; }
    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public Date getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(Date effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public Date getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(Date effectiveTo) { this.effectiveTo = effectiveTo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
