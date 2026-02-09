package com.company.algo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源配置实体类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@TableName("resource_config")
public class ResourceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 算法名称
     */
    private String algorithmName;

    /**
     * 镜像ID
     */
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
    private String cpuLimit;

    /**
     * 内存限制（如 4Gi）
     */
    private String memLimit;

    /**
     * CPU请求（如 1000m）
     */
    private String cpuRequest;

    /**
     * 内存请求（如 2Gi）
     */
    private String memRequest;

    /**
     * 状态（ACTIVE/INACTIVE）
     */
    private String status;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


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


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
