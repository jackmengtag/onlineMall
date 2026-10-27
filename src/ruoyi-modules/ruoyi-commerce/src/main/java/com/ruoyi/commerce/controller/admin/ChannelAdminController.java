package com.ruoyi.commerce.controller.admin;

import com.ruoyi.common.security.utils.SecurityUtils;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.commerce.domain.ChannelListing;
import com.ruoyi.commerce.service.IChannelListingService;

/**
 * 运营后台-渠道商品映射。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/channel")
public class ChannelAdminController extends BaseController
{
    @Autowired
    private IChannelListingService channelListingService;

    @RequiresPermissions("commerce:admin:channel:list")
    @GetMapping("/list")
    public TableDataInfo list(ChannelListing listing)
    {
        startPage();
        List<ChannelListing> list = channelListingService.selectChannelListingList(listing);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:admin:channel:query")
    @GetMapping("/{listingId}")
    public AjaxResult getInfo(@PathVariable Long listingId)
    {
        return success(channelListingService.selectChannelListingById(listingId));
    }

    @RequiresPermissions("commerce:admin:channel:add")
    @Log(title = "渠道商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChannelListing listing)
    {
        listing.setCreateBy(SecurityUtils.getUsername());
        return toAjax(channelListingService.insertChannelListing(listing));
    }

    @RequiresPermissions("commerce:admin:channel:edit")
    @Log(title = "渠道商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChannelListing listing)
    {
        listing.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(channelListingService.updateChannelListing(listing));
    }

    @RequiresPermissions("commerce:admin:channel:remove")
    @Log(title = "渠道商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{listingIds}")
    public AjaxResult remove(@PathVariable Long[] listingIds)
    {
        return toAjax(channelListingService.deleteChannelListingByIds(listingIds));
    }

    @RequiresPermissions("commerce:admin:channel:sync")
    @Log(title = "渠道商品同步", businessType = BusinessType.UPDATE)
    @PostMapping("/sync/{listingId}")
    public AjaxResult sync(@PathVariable Long listingId)
    {
        return toAjax(channelListingService.syncListing(listingId));
    }
}
