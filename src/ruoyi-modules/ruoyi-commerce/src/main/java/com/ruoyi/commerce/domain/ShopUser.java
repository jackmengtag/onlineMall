package com.ruoyi.commerce.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 店铺成员 shop_user。
 *
 * @author ruoyi
 */
public class ShopUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 店铺 */
    private Long shopId;

    /** 若依用户 */
    private Long userId;

    /** 角色：OWNER/OPERATOR/CUSTOMER_SERVICE/FINANCE */
    private String shopRole;

    /** 状态：ACTIVE/INACTIVE */
    private String status;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getShopRole()
    {
        return shopRole;
    }

    public void setShopRole(String shopRole)
    {
        this.shopRole = shopRole;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shopId", getShopId())
            .append("userId", getUserId())
            .append("shopRole", getShopRole())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
