package com.ruoyi.commerce.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;

/**
 * 客户 customer
 *
 * @author ruoyi
 */
public class Customer implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 客户主键 */
    @Excel(name = "客户主键", cellType = ColumnType.NUMERIC)
    private Long customerId;

    /** 客户编号 */
    @Excel(name = "客户编号")
    private String customerNo;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 手机号 */
    private String phone;

    /** 显示名称 */
    @Excel(name = "显示名称")
    private String displayName;

    /** 国家二字码 */
    private String countryCode;

    /** 状态 ACTIVE/INACTIVE/BLOCKED */
    @Excel(name = "状态")
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public String getCustomerNo()
    {
        return customerNo;
    }

    public void setCustomerNo(String customerNo)
    {
        this.customerNo = customerNo;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
