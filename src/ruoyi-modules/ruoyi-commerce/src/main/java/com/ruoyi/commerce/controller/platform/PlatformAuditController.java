package com.ruoyi.commerce.controller.platform;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.service.IPlatformService;

/**
 * 平台审计日志。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/platform/audit")
public class PlatformAuditController extends BaseController
{
    @Autowired
    private IPlatformService platformService;

    @RequiresPermissions("commerce:platform:audit:list")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String module,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime)
    {
        return getDataTable(platformService.audits(shopId, module, parseBegin(beginTime), parseEnd(endTime)));
    }

    private Date parseBegin(String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return null;
        }
        return DateUtils.parseDate(value.length() == 10 ? value + " 00:00:00" : value);
    }

    private Date parseEnd(String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return null;
        }
        return DateUtils.parseDate(value.length() == 10 ? value + " 23:59:59" : value);
    }
}
