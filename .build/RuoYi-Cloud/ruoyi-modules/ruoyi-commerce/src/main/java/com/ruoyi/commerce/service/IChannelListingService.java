package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.ChannelListing;

/**
 * 渠道商品映射服务
 *
 * @author ruoyi
 */
public interface IChannelListingService
{
    ChannelListing selectChannelListingById(Long listingId);

    List<ChannelListing> selectChannelListingList(ChannelListing channelListing);

    List<ChannelListing> selectByChannelAndExternalProduct(String channelCode, String externalProductId);

    int insertChannelListing(ChannelListing channelListing);

    int updateChannelListing(ChannelListing channelListing);

    int deleteChannelListingByIds(Long[] listingIds);

    boolean checkListingUnique(ChannelListing channelListing);
}
