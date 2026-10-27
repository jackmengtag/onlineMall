package com.ruoyi.commerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.ChannelListing;
import com.ruoyi.commerce.mapper.ChannelListingMapper;
import com.ruoyi.commerce.service.IChannelListingService;

/**
 * 渠道商品映射服务实现。
 *
 * @author ruoyi
 */
@Service
public class ChannelListingServiceImpl implements IChannelListingService
{
    private static final String STATUS_DRAFT = "DRAFT";

    private static final String STATUS_LIVE = "LIVE";

    private static final String STATUS_CLOSED = "CLOSED";

    @Autowired
    private ChannelListingMapper channelListingMapper;

    @Override
    public List<ChannelListing> selectChannelListingList(ChannelListing listing)
    {
        return channelListingMapper.selectChannelListingList(listing);
    }

    @Override
    public ChannelListing selectChannelListingById(Long listingId)
    {
        return channelListingMapper.selectChannelListingById(listingId);
    }

    @Override
    public int insertChannelListing(ChannelListing listing)
    {
        if (StringUtils.isEmpty(listing.getChannelCode()))
        {
            throw new ServiceException("渠道编码不能为空");
        }
        if (StringUtils.isEmpty(listing.getExternalProductId()))
        {
            throw new ServiceException("外部商品 ID 不能为空");
        }
        if (StringUtils.isEmpty(listing.getListingStatus()))
        {
            listing.setListingStatus(STATUS_DRAFT);
        }
        if (StringUtils.isEmpty(listing.getPayloadJson()))
        {
            listing.setPayloadJson("{}");
        }
        return channelListingMapper.insertChannelListing(listing);
    }

    @Override
    public int updateChannelListing(ChannelListing listing)
    {
        if (listing.getListingId() == null)
        {
            throw new ServiceException("渠道映射主键不能为空");
        }
        return channelListingMapper.updateChannelListing(listing);
    }

    @Override
    public int deleteChannelListingByIds(Long[] listingIds)
    {
        return channelListingMapper.deleteChannelListingByIds(listingIds);
    }

    @Override
    public int syncListing(Long listingId)
    {
        ChannelListing listing = channelListingMapper.selectChannelListingById(listingId);
        if (listing == null)
        {
            throw new ServiceException("渠道映射不存在");
        }
        if (listing.getSkuId() == null)
        {
            throw new ServiceException("渠道映射未绑定 SKU，无法同步");
        }
        if (STATUS_CLOSED.equals(listing.getListingStatus()))
        {
            throw new ServiceException("已关闭的渠道映射不能同步");
        }
        return channelListingMapper.updateSync(listingId, STATUS_LIVE, SecurityUtils.getUsername());
    }
}
