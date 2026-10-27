package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.FulfillmentOrder;
import com.ruoyi.commerce.domain.Shipment;
import com.ruoyi.commerce.domain.vo.FulfillmentDetailVo;

/**
 * 履约服务。
 *
 * @author ruoyi
 */
public interface IFulfillmentService
{
    List<FulfillmentOrder> selectFulfillmentList(FulfillmentOrder fulfillmentOrder);

    FulfillmentDetailVo selectFulfillmentDetail(Long fulfillmentId);

    /**
     * 按订单锁定仓拆单，返回新建履约单数量。
     */
    int splitOrder(Long orderId);

    /**
     * 变更履约状态。
     */
    void changeStatus(Long fulfillmentId, String toStatus);

    /**
     * 履约单发货。
     */
    Shipment ship(Long fulfillmentId, String carrierCode, String trackingNo);
}
