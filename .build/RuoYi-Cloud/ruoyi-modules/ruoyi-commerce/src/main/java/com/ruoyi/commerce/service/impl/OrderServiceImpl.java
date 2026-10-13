package com.ruoyi.commerce.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.OrderItem;
import com.ruoyi.commerce.domain.OrderItemSnapshot;
import com.ruoyi.commerce.domain.OrderSnapshot;
import com.ruoyi.commerce.domain.OrderStateHistory;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.dto.CreateOrderItemRequest;
import com.ruoyi.commerce.domain.dto.CreateOrderRequest;
import com.ruoyi.commerce.mapper.OrderItemMapper;
import com.ruoyi.commerce.mapper.OrderItemSnapshotMapper;
import com.ruoyi.commerce.mapper.OrderSnapshotMapper;
import com.ruoyi.commerce.mapper.OrderStateHistoryMapper;
import com.ruoyi.commerce.mapper.OrdersMapper;
import com.ruoyi.commerce.service.IInventoryService;
import com.ruoyi.commerce.service.ILocalMessageService;
import com.ruoyi.commerce.service.IOrderService;
import com.ruoyi.commerce.service.IPaymentService;
import com.ruoyi.commerce.statemachine.OrderStateMachine;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 订单服务实现
 *
 * <p>对应文档第 5.1、5.2 节：下单流程（订单主记录 + 明细 + 快照 + 锁库存 + 生成支付单）
 * 与订单状态机（四正交状态校验 + 状态历史落库）。</p>
 *
 * @author ruoyi
 */
@Service
public class OrderServiceImpl implements IOrderService
{
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderItemSnapshotMapper orderItemSnapshotMapper;

    @Autowired
    private OrderSnapshotMapper orderSnapshotMapper;

    @Autowired
    private OrderStateHistoryMapper orderStateHistoryMapper;

    @Lazy
    @Autowired
    private IInventoryService inventoryService;

    @Lazy
    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private ILocalMessageService localMessageService;

