package com.ruoyi.commerce.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.FulfillmentItem;
import com.ruoyi.commerce.domain.FulfillmentOrder;
import com.ruoyi.commerce.domain.OrderItem;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.Shipment;
import com.ruoyi.commerce.domain.TrackingEvent;
import com.ruoyi.commerce.mapper.FulfillmentItemMapper;
import com.ruoyi.commerce.mapper.FulfillmentOrderMapper;
import com.ruoyi.commerce.mapper.ShipmentMapper;
import com.ruoyi.commerce.mapper.TrackingEventMapper;
import com.ruoyi.commerce.service.IFulfillmentService;
import com.ruoyi.commerce.service.IInventoryService;
import com.ruoyi.commerce.service.ILocalMessageService;
import com.ruoyi.commerce.service.IOrderService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 履约与物流服务实现
 *
 * <p>对应文档第 5.4 节：订单按库存位置与履约能力拆分为一个或多个履约单；
 * 履约单驱动仓库拣货、打包、发货并生成运单；承运商轨迹事件回传驱动订单状态推进。</p>
 *
 * @author ruoyi
 */
@Service
public class FulfillmentServiceImpl implements IFulfillmentService
{
    /** 履约单合法状态集合 */
    private static final Set<String> FULFILLMENT_STATUSES;

    static
    {
        Set<String> statuses = new LinkedHashSet<>();
        Collections.addAll(statuses, CommerceConstants.FULFILLMENT_CREATED, CommerceConstants.FULFILLMENT_ALLOCATED,
                CommerceConstants.FULFILLMENT_PICKING, CommerceConstants.FULFILLMENT_PACKED,
                CommerceConstants.FULFILLMENT_SHIPPED, CommerceConstants.FULFILLMENT_DELIVERED,
                CommerceConstants.FULFILLMENT_CANCELLED);
        FULFILLMENT_STATUSES = Collections.unmodifiableSet(statuses);
    }

    @Autowired
    private FulfillmentOrderMapper fulfillmentOrderMapper;

    @Autowired
    private FulfillmentItemMapper fulfillmentItemMapper;

    @Autowired
    private ShipmentMapper shipmentMapper;

    @Autowired
    private TrackingEventMapper trackingEventMapper;

    @Lazy
    @Autowired
    private IOrderService orderService;

    @Lazy
    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private ILocalMessageService localMessageService;

    @Override
    @Transactional
    public List<FulfillmentOrder> createFulfillmentForOrder(Orders order)
    {
        if (order == null || order.getOrderId() == null)
        {
            throw new ServiceException("订单不存在，无法创建履约单");
        }
        List<OrderItem> items = orderService.selectOrderItems(order.getOrderId());
        if (items == null || items.isEmpty())
        {
            throw new ServiceException("订单明细为空，无法创建履约单");
        }
        // 按仓库对订单明细分组：优先选择有可用库存的仓库，无可用库存时回退默认仓库
        Map<String, List<OrderItem>> grouped = new LinkedHashMap<>();
        for (OrderItem item : items)
        {
            String warehouseCode = inventoryService.selectWarehouse(item.getSkuId(), item.getQuantity());
            if (StringUtils.isEmpty(warehouseCode))
            {
                warehouseCode = CommerceConstants.DEFAULT_WAREHOUSE;
            }
            grouped.computeIfAbsent(warehouseCode, key -> new ArrayList<>()).add(item);
        }
        Date estimatedShipAt = plusDays(DateUtils.getNowDate(), 1);
        List<FulfillmentOrder> result = new ArrayList<>();
        for (Map.Entry<String, List<OrderItem>> entry : grouped.entrySet())
        {
            FulfillmentOrder fulfillment = new FulfillmentOrder();
            fulfillment.setFulfillmentNo(CommerceNoUtils.fulfillmentNo());
            fulfillment.setOrderId(order.getOrderId());
            fulfillment.setWarehouseCode(entry.getKey());
            fulfillment.setFulfillmentStatus(CommerceConstants.FULFILLMENT_ALLOCATED);
            fulfillment.setAllocationReason("按仓库可用库存拆分");
            fulfillment.setEstimatedShipAt(estimatedShipAt);
            fulfillmentOrderMapper.insertFulfillmentOrder(fulfillment);
            for (OrderItem item : entry.getValue())
            {
                FulfillmentItem fulfillmentItem = new FulfillmentItem();
                fulfillmentItem.setFulfillmentId(fulfillment.getFulfillmentId());
                fulfillmentItem.setOrderItemId(item.getOrderItemId());
                fulfillmentItem.setQuantity(item.getQuantity());
                fulfillmentItemMapper.insertFulfillmentItem(fulfillmentItem);
            }
            result.add(fulfillment);
        }
        orderService.markOrderFulfilling(order.getOrderId());
        localMessageService.publish(CommerceConstants.TOPIC_FULFILLMENT_CREATED, order.getOrderNo(), result);
        return result;
    }

