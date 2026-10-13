package com.ruoyi.commerce.mapper;

import java.util.List;
import com.ruoyi.commerce.domain.LocalMessage;

/**
 * 本地消息数据层
 *
 * @author ruoyi
 */
public interface LocalMessageMapper
{
    /**
     * 新增本地消息
     */
    int insertLocalMessage(LocalMessage localMessage);

    /**
     * 查询本地消息
     */
    LocalMessage selectLocalMessageById(Long messageId);

    /**
     * 查询待投递消息
     */
    List<LocalMessage> selectPendingMessages(int limit);

    /**
     * 查询消息列表
     */
    List<LocalMessage> selectLocalMessageList(LocalMessage localMessage);

    /**
     * 更新消息（状态、重试次数、时间）
     */
    int updateLocalMessage(LocalMessage localMessage);
}