    /**
     * 下单
     */
    @Override
    @Transactional
    public Orders createOrder(CreateOrderRequest request)
    {
        String orderNo = CommerceNoUtils.orderNo();
        String channelCode = StringUtils.isEmpty(request.getChannelCode())
                ? CommerceConstants.DEFAULT_CHANNEL : request.getChannelCode();

        long subtotal = 0L;
        long itemDiscount = 0L;
        long itemTax = 0L;

        List<CreateOrderItemRequest> itemRequests = request.getItems();
        List<OrderItem> items = new ArrayList<>();
        for (CreateOrderItemRequest itemRequest : itemRequests)
        {
            long unitPrice = itemRequest.getUnitPriceAmount() == null ? 0L : itemRequest.getUnitPriceAmount();
            int quantity = itemRequest.getQuantity() == null ? 0 : itemRequest.getQuantity();
            long discount = itemRequest.getDiscountAmount() == null ? 0L : itemRequest.getDiscountAmount();
            long tax = itemRequest.getTaxAmount() == null ? 0L : itemRequest.getTaxAmount();
            long lineSubtotal = unitPrice * quantity;
            long lineTotal = lineSubtotal - discount + tax;

            OrderItem item = new OrderItem();
            item.setSkuId(itemRequest.getSkuId());
            item.setSkuCode(itemRequest.getSkuCode());
            item.setQuantity(quantity);
            item.setUnitPriceAmount(unitPrice);
            item.setDiscountAmount(discount);
            item.setTaxAmount(tax);
            item.setLineTotalAmount(lineTotal);
            item.setCurrencyCode(request.getCurrencyCode());
            items.add(item);

            subtotal += lineSubtotal;
            itemDiscount += discount;
            itemTax += tax;
        }

        long shippingAmount = request.getShippingAmount() == null ? 0L : request.getShippingAmount();
        long requestDiscount = request.getDiscountAmount() == null ? 0L : request.getDiscountAmount();
        long requestTax = request.getTaxAmount() == null ? 0L : request.getTaxAmount();
        long discountAmount = requestDiscount + itemDiscount;
        long taxAmount = requestTax + itemTax;
        long totalAmount = subtotal - discountAmount + shippingAmount + taxAmount;

        Orders order = new Orders();
        order.setOrderNo(orderNo);
        order.setCustomerId(request.getCustomerId());
        order.setChannelCode(channelCode);
        order.setExternalOrderId(request.getExternalOrderId());
        order.setCountryCode(request.getCountryCode());
        order.setCurrencyCode(request.getCurrencyCode());
        order.setSubtotalAmount(subtotal);
        order.setDiscountAmount(discountAmount);
        order.setShippingAmount(shippingAmount);
        order.setTaxAmount(taxAmount);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(CommerceConstants.ORDER_CREATED);
        order.setPaymentStatus(CommerceConstants.PAY_UNPAID);
        order.setFulfillmentStatus(CommerceConstants.FULFILL_UNFULFILLED);
        order.setAfterSaleStatus(CommerceConstants.AFTER_SALE_NONE);
        order.setPlacedAt(new Date());
        ordersMapper.insertOrders(order);

        Long orderId = order.getOrderId();
        for (int i = 0; i < items.size(); i++)
        {
            OrderItem item = items.get(i);
            item.setOrderId(orderId);
            orderItemMapper.insertOrderItem(item);

            CreateOrderItemRequest itemRequest = itemRequests.get(i);
            OrderItemSnapshot itemSnapshot = new OrderItemSnapshot();
            itemSnapshot.setOrderItemId(item.getOrderItemId());
            itemSnapshot.setProductName(itemRequest.getProductName());
            itemSnapshot.setSkuCode(itemRequest.getSkuCode());
            itemSnapshot.setAttributesJson(StringUtils.isEmpty(itemRequest.getAttributesJson())
                    ? "{}" : itemRequest.getAttributesJson());
            itemSnapshot.setImageUrl(itemRequest.getImageUrl());
            itemSnapshot.setOriginCountryCode(itemRequest.getOriginCountryCode());
            itemSnapshot.setHsCode(itemRequest.getHsCode());
            itemSnapshot.setDeclaredName(itemRequest.getDeclaredName());
            itemSnapshot.setDeclaredValueAmount(itemRequest.getDeclaredValueAmount());
            itemSnapshot.setDeclaredCurrencyCode(itemRequest.getDeclaredCurrencyCode());
            itemSnapshot.setUnitPriceAmount(itemRequest.getUnitPriceAmount());
            itemSnapshot.setCurrencyCode(request.getCurrencyCode());
            orderItemSnapshotMapper.insertOrderItemSnapshot(itemSnapshot);
        }

        OrderSnapshot orderSnapshot = new OrderSnapshot();
        orderSnapshot.setOrderId(orderId);
        orderSnapshot.setBuyerName(request.getBuyerName());
        orderSnapshot.setBuyerEmail(request.getBuyerEmail());
        orderSnapshot.setRecipientName(request.getRecipientName());
        orderSnapshot.setRecipientPhone(request.getRecipientPhone());
        orderSnapshot.setCountryCode(request.getCountryCode());
        orderSnapshot.setStateCode(request.getStateCode());
        orderSnapshot.setCity(request.getCity());
        orderSnapshot.setDistrict(request.getDistrict());
        orderSnapshot.setAddressLine1(request.getAddressLine1());
        orderSnapshot.setAddressLine2(request.getAddressLine2());
        orderSnapshot.setPostalCode(request.getPostalCode());
        orderSnapshot.setTaxRuleVersion(request.getTaxRuleVersion());
        orderSnapshot.setExchangeRateVersion(request.getExchangeRateVersion());
        orderSnapshot.setExchangeRate(request.getExchangeRate());
        orderSnapshot.setRawAddressJson(StringUtils.isEmpty(request.getRawAddressJson())
                ? "{}" : request.getRawAddressJson());
        orderSnapshotMapper.insertOrderSnapshot(orderSnapshot);

        for (OrderItem item : items)
        {
            if (item.getSkuId() == null)
            {
                continue;
            }
            int quantity = item.getQuantity();
            String warehouse = inventoryService.selectWarehouse(item.getSkuId(), quantity);
            if (warehouse == null)
            {
                warehouse = CommerceConstants.DEFAULT_WAREHOUSE;
            }
            if (!inventoryService.lock(item.getSkuId(), warehouse, quantity, orderNo))
            {
                throw new ServiceException("库存不足，SKU:" + item.getSkuCode());
            }
        }

        changeStatus(orderId, CommerceConstants.STATUS_TYPE_ORDER, CommerceConstants.ORDER_PENDING_PAYMENT,
                "提交订单", CommerceConstants.OPERATOR_SYSTEM, null);
        order.setOrderStatus(CommerceConstants.ORDER_PENDING_PAYMENT);

        paymentService.createPayment(order);

        order.setItems(items);
        localMessageService.publish(CommerceConstants.TOPIC_ORDER_CREATED, orderNo, order);

        return order;
    }

