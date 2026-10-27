package com.ruoyi.commerce.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 审计与风控日志 audit_log。
 *
 * @author ruoyi
 */
public class AuditLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 审计主键 */
    private Long auditId;

    /** 店铺（平台级为空） */
    private Long shopId;

    /** 模块 */
    private String module;

    /** 动作 */
    private String action;

    /** 对象类型 */
    private String targetType;

    /** 对象 ID */
    private String targetId;

    /** 操作人 */
    private String operator;

    /** 详情 */
    private String detail;

    /** IP */
    private String ip;

    /** 结果：SUCCESS/FAIL */
    private String result;

    public Long getAuditId()
    {
        return auditId;
    }

    public void setAuditId(Long auditId)
    {
        this.auditId = auditId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public String getModule()
    {
        return module;
    }

    public void setModule(String module)
    {
        this.module = module;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getTargetType()
    {
        return targetType;
    }

    public void setTargetType(String targetType)
    {
        this.targetType = targetType;
    }

    public String getTargetId()
    {
        return targetId;
    }

    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("auditId", getAuditId())
            .append("shopId", getShopId())
            .append("module", getModule())
            .append("action", getAction())
            .append("targetType", getTargetType())
            .append("targetId", getTargetId())
            .append("operator", getOperator())
            .append("result", getResult())
            .append("createTime", getCreateTime())
            .toString();
    }
}
