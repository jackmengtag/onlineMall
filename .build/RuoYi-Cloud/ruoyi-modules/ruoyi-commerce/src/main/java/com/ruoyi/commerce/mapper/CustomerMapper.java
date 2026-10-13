package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.Customer;

/**
 * 客户数据层
 *
 * @author ruoyi
 */
public interface CustomerMapper
{
    /**
     * 查询客户
     */
    Customer selectCustomerById(Long customerId);

    /**
     * 按编号查询客户
     */
    Customer selectCustomerByNo(String customerNo);

    /**
     * 按邮箱查询客户
     */
    Customer selectByEmail(String email);

    /**
     * 查询客户列表
     */
    List<Customer> selectCustomerList(Customer customer);

    /**
     * 新增客户
     */
    int insertCustomer(Customer customer);

    /**
     * 修改客户
     */
    int updateCustomer(Customer customer);

    /**
     * 删除客户
     */
    int deleteCustomerById(Long customerId);

    /**
     * 批量删除客户
     */
    int deleteCustomerByIds(Long[] customerIds);
}
