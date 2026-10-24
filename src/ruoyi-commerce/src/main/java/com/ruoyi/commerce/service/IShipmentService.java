package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.Shipment;
import com.ruoyi.commerce.domain.TrackingEvent;
import com.ruoyi.commerce.domain.vo.ShipmentDetailVo;

/**
 * 物流运单服务。
 *
 * @author ruoyi
 */
public interface IShipmentService
{
    List<Shipment> selectShipmentList(Shipment shipment);

    ShipmentDetailVo selectShipmentDetail(Long shipmentId);

    Shipment createShipment(Long fulfillmentId, String carrierCode, String trackingNo);

    void addTrackingEvent(TrackingEvent event);

    void markDelivered(Long shipmentId);
}
