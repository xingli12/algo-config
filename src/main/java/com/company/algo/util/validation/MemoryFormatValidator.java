package com.company.algo.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 内存格式校验器
 *
 * <p>格式：^[0-9]+(Mi|Gi)$ （如 4Gi、512Mi）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public class MemoryFormatValidator implements ConstraintValidator<MemoryFormat, String> {

    private static final Pattern MEMORY_PATTERN = Pattern.compile("^[0-9]+(Mi|Gi)$");

    private boolean nullable;

    @Override
    public void initialize(MemoryFormat constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 空值校验
        if (value == null || value.trim().isEmpty()) {
            return nullable;
        }

        return MEMORY_PATTERN.matcher(value).matches();
    }
}
