package com.company.algo.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * CPU 格式校验注解
 *
 * <p>格式：^[0-9]+m?$ （如 2000m、4）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = CpuFormatValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CpuFormat {

    String message() default "CPU 格式不正确，应为数字或数字+m（如 2000m、4）";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 是否允许为空
     */
    boolean nullable() default true;
}
