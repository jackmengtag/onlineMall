package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.FulfillmentItem;

/**
 * 履约明细数据层
 *
 * @author ruoyi
 */
public interface FulfillmentItemMapper
{
    /**
     * 新增履约明细
     */
    int insertFulfillmentItem(FulfillmentItem fulfillmentItem);

    /**
     * 按履约单查询明细
     */
    List<FulfillmentItem> selectItemsByFulfillmentId(Long fulfillmentId);

    /**
     * 按订单查询明细
     */
    List<FulfillmentItem> selectItemsByOrderId(Long orderId);
}
