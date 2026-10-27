package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.TaxRule;

/**
 * 税率规则 Mapper。
 *
 * @author ruoyi
 */
public interface TaxRuleMapper
{
    List<TaxRule> selectTaxRuleList(TaxRule rule);

    TaxRule selectTaxRuleById(@Param("ruleId") Long ruleId);

    int insertTaxRule(TaxRule rule);

    int updateTaxRule(TaxRule rule);

    int deleteTaxRuleByIds(Long[] ruleIds);

    TaxRule selectEffective(@Param("countryCode") String countryCode, @Param("categoryId") Long categoryId);
}
