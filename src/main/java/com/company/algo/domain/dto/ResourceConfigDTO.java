package com.company.algo.domain.dto;

import com.company.algo.util.validation.CpuFormat;
import com.company.algo.util.validation.MemoryFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 资源配置 DTO（数据传输对象）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Data
public class ResourceConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 算法名称
     */
    @NotBlank(message = "算法名称不能为空")
    private String algorithmName;

    /**
     * 镜像ID
     */
    @NotNull(message = "镜像ID不能为空")
    private Long imageId;

    /**
     * 镜像版本
     */
    private String imageVersion;

    /**
     * 当前版本
     */
    private String currentVersion;

    /**
     * NAS ID
     */
    private String nasId;

    /**
     * CPU限制（如 2000m）
     */
    @CpuFormat(message = "CPU 格式不正确")
    private String cpuLimit;

    /**
     * 内存限制（如 4Gi）
     */
    @MemoryFormat(message = "内存格式不正确")
    private String memLimit;

    /**
     * CPU请求（如 1000m）
     */
    @CpuFormat(message = "CPU 格式不正确")
    private String cpuRequest;

    /**
     * 内存请求（如 2Gi）
     */
    @MemoryFormat(message = "内存格式不正确")
    private String memRequest;

    /**
     * 状态（ACTIVE/INACTIVE）
     */
    private String status;
}
