package com.ruoyi.commerce.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.commerce.domain.LocalMessage;
import com.ruoyi.commerce.mapper.LocalMessageMapper;
import com.ruoyi.commerce.service.ILocalMessageService;
import com.ruoyi.commerce.util.CommerceNoUtils;

/**
 * 本地消息表服务实现
 *
 * <p>对应文档第 10.1 节：业务事务与消息同库落库，定时投递并指数退避重试，达到上限进入死信。</p>
 *
 * @author ruoyi
 */
@Service
public class LocalMessageServiceImpl implements ILocalMessageService
{
    /** 待投递 */
    private static final String STATUS_PENDING = "PENDING";

    /** 已投递 */
    private static final String STATUS_SENT = "SENT";

    /** 投递失败（死信） */
    private static final String STATUS_FAILED = "FAILED";

    /** 最大重试次数，达到后置为死信 */
    private static final int MAX_RETRY = 5;

    @Autowired
    private LocalMessageMapper localMessageMapper;

    @Override
    public int publish(String topic, String bizKey, Object payload)
    {
        LocalMessage message = new LocalMessage();
        message.setMessageNo(CommerceNoUtils.messageNo());
        message.setTopic(topic);
        message.setBizKey(bizKey);
        message.setPayloadJson(payload instanceof String ? (String) payload : JSON.toJSONString(payload));
        message.setStatus(STATUS_PENDING);
        message.setRetryCount(0);
        return localMessageMapper.insertLocalMessage(message);
    }

    @Override
    public List<LocalMessage> selectPendingMessages(int limit)
    {
        return localMessageMapper.selectPendingMessages(limit);
    }

    @Override
    public int markSent(Long messageId)
    {
        LocalMessage message = new LocalMessage();
        message.setMessageId(messageId);
        message.setStatus(STATUS_SENT);
        message.setSentAt(new Date());
        return localMessageMapper.updateLocalMessage(message);
    }

    @Override
    public int markFailed(Long messageId, String error)
    {
        LocalMessage current = localMessageMapper.selectLocalMessageById(messageId);
        if (current == null)
        {
            return 0;
        }
        int retryCount = (current.getRetryCount() == null ? 0 : current.getRetryCount()) + 1;
        LocalMessage message = new LocalMessage();
        message.setMessageId(messageId);
        message.setRetryCount(retryCount);
        if (retryCount >= MAX_RETRY)
        {
            message.setStatus(STATUS_FAILED);
        }
        else
        {
            message.setStatus(STATUS_PENDING);
            long backoffMinutes = (long) Math.pow(2, retryCount);
            message.setNextRetryAt(new Date(System.currentTimeMillis() + backoffMinutes * 60 * 1000L));
        }
        return localMessageMapper.updateLocalMessage(message);
    }

    @Override
    public List<LocalMessage> selectLocalMessageList(LocalMessage localMessage)
    {
        return localMessageMapper.selectLocalMessageList(localMessage);
    }
}
