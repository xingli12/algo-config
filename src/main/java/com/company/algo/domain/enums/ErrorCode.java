package com.company.algo.domain.enums;

/**
 * 错误码枚举
 *
 * <p>编码规则：
 * <ul>
 *   <li>A001~A999：算法配置模块（Algo）</li>
 *   <li>I001~I999：镜像管理模块（Image）</li>
 *   <li>R001~R999：资源配置模块（Resource）</li>
 *   <li>C001~C999：通用错误（Common）</li>
 * </ul>
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public enum ErrorCode {

    // ========== 通用错误 (C001~C999) ==========
    C001("C001", "参数格式非法", "Invalid parameter format"),
    C002("C002", "未授权访问", "Unauthorized access"),
    C003("C003", "系统内部错误", "Internal system error"),

    // ========== 算法配置模块 (A001~A999) ==========
    A001("A001", "Recipe ID 已存在", "Recipe ID already exists"),
    A002("A002", "JSON 格式错误", "Invalid JSON format"),
    A003("A003", "DC 配置缺失", "DC configuration is required"),
    A004("A004", "算法配置不存在", "Algorithm configuration not found"),

    // ========== 镜像管理模块 (I001~I999) ==========
    I001("I001", "镜像名称+标签重复", "Image name and tag already exist"),
    I002("I002", "镜像存在依赖，无法删除", "Image has dependencies, cannot delete"),
    I003("I003", "镜像已被禁用，无法引用", "Image is disabled, cannot reference"),
    I004("I004", "镜像配置不存在", "Image configuration not found"),

    // ========== 资源配置模块 (R001~R999) ==========
    R001("R001", "资源请求超过限制", "Resource request exceeds limit"),
    R002("R002", "该算法已存在生效中的资源配置", "Active resource config already exists for this algorithm"),
    R003("R003", "资源配置不存在", "Resource configuration not found");

    private final String code;
    private final String message;
    private final String messageEn;

    ErrorCode(String code, String message, String messageEn) {
        this.code = code;
        this.message = message;
        this.messageEn = messageEn;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageEn() {
        return messageEn;
    }
}
