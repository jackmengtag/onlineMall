package com.ruoyi.commerce.domain.dto;

import java.math.BigDecimal;

/**
 * 税费计算结果。
 */
public class TaxCalculation
{
    private Long taxAmount;
    private BigDecimal rate;
    private String ruleVersion;

    public TaxCalculation()
    {
    }

    public TaxCalculation(Long taxAmount, BigDecimal rate, String ruleVersion)
    {
        this.taxAmount = taxAmount;
        this.rate = rate;
        this.ruleVersion = ruleVersion;
    }

    public Long getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Long taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
    public String getRuleVersion() { return ruleVersion; }
    public void setRuleVersion(String ruleVersion) { this.ruleVersion = ruleVersion; }
}
