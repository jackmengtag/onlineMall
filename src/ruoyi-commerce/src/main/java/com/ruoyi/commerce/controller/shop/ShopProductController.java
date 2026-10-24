package com.ruoyi.commerce.controller.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.commerce.domain.ProductSku;
import com.ruoyi.commerce.domain.ProductSpu;
import com.ruoyi.commerce.service.IProductSkuService;
import com.ruoyi.commerce.service.IProductSpuService;
import com.ruoyi.commerce.service.IShopService;

/**
 * 店铺中心-商品（商品由平台统一维护，店铺仅可查询与上下架）。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/shop/product")
public class ShopProductController extends BaseController
{
    @Autowired
    private IProductSpuService productSpuService;

    @Autowired
    private IProductSkuService productSkuService;

    @Autowired
    private IShopService shopService;

    @PreAuthorize("@ss.hasPermi('commerce:shop:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductSpu spu)
    {
        Long shopId = shopService.currentShopId();
        if (shopId != null)
        {
            spu.setShopId(shopId);
        }
        startPage();
        List<ProductSpu> list = productSpuService.selectProductSpuList(spu);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('commerce:shop:product:query')")
    @GetMapping("/{spuId}")
    public AjaxResult getInfo(@PathVariable Long spuId)
    {
        ProductSpu spu = productSpuService.selectProductSpuById(spuId);
        if (spu == null)
        {
            return error("商品不存在");
        }
        Long shopId = shopService.currentShopId();
        if (shopId != null && !shopId.equals(spu.getShopId()))
        {
            return error("无权访问该商品");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("spu", spu);
        data.put("skus", productSkuService.selectSkuBySpuId(spuId));
        return success(data);
    }

    @PreAuthorize("@ss.hasPermi('commerce:shop:product:edit')")
    @Log(title = "店铺商品状态", businessType = BusinessType.UPDATE)
    @PostMapping("/status")
    public AjaxResult changeStatus(@RequestParam Long spuId, @RequestParam String status)
    {
        ProductSpu spu = productSpuService.selectProductSpuById(spuId);
        if (spu == null)
        {
            return error("商品不存在");
        }
        Long shopId = shopService.currentShopId();
        if (shopId != null && !shopId.equals(spu.getShopId()))
        {
            return error("无权操作该商品");
        }
        return toAjax(productSpuService.changeStatus(spuId, status));
    }

    @PreAuthorize("@ss.hasPermi('commerce:shop:product:list')")
    @GetMapping("/sku/list")
    public TableDataInfo skuList(@RequestParam Long spuId)
    {
        ProductSpu spu = productSpuService.selectProductSpuById(spuId);
        if (spu == null)
        {
            return getDataTable(new ArrayList<ProductSku>());
        }
        Long shopId = shopService.currentShopId();
        if (shopId != null && !shopId.equals(spu.getShopId()))
        {
            return getDataTable(new ArrayList<ProductSku>());
        }
        startPage();
        List<ProductSku> list = productSkuService.selectSkuBySpuId(spuId);
        return getDataTable(list);
    }
}
