package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.Refund;

/**
 * 退款售后接口。
 *
 * @author ruoyi
 */
public interface IRefundService
{
    List<Refund> selectRefundList(Refund refund);

    Refund selectRefundById(Long refundId);

    Refund applyRefund(Refund refund);

    void approve(Long refundId);

    void reject(Long refundId, String reason);

    void complete(Long refundId);
}
