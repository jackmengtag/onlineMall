package com.ruoyi.commerce.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.redis.service.RedisService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.Cart;
import com.ruoyi.commerce.domain.CartItem;
import com.ruoyi.commerce.domain.Customer;
import com.ruoyi.commerce.domain.CustomerAddress;
import com.ruoyi.commerce.domain.OrderItem;
import com.ruoyi.commerce.domain.OrderItemSnapshot;
import com.ruoyi.commerce.domain.OrderSnapshot;
import com.ruoyi.commerce.domain.OrderStateHistory;
import com.ruoyi.commerce.domain.Orders;
import com.ruoyi.commerce.domain.Payment;
import com.ruoyi.commerce.domain.Refund;
import com.ruoyi.commerce.domain.dto.CreateOrderRequest;
import com.ruoyi.commerce.domain.vo.OrderCreateResult;
import com.ruoyi.commerce.domain.vo.OrderDetailVo;
import com.ruoyi.commerce.mapper.RefundMapper;
import com.ruoyi.commerce.mapper.StorefrontMapper;
import com.ruoyi.commerce.service.ICartService;
import com.ruoyi.commerce.service.ICustomerAddressService;
import com.ruoyi.commerce.service.ICustomerService;
import com.ruoyi.commerce.service.IOrderService;
import com.ruoyi.commerce.service.IPaymentService;
import com.ruoyi.commerce.service.IStorefrontService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 买家端服务实现。
 *
 * @author ruoyi
 */
@Service
public class StorefrontServiceImpl implements IStorefrontService
{
    @Autowired
    private StorefrontMapper storefrontMapper;

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerAddressService customerAddressService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private RedisService redisCache;

    @Override
    public List<Map<String, Object>> categories()
    {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> all = new LinkedHashMap<>();
        all.put("key", "all");
        all.put("label", "全部商品");
        result.add(all);
        List<Map<String, Object>> rows = storefrontMapper.selectCategories();
        if (rows != null)
        {
            result.addAll(rows);
        }
        return result;
    }

    @Override
    public Map<String, Object> products(Map<String, Object> query)
    {
        Map<String, Object> condition = query == null ? new HashMap<>() : query;
        int page = Math.max(1, Convert.toInt(condition.get("page"), 1));
        int size = Math.max(1, Math.min(100, Convert.toInt(condition.get("size"), 8)));
        Map<String, Object> params = new HashMap<>();
        params.put("category", Convert.toStr(condition.get("category")));
        params.put("q", Convert.toStr(condition.get("q")));
        params.put("sort", Convert.toStr(condition.get("sort")));
        params.put("offset", (page - 1) * size);
        params.put("size", size);

        List<Map<String, Object>> rows = storefrontMapper.selectSpuList(params);
        Long total = storefrontMapper.countSpu(params);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", buildProducts(rows));
        result.put("total", total == null ? 0L : total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    @Override
    public Map<String, Object> product(Long spuId)
    {
        if (spuId == null)
        {
            throw new ServiceException("商品不存在");
        }
        Map<String, Object> row = storefrontMapper.selectSpuById(spuId);
        if (row == null)
        {
            throw new ServiceException("商品不存在");
        }
        List<Map<String, Object>> one = new ArrayList<>();
        one.add(row);
        List<Map<String, Object>> products = buildProducts(one);
        return products.isEmpty() ? null : products.get(0);
    }

    @Override
    public Map<String, Object> login(String email, String password)
    {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password))
        {
            throw new ServiceException("邮箱与密码不能为空");
        }
        Customer customer = customerService.login(email, password);
        if (customer == null || customer.getCustomerId() == null)
        {
            throw new ServiceException("登录失败");
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        redisCache.setCacheObject(CommerceConstants.CUSTOMER_TOKEN_PREFIX + token, customer.getCustomerId(),
                7L, TimeUnit.DAYS);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token", token);
        result.put("customerId", customer.getCustomerId());
        result.put("displayName", customer.getDisplayName());
        result.put("email", customer.getEmail());
        return result;
    }

    @Override
    public void logout(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            redisCache.deleteObject(CommerceConstants.CUSTOMER_TOKEN_PREFIX + token);
        }
    }

