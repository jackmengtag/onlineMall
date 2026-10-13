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
import com.ruoyi.commerce.domain.CustomerAddress;
import com.ruoyi.commerce.service.ICustomerAddressService;

/**
 * 客户收货地址控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/customer/address")
public class CustomerAddressController extends BaseController
{
    @Autowired
    private ICustomerAddressService customerAddressService;

    @RequiresPermissions("commerce:address:list")
    @GetMapping("/list")
    public TableDataInfo list(CustomerAddress customerAddress)
    {
        startPage();
        List<CustomerAddress> list = customerAddressService.selectCustomerAddressList(customerAddress);
        return getDataTable(list);
    }

    @Log(title = "客户地址", businessType = BusinessType.EXPORT)
    @RequiresPermissions("commerce:address:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerAddress customerAddress)
    {
        List<CustomerAddress> list = customerAddressService.selectCustomerAddressList(customerAddress);
        ExcelUtil<CustomerAddress> util = new ExcelUtil<CustomerAddress>(CustomerAddress.class);
        util.exportExcel(response, list, "客户地址数据");
    }

    @RequiresPermissions("commerce:address:query")
    @GetMapping(value = "/{addressId}")
    public AjaxResult getInfo(@PathVariable Long addressId)
    {
        return success(customerAddressService.selectCustomerAddressById(addressId));
    }

    @RequiresPermissions("commerce:address:add")
    @Log(title = "客户地址", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CustomerAddress customerAddress)
    {
        return toAjax(customerAddressService.insertCustomerAddress(customerAddress));
    }

    @RequiresPermissions("commerce:address:edit")
    @Log(title = "客户地址", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CustomerAddress customerAddress)
    {
        return toAjax(customerAddressService.updateCustomerAddress(customerAddress));
    }

    @RequiresPermissions("commerce:address:remove")
    @Log(title = "客户地址", businessType = BusinessType.DELETE)
    @DeleteMapping("/{addressIds}")
    public AjaxResult remove(@PathVariable Long[] addressIds)
    {
        return toAjax(customerAddressService.deleteCustomerAddressByIds(addressIds));
    }
}
