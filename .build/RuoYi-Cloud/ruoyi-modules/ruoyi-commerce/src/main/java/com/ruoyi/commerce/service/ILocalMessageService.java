package com.ruoyi.commerce.service;

import java.util.List;
import com.ruoyi.commerce.domain.LocalMessage;

/**
 * 本地消息表服务
 *
 * <p>对应文档第 10.1 节一致性机制：业务事务与消息同库落库，保证不丢消息；
 * 配合定时投递（重试 + 死信）实现跨领域最终一致。</p>
 *
 * @author ruoyi
 */
public interface ILocalMessageService
{
    /**
     * 发布消息（与业务事务同库落库）
     *
     * @param topic   主题
     * @param bizKey  业务键
     * @param payload 消息体（对象或 JSON 字符串）
     * @return 影响行数
     */
    int publish(String topic, String bizKey, Object payload);

    /**
     * 查询待投递消息
     *
     * @param limit 批量大小
     */
    List<LocalMessage> selectPendingMessages(int limit);

    /**
     * 标记投递成功
     */
    int markSent(Long messageId);

    /**
     * 标记投递失败（按退避策略设置下次重试时间）
     */
    int markFailed(Long messageId, String error);

    /**
     * 查询消息列表
     */
    List<LocalMessage> selectLocalMessageList(LocalMessage localMessage);
}
