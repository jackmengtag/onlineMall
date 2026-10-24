package com.ruoyi.commerce.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.commerce.domain.ProductSku;
import com.ruoyi.commerce.domain.ProductSpu;
import com.ruoyi.commerce.service.IProductSkuService;
import com.ruoyi.commerce.service.IProductSpuService;

/**
 * 运营后台-商品管理。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/product")
public class ProductAdminController extends BaseController
{
    @Autowired
    private IProductSpuService productSpuService;

    @Autowired
    private IProductSkuService productSkuService;

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductSpu spu)
    {
        startPage();
        List<ProductSpu> list = productSpuService.selectProductSpuList(spu);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:query')")
    @GetMapping("/{spuId}")
    public AjaxResult getInfo(@PathVariable Long spuId)
    {
        ProductSpu spu = productSpuService.selectProductSpuById(spuId);
        Map<String, Object> data = new HashMap<>();
        data.put("spu", spu);
        data.put("skus", spu == null ? new ArrayList<ProductSku>() : productSkuService.selectSkuBySpuId(spuId));
        return success(data);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:add')")
    @Log(title = "商品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductSpu spu)
    {
        spu.setCreateBy(getUsername());
        return toAjax(productSpuService.insertProductSpu(spu));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:edit')")
    @Log(title = "商品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductSpu spu)
    {
        spu.setUpdateBy(getUsername());
        return toAjax(productSpuService.updateProductSpu(spu));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:remove')")
    @Log(title = "商品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{spuIds}")
    public AjaxResult remove(@PathVariable Long[] spuIds)
    {
        return toAjax(productSpuService.deleteProductSpuByIds(spuIds));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:edit')")
    @Log(title = "商品状态", businessType = BusinessType.UPDATE)
    @PostMapping("/status")
    public AjaxResult changeStatus(@RequestParam Long spuId, @RequestParam String status)
    {
        return toAjax(productSpuService.changeStatus(spuId, status));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:list')")
    @GetMapping("/sku/list")
    public TableDataInfo skuList(ProductSku sku)
    {
        startPage();
        List<ProductSku> list = productSkuService.selectProductSkuList(sku);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:query')")
    @GetMapping("/sku/{skuId}")
    public AjaxResult skuInfo(@PathVariable Long skuId)
    {
        return success(productSkuService.selectProductSkuById(skuId));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:edit')")
    @Log(title = "商品SKU", businessType = BusinessType.INSERT)
    @PostMapping("/sku")
    public AjaxResult addSku(@RequestBody ProductSku sku)
    {
        sku.setCreateBy(getUsername());
        return toAjax(productSkuService.insertProductSku(sku));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:edit')")
    @Log(title = "商品SKU", businessType = BusinessType.UPDATE)
    @PutMapping("/sku")
    public AjaxResult editSku(@RequestBody ProductSku sku)
    {
        sku.setUpdateBy(getUsername());
        return toAjax(productSkuService.updateProductSku(sku));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:edit')")
    @Log(title = "商品SKU", businessType = BusinessType.DELETE)
    @DeleteMapping("/sku/{skuIds}")
    public AjaxResult removeSku(@PathVariable Long[] skuIds)
    {
        return toAjax(productSkuService.deleteProductSkuByIds(skuIds));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:query')")
    @GetMapping("/availability")
    public AjaxResult availability(@RequestParam Long skuId,
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String channelCode,
            @RequestParam(required = false) String currencyCode)
    {
        return success(productSpuService.selectAvailability(skuId, countryCode, channelCode, currencyCode));
    }

    @PreAuthorize("@ss.hasPermi('commerce:admin:product:list')")
    @GetMapping("/skuOptions")
    public AjaxResult skuOptions(@RequestParam(required = false) Long spuId,
            @RequestParam(required = false) String keyword)
    {
        ProductSku filter = new ProductSku();
        filter.setSpuId(spuId);
        if (StringUtils.isNotEmpty(keyword))
        {
            filter.setSkuCode(keyword);
        }
        List<ProductSku> skus = productSkuService.selectProductSkuList(filter);
        List<Map<String, Object>> options = new ArrayList<>();
        for (ProductSku sku : skus)
        {
            Map<String, Object> option = new LinkedHashMap<>();
            option.put("skuId", sku.getSkuId());
            option.put("skuCode", sku.getSkuCode());
            option.put("spuName", sku.getSpuName());
            option.put("attributesJson", sku.getAttributesJson());
            options.add(option);
        }
        return success(options);
    }
}
