package com.ruoyi.commerce.mapper;

import java.util.List;

import com.ruoyi.commerce.domain.LocalMessage;

/**
 * 本地消息 Mapper。
 *
 * @author ruoyi
 */
public interface LocalMessageMapper
{
    int insertLocalMessage(LocalMessage message);

    List<LocalMessage> selectPendingMessages();

    int markSent(Long messageId);

    int markFailed(LocalMessage message);
}
