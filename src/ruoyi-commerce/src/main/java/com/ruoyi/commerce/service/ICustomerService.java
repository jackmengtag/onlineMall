package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.Customer;

/**
 * 客户服务。
 */
public interface ICustomerService
{
    List<Customer> selectCustomerList(Customer customer);

    Customer selectCustomerById(Long customerId);

    Customer selectCustomerByEmail(String email);

    int insertCustomer(Customer customer);

    int updateCustomer(Customer customer);

    int deleteCustomerByIds(Long[] customerIds);

    /**
     * 买家登录，成功返回客户，失败抛出 ServiceException。
     */
    Customer login(String email, String password);
}
