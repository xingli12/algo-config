package com.company.algo.aspect;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * <p>标记需要记录操作日志的方法
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {

    /**
     * 操作模块
     */
    String module();

    /**
     * 操作类型
     */
    String operation();
}
