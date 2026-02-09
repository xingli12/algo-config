package com.company.algo.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 内存格式校验注解
 *
 * <p>格式：^[0-9]+(Mi|Gi)$ （如 4Gi、512Mi）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = MemoryFormatValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MemoryFormat {

    String message() default "内存格式不正确，应为数字+Mi 或数字+Gi（如 4Gi、512Mi）";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 是否允许为空
     */
    boolean nullable() default true;
}