    @Override
    public Orders selectOrdersById(Long orderId)
    {
        return ordersMapper.selectOrdersById(orderId);
    }

    @Override
    public Orders selectOrdersByNo(String orderNo)
    {
        return ordersMapper.selectOrdersByNo(orderNo);
    }

    @Override
    public List<Orders> selectOrdersList(Orders orders)
    {
        return ordersMapper.selectOrdersList(orders);
    }

    @Override
    public List<OrderItem> selectOrderItems(Long orderId)
    {
        return orderItemMapper.selectOrderItemsByOrderId(orderId);
    }

    @Override
    public List<OrderStateHistory> selectStateHistories(Long orderId)
    {
        return orderStateHistoryMapper.selectStateHistoriesByOrderId(orderId);
    }

    /**
     * 查询订单详情（含明细）
     */
    public Orders selectOrderDetail(Long orderId)
    {
        Orders order = ordersMapper.selectOrdersById(orderId);
        if (order != null)
        {
            order.setItems(orderItemMapper.selectOrderItemsByOrderId(orderId));
        }
        return order;
    }

    @Override
    public int insertOrders(Orders orders)
    {
        return ordersMapper.insertOrders(orders);
    }

    @Override
    public int updateOrders(Orders orders)
    {
        return ordersMapper.updateOrders(orders);
    }

    @Override
    public int deleteOrdersByIds(Long[] orderIds)
    {
        return ordersMapper.deleteOrdersByIds(orderIds);
    }

    /**
     * 状态流转
     */
    @Override
    @Transactional
    public int changeStatus(Long orderId, String statusType, String toStatus, String reason, String operatorType,
            String operatorId)
    {
        Orders order = ordersMapper.selectOrdersById(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在：" + orderId);
        }

        String fromStatus = currentStatus(order, statusType);
        OrderStateMachine.assertTransfer(statusType, fromStatus, toStatus);

        Date paidAt = null;
        Date cancelledAt = null;
        Date completedAt = null;
        if (CommerceConstants.STATUS_TYPE_ORDER.equals(statusType))
        {
            if (CommerceConstants.ORDER_PAID.equals(toStatus))
            {
                paidAt = new Date();
            }
            else if (CommerceConstants.ORDER_CANCELLED.equals(toStatus))
            {
                cancelledAt = new Date();
            }
            else if (CommerceConstants.ORDER_COMPLETED.equals(toStatus))
            {
                completedAt = new Date();
            }
        }

        int rows = ordersMapper.updateOrderStatus(orderId, statusType, toStatus, paidAt, cancelledAt, completedAt);

        OrderStateHistory history = new OrderStateHistory();
        history.setOrderId(orderId);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setStatusType(statusType);
        history.setReason(reason);
        history.setOperatorType(StringUtils.isEmpty(operatorType)
                ? CommerceConstants.OPERATOR_SYSTEM : operatorType);
        history.setOperatorId(operatorId);
        orderStateHistoryMapper.insertOrderStateHistory(history);

        return rows;
    }

    @Override
    @Transactional
    public int markOrderPaid(Long orderId, String paymentNo)
    {
        String reason = "支付成功:" + paymentNo;
        changeStatus(orderId, CommerceConstants.STATUS_TYPE_ORDER, CommerceConstants.ORDER_PAID, reason,
                CommerceConstants.OPERATOR_SYSTEM, null);
        return changeStatus(orderId, CommerceConstants.STATUS_TYPE_PAYMENT, CommerceConstants.PAY_PAID, reason,
                CommerceConstants.OPERATOR_SYSTEM, null);
    }

    @Override
    @Transactional
    public int markOrderFulfilling(Long orderId)
    {
        Orders order = ordersMapper.selectOrdersById(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在：" + orderId);
        }
        if (CommerceConstants.ORDER_PAID.equals(order.getOrderStatus()))
        {
            changeStatus(orderId, CommerceConstants.STATUS_TYPE_ORDER, CommerceConstants.ORDER_CONFIRMED,
                    "订单确认", CommerceConstants.OPERATOR_SYSTEM, null);
        }
        int rows = changeStatus(orderId, CommerceConstants.STATUS_TYPE_ORDER, CommerceConstants.ORDER_FULFILLING,
                "订单进入履约", CommerceConstants.OPERATOR_SYSTEM, null);
        if (CommerceConstants.FULFILL_UNFULFILLED.equals(order.getFulfillmentStatus()))
        {
            changeStatus(orderId, CommerceConstants.STATUS_TYPE_FULFILLMENT,
                    CommerceConstants.FULFILL_PARTIALLY_FULFILLED, "订单进入履约",
                    CommerceConstants.OPERATOR_SYSTEM, null);
        }
        return rows;
    }

