package com.ruoyi.commerce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.ChannelListing;

/**
 * 渠道商品映射数据层。
 *
 * @author ruoyi
 */
public interface ChannelListingMapper
{
    ChannelListing selectChannelListingById(Long listingId);

    ChannelListing selectChannelListingByUnique(@Param("channelCode") String channelCode,
            @Param("externalProductId") String externalProductId, @Param("externalSkuId") String externalSkuId);

    List<ChannelListing> selectChannelListingList(ChannelListing channelListing);

    int insertChannelListing(ChannelListing channelListing);

    int updateChannelListing(ChannelListing channelListing);

    int updateSync(@Param("listingId") Long listingId, @Param("listingStatus") String listingStatus,
            @Param("updateBy") String updateBy);

    int deleteChannelListingByIds(Long[] listingIds);
}
