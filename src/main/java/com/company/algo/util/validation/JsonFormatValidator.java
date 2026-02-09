package com.company.algo.util.validation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * JSON 格式校验器
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public class JsonFormatValidator implements ConstraintValidator<JsonFormat, String> {

    private boolean nullable;

    @Override
    public void initialize(JsonFormat constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 空值校验
        if (value == null || value.trim().isEmpty()) {
            return nullable;
        }

        try {
            JSON.parse(value);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}
