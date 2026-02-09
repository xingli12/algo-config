package com.company.algo.domain.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 镜像配置 DTO（数据传输对象）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public class ImageConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 镜像编码（如 img_001）
     */
    private String imageCode;

    /**
     * 镜像名称
     */
    @NotBlank(message = "镜像名称不能为空")
    private String imageName;

    /**
     * 镜像标签
     */
    @NotBlank(message = "镜像标签不能为空")
    private String imageTag;

    /**
     * 仓库地址
     */
    @NotBlank(message = "仓库地址不能为空")
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
     * 关联的算法名称列表
     */
    private List<String> algorithmNames;

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


    public List<String> getAlgorithmNames() {
        return algorithmNames;
    }


    public void setAlgorithmNames(List<String> algorithmNames) {
        this.algorithmNames = algorithmNames;
    }

}