    @Override
    public Long resolveCustomerId(String token)
    {
        if (StringUtils.isEmpty(token))
        {
            return null;
        }
        Object value = redisCache.getCacheObject(CommerceConstants.CUSTOMER_TOKEN_PREFIX + token);
        return Convert.toLong(value);
    }

    @Override
    public Map<String, Object> account(Long customerId)
    {
        Customer customer = customerService.selectCustomerById(customerId);
        if (customer == null)
        {
            throw new ServiceException("客户不存在");
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("customerId", customer.getCustomerId());
        result.put("customerNo", customer.getCustomerNo());
        result.put("email", customer.getEmail());
        result.put("phone", customer.getPhone());
        result.put("displayName", customer.getDisplayName());
        result.put("countryCode", customer.getCountryCode());
        result.put("locale", customer.getLocale());
        result.put("status", customer.getStatus());
        result.put("createTime", customer.getCreateTime());
        return result;
    }

    @Override
    public List<Map<String, Object>> cart(Long customerId)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        Cart cart = cartService.selectCartByCustomerId(customerId);
        if (cart == null || cart.getCartId() == null)
        {
            return result;
        }
        List<CartItem> items = cartService.selectCartItems(cart.getCartId());
        if (items == null)
        {
            return result;
        }
        for (CartItem item : items)
        {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("cartItemId", item.getCartItemId());
            row.put("skuId", item.getSkuId());
            row.put("skuCode", item.getSkuCode());
            row.put("quantity", item.getQuantity());
            Map<String, Object> info = item.getSkuId() == null ? null : storefrontMapper.selectSkuInfo(item.getSkuId());
            if (info != null)
            {
                row.put("skuCode", info.get("skuCode"));
                row.put("name", info.get("name"));
                row.put("image", info.get("image"));
                row.put("unitPriceAmount", Convert.toLong(info.get("unitPriceAmount"), 0L));
                row.put("currencyCode", info.get("currencyCode"));
                row.put("stock", Convert.toInt(info.get("stock"), 0));
            }
            else
            {
                row.put("unitPriceAmount", 0L);
                row.put("currencyCode", "USD");
                row.put("stock", 0);
            }
            result.add(row);
        }
        return result;
    }

    @Override
    public Map<String, Object> addCart(Long customerId, Long skuId, Integer quantity)
    {
        if (skuId == null)
        {
            throw new ServiceException("SKU 不能为空");
        }
        int qty = quantity == null ? 1 : quantity;
        if (qty <= 0)
        {
            throw new ServiceException("数量必须大于 0");
        }
        CartItem item = cartService.addItem(customerId, skuId, qty);
        Map<String, Object> result = new LinkedHashMap<>();
        if (item != null)
        {
            result.put("cartItemId", item.getCartItemId());
            result.put("skuId", item.getSkuId());
            result.put("quantity", item.getQuantity());
        }
        Map<String, Object> info = storefrontMapper.selectSkuInfo(skuId);
        if (info != null)
        {
            result.put("skuCode", info.get("skuCode"));
            result.put("name", info.get("name"));
            result.put("image", info.get("image"));
            result.put("unitPriceAmount", Convert.toLong(info.get("unitPriceAmount"), 0L));
            result.put("currencyCode", info.get("currencyCode"));
            result.put("stock", Convert.toInt(info.get("stock"), 0));
        }
        return result;
    }

    @Override
    public Map<String, Object> updateCart(Long customerId, Long cartItemId, Integer quantity, Boolean selected)
    {
        checkCartItemOwner(customerId, cartItemId);
        if (quantity != null && quantity <= 0)
        {
            throw new ServiceException("数量必须大于 0");
        }
        if (quantity == null && selected == null)
        {
            throw new ServiceException("请提供数量或选中状态");
        }
        CartItem item = new CartItem();
        item.setCartItemId(cartItemId);
        item.setQuantity(quantity);
        item.setSelected(selected);
        cartService.updateItem(item);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("cartItemId", cartItemId);
        if (quantity != null)
        {
            result.put("quantity", quantity);
        }
        if (selected != null)
        {
            result.put("selected", selected);
        }
        return result;
    }

