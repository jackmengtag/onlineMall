package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.Refund;

/**
 * 退款单数据层
 *
 * @author ruoyi
 */
public interface RefundMapper
{
    /**
     * 查询退款单
     */
    Refund selectRefundById(Long refundId);

    /**
     * 查询退款单列表
     */
    List<Refund> selectRefundList(Refund refund);

    /**
     * 新增退款单
     */
    int insertRefund(Refund refund);

    /**
     * 修改退款单
     */
    int updateRefund(Refund refund);

    /**
     * 统计订单已退款金额（处理中 + 已成功）
     */
    Long selectRefundedAmountByOrderId(Long orderId);
}
