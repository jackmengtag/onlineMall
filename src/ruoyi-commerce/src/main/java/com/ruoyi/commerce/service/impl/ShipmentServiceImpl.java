package com.ruoyi.commerce.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.FulfillmentOrder;
import com.ruoyi.commerce.domain.Shipment;
import com.ruoyi.commerce.domain.TrackingEvent;
import com.ruoyi.commerce.domain.vo.ShipmentDetailVo;
import com.ruoyi.commerce.mapper.FulfillmentOrderMapper;
import com.ruoyi.commerce.mapper.ShipmentMapper;
import com.ruoyi.commerce.mapper.TrackingEventMapper;
import com.ruoyi.commerce.service.IOrderService;
import com.ruoyi.commerce.service.IShipmentService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 物流运单服务实现。
 *
 * @author ruoyi
 */
@Service
public class ShipmentServiceImpl implements IShipmentService
{
    private static final Logger log = LoggerFactory.getLogger(ShipmentServiceImpl.class);

    /** 非 Web 环境的操作人 */
    private static final String SYSTEM_OPERATOR = "system";

    /** 系统生成的物流单号前缀 */
    private static final String TRACKING_NO_PREFIX = "TRK";

    @Autowired
    private ShipmentMapper shipmentMapper;

    @Autowired
    private TrackingEventMapper trackingEventMapper;

    @Autowired
    private FulfillmentOrderMapper fulfillmentOrderMapper;

    @Lazy
    @Autowired
    private IOrderService orderService;

    @Override
    public List<Shipment> selectShipmentList(Shipment shipment)
    {
        return shipmentMapper.selectList(shipment);
    }

    @Override
    public ShipmentDetailVo selectShipmentDetail(Long shipmentId)
    {
        Shipment shipment = requireShipment(shipmentId);
        ShipmentDetailVo vo = new ShipmentDetailVo();
        vo.setShipment(shipment);
        vo.setTrackingEvents(trackingEventMapper.selectByShipmentId(shipmentId));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Shipment createShipment(Long fulfillmentId, String carrierCode, String trackingNo)
    {
        if (fulfillmentId == null)
        {
            throw new ServiceException("履约单不能为空");
        }
        if (StringUtils.isEmpty(carrierCode))
        {
            throw new ServiceException("承运商不能为空");
        }
        FulfillmentOrder fulfillment = fulfillmentOrderMapper.selectById(fulfillmentId);
        if (fulfillment == null)
        {
            throw new ServiceException("履约单不存在");
        }
        Shipment shipment = new Shipment();
        shipment.setShipmentNo(CommerceNoUtils.shipmentNo());
        shipment.setFulfillmentId(fulfillmentId);
        shipment.setCarrierCode(carrierCode);
        shipment.setTrackingNo(StringUtils.isEmpty(trackingNo)
                ? TRACKING_NO_PREFIX + shipment.getShipmentNo() : trackingNo);
        shipment.setShipmentStatus(CommerceConstants.SHIPMENT_CREATED);
        shipment.setShippedAt(new Date());
        shipment.setCreateBy(currentOperator());
        shipmentMapper.insertShipment(shipment);
        return shipment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTrackingEvent(TrackingEvent event)
    {
        if (event == null || event.getShipmentId() == null)
        {
            throw new ServiceException("运单不能为空");
        }
        if (StringUtils.isEmpty(event.getEventCode()))
        {
            throw new ServiceException("轨迹事件编码不能为空");
        }
        Shipment shipment = requireShipment(event.getShipmentId());
        if (event.getOccurredAt() == null)
        {
            event.setOccurredAt(new Date());
        }
        event.setReceivedAt(new Date());
        trackingEventMapper.insertEvent(event);

        String targetStatus = resolveShipmentStatus(event.getEventCode());
        if (targetStatus == null)
        {
            return;
        }
        if (!targetStatus.equals(shipment.getShipmentStatus()))
        {
            shipmentMapper.updateStatus(shipment.getShipmentId(), targetStatus, currentOperator());
        }
        if (CommerceConstants.SHIPMENT_DELIVERED.equals(targetStatus))
        {
            completeFulfillment(shipment);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markDelivered(Long shipmentId)
    {
        Shipment shipment = requireShipment(shipmentId);
        if (!CommerceConstants.SHIPMENT_DELIVERED.equals(shipment.getShipmentStatus()))
        {
            shipmentMapper.updateStatus(shipmentId, CommerceConstants.SHIPMENT_DELIVERED, currentOperator());
        }
        completeFulfillment(shipment);
    }

    private Shipment requireShipment(Long shipmentId)
    {
        if (shipmentId == null)
        {
            throw new ServiceException("运单不能为空");
        }
        Shipment shipment = shipmentMapper.selectById(shipmentId);
        if (shipment == null)
        {
            throw new ServiceException("运单不存在");
        }
        return shipment;
    }

    /**
     * 按轨迹事件编码映射运单状态；无匹配事件不改变状态。
     */
    private String resolveShipmentStatus(String eventCode)
    {
        String code = eventCode.trim().toUpperCase();
        if (CommerceConstants.SHIPMENT_IN_TRANSIT.equals(code))
        {
            return CommerceConstants.SHIPMENT_IN_TRANSIT;
        }
        if (CommerceConstants.SHIPMENT_EXCEPTION.equals(code))
        {
            return CommerceConstants.SHIPMENT_EXCEPTION;
        }
        if (CommerceConstants.SHIPMENT_RETURNED.equals(code))
        {
            return CommerceConstants.SHIPMENT_RETURNED;
        }
        if (CommerceConstants.SHIPMENT_DELIVERED.equals(code))
        {
            return CommerceConstants.SHIPMENT_DELIVERED;
        }
        return null;
    }

    /**
     * 签收后推进履约单与订单；履约单已签收时不重复推进。
     */
    private void completeFulfillment(Shipment shipment)
    {
        FulfillmentOrder fulfillment = fulfillmentOrderMapper.selectById(shipment.getFulfillmentId());
        if (fulfillment == null)
        {
            log.warn("shipment {} has no fulfillment {}", shipment.getShipmentId(), shipment.getFulfillmentId());
            return;
        }
        int rows = fulfillmentOrderMapper.updateDelivered(fulfillment.getFulfillmentId(), currentOperator());
        if (rows > 0)
        {
            orderService.markDelivered(fulfillment.getOrderId());
        }
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
