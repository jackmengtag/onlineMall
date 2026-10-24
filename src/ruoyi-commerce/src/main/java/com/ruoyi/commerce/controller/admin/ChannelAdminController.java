package com.ruoyi.commerce.controller.admin;

import java.util.List;

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

    @PreAuthorize("@ss.hasPermi('commerce:admin:channel:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChannelListing listing)
    {
        startPage();
        List<ChannelListing> list = channelListingService.selectChannelListingList(listing);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:channel:query')")
    @GetMapping("/{listingId}")
    public AjaxResult getInfo(@PathVariable Long listingId)
    {
        return success(channelListingService.selectChannelListingById(listingId));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:channel:add')")
    @Log(title = "渠道商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChannelListing listing)
    {
        listing.setCreateBy(getUsername());
        return toAjax(channelListingService.insertChannelListing(listing));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:channel:edit')")
    @Log(title = "渠道商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChannelListing listing)
    {
        listing.setUpdateBy(getUsername());
        return toAjax(channelListingService.updateChannelListing(listing));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:channel:remove')")
    @Log(title = "渠道商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{listingIds}")
    public AjaxResult remove(@PathVariable Long[] listingIds)
    {
        return toAjax(channelListingService.deleteChannelListingByIds(listingIds));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:channel:sync')")
    @Log(title = "渠道商品同步", businessType = BusinessType.UPDATE)
    @PostMapping("/sync/{listingId}")
    public AjaxResult sync(@PathVariable Long listingId)
    {
        return toAjax(channelListingService.syncListing(listingId));
    }
}
