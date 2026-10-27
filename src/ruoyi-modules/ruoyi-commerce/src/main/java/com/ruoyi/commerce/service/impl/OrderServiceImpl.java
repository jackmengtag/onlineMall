package com.ruoyi.commerce.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.Coupon;
import com.ruoyi.commerce.domain.Customer;
import com.ruoyi.commerce.domain.CustomerAddress;
import com.ruoyi.commerce.domain.ExchangeRate;
import com.ruoyi.commerce.domain.Inventory;
import com.ruoyi.commerce.domain.OrderItem;
import com.ruoyi.commerce.domain.OrderItemSnapshot;
import com.ruoyi.commerce.domain.OrderSnapshot;
import com.ruoyi.commerce.domain.OrderStateHistory;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.Payment;
import com.ruoyi.commerce.domain.ProductSku;
import com.ruoyi.commerce.domain.ProductSpu;
import com.ruoyi.commerce.domain.SkuPrice;
import com.ruoyi.commerce.domain.dto.CreateOrderItemRequest;
import com.ruoyi.commerce.domain.dto.CreateOrderRequest;
import com.ruoyi.commerce.domain.dto.ExternalOrderRequest;
import com.ruoyi.commerce.domain.dto.TaxCalculation;
import com.ruoyi.commerce.domain.vo.OrderCreateResult;
import com.ruoyi.commerce.domain.vo.OrderDetailVo;
import com.ruoyi.commerce.mapper.OrderItemMapper;
import com.ruoyi.commerce.mapper.OrderItemSnapshotMapper;
import com.ruoyi.commerce.mapper.OrderSnapshotMapper;
import com.ruoyi.commerce.mapper.OrderStateHistoryMapper;
import com.ruoyi.commerce.mapper.OrdersMapper;
import com.ruoyi.commerce.mapper.RefundMapper;
import com.ruoyi.commerce.service.ICouponService;
import com.ruoyi.commerce.service.ICustomerAddressService;
import com.ruoyi.commerce.service.ICustomerService;
import com.ruoyi.commerce.service.IExchangeRateService;
import com.ruoyi.commerce.service.IInventoryService;
import com.ruoyi.commerce.service.ILocalMessageService;
import com.ruoyi.commerce.service.IOrderService;
import com.ruoyi.commerce.service.IPaymentService;
import com.ruoyi.commerce.service.IProductSkuService;
import com.ruoyi.commerce.service.IProductSpuService;
import com.ruoyi.commerce.service.ISkuPriceService;
import com.ruoyi.commerce.service.ITaxService;
import com.ruoyi.commerce.statemachine.OrderStateMachine;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 订单服务实现。
 *
 * @author ruoyi
 */
@Service
public class OrderServiceImpl implements IOrderService
{
    /** 免运费门槛（分） */
    private static final long FREE_SHIPPING_THRESHOLD = 9900L;

    /** 默认运费（分） */
    private static final long DEFAULT_SHIPPING_AMOUNT = 990L;

    private static final String DEFAULT_COUNTRY = "US";
    private static final String DEFAULT_CURRENCY = "USD";
    private static final long DEFAULT_SHOP_ID = 1L;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderItemSnapshotMapper orderItemSnapshotMapper;

    @Autowired
    private OrderSnapshotMapper orderSnapshotMapper;

    @Autowired
    private OrderStateHistoryMapper orderStateHistoryMapper;

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private ISkuPriceService skuPriceService;

    @Autowired
    private IProductSkuService productSkuService;

    @Autowired
    private IProductSpuService productSpuService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerAddressService customerAddressService;

    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private ICouponService couponService;

    @Autowired
    private ITaxService taxService;

    @Autowired
    private IExchangeRateService exchangeRateService;

    @Autowired
    private ILocalMessageService localMessageService;

    @Autowired
    @Lazy
    private IPaymentService paymentService;

    @Override
    public List<Orders> selectOrderList(Orders orders)
    {
        return ordersMapper.selectOrderList(orders);
    }

    @Override
    public Orders selectOrderById(Long orderId)
    {
        return ordersMapper.selectOrderById(orderId);
    }

    @Override
    public Orders selectOrderByNo(String orderNo)
    {
        return ordersMapper.selectOrderByNo(orderNo);
    }

