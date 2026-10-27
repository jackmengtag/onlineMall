package com.ruoyi.commerce.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.commerce.constant.CommerceConstants;
import com.ruoyi.commerce.domain.LocalMessage;
import com.ruoyi.commerce.mapper.LocalMessageMapper;
import com.ruoyi.commerce.service.ILocalMessageService;

/**
 * 本地消息实现。
 *
 * @author ruoyi
 */
@Service
public class LocalMessageServiceImpl implements ILocalMessageService
{
    private static final Logger log = LoggerFactory.getLogger(LocalMessageServiceImpl.class);

    @Autowired
    private LocalMessageMapper localMessageMapper;

    @Override
    public void record(String topic, String eventType, String aggregateType, String aggregateId,
            String payloadJson, String messageKey)
    {
        LocalMessage message = new LocalMessage();
        message.setMessageKey(messageKey);
        message.setTopic(topic);
        message.setEventType(eventType);
        message.setAggregateType(aggregateType);
        message.setAggregateId(aggregateId);
        message.setPayloadJson(payloadJson);
        message.setMessageStatus(CommerceConstants.MESSAGE_PENDING);
        message.setRetryCount(0);
        message.setNextRetryAt(new Date());
        localMessageMapper.insertLocalMessage(message);
    }

    @Override
    public int dispatchPending()
    {
        List<LocalMessage> messages = localMessageMapper.selectPendingMessages();
        int count = 0;
        for (LocalMessage message : messages)
        {
            try
            {
                log.info("commerce event {}/{} {}", message.getTopic(), message.getEventType(), message.getAggregateId());
                localMessageMapper.markSent(message.getMessageId());
                count++;
            }
            catch (Exception e)
            {
                message.setNextRetryAt(new Date(System.currentTimeMillis() + 60000L));
                message.setLastError(e.getMessage());
                localMessageMapper.markFailed(message);
            }
        }
        return count;
    }
}
