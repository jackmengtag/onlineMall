package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.CustomerAddress;

/**
 * 收货地址数据层。
 *
 * @author ruoyi
 */
public interface CustomerAddressMapper
{
    CustomerAddress selectCustomerAddressById(Long addressId);

    List<CustomerAddress> selectAddressByCustomerId(Long customerId);

    List<CustomerAddress> selectCustomerAddressList(CustomerAddress customerAddress);

    int insertCustomerAddress(CustomerAddress customerAddress);

    int updateCustomerAddress(CustomerAddress customerAddress);

    int deleteCustomerAddressByIds(Long[] addressIds);

    /**
     * 清除指定客户下所有地址的默认标记。
     */
    int clearDefault(@Param("customerId") Long customerId);
}
