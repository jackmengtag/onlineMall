package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单主档 orders
 *
 * <p>对应文档第 8 章数据库模型与第 5.2 节订单状态机：订单是核心聚合根，
 * 独立维护 order_status、payment_status、fulfillment_status、after_sale_status 四个正交状态。</p>
 *
 * @author ruoyi
 */
public class Orders implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 订单主键 */
    private Long orderId;

    /** 订单号 */
    private String orderNo;

    /** 客户主键 */
    private Long customerId;

    /** 渠道编码 */
    private String channelCode;

    /** 外部订单号 */
    private String externalOrderId;

    /** 国家/地区 */
    private String countryCode;

    /** 币种 */
    private String currencyCode;

    /** 商品小计（最小货币单位） */
    private Long subtotalAmount;

    /** 优惠金额（最小货币单位） */
    private Long discountAmount;

    /** 运费（最小货币单位） */
    private Long shippingAmount;

    /** 税费（最小货币单位） */
    private Long taxAmount;

    /** 应付总额（最小货币单位） */
    private Long totalAmount;

    /** 订单主状态 */
    private String orderStatus;

    /** 支付状态 */
    private String paymentStatus;

    /** 履约状态 */
    private String fulfillmentStatus;

    /** 售后状态 */
    private String afterSaleStatus;

    /** 下单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date placedAt;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paidAt;

    /** 取消时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelledAt;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completedAt;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 订单明细（非数据库字段，仅详情返回） */
    private List<OrderItem> items;

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public String getChannelCode()
    {
        return channelCode;
    }

    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
    }

    public String getExternalOrderId()
    {
        return externalOrderId;
    }

    public void setExternalOrderId(String externalOrderId)
    {
        this.externalOrderId = externalOrderId;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public Long getSubtotalAmount()
    {
        return subtotalAmount;
    }

    public void setSubtotalAmount(Long subtotalAmount)
    {
        this.subtotalAmount = subtotalAmount;
    }

    public Long getDiscountAmount()
    {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public Long getShippingAmount()
    {
        return shippingAmount;
    }

    public void setShippingAmount(Long shippingAmount)
    {
        this.shippingAmount = shippingAmount;
    }

    public Long getTaxAmount()
    {
        return taxAmount;
    }

    public void setTaxAmount(Long taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    public Long getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus()
    {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }

    public String getFulfillmentStatus()
    {
        return fulfillmentStatus;
    }

    public void setFulfillmentStatus(String fulfillmentStatus)
    {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    public String getAfterSaleStatus()
    {
        return afterSaleStatus;
    }

    public void setAfterSaleStatus(String afterSaleStatus)
    {
        this.afterSaleStatus = afterSaleStatus;
    }

    public Date getPlacedAt()
    {
        return placedAt;
    }

    public void setPlacedAt(Date placedAt)
    {
        this.placedAt = placedAt;
    }

    public Date getPaidAt()
    {
        return paidAt;
    }

    public void setPaidAt(Date paidAt)
    {
        this.paidAt = paidAt;
    }

    public Date getCancelledAt()
    {
        return cancelledAt;
    }

    public void setCancelledAt(Date cancelledAt)
    {
        this.cancelledAt = cancelledAt;
    }

    public Date getCompletedAt()
    {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt)
    {
        this.completedAt = completedAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public List<OrderItem> getItems()
    {
        return items;
    }

    public void setItems(List<OrderItem> items)
    {
        this.items = items;
    }
}