    @Override
    public FulfillmentOrder selectFulfillmentById(Long fulfillmentId)
    {
        return fulfillmentOrderMapper.selectFulfillmentOrderById(fulfillmentId);
    }

    @Override
    public List<FulfillmentOrder> selectFulfillmentList(FulfillmentOrder fulfillmentOrder)
    {
        return fulfillmentOrderMapper.selectFulfillmentOrderList(fulfillmentOrder);
    }

    @Override
    public List<FulfillmentItem> selectFulfillmentItems(Long fulfillmentId)
    {
        return fulfillmentItemMapper.selectItemsByFulfillmentId(fulfillmentId);
    }

    @Override
    @Transactional
    public int updateFulfillmentStatus(Long fulfillmentId, String toStatus, String reason)
    {
        if (StringUtils.isEmpty(toStatus) || !FULFILLMENT_STATUSES.contains(toStatus))
        {
            throw new ServiceException("非法的履约状态：" + toStatus);
        }
        FulfillmentOrder fulfillment = fulfillmentOrderMapper.selectFulfillmentOrderById(fulfillmentId);
        if (fulfillment == null)
        {
            throw new ServiceException("履约单不存在：" + fulfillmentId);
        }
        FulfillmentOrder update = new FulfillmentOrder();
        update.setFulfillmentId(fulfillmentId);
        update.setFulfillmentStatus(toStatus);
        update.setAllocationReason(reason);
        return fulfillmentOrderMapper.updateFulfillmentOrder(update);
    }

    @Override
    @Transactional
    public Shipment ship(Long fulfillmentId, String carrierCode, String trackingNo)
    {
        FulfillmentOrder fulfillment = fulfillmentOrderMapper.selectFulfillmentOrderById(fulfillmentId);
        if (fulfillment == null)
        {
            throw new ServiceException("履约单不存在：" + fulfillmentId);
        }
        String status = fulfillment.getFulfillmentStatus();
        if (CommerceConstants.FULFILLMENT_SHIPPED.equals(status)
                || CommerceConstants.FULFILLMENT_DELIVERED.equals(status)
                || CommerceConstants.FULFILLMENT_CANCELLED.equals(status))
        {
            throw new ServiceException("当前履约状态不可发货");
        }
        Orders order = orderService.selectOrdersById(fulfillment.getOrderId());
        if (order == null)
        {
            throw new ServiceException("履约单关联订单不存在：" + fulfillment.getOrderId());
        }
        String orderNo = order.getOrderNo();
        // 建立订单明细索引，用于取发货明细对应的 SKU
        Map<Long, OrderItem> orderItemMap = new HashMap<>();
        List<OrderItem> orderItems = orderService.selectOrderItems(fulfillment.getOrderId());
        if (orderItems != null)
        {
            for (OrderItem orderItem : orderItems)
            {
                orderItemMap.put(orderItem.getOrderItemId(), orderItem);
            }
        }
        List<FulfillmentItem> fulfillmentItems = fulfillmentItemMapper.selectItemsByFulfillmentId(fulfillmentId);
        if (fulfillmentItems == null || fulfillmentItems.isEmpty())
        {
            throw new ServiceException("履约明细为空，无法发货：" + fulfillmentId);
        }
        Date now = DateUtils.getNowDate();
        for (FulfillmentItem fulfillmentItem : fulfillmentItems)
        {
            OrderItem orderItem = orderItemMap.get(fulfillmentItem.getOrderItemId());
            if (orderItem == null)
            {
                throw new ServiceException("履约明细关联订单明细不存在：" + fulfillmentItem.getOrderItemId());
            }
            boolean deducted = inventoryService.deduct(orderItem.getSkuId(), fulfillment.getWarehouseCode(),
                    fulfillmentItem.getQuantity(), orderNo);
            if (!deducted)
            {
                throw new ServiceException("库存扣减失败，SKU：" + orderItem.getSkuId());
            }
        }
        fulfillmentOrderMapper.updateFulfillmentOrderStatus(fulfillmentId, CommerceConstants.FULFILLMENT_SHIPPED, now, null);

        Shipment shipment = new Shipment();
        shipment.setShipmentNo(CommerceNoUtils.shipmentNo());
        shipment.setFulfillmentId(fulfillmentId);
        shipment.setCarrierCode(carrierCode);
        shipment.setTrackingNo(trackingNo);
        shipment.setShipmentStatus(CommerceConstants.SHIPMENT_IN_TRANSIT);
        shipment.setShippedAt(now);
        shipmentMapper.insertShipment(shipment);

        // markOrderShipped 内部已发布 TOPIC_ORDER_SHIPPED 消息，此处不再重复投递
        orderService.markOrderShipped(fulfillment.getOrderId());
        return shipment;
    }

