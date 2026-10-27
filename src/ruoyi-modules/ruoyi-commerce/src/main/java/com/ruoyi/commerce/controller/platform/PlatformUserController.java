package com.ruoyi.commerce.controller.platform;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.commerce.service.IPlatformService;

/**
 * 平台用户与店铺绑定。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/platform/user")
public class PlatformUserController extends BaseController
{
    @Autowired
    private IPlatformService platformService;

    @RequiresPermissions("commerce:platform:user:list")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String userName,
            @RequestParam(required = false) Long shopId)
    {
        return getDataTable(platformService.users(userName, shopId));
    }
}
