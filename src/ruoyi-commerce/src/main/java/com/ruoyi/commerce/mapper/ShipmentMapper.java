package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.Shipment;

/**
 * 运单 Mapper。
 *
 * @author ruoyi
 */
public interface ShipmentMapper
{
    List<Shipment> selectList(Shipment shipment);

    Shipment selectById(Long shipmentId);

    List<Shipment> selectByFulfillmentId(Long fulfillmentId);

    int insertShipment(Shipment shipment);

    int updateStatus(@Param("shipmentId") Long shipmentId, @Param("status") String status,
            @Param("updateBy") String updateBy);
}
