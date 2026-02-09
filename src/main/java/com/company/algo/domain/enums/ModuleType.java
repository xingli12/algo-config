package com.company.algo.domain.enums;

/**
 * 模块类型枚举
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public enum ModuleType {

    /**
     * 算法配置模块
     */
    ALGO("ALGO", "算法配置"),

    /**
     * 镜像配置模块
     */
    IMAGE("IMAGE", "镜像配置"),

    /**
     * 资源配置模块
     */
    RESOURCE("RESOURCE", "资源配置");

    private final String code;
    private final String description;

    ModuleType(String code, String description) {
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
