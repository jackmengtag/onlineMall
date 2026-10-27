package com.ruoyi.commerce.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.FulfillmentItem;
import com.ruoyi.commerce.domain.FulfillmentOrder;
import com.ruoyi.commerce.domain.Inventory;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.Shipment;
import com.ruoyi.commerce.domain.vo.FulfillmentDetailVo;
import com.ruoyi.commerce.mapper.FulfillmentItemMapper;
import com.ruoyi.commerce.mapper.FulfillmentOrderMapper;
import com.ruoyi.commerce.mapper.ShipmentMapper;
import com.ruoyi.commerce.service.IFulfillmentService;
import com.ruoyi.commerce.service.IInventoryService;
import com.ruoyi.commerce.service.ILocalMessageService;
import com.ruoyi.commerce.service.IOrderService;
import com.ruoyi.commerce.service.IShipmentService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 履约服务实现。
 *
 * @author ruoyi
 */
@Service
public class FulfillmentServiceImpl implements IFulfillmentService
{
    /** 非 Web 环境的操作人 */
    private static final String SYSTEM_OPERATOR = "system";

    /** 无锁定库存时的默认仓 */
    private static final String DEFAULT_WAREHOUSE = CommerceConstants.DEFAULT_WAREHOUSE;

    @Autowired
    private FulfillmentOrderMapper fulfillmentOrderMapper;

    @Autowired
    private FulfillmentItemMapper fulfillmentItemMapper;

    @Autowired
    private ShipmentMapper shipmentMapper;

    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private IShipmentService shipmentService;

    @Autowired
    private ILocalMessageService localMessageService;

    @Lazy
    @Autowired
    private IOrderService orderService;

    @Override
    public List<FulfillmentOrder> selectFulfillmentList(FulfillmentOrder fulfillmentOrder)
    {
        return fulfillmentOrderMapper.selectList(fulfillmentOrder);
    }

