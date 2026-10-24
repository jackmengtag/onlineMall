package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.PaymentEvent;

/**
 * 支付回调事件 Mapper。
 *
 * @author ruoyi
 */
public interface PaymentEventMapper
{
    List<PaymentEvent> selectByPaymentId(@Param("paymentId") Long paymentId);

    int countByProviderEvent(@Param("provider") String provider, @Param("providerEventId") String providerEventId);

    int insertPaymentEvent(PaymentEvent event);
}
