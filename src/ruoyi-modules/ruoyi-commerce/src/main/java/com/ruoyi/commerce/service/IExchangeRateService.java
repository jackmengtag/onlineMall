package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.ExchangeRate;

/**
 * 汇率服务。
 */
public interface IExchangeRateService
{
    List<ExchangeRate> selectExchangeRateList(ExchangeRate rate);

    ExchangeRate selectExchangeRateById(Long rateId);

    int insertExchangeRate(ExchangeRate rate);

    int updateExchangeRate(ExchangeRate rate);

    int deleteExchangeRateByIds(Long[] rateIds);

    ExchangeRate getActiveRate(String baseCurrency, String quoteCurrency);
}
