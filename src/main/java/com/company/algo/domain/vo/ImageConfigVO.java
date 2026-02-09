package com.company.algo.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 镜像配置 VO（视图对象）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Data
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
}
