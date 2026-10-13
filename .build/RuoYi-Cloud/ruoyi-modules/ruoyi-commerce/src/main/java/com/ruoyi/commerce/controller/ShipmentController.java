package com.ruoyi.commerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.commerce.domain.Shipment;
import com.ruoyi.commerce.domain.TrackingEvent;
import com.ruoyi.commerce.service.IFulfillmentService;

/**
 * 运单控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/shipment")
public class ShipmentController extends BaseController
{
    @Autowired
    private IFulfillmentService fulfillmentService;

    @RequiresPermissions("commerce:shipment:list")
    @GetMapping("/list")
    public TableDataInfo list(Shipment shipment)
    {
        startPage();
        List<Shipment> list = fulfillmentService.selectShipmentList(shipment);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:shipment:query")
    @GetMapping("/{shipmentId}")
    public AjaxResult getInfo(@PathVariable Long shipmentId)
    {
        return success(fulfillmentService.selectShipmentById(shipmentId));
    }

    @RequiresPermissions("commerce:shipment:query")
    @GetMapping("/tracking/{shipmentId}")
    public AjaxResult tracking(@PathVariable Long shipmentId)
    {
        return success(fulfillmentService.selectTrackingEvents(shipmentId));
    }

    @RequiresPermissions("commerce:shipment:edit")
    @Log(title = "物流轨迹", businessType = BusinessType.INSERT)
    @PostMapping("/tracking")
    public AjaxResult addTracking(@RequestBody TrackingEvent trackingEvent)
    {
        return toAjax(fulfillmentService.addTrackingEvent(trackingEvent));
    }
}
