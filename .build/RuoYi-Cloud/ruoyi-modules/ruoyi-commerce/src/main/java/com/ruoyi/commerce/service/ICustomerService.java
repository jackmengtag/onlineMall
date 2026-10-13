package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.Customer;

/**
 * 客户服务
 *
 * @author ruoyi
 */
public interface ICustomerService
{
    Customer selectCustomerById(Long customerId);

    List<Customer> selectCustomerList(Customer customer);

    Customer selectByEmail(String email);

    int insertCustomer(Customer customer);

    int updateCustomer(Customer customer);

    int deleteCustomerByIds(Long[] customerIds);

    boolean checkCustomerNoUnique(Customer customer);
}
