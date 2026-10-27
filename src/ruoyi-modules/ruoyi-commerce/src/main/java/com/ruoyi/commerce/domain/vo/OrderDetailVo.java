package com.ruoyi.commerce.domain.vo;

import java.util.List;

import com.ruoyi.commerce.domain.OrderItem;
import com.ruoyi.commerce.domain.OrderItemSnapshot;
import com.ruoyi.commerce.domain.OrderSnapshot;
import com.ruoyi.commerce.domain.OrderStateHistory;
import com.ruoyi.commerce.domain.Orders;

/**
 * 订单详情视图。
 */
public class OrderDetailVo
{
    private Orders order;
    private List<OrderItem> items;
    private List<OrderItemSnapshot> itemSnapshots;
    private OrderSnapshot snapshot;
    private List<OrderStateHistory> histories;

    public Orders getOrder() { return order; }
    public void setOrder(Orders order) { this.order = order; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public List<OrderItemSnapshot> getItemSnapshots() { return itemSnapshots; }
    public void setItemSnapshots(List<OrderItemSnapshot> itemSnapshots) { this.itemSnapshots = itemSnapshots; }
    public OrderSnapshot getSnapshot() { return snapshot; }
    public void setSnapshot(OrderSnapshot snapshot) { this.snapshot = snapshot; }
    public List<OrderStateHistory> getHistories() { return histories; }
    public void setHistories(List<OrderStateHistory> histories) { this.histories = histories; }
}
