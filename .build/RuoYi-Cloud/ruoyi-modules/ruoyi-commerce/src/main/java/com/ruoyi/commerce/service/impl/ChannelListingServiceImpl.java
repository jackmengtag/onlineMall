package com.ruoyi.commerce.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.commerce.domain.ChannelListing;
import com.ruoyi.commerce.mapper.ChannelListingMapper;
import com.ruoyi.commerce.service.IChannelListingService;

/**
 * 渠道商品映射服务实现
 *
 * @author ruoyi
 */
@Service
public class ChannelListingServiceImpl implements IChannelListingService
{
    @Autowired
    private ChannelListingMapper channelListingMapper;

    @Override
    public ChannelListing selectChannelListingById(Long listingId)
    {
        return channelListingMapper.selectChannelListingById(listingId);
    }

    @Override
    public List<ChannelListing> selectChannelListingList(ChannelListing channelListing)
    {
        return channelListingMapper.selectChannelListingList(channelListing);
    }

    @Override
    public List<ChannelListing> selectByChannelAndExternalProduct(String channelCode, String externalProductId)
    {
        return channelListingMapper.selectByChannelAndExternalProduct(channelCode, externalProductId);
    }

    @Override
    public int insertChannelListing(ChannelListing channelListing)
    {
        if (StringUtils.isEmpty(channelListing.getPayloadJson()))
        {
            channelListing.setPayloadJson("{}");
        }
        return channelListingMapper.insertChannelListing(channelListing);
    }

    @Override
    public int updateChannelListing(ChannelListing channelListing)
    {
        return channelListingMapper.updateChannelListing(channelListing);
    }

    @Override
    public int deleteChannelListingByIds(Long[] listingIds)
    {
        return channelListingMapper.deleteChannelListingByIds(listingIds);
    }

    @Override
    public boolean checkListingUnique(ChannelListing channelListing)
    {
        Long listingId = channelListing.getListingId() == null ? -1L : channelListing.getListingId();
        ChannelListing info = channelListingMapper.selectChannelListingByUnique(channelListing.getChannelCode(),
                channelListing.getExternalProductId(), channelListing.getExternalSkuId());
        return info == null || info.getListingId().longValue() == listingId.longValue();
    }
}
