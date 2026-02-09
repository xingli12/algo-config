package com.company.algo.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * CPU 格式校验器
 *
 * <p>格式：^[0-9]+m?$ （如 2000m、4）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public class CpuFormatValidator implements ConstraintValidator<CpuFormat, String> {

    private static final Pattern CPU_PATTERN = Pattern.compile("^[0-9]+m?$");

    private boolean nullable;

    @Override
    public void initialize(CpuFormat constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 空值校验
        if (value == null || value.trim().isEmpty()) {
            return nullable;
        }

        return CPU_PATTERN.matcher(value).matches();
    }
}
