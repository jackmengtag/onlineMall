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
import com.ruoyi.commerce.domain.Customer;
import com.ruoyi.commerce.service.ICustomerService;

/**
 * 客户控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController
{
    @Autowired
    private ICustomerService customerService;

    @RequiresPermissions("commerce:customer:list")
    @GetMapping("/list")
    public TableDataInfo list(Customer customer)
    {
        startPage();
        List<Customer> list = customerService.selectCustomerList(customer);
        return getDataTable(list);
    }

    @Log(title = "客户", businessType = BusinessType.EXPORT)
    @RequiresPermissions("commerce:customer:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Customer customer)
    {
        List<Customer> list = customerService.selectCustomerList(customer);
        ExcelUtil<Customer> util = new ExcelUtil<Customer>(Customer.class);
        util.exportExcel(response, list, "客户数据");
    }

    @RequiresPermissions("commerce:customer:query")
    @GetMapping(value = "/{customerId}")
    public AjaxResult getInfo(@PathVariable Long customerId)
    {
        return success(customerService.selectCustomerById(customerId));
    }

    @RequiresPermissions("commerce:customer:add")
    @Log(title = "客户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Customer customer)
    {
        if (!customerService.checkCustomerNoUnique(customer))
        {
            return error("新增客户失败，客户编号已存在");
        }
        return toAjax(customerService.insertCustomer(customer));
    }

    @RequiresPermissions("commerce:customer:edit")
    @Log(title = "客户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Customer customer)
    {
        if (!customerService.checkCustomerNoUnique(customer))
        {
            return error("修改客户失败，客户编号已存在");
        }
        return toAjax(customerService.updateCustomer(customer));
    }

    @RequiresPermissions("commerce:customer:remove")
    @Log(title = "客户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{customerIds}")
    public AjaxResult remove(@PathVariable Long[] customerIds)
    {
        return toAjax(customerService.deleteCustomerByIds(customerIds));
    }
}
