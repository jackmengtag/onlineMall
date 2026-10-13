package com.ruoyi.commerce.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.commerce.domain.Shipment;

/**
 * 运单数据层
 *
 * @author ruoyi
 */
public interface ShipmentMapper
{
    /**
     * 查询运单
     */
    Shipment selectShipmentById(Long shipmentId);

    /**
     * 查询运单列表
     */
    List<Shipment> selectShipmentList(Shipment shipment);

    /**
     * 按履约单查询运单
     */
    Shipment selectByFulfillmentId(Long fulfillmentId);

    /**
     * 新增运单
     */
    int insertShipment(Shipment shipment);

    /**
     * 更新运单状态
     */
    int updateShipmentStatus(@Param("shipmentId") Long shipmentId,
                             @Param("status") String status,
                             @Param("deliveredAt") Date deliveredAt);
}
