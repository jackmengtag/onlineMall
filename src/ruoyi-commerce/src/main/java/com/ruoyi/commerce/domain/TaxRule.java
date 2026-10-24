package com.ruoyi.commerce.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 税率规则 tax_rule。
 *
 * @author ruoyi
 */
public class TaxRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long ruleId;
    private String countryCode;
    private Long categoryId;
    private String taxType;
    private String ruleName;
    private BigDecimal rate;
    private String version;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveFrom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveTo;

    private String status;

    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
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