    @Override
    @Transactional
    public int markOrderShipped(Long orderId)
    {
        int rows = changeStatus(orderId, CommerceConstants.STATUS_TYPE_ORDER, CommerceConstants.ORDER_SHIPPED,
                "订单已发货", CommerceConstants.OPERATOR_SYSTEM, null);
        localMessageService.publish(CommerceConstants.TOPIC_ORDER_SHIPPED, orderId.toString(), orderId);
        return rows;
    }

    @Override
    @Transactional
    public int markOrderDelivered(Long orderId)
    {
        Orders order = ordersMapper.selectOrdersById(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在：" + orderId);
        }
        if (CommerceConstants.ORDER_SHIPPED.equals(order.getOrderStatus()))
        {
            changeStatus(orderId, CommerceConstants.STATUS_TYPE_ORDER, CommerceConstants.ORDER_IN_TRANSIT,
                    "运输中", CommerceConstants.OPERATOR_SYSTEM, null);
        }
        int rows = changeStatus(orderId, CommerceConstants.STATUS_TYPE_ORDER, CommerceConstants.ORDER_DELIVERED,
                "订单已签收", CommerceConstants.OPERATOR_SYSTEM, null);
        if (!CommerceConstants.FULFILL_FULFILLED.equals(order.getFulfillmentStatus()))
        {
            changeStatus(orderId, CommerceConstants.STATUS_TYPE_FULFILLMENT, CommerceConstants.FULFILL_FULFILLED,
                    "订单已签收", CommerceConstants.OPERATOR_SYSTEM, null);
        }
        localMessageService.publish(CommerceConstants.TOPIC_ORDER_DELIVERED, orderId.toString(), orderId);
        return rows;
    }

    @Override
    @Transactional
    public int markOrderCompleted(Long orderId)
    {
        return changeStatus(orderId, CommerceConstants.STATUS_TYPE_ORDER, CommerceConstants.ORDER_COMPLETED,
                "订单完成", CommerceConstants.OPERATOR_SYSTEM, null);
    }

    @Override
    @Transactional
    public int cancelOrder(Long orderId, String reason)
    {
        Orders order = selectOrderDetail(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在：" + orderId);
        }
        String orderStatus = order.getOrderStatus();
        boolean cancellable = CommerceConstants.ORDER_CREATED.equals(orderStatus)
                || CommerceConstants.ORDER_PENDING_PAYMENT.equals(orderStatus)
                || CommerceConstants.ORDER_PAID.equals(orderStatus)
                || CommerceConstants.ORDER_CONFIRMED.equals(orderStatus);
        if (!cancellable)
        {
            throw new ServiceException("当前订单状态不允许取消：" + orderStatus);
        }

        int rows = changeStatus(orderId, CommerceConstants.STATUS_TYPE_ORDER, CommerceConstants.ORDER_CANCELLED,
                reason, CommerceConstants.OPERATOR_SYSTEM, null);

        List<OrderItem> items = order.getItems();
        if (items != null)
        {
            for (OrderItem item : items)
            {
                if (item.getSkuId() == null)
                {
                    continue;
                }
                inventoryService.unlock(item.getSkuId(), CommerceConstants.DEFAULT_WAREHOUSE,
                        item.getQuantity(), order.getOrderNo());
            }
        }

        localMessageService.publish(CommerceConstants.TOPIC_ORDER_CANCELLED, order.getOrderNo(), order);
        return rows;
    }

    private String currentStatus(Orders order, String statusType)
    {
        switch (statusType)
        {
            case CommerceConstants.STATUS_TYPE_ORDER:
                return order.getOrderStatus();
            case CommerceConstants.STATUS_TYPE_PAYMENT:
                return order.getPaymentStatus();
            case CommerceConstants.STATUS_TYPE_FULFILLMENT:
                return order.getFulfillmentStatus();
            case CommerceConstants.STATUS_TYPE_AFTER_SALE:
                return order.getAfterSaleStatus();
            default:
                throw new ServiceException("未知的状态类型：" + statusType);
        }
    }
}
