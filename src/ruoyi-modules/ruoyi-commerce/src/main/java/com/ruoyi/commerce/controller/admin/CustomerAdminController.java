package com.ruoyi.commerce.controller.admin;

import com.ruoyi.common.security.utils.SecurityUtils;

import com.ruoyi.common.security.annotation.RequiresPermissions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.commerce.domain.Customer;
import com.ruoyi.commerce.domain.CustomerAddress;
import com.ruoyi.commerce.service.ICustomerAddressService;
import com.ruoyi.commerce.service.ICustomerService;

/**
 * 运营后台-客户与收货地址。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/commerce/admin/customer")
public class CustomerAdminController extends BaseController
{
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerAddressService customerAddressService;

    @RequiresPermissions("commerce:admin:customer:list")
    @GetMapping("/list")
    public TableDataInfo list(Customer customer)
    {
        startPage();
        List<Customer> list = customerService.selectCustomerList(customer);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:admin:customer:query")
    @GetMapping("/{customerId}")
    public AjaxResult getInfo(@PathVariable Long customerId)
    {
        return success(customerService.selectCustomerById(customerId));
    }

    @RequiresPermissions("commerce:admin:customer:add")
    @Log(title = "客户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Customer customer)
    {
        customer.setCreateBy(SecurityUtils.getUsername());
        return toAjax(customerService.insertCustomer(customer));
    }

    @RequiresPermissions("commerce:admin:customer:edit")
    @Log(title = "客户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Customer customer)
    {
        customer.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(customerService.updateCustomer(customer));
    }

    @RequiresPermissions("commerce:admin:customer:remove")
    @Log(title = "客户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{customerIds}")
    public AjaxResult remove(@PathVariable Long[] customerIds)
    {
        return toAjax(customerService.deleteCustomerByIds(customerIds));
    }

    @RequiresPermissions("commerce:admin:customer:list")
    @GetMapping("/address/list")
    public TableDataInfo addressList(@RequestParam(required = false) Long customerId)
    {
        startPage();
        CustomerAddress address = new CustomerAddress();
        address.setCustomerId(customerId);
        List<CustomerAddress> list = customerAddressService.selectAddressList(address);
        return getDataTable(list);
    }

    @RequiresPermissions("commerce:admin:customer:query")
    @GetMapping("/address/{addressId}")
    public AjaxResult addressInfo(@PathVariable Long addressId)
    {
        return success(customerAddressService.selectAddressById(addressId));
    }

    @RequiresPermissions("commerce:admin:customer:add")
    @Log(title = "客户地址", businessType = BusinessType.INSERT)
    @PostMapping("/address")
    public AjaxResult addAddress(@RequestBody CustomerAddress address)
    {
        address.setCreateBy(SecurityUtils.getUsername());
        return toAjax(customerAddressService.insertAddress(address));
    }

    @RequiresPermissions("commerce:admin:customer:edit")
    @Log(title = "客户地址", businessType = BusinessType.UPDATE)
    @PutMapping("/address")
    public AjaxResult editAddress(@RequestBody CustomerAddress address)
    {
        address.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(customerAddressService.updateAddress(address));
    }

    @RequiresPermissions("commerce:admin:customer:remove")
    @Log(title = "客户地址", businessType = BusinessType.DELETE)
    @DeleteMapping("/address/{addressIds}")
    public AjaxResult removeAddress(@PathVariable Long[] addressIds)
    {
        return toAjax(customerAddressService.deleteAddressByIds(addressIds));
    }
}
