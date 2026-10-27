package com.ruoyi.commerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.ExchangeRate;
import com.ruoyi.commerce.mapper.ExchangeRateMapper;
import com.ruoyi.commerce.service.IExchangeRateService;

/**
 * 汇率服务实现。
 *
 * @author ruoyi
 */
@Service
public class ExchangeRateServiceImpl implements IExchangeRateService
{
    private static final String STATUS_ACTIVE = "ACTIVE";

    @Autowired
    private ExchangeRateMapper exchangeRateMapper;

    @Override
    public List<ExchangeRate> selectExchangeRateList(ExchangeRate rate)
    {
        return exchangeRateMapper.selectExchangeRateList(rate);
    }

    @Override
    public ExchangeRate selectExchangeRateById(Long rateId)
    {
        return exchangeRateMapper.selectExchangeRateById(rateId);
    }

    @Override
    public int insertExchangeRate(ExchangeRate rate)
    {
        if (StringUtils.isEmpty(rate.getStatus()))
        {
            rate.setStatus(STATUS_ACTIVE);
        }
        return exchangeRateMapper.insertExchangeRate(rate);
    }

    @Override
    public int updateExchangeRate(ExchangeRate rate)
    {
        return exchangeRateMapper.updateExchangeRate(rate);
    }

    @Override
    public int deleteExchangeRateByIds(Long[] rateIds)
    {
        return exchangeRateMapper.deleteExchangeRateByIds(rateIds);
    }

    @Override
    public ExchangeRate getActiveRate(String baseCurrency, String quoteCurrency)
    {
        if (StringUtils.isEmpty(baseCurrency) || StringUtils.isEmpty(quoteCurrency))
        {
            return null;
        }
        return exchangeRateMapper.selectEffective(baseCurrency, quoteCurrency);
    }
}
