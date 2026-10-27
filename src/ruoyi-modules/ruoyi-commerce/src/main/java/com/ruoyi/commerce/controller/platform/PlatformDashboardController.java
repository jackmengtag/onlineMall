package com.ruoyi.commerce.controller.platform;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.commerce.service.IPlatformService;

/**
 * 平台驾驶舱。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/platform/dashboard")
public class PlatformDashboardController extends BaseController
{
    @Autowired
    private IPlatformService platformService;

    @RequiresPermissions("commerce:platform:dashboard:view")
    @GetMapping("/summary")
    public AjaxResult summary()
    {
        return success(platformService.dashboard());
    }
}
