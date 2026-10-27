package com.ruoyi.commerce.mapper;

import java.util.List;

import com.ruoyi.commerce.domain.TrackingEvent;

/**
 * 物流轨迹 Mapper。
 *
 * @author ruoyi
 */
public interface TrackingEventMapper
{
    List<TrackingEvent> selectByShipmentId(Long shipmentId);

    int insertEvent(TrackingEvent event);
}
