package com.ruoyi.commerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.commerce.domain.LocalMessage;
import com.ruoyi.commerce.service.ILocalMessageService;

/**
 * 本地消息控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/message")
public class LocalMessageController extends BaseController
{
    @Autowired
    private ILocalMessageService localMessageService;

    @RequiresPermissions("commerce:message:list")
    @GetMapping("/list")
    public TableDataInfo list(LocalMessage localMessage)
    {
        startPage();
        List<LocalMessage> list = localMessageService.selectLocalMessageList(localMessage);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:message:list")
    @GetMapping("/pending")
    public AjaxResult pending(@RequestParam(defaultValue = "100") int limit)
    {
        List<LocalMessage> list = localMessageService.selectPendingMessages(limit);
        return success(list);
    }
}
