package com.ruoyi.commerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.commerce.domain.Payment;
import com.ruoyi.commerce.domain.PaymentEvent;
import com.ruoyi.commerce.domain.Refund;
import com.ruoyi.commerce.domain.dto.PaymentCallbackRequest;
import com.ruoyi.commerce.service.IPaymentService;

/**
 * 支付控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/payment")
public class PaymentController extends BaseController
{
    @Autowired
    private IPaymentService paymentService;

    @RequiresPermissions("commerce:payment:list")
    @GetMapping("/list")
    public TableDataInfo list(Payment payment)
    {
        startPage();
        List<Payment> list = paymentService.selectPaymentList(payment);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:payment:query")
    @GetMapping(value = "/{paymentId}")
    public AjaxResult getInfo(@PathVariable Long paymentId)
    {
        return success(paymentService.selectPaymentById(paymentId));
    }

    @RequiresPermissions("commerce:payment:query")
    @GetMapping("/events/{paymentId}")
    public AjaxResult events(@PathVariable Long paymentId)
    {
        List<PaymentEvent> list = paymentService.selectPaymentEvents(paymentId);
        return success(list);
    }

    @RequiresPermissions("commerce:payment:edit")
    @Log(title = "支付回调", businessType = BusinessType.UPDATE)
    @PostMapping("/callback")
    public AjaxResult callback(@Validated @RequestBody PaymentCallbackRequest request)
    {
        return toAjax(paymentService.handleCallback(request));
    }

    @RequiresPermissions("commerce:payment:edit")
    @Log(title = "退款单", businessType = BusinessType.INSERT)
    @PostMapping("/refund")
    public AjaxResult createRefund(@RequestBody Refund refund)
    {
        return toAjax(paymentService.createRefund(refund) != null);
    }

    @RequiresPermissions("commerce:payment:list")
    @GetMapping("/refund/list")
    public TableDataInfo refundList(Refund refund)
    {
        startPage();
        List<Refund> list = paymentService.selectRefundList(refund);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:payment:edit")
    @Log(title = "退款单", businessType = BusinessType.UPDATE)
    @PostMapping("/refund/complete/{refundId}")
    public AjaxResult completeRefund(@PathVariable Long refundId, @RequestParam("providerRefundId") String providerRefundId)
    {
        return toAjax(paymentService.completeRefund(refundId, providerRefundId));
    }
}
