package com.ruoyi.commerce.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.Inventory;
import com.ruoyi.commerce.domain.OrderItem;
import com.ruoyi.commerce.domain.OrderStateHistory;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.Refund;
import com.ruoyi.commerce.mapper.OrderItemMapper;
import com.ruoyi.commerce.mapper.OrderStateHistoryMapper;
import com.ruoyi.commerce.mapper.OrdersMapper;
import com.ruoyi.commerce.mapper.RefundMapper;
import com.ruoyi.commerce.service.IInventoryService;
import com.ruoyi.commerce.service.IOrderService;
import com.ruoyi.commerce.service.IRefundService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 退款售后服务实现。
 *
 * @author ruoyi
 */
@Service
public class RefundServiceImpl implements IRefundService
{
    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderStateHistoryMapper orderStateHistoryMapper;

    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private IOrderService orderService;

    @Override
    public List<Refund> selectRefundList(Refund refund)
    {
        return refundMapper.selectRefundList(refund);
    }

    @Override
    public Refund selectRefundById(Long refundId)
    {
        return refundMapper.selectRefundById(refundId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Refund applyRefund(Refund refund)
    {
        if (refund == null || refund.getOrderId() == null)
        {
            throw new ServiceException("订单不能为空");
        }
        if (refund.getAmount() == null || refund.getAmount() <= 0)
        {
            throw new ServiceException("退款金额必须大于 0");
        }
        Orders order = ordersMapper.selectOrderById(refund.getOrderId());
        if (order == null)
        {
            throw new ServiceException("订单不存在：" + refund.getOrderId());
        }
        if (!CommerceConstants.PAY_PAID.equals(order.getPaymentStatus())
                && !CommerceConstants.PAY_PARTIALLY_REFUNDED.equals(order.getPaymentStatus()))
        {
            throw new ServiceException("订单未支付或已全额退款，不能申请退款");
        }
        Long refunded = refundMapper.sumRefundedByOrderId(order.getOrderId());
        long refundedTotal = refunded == null ? 0L : refunded;
        if (refundedTotal + refund.getAmount() > (order.getTotalAmount() == null ? 0L : order.getTotalAmount()))
        {
            throw new ServiceException("累计退款金额不能超过订单实付金额");
        }

        Refund entity = new Refund();
        entity.setRefundNo(CommerceNoUtils.refundNo());
        entity.setOrderId(order.getOrderId());
        entity.setPaymentId(refund.getPaymentId());
        entity.setAmount(refund.getAmount());
        entity.setCurrencyCode(StringUtils.isEmpty(refund.getCurrencyCode())
                ? order.getCurrencyCode() : refund.getCurrencyCode());
        entity.setRefundStatus(CommerceConstants.REFUND_REQUESTED);
        entity.setReason(refund.getReason());
        entity.setRequestedAt(new Date());
        entity.setRemark(refund.getRemark());
        entity.setCreateBy(currentUsername());
        refundMapper.insertRefund(entity);

        ordersMapper.updateAfterSaleStatus(order.getOrderId(), CommerceConstants.AFTER_SALE_REQUESTED,
                currentUsername());
        writeHistory(order.getOrderId(), order.getAfterSaleStatus(), CommerceConstants.AFTER_SALE_REQUESTED,
                "售后申请");
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long refundId)
    {
        Refund refund = requireRefund(refundId);
        if (!CommerceConstants.REFUND_REQUESTED.equals(refund.getRefundStatus())
                && !CommerceConstants.REFUND_PROCESSING.equals(refund.getRefundStatus()))
        {
            throw new ServiceException("当前退款单状态不允许审核通过");
        }
        Orders order = ordersMapper.selectOrderById(refund.getOrderId());
        Refund update = new Refund();
        update.setRefundId(refundId);
        update.setRefundStatus(CommerceConstants.REFUND_APPROVED);
        update.setUpdateBy(currentUsername());
        refundMapper.updateRefund(update);

        ordersMapper.updateAfterSaleStatus(refund.getOrderId(), CommerceConstants.AFTER_SALE_PROCESSING,
                currentUsername());
        writeHistory(refund.getOrderId(), order == null ? null : order.getAfterSaleStatus(),
                CommerceConstants.AFTER_SALE_PROCESSING, "退款审核通过");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long refundId, String reason)
    {
        Refund refund = requireRefund(refundId);
        if (CommerceConstants.REFUND_SUCCEEDED.equals(refund.getRefundStatus())
                || CommerceConstants.REFUND_REJECTED.equals(refund.getRefundStatus()))
        {
            throw new ServiceException("当前退款单状态不允许驳回");
        }
        Orders order = ordersMapper.selectOrderById(refund.getOrderId());
        Refund update = new Refund();
        update.setRefundId(refundId);
        update.setRefundStatus(CommerceConstants.REFUND_REJECTED);
        update.setReason(StringUtils.isEmpty(reason) ? refund.getReason() : reason);
        update.setUpdateBy(currentUsername());
        refundMapper.updateRefund(update);

        ordersMapper.updateAfterSaleStatus(refund.getOrderId(), CommerceConstants.AFTER_SALE_REJECTED,
                currentUsername());
        writeHistory(refund.getOrderId(), order == null ? null : order.getAfterSaleStatus(),
                CommerceConstants.AFTER_SALE_REJECTED, StringUtils.isEmpty(reason) ? "退款驳回" : reason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(Long refundId)
    {
        Refund refund = requireRefund(refundId);
        if (!CommerceConstants.REFUND_APPROVED.equals(refund.getRefundStatus())
                && !CommerceConstants.REFUND_PROCESSING.equals(refund.getRefundStatus()))
        {
            throw new ServiceException("当前退款单状态不允许完成退款");
        }
        Refund update = new Refund();
        update.setRefundId(refundId);
        update.setRefundStatus(CommerceConstants.REFUND_SUCCEEDED);
        update.setCompletedAt(new Date());
        update.setUpdateBy(currentUsername());
        refundMapper.updateRefund(update);

        orderService.markRefundResult(refund.getOrderId(), refund.getAmount(), true);

        Orders order = ordersMapper.selectOrderById(refund.getOrderId());
        String bizNo = (order == null ? String.valueOf(refund.getOrderId()) : order.getOrderNo()) + "-refund";
        List<OrderItem> items = orderItemMapper.selectByOrderId(refund.getOrderId());
        for (OrderItem item : items)
        {
            List<Inventory> inventories = inventoryService.selectInventoryBySkuId(item.getSkuId());
            if (inventories == null || inventories.isEmpty())
            {
                continue;
            }
            Inventory inventory = inventories.get(0);
            inventoryService.restore(item.getSkuId(), inventory.getWarehouseCode(), item.getQuantity(), bizNo);
        }
    }

    private Refund requireRefund(Long refundId)
    {
        Refund refund = refundId == null ? null : refundMapper.selectRefundById(refundId);
        if (refund == null)
        {
            throw new ServiceException("退款单不存在：" + refundId);
        }
        return refund;
    }

    private void writeHistory(Long orderId, String fromStatus, String toStatus, String reason)
    {
        OrderStateHistory history = new OrderStateHistory();
        history.setOrderId(orderId);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setStatusType("AFTER_SALE");
        history.setReason(reason);
        history.setOperatorType("USER");
        history.setOperatorId(currentUsername());
        history.setOccurredAt(new Date());
        orderStateHistoryMapper.insertHistory(history);
    }

    private String currentUsername()
    {
        try
        {
            return SecurityUtils.getUsername();
        }
        catch (Exception e)
        {
            return "system";
        }
    }
}
