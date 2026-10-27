package com.ruoyi.commerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.Customer;
import com.ruoyi.commerce.mapper.CustomerMapper;
import com.ruoyi.commerce.service.ICustomerService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 客户服务实现。
 *
 * @author ruoyi
 */
@Service
public class CustomerServiceImpl implements ICustomerService
{
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> selectCustomerList(Customer customer)
    {
        return customerMapper.selectCustomerList(customer);
    }

    @Override
    public Customer selectCustomerById(Long customerId)
    {
        return customerMapper.selectCustomerById(customerId);
    }

    @Override
    public Customer selectCustomerByEmail(String email)
    {
        return customerMapper.selectByEmail(email);
    }

    @Override
    public int insertCustomer(Customer customer)
    {
        if (StringUtils.isEmpty(customer.getEmail()))
        {
            throw new ServiceException("邮箱不能为空");
        }
        if (customerMapper.selectByEmail(customer.getEmail()) != null)
        {
            throw new ServiceException("邮箱'" + customer.getEmail() + "'已被注册");
        }
        if (StringUtils.isEmpty(customer.getCustomerNo()))
        {
            customer.setCustomerNo(CommerceNoUtils.customerNo());
        }
        if (StringUtils.isEmpty(customer.getStatus()))
        {
            customer.setStatus("ACTIVE");
        }
        customer.setPassword(encodeIfNecessary(customer.getPassword()));
        return customerMapper.insertCustomer(customer);
    }

    @Override
    public int updateCustomer(Customer customer)
    {
        if (customer.getCustomerId() == null)
        {
            throw new ServiceException("客户主键不能为空");
        }
        if (!StringUtils.isEmpty(customer.getEmail()))
        {
            Customer exists = customerMapper.selectByEmail(customer.getEmail());
            if (exists != null && !exists.getCustomerId().equals(customer.getCustomerId()))
            {
                throw new ServiceException("邮箱'" + customer.getEmail() + "'已被注册");
            }
        }
        customer.setPassword(encodeIfNecessary(customer.getPassword()));
        return customerMapper.updateCustomer(customer);
    }

    @Override
    public int deleteCustomerByIds(Long[] customerIds)
    {
        return customerMapper.deleteCustomerByIds(customerIds);
    }

    @Override
    public Customer login(String email, String password)
    {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password))
        {
            throw new ServiceException("邮箱和密码不能为空");
        }
        Customer customer = customerMapper.selectByEmail(email);
        if (customer == null || StringUtils.isEmpty(customer.getPassword())
                || !SecurityUtils.matchesPassword(password, customer.getPassword()))
        {
            throw new ServiceException("邮箱或密码错误");
        }
        if (!"ACTIVE".equals(customer.getStatus()))
        {
            throw new ServiceException("账号已被禁用");
        }
        return customer;
    }

    /**
     * 明文密码按 BCrypt 加密；已加密的密码保持原样。
     */
    private String encodeIfNecessary(String password)
    {
        if (StringUtils.isEmpty(password) || password.startsWith("$2"))
        {
            return password;
        }
        return SecurityUtils.encryptPassword(password);
    }
}