    @Override
    public int removeCart(Long customerId, Long cartItemId)
    {
        checkCartItemOwner(customerId, cartItemId);
        return cartService.removeItem(cartItemId);
    }

    @Override
    public Map<String, Object> orders(Long customerId, String status)
    {
        List<Map<String, Object>> rows = storefrontMapper.selectOrders(customerId,
                "all".equals(status) ? null : status);
        Map<Long, List<Map<String, Object>>> itemMap = new HashMap<>();
        if (rows != null && !rows.isEmpty())
        {
            List<Long> orderIds = new ArrayList<>();
            for (Map<String, Object> row : rows)
            {
                Long orderId = Convert.toLong(row.get("orderId"));
                if (orderId != null)
                {
                    orderIds.add(orderId);
                }
            }
            if (!orderIds.isEmpty())
            {
                List<Map<String, Object>> itemRows = storefrontMapper.selectOrderItems(orderIds);
                if (itemRows != null)
                {
                    for (Map<String, Object> item : itemRows)
                    {
                        Long orderId = Convert.toLong(item.get("orderId"));
                        itemMap.computeIfAbsent(orderId, k -> new ArrayList<>()).add(toItemMap(item));
                    }
                }
            }
            for (Map<String, Object> row : rows)
            {
                row.put("items", itemMap.getOrDefault(Convert.toLong(row.get("orderId")), new ArrayList<>()));
            }
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", rows == null ? new ArrayList<>() : rows);
        result.put("total", rows == null ? 0 : rows.size());
        return result;
    }

    @Override
    public Map<String, Object> order(Long customerId, String orderNo)
    {
        Orders order = orderService.selectOrderByNo(orderNo);
        if (order == null)
        {
            throw new ServiceException("订单不存在");
        }
        if (order.getCustomerId() == null || !order.getCustomerId().equals(customerId))
        {
            throw new ServiceException("无权查看该订单");
        }
        OrderDetailVo detail = orderService.selectOrderDetail(order.getOrderId());
        Map<Long, OrderItemSnapshot> snapshotMap = new HashMap<>();
        if (detail != null && detail.getItemSnapshots() != null)
        {
            for (OrderItemSnapshot snapshot : detail.getItemSnapshots())
            {
                if (snapshot.getOrderItemId() != null)
                {
                    snapshotMap.put(snapshot.getOrderItemId(), snapshot);
                }
            }
        }

        List<Map<String, Object>> items = new ArrayList<>();
        if (detail != null && detail.getItems() != null)
        {
            for (OrderItem item : detail.getItems())
            {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("orderItemId", item.getOrderItemId());
                row.put("skuId", item.getSkuId());
                row.put("sku", item.getSkuCode());
                row.put("quantity", item.getQuantity());
                row.put("qty", item.getQuantity());
                row.put("unitPriceAmount", item.getUnitPriceAmount());
                row.put("unit", item.getUnitPriceAmount());
                row.put("lineTotalAmount", item.getLineTotalAmount());
                row.put("currencyCode", item.getCurrencyCode());
                OrderItemSnapshot snapshot = snapshotMap.get(item.getOrderItemId());
                if (snapshot != null)
                {
                    row.put("name", snapshot.getProductName());
                    row.put("image", snapshot.getImageUrl());
                    row.put("spec", specText(snapshot.getAttributesJson()));
                }
                items.add(row);
            }
        }

        List<Map<String, Object>> itemSnapshots = new ArrayList<>();
        if (detail != null && detail.getItemSnapshots() != null)
        {
            for (OrderItemSnapshot snapshot : detail.getItemSnapshots())
            {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("orderItemId", snapshot.getOrderItemId());
                row.put("productName", snapshot.getProductName());
                row.put("skuCode", snapshot.getSkuCode());
                row.put("attributes", parseMap(snapshot.getAttributesJson()));
                row.put("imageUrl", snapshot.getImageUrl());
                row.put("originCountryCode", snapshot.getOriginCountryCode());
                row.put("hsCode", snapshot.getHsCode());
                row.put("declaredName", snapshot.getDeclaredName());
                row.put("declaredValueAmount", snapshot.getDeclaredValueAmount());
                row.put("declaredCurrencyCode", snapshot.getDeclaredCurrencyCode());
                row.put("unitPriceAmount", snapshot.getUnitPriceAmount());
                row.put("currencyCode", snapshot.getCurrencyCode());
                itemSnapshots.add(row);
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("order", toOrderMap(order));
        result.put("items", items);
        result.put("itemSnapshots", itemSnapshots);
        result.put("snapshot", toSnapshotMap(detail == null ? null : detail.getSnapshot()));
        List<Map<String, Object>> histories = new ArrayList<>();
        if (detail != null && detail.getHistories() != null)
        {
            for (OrderStateHistory history : detail.getHistories())
            {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("fromStatus", history.getFromStatus());
                row.put("toStatus", history.getToStatus());
                row.put("statusType", history.getStatusType());
                row.put("reason", history.getReason());
                row.put("operatorType", history.getOperatorType());
                row.put("operatorId", history.getOperatorId());
                row.put("occurredAt", history.getOccurredAt());
                histories.add(row);
            }
        }
        result.put("histories", histories);
        return result;
    }

    @Override
    public Map<String, Object> createOrder(Long customerId, CreateOrderRequest request)
    {
        if (request == null)
        {
            throw new ServiceException("下单参数不能为空");
        }
        request.setCustomerId(customerId);
        OrderCreateResult createResult = orderService.createOrder(request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("orderId", createResult.getOrderId());
        result.put("orderNo", createResult.getOrderNo());
        result.put("totalAmount", createResult.getTotalAmount());
        result.put("currencyCode", createResult.getCurrencyCode());
        result.put("paymentId", createResult.getPaymentId());
        result.put("status", CommerceConstants.ORDER_PENDING_PAYMENT);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> pay(Long customerId, String orderNo)
    {
        Map<String, Object> brief = storefrontMapper.selectOrderBrief(orderNo);
        if (brief == null)
        {
            throw new ServiceException("订单不存在");
        }
        Long ownerId = Convert.toLong(brief.get("customerId"));
        if (ownerId == null || !ownerId.equals(customerId))
        {
            throw new ServiceException("无权操作该订单");
        }
        if (CommerceConstants.ORDER_CANCELLED.equals(brief.get("status")))
        {
            throw new ServiceException("订单已取消，无法支付");
        }
        if (CommerceConstants.PAY_PAID.equals(brief.get("paymentStatus")))
        {
            Map<String, Object> paid = new LinkedHashMap<>();
            paid.put("orderNo", brief.get("orderNo"));
            paid.put("status", brief.get("status"));
            paid.put("paymentStatus", brief.get("paymentStatus"));
            return paid;
        }
        Long orderId = Convert.toLong(brief.get("orderId"));
        Map<String, Object> unpaid = storefrontMapper.selectLatestPayment(orderId);
        Long paymentId;
        if (unpaid == null)
        {
            Payment payment = paymentService.createPayment(orderId, "MOCK", "MOCK");
            if (payment == null || payment.getPaymentId() == null)
            {
                throw new ServiceException("支付单创建失败");
            }
            paymentId = payment.getPaymentId();
        }
        else
        {
            paymentId = Convert.toLong(unpaid.get("paymentId"));
        }
        paymentService.mockSuccess(paymentId);
        Map<String, Object> updated = storefrontMapper.selectOrderBrief(orderNo);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("orderNo", orderNo);
        result.put("status", updated == null ? brief.get("status") : updated.get("status"));
        result.put("paymentStatus", updated == null ? brief.get("paymentStatus") : updated.get("paymentStatus"));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> applyAftersale(Long customerId, String orderNo, Long amount, String reason, String type)
    {
        Orders order = orderService.selectOrderByNo(orderNo);
        if (order == null)
        {
            throw new ServiceException("订单不存在");
        }
        if (order.getCustomerId() == null || !order.getCustomerId().equals(customerId))
        {
            throw new ServiceException("无权操作该订单");
        }
        if (CommerceConstants.ORDER_CANCELLED.equals(order.getOrderStatus()))
        {
            throw new ServiceException("已取消订单不能申请售后");
        }
        long total = order.getTotalAmount() == null ? 0L : order.getTotalAmount();
        long refunded = Convert.toLong(refundMapper.sumRefundedByOrderId(order.getOrderId()), 0L);
        long apply = amount == null ? total - refunded : amount;
        if (apply <= 0)
        {
            throw new ServiceException("退款金额必须大于 0");
        }
        if (refunded + apply > total)
        {
            throw new ServiceException("累计退款金额超过订单金额");
        }
        String refundNo = CommerceNoUtils.refundNo();
        Refund refund = new Refund();
        refund.setRefundNo(refundNo);
        refund.setOrderId(order.getOrderId());
        refund.setAmount(apply);
        refund.setCurrencyCode(order.getCurrencyCode());
        refund.setRefundStatus(CommerceConstants.REFUND_REQUESTED);
        refund.setReason(reason);
        refund.setRequestedAt(new Date());
        refund.setRemark(StringUtils.isEmpty(type) ? "仅退款" : type);
        refundMapper.insertRefund(refund);
        refundMapper.updateOrderAfterSaleStatus(order.getOrderId(), CommerceConstants.AFTER_SALE_REQUESTED);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("no", refundNo);
        result.put("status", CommerceConstants.REFUND_REQUESTED);
        return result;
    }

    @Override
    public List<Map<String, Object>> aftersales(Long customerId)
    {
        List<Map<String, Object>> rows = refundMapper.selectRefundsByCustomer(customerId);
        List<Map<String, Object>> result = new ArrayList<>();
        if (rows == null)
        {
            return result;
        }
        for (Map<String, Object> row : rows)
        {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("no", row.get("refundNo"));
            item.put("refundNo", row.get("refundNo"));
            item.put("orderNo", row.get("orderNo"));
            item.put("amount", row.get("amount"));
            item.put("currencyCode", row.get("currencyCode"));
            item.put("status", row.get("refundStatus"));
            item.put("reason", row.get("reason"));
            item.put("type", row.get("remark"));
            item.put("requestedAt", row.get("requestedAt"));
            item.put("completedAt", row.get("completedAt"));
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> addresses(Long customerId)
    {
        CustomerAddress query = new CustomerAddress();
        query.setCustomerId(customerId);
        List<CustomerAddress> rows = customerAddressService.selectAddressList(query);
        List<Map<String, Object>> result = new ArrayList<>();
        if (rows == null)
        {
            return result;
        }
        for (CustomerAddress address : rows)
        {
            result.add(toAddressMap(address));
        }
        return result;
    }

    @Override
    public Map<String, Object> saveAddress(Long customerId, Map<String, Object> address)
    {
        if (address == null)
        {
            throw new ServiceException("地址参数不能为空");
        }
        Long addressId = Convert.toLong(address.get("addressId"));
        CustomerAddress entity = new CustomerAddress();
        entity.setCustomerId(customerId);
        entity.setRecipientName(Convert.toStr(address.get("recipientName")));
        entity.setRecipientPhone(Convert.toStr(address.get("recipientPhone")));
        entity.setCountryCode(Convert.toStr(address.get("countryCode")));
        entity.setStateCode(Convert.toStr(address.get("stateCode")));
        entity.setCity(Convert.toStr(address.get("city")));
        entity.setDistrict(Convert.toStr(address.get("district")));
        entity.setAddressLine1(Convert.toStr(address.get("addressLine1")));
        entity.setAddressLine2(Convert.toStr(address.get("addressLine2")));
        entity.setPostalCode(Convert.toStr(address.get("postalCode")));
        entity.setIsDefault(Convert.toBool(address.get("isDefault"), false));
        if (addressId != null)
        {
            CustomerAddress exist = customerAddressService.selectAddressById(addressId);
            if (exist == null)
            {
                throw new ServiceException("收货地址不存在");
            }
            if (!customerId.equals(exist.getCustomerId()))
            {
                throw new ServiceException("无权操作该地址");
            }
            entity.setAddressId(addressId);
            customerAddressService.updateAddress(entity);
        }
        else
        {
            if (StringUtils.isEmpty(entity.getRecipientName()) || StringUtils.isEmpty(entity.getCity())
                    || StringUtils.isEmpty(entity.getAddressLine1()))
            {
                throw new ServiceException("收件人、城市与详细地址不能为空");
            }
            customerAddressService.insertAddress(entity);
        }
        CustomerAddress saved = entity.getAddressId() == null ? null
                : customerAddressService.selectAddressById(entity.getAddressId());
        return toAddressMap(saved == null ? entity : saved);
    }

    private void checkCartItemOwner(Long customerId, Long cartItemId)
    {
        if (cartItemId == null)
        {
            throw new ServiceException("购物车明细不能为空");
        }
        Long ownerId = storefrontMapper.selectCartItemCustomerId(cartItemId);
        if (ownerId == null)
        {
            throw new ServiceException("购物车明细不存在");
        }
        if (!ownerId.equals(customerId))
        {
            throw new ServiceException("无权操作该购物车明细");
        }
    }

    private List<Map<String, Object>> buildProducts(List<Map<String, Object>> rows)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        if (rows == null || rows.isEmpty())
        {
            return result;
        }
        List<Long> spuIds = new ArrayList<>();
        for (Map<String, Object> row : rows)
        {
            Long spuId = Convert.toLong(row.get("spuId"));
            if (spuId != null)
            {
                spuIds.add(spuId);
            }
        }
        if (spuIds.isEmpty())
        {
            return result;
        }
        Map<Long, List<Map<String, Object>>> skuMap = new HashMap<>();
        Map<Long, Map<String, Object>> stockMap = new HashMap<>();
        Map<Long, Map<String, Object>> channelMap = new HashMap<>();
        List<Map<String, Object>> skuRows = storefrontMapper.selectSkuRows(spuIds);
        if (skuRows != null)
        {
            for (Map<String, Object> sku : skuRows)
            {
                Long spuId = Convert.toLong(sku.get("spuId"));
                skuMap.computeIfAbsent(spuId, k -> new ArrayList<>()).add(sku);
            }
        }
        List<Map<String, Object>> inventoryRows = storefrontMapper.selectInventoryRows(spuIds);
        if (inventoryRows != null)
        {
            for (Map<String, Object> inventory : inventoryRows)
            {
                Long spuId = Convert.toLong(inventory.get("spuId"));
                Map<String, Object> map = stockMap.computeIfAbsent(spuId, k -> new LinkedHashMap<>());
                map.put(String.valueOf(inventory.get("warehouseCode")),
                        Convert.toInt(inventory.get("available"), 0));
            }
        }
        List<Map<String, Object>> channelRows = storefrontMapper.selectChannelRows(spuIds);
        if (channelRows != null)
        {
            for (Map<String, Object> channel : channelRows)
            {
                Long spuId = Convert.toLong(channel.get("spuId"));
                Map<String, Object> map = channelMap.computeIfAbsent(spuId, k -> new LinkedHashMap<>());
                map.put(String.valueOf(channel.get("channelCode")),
                        channelStatusLabel(String.valueOf(channel.get("listingStatus"))));
            }
        }
        for (Map<String, Object> row : rows)
        {
            Long spuId = Convert.toLong(row.get("spuId"));
            result.add(buildProduct(row, skuMap.get(spuId), stockMap.get(spuId), channelMap.get(spuId)));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> buildProduct(Map<String, Object> row, List<Map<String, Object>> skus,
            Map<String, Object> stock, Map<String, Object> channels)
    {
        Map<String, Object> dto = new LinkedHashMap<>();
        Long spuId = Convert.toLong(row.get("spuId"));
        dto.put("id", spuId);
        dto.put("spu", row.get("spuCode"));
        dto.put("sku", row.get("skuCode"));
        dto.put("name", row.get("name"));
        dto.put("nameEn", row.get("nameEn"));
        dto.put("category", row.get("category"));
        dto.put("categoryLabel", row.get("categoryLabel"));
        dto.put("brand", row.get("brand"));
        dto.put("origin", row.get("origin"));
        dto.put("hsCode", row.get("hsCode"));
        dto.put("declaredName", row.get("declaredName"));
        dto.put("declaredValue", toYuan(row.get("declaredAmount")));
        dto.put("weightG", row.get("weightG"));
        dto.put("status", row.get("status"));
        dto.put("badge", row.get("badge"));
        dto.put("price", toPrice(row));
        dto.put("was", toWas(row));
        dto.put("rating", row.get("rating"));
        dto.put("reviews", row.get("reviews"));
        dto.put("sold", formatSold(Convert.toInt(row.get("soldCount"), 0)));
        dto.put("colors", parseList(row.get("colorsJson")));
        dto.put("sizes", parseList(row.get("sizesJson")));
        dto.put("tags", parseList(row.get("tagsJson")));
        dto.put("stock", stock == null ? new LinkedHashMap<String, Object>() : stock);
        dto.put("channels", channels == null ? new LinkedHashMap<String, Object>() : channels);
        List<Object> images = parseList(row.get("imagesJson"));
        if (images.isEmpty() && row.get("imageUrl") != null)
        {
            images.add(row.get("imageUrl"));
        }
        dto.put("images", images);
        dto.put("desc", row.get("description"));
        dto.put("specs", parseMap(row.get("specsJson")));

        List<Map<String, Object>> skuList = new ArrayList<>();
        if (skus != null)
        {
            for (Map<String, Object> sku : skus)
            {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", sku.get("skuId"));
                item.put("sku", sku.get("skuCode"));
                item.put("attributes", parseMap(sku.get("attributesJson")));
                item.put("weightG", sku.get("weightG"));
                item.put("hsCode", sku.get("hsCode"));
                item.put("declaredName", sku.get("declaredName"));
                item.put("declaredValue", toYuan(sku.get("declaredAmount")));
                item.put("stock", Convert.toInt(sku.get("stock"), 0));
                item.put("price", skuPrice(sku));
                item.put("status", sku.get("status"));
                skuList.add(item);
            }
        }
        dto.put("skus", skuList);
        return dto;
    }

    private Map<String, Object> skuPrice(Map<String, Object> row)
    {
        Map<String, Object> price = new LinkedHashMap<>();
        price.put("USD", Convert.toLong(row.get("priceUsd"), 0L));
        price.put("EUR", Convert.toLong(row.get("priceEur"), 0L));
        price.put("CNY", Convert.toLong(row.get("priceCny"), 0L));
        return price;
    }

    private Map<String, Object> toPrice(Map<String, Object> row)
    {
        return skuPrice(row);
    }

    private Map<String, Object> toWas(Map<String, Object> row)
    {
        Map<String, Object> was = new LinkedHashMap<>();
        was.put("USD", Convert.toLong(row.get("wasUsd"), 0L));
        was.put("EUR", Convert.toLong(row.get("wasEur"), 0L));
        was.put("CNY", Convert.toLong(row.get("wasCny"), 0L));
        return was;
    }

    private Object parseJsonValue(Object raw)
    {
        if (raw == null)
        {
            return null;
        }
        String text = String.valueOf(raw);
        if (StringUtils.isEmpty(text) || "null".equalsIgnoreCase(text))
        {
            return null;
        }
        try
        {
            return JSON.parse(text);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Object> parseList(Object raw)
    {
        Object value = parseJsonValue(raw);
        if (value instanceof List)
        {
            return (List<Object>) value;
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseMap(Object raw)
    {
        Object value = parseJsonValue(raw);
        if (value instanceof Map)
        {
            return (Map<String, Object>) value;
        }
        return new LinkedHashMap<>();
    }

    private BigDecimal toYuan(Object amount)
    {
        Long value = Convert.toLong(amount);
        if (value == null)
        {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private String formatSold(Integer soldCount)
    {
        int count = soldCount == null ? 0 : soldCount;
        if (count >= 1000)
        {
            return BigDecimal.valueOf(count).divide(BigDecimal.valueOf(1000), 1, RoundingMode.HALF_UP)
                    .stripTrailingZeros().toPlainString() + "k";
        }
        return String.valueOf(count);
    }

    private String channelStatusLabel(String status)
    {
        if (status == null)
        {
            return "";
        }
        switch (status)
        {
            case "LIVE":
                return "在售";
            case "DRAFT":
                return "草稿";
            case "PENDING":
                return "同步中";
            case "PAUSED":
                return "暂停";
            case "CLOSED":
                return "已关闭";
            default:
                return status;
        }
    }

    private String specText(Object attributesJson)
    {
        Map<String, Object> attributes = parseMap(attributesJson);
        if (attributes.isEmpty())
        {
            return null;
        }
        StringBuilder text = new StringBuilder();
        for (Object value : attributes.values())
        {
            if (value == null)
            {
                continue;
            }
            if (text.length() > 0)
            {
                text.append(" / ");
            }
            text.append(value);
        }
        return text.length() == 0 ? null : text.toString();
    }

    private Map<String, Object> toItemMap(Map<String, Object> item)
    {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("orderItemId", item.get("orderItemId"));
        row.put("productId", item.get("skuId"));
        row.put("skuId", item.get("skuId"));
        row.put("name", item.get("name"));
        row.put("sku", item.get("skuCode"));
        row.put("image", item.get("image"));
        row.put("spec", specText(item.get("attributesJson")));
        row.put("qty", item.get("quantity"));
        row.put("quantity", item.get("quantity"));
        row.put("unit", item.get("unitPriceAmount"));
        row.put("unitPriceAmount", item.get("unitPriceAmount"));
        row.put("lineTotalAmount", item.get("lineTotalAmount"));
        row.put("currencyCode", item.get("currencyCode"));
        return row;
    }

    private Map<String, Object> toOrderMap(Orders order)
    {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("orderId", order.getOrderId());
        row.put("orderNo", order.getOrderNo());
        row.put("shopId", order.getShopId());
        row.put("customerId", order.getCustomerId());
        row.put("channelCode", order.getChannelCode());
        row.put("countryCode", order.getCountryCode());
        row.put("currencyCode", order.getCurrencyCode());
        row.put("subtotalAmount", order.getSubtotalAmount());
        row.put("discountAmount", order.getDiscountAmount());
        row.put("shippingAmount", order.getShippingAmount());
        row.put("taxAmount", order.getTaxAmount());
        row.put("totalAmount", order.getTotalAmount());
        row.put("status", order.getOrderStatus());
        row.put("paymentStatus", order.getPaymentStatus());
        row.put("fulfillmentStatus", order.getFulfillmentStatus());
        row.put("afterSaleStatus", order.getAfterSaleStatus());
        row.put("couponCode", order.getCouponCode());
        row.put("placedAt", order.getPlacedAt());
        row.put("paidAt", order.getPaidAt());
        row.put("createTime", order.getCreateTime());
        return row;
    }

    private Map<String, Object> toSnapshotMap(OrderSnapshot snapshot)
    {
        if (snapshot == null)
        {
            return null;
        }
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("recipientName", snapshot.getRecipientName());
        row.put("recipientPhone", snapshot.getRecipientPhone());
        row.put("countryCode", snapshot.getCountryCode());
        row.put("stateCode", snapshot.getStateCode());
        row.put("city", snapshot.getCity());
        row.put("district", snapshot.getDistrict());
        row.put("addressLine1", snapshot.getAddressLine1());
        row.put("addressLine2", snapshot.getAddressLine2());
        row.put("postalCode", snapshot.getPostalCode());
        return row;
    }

    private Map<String, Object> toAddressMap(CustomerAddress address)
    {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("addressId", address.getAddressId());
        row.put("recipientName", address.getRecipientName());
        row.put("recipientPhone", address.getRecipientPhone());
        row.put("countryCode", address.getCountryCode());
        row.put("stateCode", address.getStateCode());
        row.put("city", address.getCity());
        row.put("district", address.getDistrict());
        row.put("addressLine1", address.getAddressLine1());
        row.put("addressLine2", address.getAddressLine2());
        row.put("postalCode", address.getPostalCode());
        row.put("isDefault", address.getIsDefault());
        return row;
    }
}
