package com.ruoyi.commerce.service;

/**
 * 本地消息（outbox）服务。
 *
 * @author ruoyi
 */
public interface ILocalMessageService
{
    void record(String topic, String eventType, String aggregateType, String aggregateId, String payloadJson, String messageKey);

    int dispatchPending();
}
