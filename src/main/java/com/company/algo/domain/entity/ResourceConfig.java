package com.company.algo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源配置实体类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Data
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
}
