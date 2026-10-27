package com.ruoyi.commerce.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.TaxRule;
import com.ruoyi.commerce.domain.dto.TaxCalculation;
import com.ruoyi.commerce.mapper.TaxRuleMapper;
import com.ruoyi.commerce.service.ITaxService;

/**
 * 税率与税费服务实现。
 *
 * @author ruoyi
 */
@Service
public class TaxServiceImpl implements ITaxService
{
    private static final String STATUS_ACTIVE = "ACTIVE";

    private static final String TYPE_VAT = "VAT";

    @Autowired
    private TaxRuleMapper taxRuleMapper;

    @Override
    public List<TaxRule> selectTaxRuleList(TaxRule rule)
    {
        return taxRuleMapper.selectTaxRuleList(rule);
    }

    @Override
    public TaxRule selectTaxRuleById(Long ruleId)
    {
        return taxRuleMapper.selectTaxRuleById(ruleId);
    }

    @Override
    public int insertTaxRule(TaxRule rule)
    {
        if (StringUtils.isEmpty(rule.getStatus()))
        {
            rule.setStatus(STATUS_ACTIVE);
        }
        if (StringUtils.isEmpty(rule.getTaxType()))
        {
            rule.setTaxType(TYPE_VAT);
        }
        return taxRuleMapper.insertTaxRule(rule);
    }

    @Override
    public int updateTaxRule(TaxRule rule)
    {
        return taxRuleMapper.updateTaxRule(rule);
    }

    @Override
    public int deleteTaxRuleByIds(Long[] ruleIds)
    {
        return taxRuleMapper.deleteTaxRuleByIds(ruleIds);
    }

    @Override
    public TaxRule resolveRule(String countryCode, Long categoryId)
    {
        if (StringUtils.isEmpty(countryCode))
        {
            return null;
        }
        return taxRuleMapper.selectEffective(countryCode, categoryId);
    }

    @Override
    public TaxCalculation calculate(long taxableAmount, String countryCode, Long categoryId)
    {
        TaxRule rule = resolveRule(countryCode, categoryId);
        if (rule == null || rule.getRate() == null || taxableAmount <= 0)
        {
            return new TaxCalculation(0L, BigDecimal.ZERO, "NONE");
        }
        long taxAmount = BigDecimal.valueOf(taxableAmount)
                .multiply(rule.getRate())
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();
        return new TaxCalculation(taxAmount, rule.getRate(), rule.getVersion());
    }
}
