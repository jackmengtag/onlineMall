package com.ruoyi.commerce.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.commerce.domain.ChannelListing;
import com.ruoyi.commerce.service.IChannelListingService;

/**
 * 渠道商品映射控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/product/listing")
public class ChannelListingController extends BaseController
{
    @Autowired
    private IChannelListingService channelListingService;

    @RequiresPermissions("commerce:listing:list")
    @GetMapping("/list")
    public TableDataInfo list(ChannelListing channelListing)
    {
        startPage();
        List<ChannelListing> list = channelListingService.selectChannelListingList(channelListing);
        return getDataTable(list);
    }

    @Log(title = "渠道商品映射", businessType = BusinessType.EXPORT)
    @RequiresPermissions("commerce:listing:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChannelListing channelListing)
    {
        List<ChannelListing> list = channelListingService.selectChannelListingList(channelListing);
        ExcelUtil<ChannelListing> util = new ExcelUtil<ChannelListing>(ChannelListing.class);
        util.exportExcel(response, list, "渠道商品映射数据");
    }

    @RequiresPermissions("commerce:listing:query")
    @GetMapping(value = "/{listingId}")
    public AjaxResult getInfo(@PathVariable Long listingId)
    {
        return success(channelListingService.selectChannelListingById(listingId));
    }

    @RequiresPermissions("commerce:listing:add")
    @Log(title = "渠道商品映射", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ChannelListing channelListing)
    {
        if (!channelListingService.checkListingUnique(channelListing))
        {
            return error("新增渠道商品映射失败，渠道商品映射已存在");
        }
        return toAjax(channelListingService.insertChannelListing(channelListing));
    }

    @RequiresPermissions("commerce:listing:edit")
    @Log(title = "渠道商品映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ChannelListing channelListing)
    {
        if (!channelListingService.checkListingUnique(channelListing))
        {
            return error("修改渠道商品映射失败，渠道商品映射已存在");
        }
        return toAjax(channelListingService.updateChannelListing(channelListing));
    }

    @RequiresPermissions("commerce:listing:remove")
    @Log(title = "渠道商品映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{listingIds}")
    public AjaxResult remove(@PathVariable Long[] listingIds)
    {
        return toAjax(channelListingService.deleteChannelListingByIds(listingIds));
    }
}
