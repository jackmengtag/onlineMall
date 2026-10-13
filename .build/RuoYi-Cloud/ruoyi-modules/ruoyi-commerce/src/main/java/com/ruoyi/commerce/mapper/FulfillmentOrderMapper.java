package com.ruoyi.commerce.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.commerce.domain.FulfillmentOrder;

/**
 * 履约单数据层
 *
 * @author ruoyi
 */
public interface FulfillmentOrderMapper
{
    /**
     * 查询履约单
     */
    FulfillmentOrder selectFulfillmentOrderById(Long fulfillmentId);

    /**
     * 查询履约单列表
     */
    List<FulfillmentOrder> selectFulfillmentOrderList(FulfillmentOrder fulfillmentOrder);

    /**
     * 按订单查询履约单列表
     */
    List<FulfillmentOrder> selectByOrderId(Long orderId);

    /**
     * 新增履约单
     */
    int insertFulfillmentOrder(FulfillmentOrder fulfillmentOrder);

    /**
     * 修改履约单
     */
    int updateFulfillmentOrder(FulfillmentOrder fulfillmentOrder);

    /**
     * 更新履约单状态
     */
    int updateFulfillmentOrderStatus(@Param("fulfillmentId") Long fulfillmentId,
                                     @Param("toStatus") String toStatus,
                                     @Param("actualShipAt") Date actualShipAt,
                                     @Param("completedAt") Date completedAt);
}
