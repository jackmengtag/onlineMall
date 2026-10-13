package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;

/**
 * 本地消息 local_message
 *
 * <p>对应文档第 10.1 节：业务事务与消息同库落库保证不丢消息，定时任务投递并重试；
 * status 取值 PENDING/SENT/FAILED。</p>
 *
 * @author ruoyi
 */
public class LocalMessage implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 消息主键 */
    private Long messageId;

    /** 消息编号 */
    @Excel(name = "消息编号")
    private String messageNo;

    /** 主题 */
    @Excel(name = "主题")
    private String topic;

    /** 业务键 */
    private String bizKey;

    /** 消息体(JSON) */
    private String payloadJson;

    /** 状态 PENDING/SENT/FAILED */
    @Excel(name = "状态")
    private String status;

    /** 重试次数 */
    @Excel(name = "重试次数", cellType = ColumnType.NUMERIC)
    private Integer retryCount;

    /** 下次重试时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextRetryAt;

    /** 投递成功时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sentAt;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getMessageId()
    {
        return messageId;
    }

    public void setMessageId(Long messageId)
    {
        this.messageId = messageId;
    }

    public String getMessageNo()
    {
        return messageNo;
    }

    public void setMessageNo(String messageNo)
    {
        this.messageNo = messageNo;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getBizKey()
    {
        return bizKey;
    }

    public void setBizKey(String bizKey)
    {
        this.bizKey = bizKey;
    }

    public String getPayloadJson()
    {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson)
    {
        this.payloadJson = payloadJson;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getRetryCount()
    {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount)
    {
        this.retryCount = retryCount;
    }

    public Date getNextRetryAt()
    {
        return nextRetryAt;
    }

    public void setNextRetryAt(Date nextRetryAt)
    {
        this.nextRetryAt = nextRetryAt;
    }

    public Date getSentAt()
    {
        return sentAt;
    }

    public void setSentAt(Date sentAt)
    {
        this.sentAt = sentAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
