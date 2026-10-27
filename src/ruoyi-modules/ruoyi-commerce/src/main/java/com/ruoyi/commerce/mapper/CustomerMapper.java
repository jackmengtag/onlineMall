package com.ruoyi.commerce.mapper;

import java.util.List;

import com.ruoyi.commerce.domain.Customer;

/**
 * 客户数据层。
 *
 * @author ruoyi
 */
public interface CustomerMapper
{
    Customer selectCustomerById(Long customerId);

    Customer selectCustomerByNo(String customerNo);

    Customer selectByEmail(String email);

    List<Customer> selectCustomerList(Customer customer);

    int insertCustomer(Customer customer);

    int updateCustomer(Customer customer);

    int deleteCustomerByIds(Long[] customerIds);
}
