package com.ruoyi.commerce.controller.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.commerce.domain.ChannelConfig;
import com.ruoyi.commerce.service.IPlatformService;

/**
 * 平台渠道配置。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/platform/channel")
public class PlatformChannelController extends BaseController
{
    @Autowired
    private IPlatformService platformService;

    @PreAuthorize("@ss.hasPermi('commerce:platform:channel:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChannelConfig config)
    {
        return getDataTable(platformService.channels(config));
    }

    @PreAuthorize("@ss.hasPermi('commerce:platform:channel:query')")
    @GetMapping("/{channelConfigId}")
    public AjaxResult getInfo(@PathVariable Long channelConfigId)
    {
        return success(platformService.channel(channelConfigId));
    }

    @PreAuthorize("@ss.hasPermi('commerce:platform:channel:add')")
    @Log(title = "渠道配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChannelConfig config)
    {
        return toAjax(platformService.createChannel(config));
    }

    @PreAuthorize("@ss.hasPermi('commerce:platform:channel:edit')")
    @Log(title = "渠道配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChannelConfig config)
    {
        return toAjax(platformService.updateChannel(config));
    }

    @PreAuthorize("@ss.hasPermi('commerce:platform:channel:remove')")
    @Log(title = "渠道配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{channelConfigIds}")
    public AjaxResult remove(@PathVariable Long[] channelConfigIds)
    {
        return toAjax(platformService.deleteChannels(channelConfigIds));
    }
}
