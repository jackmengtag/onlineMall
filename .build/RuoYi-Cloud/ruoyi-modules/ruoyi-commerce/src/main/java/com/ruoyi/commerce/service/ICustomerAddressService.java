package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.CustomerAddress;

/**
 * 客户收货地址服务
 *
 * @author ruoyi
 */
public interface ICustomerAddressService
{
    CustomerAddress selectCustomerAddressById(Long addressId);

    List<CustomerAddress> selectAddressByCustomerId(Long customerId);

    List<CustomerAddress> selectCustomerAddressList(CustomerAddress customerAddress);

    int insertCustomerAddress(CustomerAddress customerAddress);

    int updateCustomerAddress(CustomerAddress customerAddress);

    int deleteCustomerAddressByIds(Long[] addressIds);
}
