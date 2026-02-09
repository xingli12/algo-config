package com.company.algo.exception;

import com.company.algo.domain.enums.ErrorCode;
import lombok.Getter;

/**
 * 业务异常类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final String code;

    /**
     * 中文错误信息
     */
    private final String message;

    /**
     * 英文错误信息
     */
    private final String messageEn;

    /**
     * 使用错误码枚举构造
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.messageEn = errorCode.getMessageEn();
    }

    /**
     * 使用错误码枚举 + 自定义消息构造
     */
    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.code = errorCode.getCode();
        this.message = customMessage;
        this.messageEn = errorCode.getMessageEn();
    }

    /**
     * 直接指定错误码和消息构造
     */
    public BusinessException(String code, String message, String messageEn) {
        super(message);
        this.code = code;
        this.message = message;
        this.messageEn = messageEn;
    }
}
