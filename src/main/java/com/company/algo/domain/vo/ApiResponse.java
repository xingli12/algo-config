package com.company.algo.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应体
 *
 * @param <T> 数据类型
 * @author Algo Config Team
 * @since 1.0.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码："0" 表示成功，非 0 表示错误码
     */
    private String code;

    /**
     * 中文错误信息
     */
    private String message;

    /**
     * 英文错误信息
     */
    private String messageEn;

    /**
     * 业务数据
     */
    private T data;

    /**
     * 成功响应
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode("0");
        response.setMessage("ok");
        response.setMessageEn("ok");
        response.setData(data);
        return response;
    }

    /**
     * 失败响应
     */
    public static <T> ApiResponse<T> error(String code, String message, String messageEn) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setMessageEn(messageEn);
        return response;
    }

    /**
     * 失败响应（仅中文）
     */
    public static <T> ApiResponse<T> error(String code, String message) {
        return error(code, message, message);
    }
}
