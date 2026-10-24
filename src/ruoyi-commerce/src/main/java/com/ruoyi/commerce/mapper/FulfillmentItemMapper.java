package com.ruoyi.commerce.mapper;

import java.util.List;

import com.ruoyi.commerce.domain.FulfillmentItem;

/**
 * 履约明细 Mapper。
 *
 * @author ruoyi
 */
public interface FulfillmentItemMapper
{
    List<FulfillmentItem> selectByFulfillmentId(Long fulfillmentId);

    List<FulfillmentItem> selectOrderItemsForSplit(Long orderId);

    int insertItem(FulfillmentItem item);
}
