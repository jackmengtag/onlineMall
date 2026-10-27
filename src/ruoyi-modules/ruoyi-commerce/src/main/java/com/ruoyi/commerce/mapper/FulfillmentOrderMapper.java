package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.FulfillmentOrder;

/**
 * 履约单 Mapper。
 *
 * @author ruoyi
 */
public interface FulfillmentOrderMapper
{
    List<FulfillmentOrder> selectList(FulfillmentOrder fulfillmentOrder);

    FulfillmentOrder selectById(Long fulfillmentId);

    List<FulfillmentOrder> selectByOrderId(Long orderId);

    int insertFulfillment(FulfillmentOrder fulfillmentOrder);

    int updateStatus(@Param("fulfillmentId") Long fulfillmentId, @Param("fromStatus") String fromStatus,
            @Param("toStatus") String toStatus, @Param("updateBy") String updateBy);

    int updateShipped(@Param("fulfillmentId") Long fulfillmentId, @Param("updateBy") String updateBy);

    int updateDelivered(@Param("fulfillmentId") Long fulfillmentId, @Param("updateBy") String updateBy);

    int countByOrderAndStatus(@Param("orderId") Long orderId, @Param("statusList") List<String> statusList);
}
