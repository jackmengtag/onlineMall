package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.FulfillmentItem;
import com.ruoyi.commerce.domain.FulfillmentOrder;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.Shipment;
import com.ruoyi.commerce.domain.TrackingEvent;

/**
 * 履约与物流服务
 *
 * <p>对应文档第 5.4 节：订单按库存位置与履约能力拆分为一个或多个履约单；
 * 履约单驱动仓库拣货、打包、发货并生成运单；承运商轨迹事件回传驱动订单状态推进。</p>
 *
 * @author ruoyi
 */
public interface IFulfillmentService
{
    /**
     * 按仓库对订单进行履约拆分，创建一个或多个履约单
     *
     * @param order 订单
     * @return 履约单列表
     */
    List<FulfillmentOrder> createFulfillmentForOrder(Orders order);

    /**
     * 查询履约单
     */
    FulfillmentOrder selectFulfillmentById(Long fulfillmentId);

    /**
     * 查询履约单列表
     */
    List<FulfillmentOrder> selectFulfillmentList(FulfillmentOrder fulfillmentOrder);

    /**
     * 查询履约明细
     */
    List<FulfillmentItem> selectFulfillmentItems(Long fulfillmentId);

    /**
     * 更新履约单状态
     */
    int updateFulfillmentStatus(Long fulfillmentId, String toStatus, String reason);

    /**
     * 发货：扣减库存并生成运单
     *
     * @param fulfillmentId 履约单主键
     * @param carrierCode   承运商编码
     * @param trackingNo    物流追踪号
     * @return 运单
     */
    Shipment ship(Long fulfillmentId, String carrierCode, String trackingNo);

    /**
     * 查询运单
     */
    Shipment selectShipmentById(Long shipmentId);

    /**
     * 查询运单列表
     */
    List<Shipment> selectShipmentList(Shipment shipment);

    /**
     * 查询运单轨迹
     */
    List<TrackingEvent> selectTrackingEvents(Long shipmentId);

    /**
     * 回传物流轨迹事件，驱动运单与订单状态推进
     *
     * @param event 轨迹事件
     * @return 影响行数
     */
    int addTrackingEvent(TrackingEvent event);
}
