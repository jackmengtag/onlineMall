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
import com.ruoyi.commerce.domain.ProductSku;
import com.ruoyi.commerce.service.IProductSkuService;

/**
 * 商品 SKU 控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/product/sku")
public class ProductSkuController extends BaseController
{
    @Autowired
    private IProductSkuService productSkuService;

    @RequiresPermissions("commerce:sku:list")
    @GetMapping("/list")
    public TableDataInfo list(ProductSku productSku)
    {
        startPage();
        List<ProductSku> list = productSkuService.selectProductSkuList(productSku);
        return getDataTable(list);
    }

    @Log(title = "商品SKU", businessType = BusinessType.EXPORT)
    @RequiresPermissions("commerce:sku:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductSku productSku)
    {
        List<ProductSku> list = productSkuService.selectProductSkuList(productSku);
        ExcelUtil<ProductSku> util = new ExcelUtil<ProductSku>(ProductSku.class);
        util.exportExcel(response, list, "商品SKU数据");
    }

    @RequiresPermissions("commerce:sku:query")
    @GetMapping(value = "/{skuId}")
    public AjaxResult getInfo(@PathVariable Long skuId)
    {
        return success(productSkuService.selectProductSkuById(skuId));
    }

    @RequiresPermissions("commerce:sku:add")
    @Log(title = "商品SKU", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ProductSku productSku)
    {
        if (!productSkuService.checkSkuCodeUnique(productSku))
        {
            return error("新增商品SKU失败，SKU编码已存在");
        }
        return toAjax(productSkuService.insertProductSku(productSku));
    }

    @RequiresPermissions("commerce:sku:edit")
    @Log(title = "商品SKU", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ProductSku productSku)
    {
        if (!productSkuService.checkSkuCodeUnique(productSku))
        {
            return error("修改商品SKU失败，SKU编码已存在");
        }
        return toAjax(productSkuService.updateProductSku(productSku));
    }

    @RequiresPermissions("commerce:sku:remove")
    @Log(title = "商品SKU", businessType = BusinessType.DELETE)
    @DeleteMapping("/{skuIds}")
    public AjaxResult remove(@PathVariable Long[] skuIds)
    {
        return toAjax(productSkuService.deleteProductSkuByIds(skuIds));
    }
}
