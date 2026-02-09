package com.company.algo.domain.enums;

/**
 * 操作类型枚举
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public enum OperationType {

    /**
     * 新增
     */
    CREATE("CREATE", "新增"),

    /**
     * 编辑
     */
    UPDATE("UPDATE", "编辑"),

    /**
     * 删除
     */
    DELETE("DELETE", "删除"),

    /**
     * 启用
     */
    ENABLE("ENABLE", "启用"),

    /**
     * 禁用
     */
    DISABLE("DISABLE", "禁用"),

    /**
     * 部署
     */
    DEPLOY("DEPLOY", "部署"),

    /**
     * 回滚
     */
    ROLLBACK("ROLLBACK", "回滚");

    private final String code;
    private final String description;

    OperationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
