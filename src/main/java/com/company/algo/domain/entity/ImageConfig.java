package com.company.algo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 镜像配置实体类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Data
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
}
