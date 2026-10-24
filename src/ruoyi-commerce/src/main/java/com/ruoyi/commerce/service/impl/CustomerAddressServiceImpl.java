package com.ruoyi.commerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.commerce.domain.CustomerAddress;
import com.ruoyi.commerce.mapper.CustomerAddressMapper;
import com.ruoyi.commerce.service.ICustomerAddressService;

/**
 * 收货地址服务实现。
 *
 * @author ruoyi
 */
@Service
public class CustomerAddressServiceImpl implements ICustomerAddressService
{
    @Autowired
    private CustomerAddressMapper customerAddressMapper;

    @Override
    public List<CustomerAddress> selectAddressList(CustomerAddress address)
    {
        return customerAddressMapper.selectCustomerAddressList(address);
    }

    @Override
    public CustomerAddress selectAddressById(Long addressId)
    {
        return customerAddressMapper.selectCustomerAddressById(addressId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAddress(CustomerAddress address)
    {
        if (address.getCustomerId() == null)
        {
            throw new ServiceException("客户不能为空");
        }
        if (Boolean.TRUE.equals(address.getIsDefault()))
        {
            customerAddressMapper.clearDefault(address.getCustomerId());
        }
        return customerAddressMapper.insertCustomerAddress(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAddress(CustomerAddress address)
    {
        if (address.getAddressId() == null)
        {
            throw new ServiceException("地址主键不能为空");
        }
        CustomerAddress existing = customerAddressMapper.selectCustomerAddressById(address.getAddressId());
        if (existing == null)
        {
            throw new ServiceException("地址不存在");
        }
        Long customerId = address.getCustomerId() != null ? address.getCustomerId() : existing.getCustomerId();
        address.setCustomerId(customerId);
        if (Boolean.TRUE.equals(address.getIsDefault()))
        {
            customerAddressMapper.clearDefault(customerId);
        }
        return customerAddressMapper.updateCustomerAddress(address);
    }

    @Override
    public int deleteAddressByIds(Long[] addressIds)
    {
        return customerAddressMapper.deleteCustomerAddressByIds(addressIds);
    }
}
