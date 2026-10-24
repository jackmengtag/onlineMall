package com.ruoyi.commerce.service;

import java.util.List;

import com.ruoyi.commerce.domain.ChannelListing;

/**
 * 渠道商品映射服务。
 */
public interface IChannelListingService
{
    List<ChannelListing> selectChannelListingList(ChannelListing listing);

    ChannelListing selectChannelListingById(Long listingId);

    int insertChannelListing(ChannelListing listing);

    int updateChannelListing(ChannelListing listing);

    int deleteChannelListingByIds(Long[] listingIds);

    int syncListing(Long listingId);
}
