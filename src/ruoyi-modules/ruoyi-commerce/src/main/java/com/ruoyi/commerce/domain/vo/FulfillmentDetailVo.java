package com.ruoyi.commerce.domain.vo;

import java.util.List;

import com.ruoyi.commerce.domain.FulfillmentItem;
import com.ruoyi.commerce.domain.FulfillmentOrder;
import com.ruoyi.commerce.domain.Shipment;

/**
 * 履约单详情。
 */
public class FulfillmentDetailVo
{
    private FulfillmentOrder fulfillment;
    private List<FulfillmentItem> items;
    private List<Shipment> shipments;

    public FulfillmentOrder getFulfillment() { return fulfillment; }
    public void setFulfillment(FulfillmentOrder fulfillment) { this.fulfillment = fulfillment; }
    public List<FulfillmentItem> getItems() { return items; }
    public void setItems(List<FulfillmentItem> items) { this.items = items; }
    public List<Shipment> getShipments() { return shipments; }
    public void setShipments(List<Shipment> shipments) { this.shipments = shipments; }
}
