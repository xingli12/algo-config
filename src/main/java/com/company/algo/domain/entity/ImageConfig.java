package com.company.algo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 镜像配置实体类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@TableName("image_config")
public class ImageConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 镜像编码（如 img_001）
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
