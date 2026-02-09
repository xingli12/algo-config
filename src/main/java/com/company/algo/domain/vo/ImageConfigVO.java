package com.company.algo.domain.vo;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 镜像配置 VO（视图对象）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public class ImageConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 镜像编码
     */
    private String imageCode;

    /**
     * 镜像名称
     */
    private String imageName;

    /**
     * 镜像标签
     */
    private String imageTag;

    /**
     * 仓库地址
     */
    private String registryUrl;

    /**
     * 镜像大小
     */
    private String imageSize;

    /**
     * 镜像描述
     */
    private String imageDesc;

    /**
     * 状态（ENABLED/DISABLED）
     */
    private String status;

    /**
     * 关联的算法名称列表
     */
    private List<String> algorithmNames;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

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


    public String getImageCode() {
        return imageCode;
    }


    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }


    public String getImageName() {
        return imageName;
    }


    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public String getImageTag() {
        return imageTag;
    }


    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }


    public String getRegistryUrl() {
        return registryUrl;
    }


    public void setRegistryUrl(String registryUrl) {
        this.registryUrl = registryUrl;
    }


    public String getImageSize() {
        return imageSize;
    }


    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }


    public String getImageDesc() {
        return imageDesc;
    }


    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public List<String> getAlgorithmNames() {
        return algorithmNames;
    }


    public void setAlgorithmNames(List<String> algorithmNames) {
        this.algorithmNames = algorithmNames;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
