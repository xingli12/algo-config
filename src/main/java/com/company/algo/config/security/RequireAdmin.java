package com.company.algo.config.security;

import java.lang.annotation.*;

/**
 * 需要管理员角色注解
 *
 * <p>标记需要管理员权限才能访问的接口
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAdmin {
}
