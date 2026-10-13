package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.CustomerAddress;

/**
 * 客户收货地址数据层
 *
 * @author ruoyi
 */
public interface CustomerAddressMapper
{
    /**
     * 查询客户地址
     */
    CustomerAddress selectCustomerAddressById(Long addressId);

    /**
     * 按客户查询地址列表
     */
    List<CustomerAddress> selectAddressByCustomerId(Long customerId);

    /**
     * 查询客户地址列表
     */
    List<CustomerAddress> selectCustomerAddressList(CustomerAddress customerAddress);

    /**
     * 新增客户地址
     */
    int insertCustomerAddress(CustomerAddress customerAddress);

    /**
     * 修改客户地址
     */
    int updateCustomerAddress(CustomerAddress customerAddress);

    /**
     * 删除客户地址
     */
    int deleteCustomerAddressById(Long addressId);

    /**
     * 批量删除客户地址
     */
    int deleteCustomerAddressByIds(Long[] addressIds);

    /**
     * 清除指定客户下所有地址的默认标记
     */
    int clearDefaultByCustomerId(Long customerId);
}
