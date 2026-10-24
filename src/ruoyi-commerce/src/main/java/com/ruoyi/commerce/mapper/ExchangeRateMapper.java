package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.ExchangeRate;

/**
 * 汇率 Mapper。
 *
 * @author ruoyi
 */
public interface ExchangeRateMapper
{
    List<ExchangeRate> selectExchangeRateList(ExchangeRate rate);

    ExchangeRate selectExchangeRateById(@Param("rateId") Long rateId);

    int insertExchangeRate(ExchangeRate rate);

    int updateExchangeRate(ExchangeRate rate);

    int deleteExchangeRateByIds(Long[] rateIds);

    ExchangeRate selectEffective(@Param("baseCurrency") String baseCurrency, @Param("quoteCurrency") String quoteCurrency);
}