    @Override
    public Shipment selectShipmentById(Long shipmentId)
    {
        return shipmentMapper.selectShipmentById(shipmentId);
    }

    @Override
    public List<Shipment> selectShipmentList(Shipment shipment)
    {
        return shipmentMapper.selectShipmentList(shipment);
    }

    @Override
    public List<TrackingEvent> selectTrackingEvents(Long shipmentId)
    {
        return trackingEventMapper.selectByShipmentId(shipmentId);
    }

    @Override
    @Transactional
    public int addTrackingEvent(TrackingEvent event)
    {
        if (event == null || event.getShipmentId() == null)
        {
            throw new ServiceException("轨迹事件缺少运单信息");
        }
        Shipment shipment = shipmentMapper.selectShipmentById(event.getShipmentId());
        if (shipment == null)
        {
            throw new ServiceException("运单不存在：" + event.getShipmentId());
        }
        // occurred_at 使用承运商回传的客户端时间，received_at 由 SQL 落库
        int rows = trackingEventMapper.insertTrackingEvent(event);
        String eventCode = event.getEventCode() == null ? "" : event.getEventCode().toUpperCase();
        if (eventCode.contains("DELIVERED") || "SIGNED".equals(eventCode))
        {
            Date now = DateUtils.getNowDate();
            shipmentMapper.updateShipmentStatus(shipment.getShipmentId(), CommerceConstants.SHIPMENT_DELIVERED, now);
            FulfillmentOrder fulfillment = fulfillmentOrderMapper.selectFulfillmentOrderById(shipment.getFulfillmentId());
            if (fulfillment != null)
            {
                fulfillmentOrderMapper.updateFulfillmentOrderStatus(fulfillment.getFulfillmentId(),
                        CommerceConstants.FULFILLMENT_DELIVERED, null, now);
                orderService.markOrderDelivered(fulfillment.getOrderId());
                Orders order = orderService.selectOrdersById(fulfillment.getOrderId());
                String orderNo = order == null ? null : order.getOrderNo();
                localMessageService.publish(CommerceConstants.TOPIC_ORDER_DELIVERED, orderNo, shipment);
            }
        }
        else if (eventCode.contains("EXCEPTION"))
        {
            shipmentMapper.updateShipmentStatus(shipment.getShipmentId(), CommerceConstants.SHIPMENT_EXCEPTION, null);
        }
        return rows;
    }

    private static Date plusDays(Date date, int days)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }
}
