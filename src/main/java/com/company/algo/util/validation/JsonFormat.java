package com.company.algo.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * JSON 格式校验注解
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = JsonFormatValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonFormat {

    String message() default "JSON 格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 是否允许为空
     */
    boolean nullable() default true;
}
