package com.ruoyi.commerce.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.Customer;
import com.ruoyi.commerce.mapper.CustomerMapper;
import com.ruoyi.commerce.service.ICustomerService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 客户服务实现
 *
 * @author ruoyi
 */
@Service
public class CustomerServiceImpl implements ICustomerService
{
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Customer selectCustomerById(Long customerId)
    {
        return customerMapper.selectCustomerById(customerId);
    }

    @Override
    public List<Customer> selectCustomerList(Customer customer)
    {
        return customerMapper.selectCustomerList(customer);
    }

    @Override
    public Customer selectByEmail(String email)
    {
        return customerMapper.selectByEmail(email);
    }

    @Override
    public int insertCustomer(Customer customer)
    {
        if (StringUtils.isEmpty(customer.getCustomerNo()))
        {
            customer.setCustomerNo(CommerceNoUtils.customerNo());
        }
        if (StringUtils.isEmpty(customer.getStatus()))
        {
            customer.setStatus("ACTIVE");
        }
        return customerMapper.insertCustomer(customer);
    }

    @Override
    public int updateCustomer(Customer customer)
    {
        return customerMapper.updateCustomer(customer);
    }

    @Override
    public int deleteCustomerByIds(Long[] customerIds)
    {
        return customerMapper.deleteCustomerByIds(customerIds);
    }

    @Override
    public boolean checkCustomerNoUnique(Customer customer)
    {
        Long customerId = customer.getCustomerId() == null ? -1L : customer.getCustomerId();
        Customer info = customerMapper.selectCustomerByNo(customer.getCustomerNo());
        return info == null || info.getCustomerId().longValue() == customerId.longValue();
    }
}
