package com.ruoyi.commerce.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.commerce.domain.Shipment;
import com.ruoyi.commerce.domain.TrackingEvent;
import com.ruoyi.commerce.service.IShipmentService;

/**
 * 运营后台 · 物流运单管理。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/shipment")
public class ShipmentAdminController extends BaseController
{
    @Autowired
    private IShipmentService shipmentService;

    /**
     * 运单列表。
     */
    @PreAuthorize("@ss.hasPermi('commerce:admin:shipment:list')")
    @GetMapping("/list")
    public TableDataInfo list(Shipment shipment)
    {
        startPage();
        List<Shipment> list = shipmentService.selectShipmentList(shipment);
        return getDataTable(list);
    }

    /**
     * 运单详情。
     */
    @PreAuthorize("@ss.hasPermi('commerce:admin:shipment:query')")
    @GetMapping("/{shipmentId}")
    public AjaxResult getInfo(@PathVariable Long shipmentId)
    {
        return success(shipmentService.selectShipmentDetail(shipmentId));
    }

    /**
     * 创建运单。
     */
    @PreAuthorize("@ss.hasPermi('commerce:admin:shipment:add')")
    @Log(title = "运单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestParam("fulfillmentId") Long fulfillmentId,
            @RequestParam("carrierCode") String carrierCode,
            @RequestParam(value = "trackingNo", required = false) String trackingNo)
    {
        return success(shipmentService.createShipment(fulfillmentId, carrierCode, trackingNo));
    }

    /**
     * 追加物流轨迹。
     */
    @PreAuthorize("@ss.hasPermi('commerce:admin:shipment:track')")
    @Log(title = "物流轨迹", businessType = BusinessType.UPDATE)
    @PostMapping("/track")
    public AjaxResult track(@RequestBody TrackingEvent event)
    {
        shipmentService.addTrackingEvent(event);
        return success();
    }

    /**
     * 标记运单签收。
     */
    @PreAuthorize("@ss.hasPermi('commerce:admin:shipment:deliver')")
    @Log(title = "运单签收", businessType = BusinessType.UPDATE)
    @PostMapping("/deliver/{shipmentId}")
    public AjaxResult deliver(@PathVariable Long shipmentId)
    {
        shipmentService.markDelivered(shipmentId);
        return success();
    }
}
