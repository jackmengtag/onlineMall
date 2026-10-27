package com.ruoyi.commerce.domain.vo;

import java.util.List;

import com.ruoyi.commerce.domain.Shipment;
import com.ruoyi.commerce.domain.TrackingEvent;

/**
 * 运单详情。
 */
public class ShipmentDetailVo
{
    private Shipment shipment;
    private List<TrackingEvent> trackingEvents;

    public Shipment getShipment() { return shipment; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }
    public List<TrackingEvent> getTrackingEvents() { return trackingEvents; }
    public void setTrackingEvents(List<TrackingEvent> trackingEvents) { this.trackingEvents = trackingEvents; }
}
