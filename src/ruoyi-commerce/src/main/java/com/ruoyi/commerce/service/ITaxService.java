package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.TaxRule;
import com.ruoyi.commerce.domain.dto.TaxCalculation;

/**
 * 税率与税费服务。
 */
public interface ITaxService
{
    List<TaxRule> selectTaxRuleList(TaxRule rule);

    TaxRule selectTaxRuleById(Long ruleId);

    int insertTaxRule(TaxRule rule);

    int updateTaxRule(TaxRule rule);

    int deleteTaxRuleByIds(Long[] ruleIds);

    TaxRule resolveRule(String countryCode, Long categoryId);

    TaxCalculation calculate(long taxableAmount, String countryCode, Long categoryId);
}
