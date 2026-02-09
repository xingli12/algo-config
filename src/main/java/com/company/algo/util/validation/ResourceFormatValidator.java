package com.company.algo.util.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 资源格式验证工具类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public class ResourceFormatValidator {

    private static final Pattern CPU_PATTERN = Pattern.compile("^([0-9]+)m?$");
    private static final Pattern MEMORY_PATTERN = Pattern.compile("^([0-9]+)(Mi|Gi)$");

    /**
     * 校验 CPU 格式是否合法
     */
    public static boolean isValidCpuValue(String cpuValue) {
        if (cpuValue == null || cpuValue.isEmpty()) {
            return false;
        }
        return CPU_PATTERN.matcher(cpuValue).matches();
    }

    /**
     * 校验内存格式是否合法
     */
    public static boolean isValidMemoryValue(String memValue) {
        if (memValue == null || memValue.isEmpty()) {
            return false;
        }
        return MEMORY_PATTERN.matcher(memValue).matches();
    }

    /**
     * 解析 CPU 值（转换为毫核数）
     * 例如：2000m -> 2000, 4 -> 4000
     */
    public static int parseCpuValue(String cpuValue) {
        if (!isValidCpuValue(cpuValue)) {
            throw new IllegalArgumentException("Invalid CPU value: " + cpuValue);
        }

        Matcher matcher = CPU_PATTERN.matcher(cpuValue);
        if (matcher.matches()) {
            int value = Integer.parseInt(matcher.group(1));
            // 如果有 m 后缀，直接返回；否则转换为毫核（1 core = 1000m）
            return cpuValue.endsWith("m") ? value : value * 1000;
        }

        throw new IllegalArgumentException("Invalid CPU value: " + cpuValue);
    }

    /**
     * 解析内存值（转换为 MB）
     * 例如：4Gi -> 4096, 512Mi -> 512
     */
    public static int parseMemoryValue(String memValue) {
        if (!isValidMemoryValue(memValue)) {
            throw new IllegalArgumentException("Invalid memory value: " + memValue);
        }

        Matcher matcher = MEMORY_PATTERN.matcher(memValue);
        if (matcher.matches()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            // 转换为 MB
            if ("Gi".equals(unit)) {
                return value * 1024;
            } else { // Mi
                return value;
            }
        }

        throw new IllegalArgumentException("Invalid memory value: " + memValue);
    }
}
