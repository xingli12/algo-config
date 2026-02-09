package com.company.algo.util.validation;

import com.company.algo.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 自定义校验器测试
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@DisplayName("自定义校验器测试")
public class CustomValidatorTest extends BaseTest {

    @Test
    @DisplayName("应该验证合法的 JSON 格式")
    void shouldValidateValidJson() {
        JsonFormatValidator validator = new JsonFormatValidator();
        JsonFormat mockAnnotation = mock(JsonFormat.class);
        when(mockAnnotation.nullable()).thenReturn(true);
        validator.initialize(mockAnnotation);

        assertTrue(validator.isValid("{\"key\": \"value\"}", null));
        assertTrue(validator.isValid("null", null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("[]", null));
    }

    @Test
    @DisplayName("应该拒绝非法的 JSON 格式")
    void shouldRejectInvalidJson() {
        JsonFormatValidator validator = new JsonFormatValidator();
        JsonFormat mockAnnotation = mock(JsonFormat.class);
        when(mockAnnotation.nullable()).thenReturn(true);
        validator.initialize(mockAnnotation);

        assertFalse(validator.isValid("{key: value}", null)); // 缺少引号
        assertFalse(validator.isValid("{\"key\": }", null)); // 缺少值
    }

    @Test
    @DisplayName("应该验证合法的 CPU 格式")
    void shouldValidateValidCpuFormat() {
        CpuFormatValidator validator = new CpuFormatValidator();
        CpuFormat mockAnnotation = mock(CpuFormat.class);
        when(mockAnnotation.nullable()).thenReturn(true);
        validator.initialize(mockAnnotation);

        assertTrue(validator.isValid("2000m", null));
        assertTrue(validator.isValid("4", null));
        assertTrue(validator.isValid("100m", null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid(null, null));
    }

    @Test
    @DisplayName("应该拒绝非法的 CPU 格式")
    void shouldRejectInvalidCpuFormat() {
        CpuFormatValidator validator = new CpuFormatValidator();
        CpuFormat mockAnnotation = mock(CpuFormat.class);
        when(mockAnnotation.nullable()).thenReturn(false);
        validator.initialize(mockAnnotation);

        assertFalse(validator.isValid("abc", null));
        assertFalse(validator.isValid("2.5m", null));
        assertFalse(validator.isValid("2000mm", null));
    }

    @Test
    @DisplayName("应该验证合法的内存格式")
    void shouldValidateValidMemoryFormat() {
        MemoryFormatValidator validator = new MemoryFormatValidator();
        MemoryFormat mockAnnotation = mock(MemoryFormat.class);
        when(mockAnnotation.nullable()).thenReturn(true);
        validator.initialize(mockAnnotation);

        assertTrue(validator.isValid("4Gi", null));
        assertTrue(validator.isValid("512Mi", null));
        assertTrue(validator.isValid("1024Mi", null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid(null, null));
    }

    @Test
    @DisplayName("应该拒绝非法的内存格式")
    void shouldRejectInvalidMemoryFormat() {
        MemoryFormatValidator validator = new MemoryFormatValidator();
        MemoryFormat mockAnnotation = mock(MemoryFormat.class);
        when(mockAnnotation.nullable()).thenReturn(false);
        validator.initialize(mockAnnotation);

        assertFalse(validator.isValid("4", null)); // 缺少单位
        assertFalse(validator.isValid("512MB", null)); // MB 不是有效单位
        assertFalse(validator.isValid("4gi", null)); // 大小写
        assertFalse(validator.isValid("abc", null));
    }

    @Test
    @DisplayName("应该正确解析 CPU 值")
    void shouldParseCpuValue() {
        assertEquals(2000, ResourceFormatValidator.parseCpuValue("2000m"));
        assertEquals(4000, ResourceFormatValidator.parseCpuValue("4"));
        assertEquals(100, ResourceFormatValidator.parseCpuValue("100m"));
    }

    @Test
    @DisplayName("应该正确解析内存值")
    void shouldParseMemoryValue() {
        assertEquals(4096, ResourceFormatValidator.parseMemoryValue("4Gi"));
        assertEquals(512, ResourceFormatValidator.parseMemoryValue("512Mi"));
        assertEquals(1024, ResourceFormatValidator.parseMemoryValue("1Gi"));
    }
}
