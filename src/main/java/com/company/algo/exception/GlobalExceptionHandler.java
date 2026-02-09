package com.company.algo.exception;

import com.company.algo.domain.enums.ErrorCode;
import com.company.algo.domain.vo.ApiResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.getLog(GlobalExceptionHandler.class);

    @Resource
    private MessageSource messageSource;

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code=" + e.getCode() + ", message=" + e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage(), e.getMessageEn());
    }

    /**
     * 处理参数校验异常（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        log.warn("参数校验异常: " + message);
        return ApiResponse.error(ErrorCode.C001.getCode(), message, "Parameter validation failed");
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBindException(BindException e) {
        FieldError fieldError = e.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        log.warn("参数绑定异常: " + message);
        return ApiResponse.error(ErrorCode.C001.getCode(), message, "Parameter binding failed");
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.warn("约束违反异常: " + message);
        return ApiResponse.error(ErrorCode.C001.getCode(), message, "Constraint violation");
    }

    /**
     * 处理未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception e) {
        log.error("系统异常", e);
        String message = messageSource.getMessage(ErrorCode.C003.getCode(), null,
                ErrorCode.C003.getMessage(), LocaleContextHolder.getLocale());
        String messageEn = ErrorCode.C003.getMessageEn();
        return ApiResponse.error(ErrorCode.C003.getCode(), message, messageEn);
    }
}
