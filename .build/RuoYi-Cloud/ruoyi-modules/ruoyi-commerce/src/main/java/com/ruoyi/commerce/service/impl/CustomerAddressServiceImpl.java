package com.ruoyi.commerce.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.commerce.domain.CustomerAddress;
import com.ruoyi.commerce.mapper.CustomerAddressMapper;
import com.ruoyi.commerce.service.ICustomerAddressService;

/**
 * 客户收货地址服务实现
 *
 * @author ruoyi
 */
@Service
public class CustomerAddressServiceImpl implements ICustomerAddressService
{
    @Autowired
    private CustomerAddressMapper customerAddressMapper;

    @Override
    public CustomerAddress selectCustomerAddressById(Long addressId)
    {
        return customerAddressMapper.selectCustomerAddressById(addressId);
    }

    @Override
    public List<CustomerAddress> selectAddressByCustomerId(Long customerId)
    {
        return customerAddressMapper.selectAddressByCustomerId(customerId);
    }

    @Override
    public List<CustomerAddress> selectCustomerAddressList(CustomerAddress customerAddress)
    {
        return customerAddressMapper.selectCustomerAddressList(customerAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCustomerAddress(CustomerAddress customerAddress)
    {
        // 新增默认地址前，先清除该客户其它地址的默认标记
        if (Boolean.TRUE.equals(customerAddress.getIsDefault()) && customerAddress.getCustomerId() != null)
        {
            customerAddressMapper.clearDefaultByCustomerId(customerAddress.getCustomerId());
        }
        return customerAddressMapper.insertCustomerAddress(customerAddress);
    }

    @Override
    public int updateCustomerAddress(CustomerAddress customerAddress)
    {
        return customerAddressMapper.updateCustomerAddress(customerAddress);
    }

    @Override
    public int deleteCustomerAddressByIds(Long[] addressIds)
    {
        return customerAddressMapper.deleteCustomerAddressByIds(addressIds);
    }
}
