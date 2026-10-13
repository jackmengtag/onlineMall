package com.ruoyi.commerce.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.commerce.domain.ChannelListing;

/**
 * 渠道商品映射数据层
 *
 * @author ruoyi
 */
public interface ChannelListingMapper
{
    /**
     * 查询渠道商品映射
     */
    ChannelListing selectChannelListingById(Long listingId);

    /**
     * 按唯一维度查询渠道商品映射
     */
    ChannelListing selectChannelListingByUnique(@Param("channelCode") String channelCode,
            @Param("externalProductId") String externalProductId, @Param("externalSkuId") String externalSkuId);

    /**
     * 按渠道与渠道商品查询映射列表
     */
    List<ChannelListing> selectByChannelAndExternalProduct(@Param("channelCode") String channelCode,
            @Param("externalProductId") String externalProductId);

    /**
     * 查询渠道商品映射列表
     */
    List<ChannelListing> selectChannelListingList(ChannelListing channelListing);

    /**
     * 新增渠道商品映射
     */
    int insertChannelListing(ChannelListing channelListing);

    /**
     * 修改渠道商品映射
     */
    int updateChannelListing(ChannelListing channelListing);

    /**
     * 删除渠道商品映射
     */
    int deleteChannelListingById(Long listingId);

    /**
     * 批量删除渠道商品映射
     */
    int deleteChannelListingByIds(Long[] listingIds);
}
