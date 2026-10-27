package com.ruoyi.commerce.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.commerce.domain.AuditLog;

/**
 * 审计日志 Mapper。
 *
 * @author ruoyi
 */
public interface AuditLogMapper
{
    int insert(AuditLog auditLog);

    List<AuditLog> selectList(@Param("shopId") Long shopId, @Param("module") String module,
            @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
