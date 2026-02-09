package com.company.algo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
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

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getModule() {
        return module;
    }


    public void setModule(String module) {
        this.module = module;
    }


    public String getOperation() {
        return operation;
    }


    public void setOperation(String operation) {
        this.operation = operation;
    }


    public String getBizId() {
        return bizId;
    }


    public void setBizId(String bizId) {
        this.bizId = bizId;
    }


    public String getOperator() {
        return operator;
    }


    public void setOperator(String operator) {
        this.operator = operator;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
