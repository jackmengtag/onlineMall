package com.ruoyi.commerce.controller.admin;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.commerce.domain.Payment;
import com.ruoyi.commerce.domain.PaymentEvent;
import com.ruoyi.commerce.domain.dto.PaymentCallbackRequest;
import com.ruoyi.commerce.service.IPaymentService;

/**
 * 运营后台支付。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/payment")
public class PaymentAdminController extends BaseController
{
    @Autowired
    private IPaymentService paymentService;

    @RequiresPermissions("commerce:admin:payment:list")
    @GetMapping("/list")
    public TableDataInfo list(Payment payment, @RequestParam(required = false) Date beginTime,
            @RequestParam(required = false) Date endTime)
    {
        if (beginTime != null)
        {
            payment.getParams().put("beginTime", beginTime);
        }
        if (endTime != null)
        {
            payment.getParams().put("endTime", endTime);
        }
        startPage();
        List<Payment> list = paymentService.selectPaymentList(payment);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:admin:payment:query")
    @GetMapping("/{paymentId}")
    public AjaxResult getInfo(@PathVariable Long paymentId)
    {
        return success(paymentService.selectPaymentById(paymentId));
    }

    @RequiresPermissions("commerce:admin:payment:add")
    @Log(title = "支付管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestParam Long orderId, @RequestParam(required = false) String provider,
            @RequestParam(required = false) String method)
    {
        return success(paymentService.createPayment(orderId, provider, method));
    }

    @RequiresPermissions("commerce:admin:payment:add")
    @Log(title = "支付回调", businessType = BusinessType.UPDATE)
    @PostMapping("/callback")
    public AjaxResult callback(@RequestBody PaymentCallbackRequest request)
    {
        paymentService.handleCallback(request);
        return success();
    }

    @RequiresPermissions("commerce:admin:payment:mock")
    @Log(title = "支付管理", businessType = BusinessType.UPDATE)
    @PostMapping("/mock/{paymentId}")
    public AjaxResult mock(@PathVariable Long paymentId)
    {
        return toAjax(paymentService.mockSuccess(paymentId));
    }

    @RequiresPermissions("commerce:admin:payment:list")
    @GetMapping("/events/{paymentId}")
    public AjaxResult events(@PathVariable Long paymentId)
    {
        List<PaymentEvent> events = paymentService.selectPaymentEvents(paymentId);
        return success(events);
    }
}
