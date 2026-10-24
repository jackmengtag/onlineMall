package com.ruoyi.commerce.controller.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.commerce.domain.Shipment;
import com.ruoyi.commerce.domain.TrackingEvent;
import com.ruoyi.commerce.service.IShipmentService;
import com.ruoyi.commerce.service.IShopService;
import com.ruoyi.commerce.domain.vo.ShipmentDetailVo;

/**
 * 店铺中心 · 物流跟踪。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/shop/logistics")
public class ShopLogisticsController extends BaseController
{
    @Autowired
    private IShipmentService shipmentService;

    @Autowired
    private IShopService shopService;

    /**
     * 当前店铺运单列表。
     */
    @PreAuthorize("@ss.hasPermi('commerce:shop:logistics:list')")
    @GetMapping("/list")
    public TableDataInfo list(Shipment shipment)
    {
        shipment.setShopId(currentShopId());
        startPage();
        List<Shipment> list = shipmentService.selectShipmentList(shipment);
        return getDataTable(list);
    }

    /**
     * 运单详情。
     */
    @PreAuthorize("@ss.hasPermi('commerce:shop:logistics:list')")
    @GetMapping("/{shipmentId}")
    public AjaxResult getInfo(@PathVariable Long shipmentId)
    {
        ShipmentDetailVo detail = shipmentService.selectShipmentDetail(shipmentId);
        checkOwnedShipment(detail.getShipment());
        return success(detail);
    }

    /**
     * 追加物流轨迹。
     */
    @PreAuthorize("@ss.hasPermi('commerce:shop:logistics:edit')")
    @Log(title = "店铺物流轨迹", businessType = BusinessType.UPDATE)
    @PostMapping("/track")
    public AjaxResult track(@RequestBody TrackingEvent event)
    {
        if (event == null || event.getShipmentId() == null)
        {
            throw new ServiceException("运单不能为空");
        }
        checkOwnedShipment(shipmentService.selectShipmentDetail(event.getShipmentId()).getShipment());
        shipmentService.addTrackingEvent(event);
        return success();
    }

    private Long currentShopId()
    {
        Long shopId = shopService.currentShopId();
        if (shopId == null)
        {
            throw new ServiceException("当前用户未绑定店铺");
        }
        return shopId;
    }

    private Shipment checkOwnedShipment(Shipment shipment)
    {
        Long shopId = currentShopId();
        if (shipment == null || !shopId.equals(shipment.getShopId()))
        {
            throw new ServiceException("无权操作该运单");
        }
        return shipment;
    }
}
