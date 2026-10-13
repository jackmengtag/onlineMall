package com.ruoyi.commerce.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.commerce.domain.PaymentEvent;

/**
 * 支付事件数据层
 *
 * @author ruoyi
 */
public interface PaymentEventMapper
{
    /**
     * 按服务商与事件 ID 查询（回调幂等去重）
     */
    PaymentEvent selectByProviderAndEventId(@Param("provider") String provider, @Param("providerEventId") String providerEventId);

    /**
     * 查询支付事件列表
     */
    List<PaymentEvent> selectPaymentEvents(Long paymentId);

    /**
     * 新增支付事件
     */
    int insertPaymentEvent(PaymentEvent paymentEvent);

    /**
     * 修改支付事件
     */
    int updatePaymentEvent(PaymentEvent paymentEvent);
}