    @Override
    public OrderDetailVo selectOrderDetail(Long orderId)
    {
        Orders order = ordersMapper.selectOrderById(orderId);
        if (order == null)
        {
            return null;
        }
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setItems(orderItemMapper.selectByOrderId(orderId));
        vo.setItemSnapshots(orderItemSnapshotMapper.selectByOrderId(orderId));
        vo.setSnapshot(orderSnapshotMapper.selectByOrderId(orderId));
        vo.setHistories(orderStateHistoryMapper.selectByOrderId(orderId));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderCreateResult createOrder(CreateOrderRequest request)
    {
        if (request == null || request.getItems() == null || request.getItems().isEmpty())
        {
            throw new ServiceException("订单明细不能为空");
        }
        String countryCode = defaultIfBlank(request.getCountryCode(), DEFAULT_COUNTRY);
        String channelCode = defaultIfBlank(request.getChannelCode(), CommerceConstants.DEFAULT_CHANNEL);
        String currencyCode = defaultIfBlank(request.getCurrencyCode(), DEFAULT_CURRENCY);
        Long shopId = request.getShopId() == null ? DEFAULT_SHOP_ID : request.getShopId();

        List<PricingLine> lines = new ArrayList<>();
        long subtotal = 0L;
        Long categoryId = null;
        for (int i = 0; i < request.getItems().size(); i++)
        {
            CreateOrderItemRequest itemRequest = request.getItems().get(i);
            if (itemRequest == null || itemRequest.getSkuId() == null)
            {
                throw new ServiceException("SKU 不能为空");
            }
            int quantity = itemRequest.getQuantity() == null ? 0 : itemRequest.getQuantity();
            if (quantity <= 0)
            {
                throw new ServiceException("购买数量必须大于 0");
            }
            ProductSku sku = productSkuService.selectProductSkuById(itemRequest.getSkuId());
            if (sku == null)
            {
                throw new ServiceException("SKU 不存在：" + itemRequest.getSkuId());
            }
            SkuPrice price = skuPriceService.resolvePrice(itemRequest.getSkuId(), countryCode, channelCode,
                    currencyCode);
            if (price == null || price.getSaleAmount() == null)
            {
                throw new ServiceException("SKU 未配置有效价格：" + itemRequest.getSkuId());
            }
            ProductSpu spu = sku.getSpuId() == null ? null : productSpuService.selectProductSpuById(sku.getSpuId());
            if (i == 0)
            {
                categoryId = spu == null ? null : spu.getCategoryId();
            }
            long unitPrice = price.getSaleAmount();
            long lineSubtotal = unitPrice * quantity;
            subtotal += lineSubtotal;
            lines.add(new PricingLine(sku, spu, unitPrice, quantity, lineSubtotal));
        }

        long discountAmount = 0L;
        String couponCode = request.getCouponCode();
        if (StringUtils.isNotEmpty(couponCode))
        {
            Coupon coupon = couponService.validateCoupon(couponCode, subtotal, countryCode, currencyCode);
            discountAmount = couponService.calculateDiscount(coupon, subtotal);
            if (discountAmount < 0)
            {
                discountAmount = 0L;
            }
            if (discountAmount > subtotal)
            {
                discountAmount = subtotal;
            }
        }

        long shippingAmount = request.getShippingAmount() == null
                ? ((subtotal - discountAmount) >= FREE_SHIPPING_THRESHOLD ? 0L : DEFAULT_SHIPPING_AMOUNT)
                : Math.max(0L, request.getShippingAmount());

        TaxCalculation taxCalculation = taxService.calculate(subtotal - discountAmount, countryCode, categoryId);
        long taxAmount = taxCalculation == null || taxCalculation.getTaxAmount() == null
                ? 0L : taxCalculation.getTaxAmount();

        long totalAmount = subtotal - discountAmount + shippingAmount + taxAmount;
        if (totalAmount < 0)
        {
            totalAmount = 0L;
        }

        String operator = currentUsername();
        Date now = new Date();

        Orders order = new Orders();
        order.setOrderNo(CommerceNoUtils.orderNo());
        order.setShopId(shopId);
        order.setCustomerId(request.getCustomerId());
        order.setChannelCode(channelCode);
        order.setCountryCode(countryCode);
        order.setCurrencyCode(currencyCode);
        order.setSubtotalAmount(subtotal);
        order.setDiscountAmount(discountAmount);
        order.setShippingAmount(shippingAmount);
        order.setTaxAmount(taxAmount);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(CommerceConstants.ORDER_PENDING_PAYMENT);
        order.setPaymentStatus(CommerceConstants.PAY_UNPAID);
        order.setFulfillmentStatus(CommerceConstants.FULFILL_UNFULFILLED);
        order.setAfterSaleStatus(CommerceConstants.AFTER_SALE_NONE);
        order.setCouponCode(StringUtils.isNotEmpty(couponCode) ? couponCode : null);
        order.setPlacedAt(now);
        order.setRemark(request.getRemark());
        order.setCreateBy(operator);
        ordersMapper.insertOrders(order);

        Long orderId = order.getOrderId();
        long allocatedDiscount = 0L;
        long allocatedTax = 0L;
        for (int i = 0; i < lines.size(); i++)
        {
            PricingLine line = lines.get(i);
            long lineDiscount;
            long lineTax;
            if (i == lines.size() - 1)
            {
                lineDiscount = discountAmount - allocatedDiscount;
                lineTax = taxAmount - allocatedTax;
            }
            else
            {
                lineDiscount = subtotal == 0 ? 0L : discountAmount * line.lineSubtotal / subtotal;
                lineTax = subtotal == 0 ? 0L : taxAmount * line.lineSubtotal / subtotal;
            }
            allocatedDiscount += lineDiscount;
            allocatedTax += lineTax;

            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setSkuId(line.sku.getSkuId());
            item.setSkuCode(line.sku.getSkuCode());
            item.setQuantity(line.quantity);
            item.setUnitPriceAmount(line.unitPrice);
            item.setDiscountAmount(lineDiscount);
            item.setTaxAmount(lineTax);
            item.setLineTotalAmount(line.lineSubtotal - lineDiscount + lineTax);
            item.setCurrencyCode(currencyCode);
            orderItemMapper.insertOrderItem(item);

            OrderItemSnapshot snapshot = new OrderItemSnapshot();
            snapshot.setOrderItemId(item.getOrderItemId());
            snapshot.setProductName(line.spu == null ? line.sku.getSkuCode() : line.spu.getDefaultName());
            snapshot.setSkuCode(line.sku.getSkuCode());
            snapshot.setAttributesJson(line.sku.getAttributesJson());
            snapshot.setImageUrl(line.spu == null ? null : line.spu.getDefaultImageUrl());
            snapshot.setOriginCountryCode(line.sku.getOriginCountryCode());
            snapshot.setHsCode(line.sku.getHsCode());
            snapshot.setDeclaredName(line.sku.getDeclaredName());
            snapshot.setDeclaredValueAmount(line.sku.getDeclaredValueAmount());
            snapshot.setDeclaredCurrencyCode(line.sku.getDeclaredCurrencyCode());
            snapshot.setUnitPriceAmount(line.unitPrice);
            snapshot.setCurrencyCode(currencyCode);
            orderItemSnapshotMapper.insertOrderItemSnapshot(snapshot);

            inventoryService.reserve(line.sku.getSkuId(), line.quantity, order.getOrderNo());
        }

        insertOrderSnapshot(order, request, taxCalculation);

        OrderStateHistory history = new OrderStateHistory();
        history.setOrderId(orderId);
        history.setToStatus(CommerceConstants.ORDER_PENDING_PAYMENT);
        history.setStatusType("ORDER");
        history.setReason("订单创建");
        history.setOperatorType(request.getCustomerId() == null ? "SYSTEM" : "USER");
        history.setOperatorId(request.getCustomerId() == null ? operator : String.valueOf(request.getCustomerId()));
        history.setOccurredAt(now);
        orderStateHistoryMapper.insertHistory(history);

        if (StringUtils.isNotEmpty(couponCode))
        {
            couponService.markUsed(couponCode);
        }

        Payment payment = paymentService.createPayment(orderId, "MOCK", "CARD");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("orderId", orderId);
        payload.put("orderNo", order.getOrderNo());
        payload.put("totalAmount", totalAmount);
        payload.put("currencyCode", currencyCode);
        localMessageService.record(CommerceConstants.TOPIC_ORDER, "OrderCreated", "ORDER",
                String.valueOf(orderId), JSON.toJSONString(payload), "ORDER_CREATED:" + order.getOrderNo());

        OrderCreateResult result = new OrderCreateResult();
        result.setOrderId(orderId);
        result.setOrderNo(order.getOrderNo());
        result.setTotalAmount(totalAmount);
        result.setCurrencyCode(currencyCode);
        if (payment != null)
        {
            result.setPaymentId(payment.getPaymentId());
            result.setPaymentNo(payment.getPaymentNo());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long importExternalOrder(ExternalOrderRequest request)
    {
        if (request == null || StringUtils.isEmpty(request.getChannelCode())
                || StringUtils.isEmpty(request.getExternalOrderId()))
        {
            throw new ServiceException("渠道编码与外部订单号不能为空");
        }
        Orders existing = ordersMapper.selectExternal(request.getChannelCode(), request.getExternalOrderId());
        if (existing != null)
        {
            return existing.getOrderId();
        }
        CreateOrderRequest createRequest = new CreateOrderRequest();
        createRequest.setCustomerId(request.getCustomerId());
        createRequest.setShopId(request.getShopId());
        createRequest.setCountryCode(request.getCountryCode());
        createRequest.setChannelCode(request.getChannelCode());
        createRequest.setCurrencyCode(request.getCurrencyCode());
        createRequest.setItems(request.getItems());
        OrderCreateResult result = createOrder(createRequest);
        ordersMapper.updateExternalOrderId(result.getOrderId(), request.getExternalOrderId());
        return result.getOrderId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markPaid(Long orderId)
    {
        Orders order = requireOrder(orderId);
        String fromStatus = order.getOrderStatus();
        OrderStateMachine.validate(fromStatus, CommerceConstants.ORDER_PAID);
        String operator = currentUsername();
        ordersMapper.updateStatus(orderId, fromStatus, CommerceConstants.ORDER_PAID, operator);
        ordersMapper.updatePaymentStatus(orderId, CommerceConstants.PAY_PAID, operator);
        ordersMapper.markPaidAt(orderId);
        writeHistory(orderId, fromStatus, CommerceConstants.ORDER_PAID, "ORDER", "支付成功", "SYSTEM", null);
        writeHistory(orderId, order.getPaymentStatus(), CommerceConstants.PAY_PAID, "PAYMENT", "支付成功", "SYSTEM", null);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("orderId", orderId);
        payload.put("orderNo", order.getOrderNo());
        payload.put("amount", order.getTotalAmount());
        localMessageService.record(CommerceConstants.TOPIC_PAYMENT, "PaymentSucceeded", "ORDER",
                String.valueOf(orderId), JSON.toJSONString(payload), "PAYMENT_SUCCEEDED:" + order.getOrderNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markPaymentFailed(Long orderId)
    {
        Orders order = requireOrder(orderId);
        if (CommerceConstants.PAY_PAID.equals(order.getPaymentStatus()))
        {
            return;
        }
        String operator = currentUsername();
        ordersMapper.updatePaymentStatus(orderId, CommerceConstants.PAY_FAILED, operator);
        writeHistory(orderId, order.getPaymentStatus(), CommerceConstants.PAY_FAILED, "PAYMENT", "支付失败", "SYSTEM", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markFulfilling(Long orderId)
    {
        Orders order = requireOrder(orderId);
        String fromStatus = order.getOrderStatus();
        String operator = currentUsername();
        if (CommerceConstants.ORDER_PAID.equals(fromStatus))
        {
            OrderStateMachine.validate(fromStatus, CommerceConstants.ORDER_CONFIRMED);
            ordersMapper.updateStatus(orderId, fromStatus, CommerceConstants.ORDER_CONFIRMED, operator);
            writeHistory(orderId, fromStatus, CommerceConstants.ORDER_CONFIRMED, "ORDER", "支付确认", "SYSTEM", null);
            fromStatus = CommerceConstants.ORDER_CONFIRMED;
        }
        OrderStateMachine.validate(fromStatus, CommerceConstants.ORDER_FULFILLING);
        ordersMapper.updateStatus(orderId, fromStatus, CommerceConstants.ORDER_FULFILLING, operator);
        writeHistory(orderId, fromStatus, CommerceConstants.ORDER_FULFILLING, "ORDER", "进入履约", "SYSTEM", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markShipped(Long orderId)
    {
        Orders order = requireOrder(orderId);
        String fromStatus = order.getOrderStatus();
        OrderStateMachine.validate(fromStatus, CommerceConstants.ORDER_SHIPPED);
        String operator = currentUsername();
        ordersMapper.updateStatus(orderId, fromStatus, CommerceConstants.ORDER_SHIPPED, operator);
        ordersMapper.updateFulfillmentStatus(orderId, CommerceConstants.FULFILL_FULFILLED, operator);
        writeHistory(orderId, fromStatus, CommerceConstants.ORDER_SHIPPED, "ORDER", "订单发货", "SYSTEM", null);
        writeHistory(orderId, order.getFulfillmentStatus(), CommerceConstants.FULFILL_FULFILLED, "FULFILLMENT",
                "订单发货", "SYSTEM", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markDelivered(Long orderId)
    {
        Orders order = requireOrder(orderId);
        String fromStatus = order.getOrderStatus();
        OrderStateMachine.validate(fromStatus, CommerceConstants.ORDER_DELIVERED);
        String operator = currentUsername();
        ordersMapper.updateStatus(orderId, fromStatus, CommerceConstants.ORDER_DELIVERED, operator);
        writeHistory(orderId, fromStatus, CommerceConstants.ORDER_DELIVERED, "ORDER", "订单签收", "SYSTEM", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeOrder(Long orderId)
    {
        Orders order = requireOrder(orderId);
        String fromStatus = order.getOrderStatus();
        OrderStateMachine.validate(fromStatus, CommerceConstants.ORDER_COMPLETED);
        String operator = currentUsername();
        ordersMapper.updateStatus(orderId, fromStatus, CommerceConstants.ORDER_COMPLETED, operator);
        ordersMapper.markCompletedAt(orderId);
        writeHistory(orderId, fromStatus, CommerceConstants.ORDER_COMPLETED, "ORDER", "订单完成", "SYSTEM", null);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("orderId", orderId);
        payload.put("orderNo", order.getOrderNo());
        localMessageService.record(CommerceConstants.TOPIC_ORDER, "OrderCompleted", "ORDER",
                String.valueOf(orderId), JSON.toJSONString(payload), "ORDER_COMPLETED:" + order.getOrderNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, String reason)
    {
        Orders order = requireOrder(orderId);
        String fromStatus = order.getOrderStatus();
        if (!OrderStateMachine.cancellable(fromStatus))
        {
            throw new ServiceException("当前订单状态不允许取消");
        }
        OrderStateMachine.validate(fromStatus, CommerceConstants.ORDER_CANCELLED);
        String operator = currentUsername();
        ordersMapper.updateStatus(orderId, fromStatus, CommerceConstants.ORDER_CANCELLED, operator);
        ordersMapper.markCancelled(orderId, reason);

        List<OrderItem> items = orderItemMapper.selectByOrderId(orderId);
        for (OrderItem item : items)
        {
            List<Inventory> inventories = inventoryService.selectInventoryBySkuId(item.getSkuId());
            if (inventories == null)
            {
                continue;
            }
            for (Inventory inventory : inventories)
            {
                if (inventory.getLockedQty() != null && inventory.getLockedQty() > 0)
                {
                    inventoryService.release(item.getSkuId(), inventory.getWarehouseCode(), item.getQuantity(),
                            order.getOrderNo());
                    break;
                }
            }
        }
        writeHistory(orderId, fromStatus, CommerceConstants.ORDER_CANCELLED, "ORDER", reason, "USER", operator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markRefundResult(Long orderId, long refundAmount, boolean success)
    {
        Orders order = requireOrder(orderId);
        String operator = currentUsername();
        String fromAfterSale = order.getAfterSaleStatus();
        String toAfterSale = success ? CommerceConstants.AFTER_SALE_RESOLVED : CommerceConstants.AFTER_SALE_PROCESSING;
        ordersMapper.updateAfterSaleStatus(orderId, toAfterSale, operator);
        writeHistory(orderId, fromAfterSale, toAfterSale, "AFTER_SALE",
                success ? "退款成功" : "退款失败", "SYSTEM", null);

        if (success)
        {
            Long refunded = refundMapper.sumRefundedByOrderId(orderId);
            long refundedTotal = refunded == null ? 0L : refunded;
            if (order.getTotalAmount() != null && refundedTotal >= order.getTotalAmount())
            {
                ordersMapper.updatePaymentStatus(orderId, CommerceConstants.PAY_REFUNDED, operator);
            }
            else
            {
                ordersMapper.updatePaymentStatus(orderId, CommerceConstants.PAY_PARTIALLY_REFUNDED, operator);
            }
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("orderId", orderId);
        payload.put("orderNo", order.getOrderNo());
        payload.put("refundAmount", refundAmount);
        payload.put("success", success);
        localMessageService.record(CommerceConstants.TOPIC_AFTER_SALE,
                success ? "RefundSucceeded" : "RefundFailed", "ORDER", String.valueOf(orderId),
                JSON.toJSONString(payload),
                "REFUND_RESULT:" + order.getOrderNo() + ":" + refundAmount);
    }

    @Override
    public List<OrderStateHistory> selectHistories(Long orderId)
    {
        return orderStateHistoryMapper.selectByOrderId(orderId);
    }

    private Orders requireOrder(Long orderId)
    {
        Orders order = orderId == null ? null : ordersMapper.selectOrderById(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在：" + orderId);
        }
        return order;
    }

    private void insertOrderSnapshot(Orders order, CreateOrderRequest request, TaxCalculation taxCalculation)
    {
        OrderSnapshot snapshot = new OrderSnapshot();
        snapshot.setOrderId(order.getOrderId());

        Customer customer = request.getCustomerId() == null ? null
                : customerService.selectCustomerById(request.getCustomerId());
        if (customer != null)
        {
            snapshot.setBuyerName(customer.getDisplayName());
            snapshot.setBuyerEmail(customer.getEmail());
        }
        CustomerAddress address = request.getAddressId() == null ? null
                : customerAddressService.selectAddressById(request.getAddressId());
        if (address != null)
        {
            snapshot.setRecipientName(address.getRecipientName());
            snapshot.setRecipientPhone(address.getRecipientPhone());
            snapshot.setCountryCode(address.getCountryCode());
            snapshot.setStateCode(address.getStateCode());
            snapshot.setCity(address.getCity());
            snapshot.setDistrict(address.getDistrict());
            snapshot.setAddressLine1(address.getAddressLine1());
            snapshot.setAddressLine2(address.getAddressLine2());
            snapshot.setPostalCode(address.getPostalCode());
            snapshot.setRawAddressJson(JSON.toJSONString(address));
        }
        else
        {
            snapshot.setRecipientName(customer == null ? "未指定" : customer.getDisplayName());
            snapshot.setCountryCode(order.getCountryCode());
            snapshot.setCity("未指定");
            snapshot.setAddressLine1("未指定");
        }
        if (taxCalculation != null)
        {
            snapshot.setTaxRuleVersion(taxCalculation.getRuleVersion());
        }
        if (!DEFAULT_CURRENCY.equalsIgnoreCase(order.getCurrencyCode()))
        {
            ExchangeRate exchangeRate = exchangeRateService.getActiveRate(DEFAULT_CURRENCY, order.getCurrencyCode());
            if (exchangeRate != null)
            {
                snapshot.setExchangeRateVersion(exchangeRate.getVersion());
                snapshot.setExchangeRate(exchangeRate.getRate());
            }
        }
        orderSnapshotMapper.insertOrderSnapshot(snapshot);
    }

    private void writeHistory(Long orderId, String fromStatus, String toStatus, String statusType, String reason,
            String operatorType, String operatorId)
    {
        OrderStateHistory history = new OrderStateHistory();
        history.setOrderId(orderId);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setStatusType(statusType);
        history.setReason(reason);
        history.setOperatorType(operatorType);
        history.setOperatorId(operatorId);
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

    private static String defaultIfBlank(String value, String defaultValue)
    {
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * 下单价格计算中间结果。
     */
    private static class PricingLine
    {
        private final ProductSku sku;
        private final ProductSpu spu;
        private final long unitPrice;
        private final int quantity;
        private final long lineSubtotal;

        private PricingLine(ProductSku sku, ProductSpu spu, long unitPrice, int quantity, long lineSubtotal)
        {
            this.sku = sku;
            this.spu = spu;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.lineSubtotal = lineSubtotal;
        }
    }
}