    @Override
    public FulfillmentDetailVo selectFulfillmentDetail(Long fulfillmentId)
    {
        FulfillmentOrder fulfillment = fulfillmentOrderMapper.selectById(fulfillmentId);
        if (fulfillment == null)
        {
            throw new ServiceException("履约单不存在");
        }
        FulfillmentDetailVo vo = new FulfillmentDetailVo();
        vo.setFulfillment(fulfillment);
        vo.setItems(fulfillmentItemMapper.selectByFulfillmentId(fulfillmentId));
        vo.setShipments(shipmentMapper.selectByFulfillmentId(fulfillmentId));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int splitOrder(Long orderId)
    {
        if (orderId == null)
        {
            throw new ServiceException("订单不能为空");
        }
        Orders order = orderService.selectOrderById(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在");
        }
        String orderStatus = order.getOrderStatus();
        if (!Arrays.asList(CommerceConstants.ORDER_PAID, CommerceConstants.ORDER_CONFIRMED,
                CommerceConstants.ORDER_FULFILLING).contains(orderStatus))
        {
            throw new ServiceException("订单状态 " + orderStatus + " 不允许拆分履约");
        }
        List<FulfillmentOrder> existing = fulfillmentOrderMapper.selectByOrderId(orderId);
        if (existing != null && !existing.isEmpty())
        {
            return 0;
        }
        List<FulfillmentItem> orderItems = fulfillmentItemMapper.selectOrderItemsForSplit(orderId);
        if (orderItems == null || orderItems.isEmpty())
        {
            throw new ServiceException("订单无可履约明细");
        }
        String operator = currentOperator();
        Map<String, List<FulfillmentItem>> warehouseGroups = new LinkedHashMap<>();
        for (FulfillmentItem item : orderItems)
        {
            String warehouseCode = resolveWarehouse(item.getSkuId());
            warehouseGroups.computeIfAbsent(warehouseCode, key -> new ArrayList<>()).add(item);
        }
        int created = 0;
        for (Map.Entry<String, List<FulfillmentItem>> entry : warehouseGroups.entrySet())
        {
            String warehouseCode = entry.getKey();
            FulfillmentOrder fulfillment = new FulfillmentOrder();
            fulfillment.setFulfillmentNo(CommerceNoUtils.fulfillmentNo());
            fulfillment.setShopId(order.getShopId());
            fulfillment.setOrderId(orderId);
            fulfillment.setWarehouseCode(warehouseCode);
            fulfillment.setFulfillmentStatus(CommerceConstants.FUL_ORDER_CREATED);
            fulfillment.setAllocationReason(DEFAULT_WAREHOUSE.equals(warehouseCode) ? "DEFAULT" : "LOCKED_STOCK");
            fulfillment.setCreateBy(operator);
            fulfillmentOrderMapper.insertFulfillment(fulfillment);
            for (FulfillmentItem item : entry.getValue())
            {
                FulfillmentItem fulfillmentItem = new FulfillmentItem();
                fulfillmentItem.setFulfillmentId(fulfillment.getFulfillmentId());
                fulfillmentItem.setOrderItemId(item.getOrderItemId());
                fulfillmentItem.setQuantity(item.getQuantity());
                fulfillmentItemMapper.insertItem(fulfillmentItem);
            }
            created++;
        }
        orderService.markFulfilling(orderId);
        return created;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long fulfillmentId, String toStatus)
    {
        FulfillmentOrder fulfillment = requireFulfillment(fulfillmentId);
        if (StringUtils.isEmpty(toStatus))
        {
            throw new ServiceException("目标状态不能为空");
        }
        String fromStatus = fulfillment.getFulfillmentStatus();
        if (!allowedTransition(fromStatus, toStatus))
        {
            throw new ServiceException("履约状态不允许从 " + fromStatus + " 流转到 " + toStatus);
        }
        int rows = fulfillmentOrderMapper.updateStatus(fulfillmentId, fromStatus, toStatus, currentOperator());
        if (rows == 0)
        {
            throw new ServiceException("履约单状态已变更，请刷新后重试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Shipment ship(Long fulfillmentId, String carrierCode, String trackingNo)
    {
        FulfillmentOrder fulfillment = requireFulfillment(fulfillmentId);
        if (!CommerceConstants.FUL_ORDER_PACKED.equals(fulfillment.getFulfillmentStatus()))
        {
            throw new ServiceException("只有已打包的履约单才能发货");
        }
        Shipment shipment = shipmentService.createShipment(fulfillmentId, carrierCode, trackingNo);
        List<FulfillmentItem> items = fulfillmentItemMapper.selectByFulfillmentId(fulfillmentId);
        if (items == null || items.isEmpty())
        {
            throw new ServiceException("履约单无明细，无法发货");
        }
        for (FulfillmentItem item : items)
        {
            if (item.getSkuId() == null)
            {
                throw new ServiceException("履约明细缺少 SKU，无法扣减库存");
            }
            inventoryService.deduct(item.getSkuId(), fulfillment.getWarehouseCode(), item.getQuantity(),
                    fulfillment.getFulfillmentNo());
        }
        String operator = currentOperator();
        int rows = fulfillmentOrderMapper.updateShipped(fulfillmentId, operator);
        if (rows == 0)
        {
            throw new ServiceException("履约单状态已变更，发货失败");
        }
        int total = fulfillmentOrderMapper.countByOrderAndStatus(fulfillment.getOrderId(), null);
        int shipped = fulfillmentOrderMapper.countByOrderAndStatus(fulfillment.getOrderId(),
                Arrays.asList(CommerceConstants.FUL_ORDER_SHIPPED, CommerceConstants.FUL_ORDER_DELIVERED));
        if (total > 0 && total == shipped)
        {
            orderService.markShipped(fulfillment.getOrderId());
        }
        recordShippedMessage(fulfillment, shipment);
        return shipment;
    }

    private String resolveWarehouse(Long skuId)
    {
        if (skuId == null)
        {
            return DEFAULT_WAREHOUSE;
        }
        List<Inventory> inventories = inventoryService.selectInventoryBySkuId(skuId);
        if (inventories == null || inventories.isEmpty())
        {
            return DEFAULT_WAREHOUSE;
        }
        Inventory selected = null;
        for (Inventory inventory : inventories)
        {
            if (inventory.getLockedQty() == null || inventory.getLockedQty() <= 0)
            {
                continue;
            }
            if (selected == null || inventory.getLockedQty() > selected.getLockedQty())
            {
                selected = inventory;
            }
        }
        return selected == null ? DEFAULT_WAREHOUSE : selected.getWarehouseCode();
    }

    private FulfillmentOrder requireFulfillment(Long fulfillmentId)
    {
        if (fulfillmentId == null)
        {
            throw new ServiceException("履约单不能为空");
        }
        FulfillmentOrder fulfillment = fulfillmentOrderMapper.selectById(fulfillmentId);
        if (fulfillment == null)
        {
            throw new ServiceException("履约单不存在");
        }
        return fulfillment;
    }

    private boolean allowedTransition(String fromStatus, String toStatus)
    {
        if (CommerceConstants.FUL_ORDER_CANCELLED.equals(toStatus))
        {
            return Arrays.asList(CommerceConstants.FUL_ORDER_CREATED, CommerceConstants.FUL_ORDER_ALLOCATED,
                    CommerceConstants.FUL_ORDER_PICKING, CommerceConstants.FUL_ORDER_PACKED).contains(fromStatus);
        }
        if (CommerceConstants.FUL_ORDER_ALLOCATED.equals(toStatus))
        {
            return CommerceConstants.FUL_ORDER_CREATED.equals(fromStatus);
        }
        if (CommerceConstants.FUL_ORDER_PICKING.equals(toStatus))
        {
            return CommerceConstants.FUL_ORDER_ALLOCATED.equals(fromStatus);
        }
        if (CommerceConstants.FUL_ORDER_PACKED.equals(toStatus))
        {
            return CommerceConstants.FUL_ORDER_PICKING.equals(fromStatus);
        }
        return false;
    }

    private void recordShippedMessage(FulfillmentOrder fulfillment, Shipment shipment)
    {
        String payload = String.format(
                "{\"fulfillmentId\":%d,\"fulfillmentNo\":\"%s\",\"orderId\":%d,\"shipmentNo\":\"%s\",\"trackingNo\":\"%s\"}",
                fulfillment.getFulfillmentId(), fulfillment.getFulfillmentNo(), fulfillment.getOrderId(),
                shipment.getShipmentNo(), shipment.getTrackingNo());
        localMessageService.record(CommerceConstants.TOPIC_FULFILLMENT, "FulfillmentShipped", "FULFILLMENT",
                String.valueOf(fulfillment.getFulfillmentId()), payload,
                "FulfillmentShipped:" + fulfillment.getFulfillmentNo());
    }

    /**
     * 当前登录用户；非 Web 环境回退 system。
     */
    private String currentOperator()
    {
        try
        {
            String username = SecurityUtils.getUsername();
            return StringUtils.isEmpty(username) ? SYSTEM_OPERATOR : username;
        }
        catch (Exception e)
        {
            return SYSTEM_OPERATOR;
        }
    }
}
