package com.company.algo.domain.dto;

import lombok.Data;

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
@Data
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
}
