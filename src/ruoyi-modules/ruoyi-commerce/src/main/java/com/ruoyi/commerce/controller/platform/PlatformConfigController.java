package com.ruoyi.commerce.controller.platform;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.commerce.domain.PlatformConfig;
import com.ruoyi.commerce.service.IPlatformService;

/**
 * 平台设置。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/platform/config")
public class PlatformConfigController extends BaseController
{
    @Autowired
    private IPlatformService platformService;

    @RequiresPermissions("commerce:platform:config:list")
    @GetMapping("/list")
    public TableDataInfo list(PlatformConfig config)
    {
        return getDataTable(platformService.configs(config));
    }

    @RequiresPermissions("commerce:platform:config:edit")
    @Log(title = "平台设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody List<PlatformConfig> configs)
    {
        return toAjax(platformService.updateConfigs(configs));
    }
}
