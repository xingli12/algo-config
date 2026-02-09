package com.company.algo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Data
@TableName("operation_log")
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模块（ALGO/IMAGE/RESOURCE）
     */
    private String module;

    /**
     * 操作类型（CREATE/UPDATE/DELETE/ENABLE/DISABLE/DEPLOY/ROLLBACK）
     */
    private String operation;

    /**
     * 业务ID
     */
    private String bizId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作内容（JSON格式）
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
