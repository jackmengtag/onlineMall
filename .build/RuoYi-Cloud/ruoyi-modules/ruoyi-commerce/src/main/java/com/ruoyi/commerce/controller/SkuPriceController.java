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
import com.ruoyi.commerce.domain.SkuPrice;
import com.ruoyi.commerce.service.ISkuPriceService;

/**
 * SKU 价格控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/product/price")
public class SkuPriceController extends BaseController
{
    @Autowired
    private ISkuPriceService skuPriceService;

    @RequiresPermissions("commerce:price:list")
    @GetMapping("/list")
    public TableDataInfo list(SkuPrice skuPrice)
    {
        startPage();
        List<SkuPrice> list = skuPriceService.selectSkuPriceList(skuPrice);
        return getDataTable(list);
    }

    @Log(title = "SKU价格", businessType = BusinessType.EXPORT)
    @RequiresPermissions("commerce:price:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SkuPrice skuPrice)
    {
        List<SkuPrice> list = skuPriceService.selectSkuPriceList(skuPrice);
        ExcelUtil<SkuPrice> util = new ExcelUtil<SkuPrice>(SkuPrice.class);
        util.exportExcel(response, list, "SKU价格数据");
    }

    @RequiresPermissions("commerce:price:query")
    @GetMapping(value = "/{priceId}")
    public AjaxResult getInfo(@PathVariable Long priceId)
    {
        return success(skuPriceService.selectSkuPriceById(priceId));
    }

    @RequiresPermissions("commerce:price:add")
    @Log(title = "SKU价格", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SkuPrice skuPrice)
    {
        return toAjax(skuPriceService.insertSkuPrice(skuPrice));
    }

    @RequiresPermissions("commerce:price:edit")
    @Log(title = "SKU价格", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SkuPrice skuPrice)
    {
        return toAjax(skuPriceService.updateSkuPrice(skuPrice));
    }

    @RequiresPermissions("commerce:price:remove")
    @Log(title = "SKU价格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{priceIds}")
    public AjaxResult remove(@PathVariable Long[] priceIds)
    {
        return toAjax(skuPriceService.deleteSkuPriceByIds(priceIds));
    }
}
