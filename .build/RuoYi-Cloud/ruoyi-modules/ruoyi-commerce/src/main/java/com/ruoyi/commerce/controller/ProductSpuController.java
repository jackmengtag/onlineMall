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
import com.ruoyi.commerce.domain.ProductSpu;
import com.ruoyi.commerce.service.IProductSpuService;

/**
 * 商品 SPU 控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/product/spu")
public class ProductSpuController extends BaseController
{
    @Autowired
    private IProductSpuService productSpuService;

    @RequiresPermissions("commerce:spu:list")
    @GetMapping("/list")
    public TableDataInfo list(ProductSpu productSpu)
    {
        startPage();
        List<ProductSpu> list = productSpuService.selectProductSpuList(productSpu);
        return getDataTable(list);
    }

    @Log(title = "商品SPU", businessType = BusinessType.EXPORT)
    @RequiresPermissions("commerce:spu:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductSpu productSpu)
    {
        List<ProductSpu> list = productSpuService.selectProductSpuList(productSpu);
        ExcelUtil<ProductSpu> util = new ExcelUtil<ProductSpu>(ProductSpu.class);
        util.exportExcel(response, list, "商品SPU数据");
    }

    @RequiresPermissions("commerce:spu:query")
    @GetMapping(value = "/{spuId}")
    public AjaxResult getInfo(@PathVariable Long spuId)
    {
        return success(productSpuService.selectProductSpuById(spuId));
    }

    @RequiresPermissions("commerce:spu:add")
    @Log(title = "商品SPU", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ProductSpu productSpu)
    {
        if (!productSpuService.checkSpuCodeUnique(productSpu))
        {
            return error("新增商品SPU失败，SPU编码已存在");
        }
        return toAjax(productSpuService.insertProductSpu(productSpu));
    }

    @RequiresPermissions("commerce:spu:edit")
    @Log(title = "商品SPU", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ProductSpu productSpu)
    {
        if (!productSpuService.checkSpuCodeUnique(productSpu))
        {
            return error("修改商品SPU失败，SPU编码已存在");
        }
        return toAjax(productSpuService.updateProductSpu(productSpu));
    }

    @RequiresPermissions("commerce:spu:remove")
    @Log(title = "商品SPU", businessType = BusinessType.DELETE)
    @DeleteMapping("/{spuIds}")
    public AjaxResult remove(@PathVariable Long[] spuIds)
    {
        return toAjax(productSpuService.deleteProductSpuByIds(spuIds));
    }
}
