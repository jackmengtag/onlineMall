package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.CustomerAddress;

/**
 * 收货地址服务。
 */
public interface ICustomerAddressService
{
    List<CustomerAddress> selectAddressList(CustomerAddress address);

    CustomerAddress selectAddressById(Long addressId);

    int insertAddress(CustomerAddress address);

    int updateAddress(CustomerAddress address);

    int deleteAddressByIds(Long[] addressIds);
}
