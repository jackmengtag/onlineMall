package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.TrackingEvent;

/**
 * 物流轨迹事件数据层
 *
 * @author ruoyi
 */
public interface TrackingEventMapper
{
    /**
     * 新增轨迹事件
     */
    int insertTrackingEvent(TrackingEvent trackingEvent);

    /**
     * 按运单查询轨迹
     */
    List<TrackingEvent> selectByShipmentId(Long shipmentId);
}
