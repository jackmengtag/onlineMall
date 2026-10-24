package com.ruoyi.commerce.controller.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.commerce.service.IDashboardService;
import com.ruoyi.commerce.service.IShopService;

/**
 * 店铺中心经营报表。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/shop/report")
public class ShopReportController extends BaseController
{
    @Autowired
    private IDashboardService dashboardService;

    @Autowired
    private IShopService shopService;

    @PreAuthorize("@ss.hasPermi('commerce:shop:report:view')")
    @GetMapping("/summary")
    public AjaxResult summary(@RequestParam(defaultValue = "7") int days)
    {
        Long shopId = shopService.currentShopId();
        if (shopId == null)
        {
            throw new ServiceException("当前账号未绑定店铺");
        }
        return success(dashboardService.shopReport(shopId, days));
    }
}
