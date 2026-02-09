package com.company.algo.domain.dto;

import com.company.algo.util.validation.CpuFormat;
import com.company.algo.util.validation.MemoryFormat;

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

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(String imageVersion) {
        this.imageVersion = imageVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getNasId() {
        return nasId;
    }

    public void setNasId(String nasId) {
        this.nasId = nasId;
    }

    public String getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(String cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public String getMemLimit() {
        return memLimit;
    }

    public void setMemLimit(String memLimit) {
        this.memLimit = memLimit;
    }

    public String getCpuRequest() {
        return cpuRequest;
    }

    public void setCpuRequest(String cpuRequest) {
        this.cpuRequest = cpuRequest;
    }

    public String getMemRequest() {
        return memRequest;
    }

    public void setMemRequest(String memRequest) {
        this.memRequest = memRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
