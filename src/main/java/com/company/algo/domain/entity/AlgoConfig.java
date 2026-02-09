package com.company.algo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 算法配置实体类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Data
@TableName("algo_config")
public class AlgoConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Recipe ID（唯一）
     */
    private String recipeId;

    /**
     * 设备类型
     */
    private String eqpType;

    /**
     * 设备ID
     */
    private String eqpId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * Case名称
     */
    private String caseName;

    /**
     * Python版本
     */
    private String pythonVersion;

    /**
     * 算法名称
     */
    private String algorithmName;

    /**
     * 算法版本
     */
    private String algorithmVersion;

    /**
     * 算法描述
     */
    private String algorithmDesc;

    /**
     * 算法文件路径
     */
    private String algorithmFilePath;

    /**
     * 算法配置JSON
     */
    private String algorithmConfigJson;

    /**
     * 是否启用聚合（0否1是）
     */
    private Integer aggregateEnabled;

    /**
     * DC配置JSON
     */
    private String dcConfigJson;

    /**
     * 是否发送KOV（0否1是）
     */
    private Integer sendKov;

    /**
     * 是否发送LIS（0否1是）
     */
    private Integer sendLis;

    /**
     * 是否发送Lithops（0否1是）
     */
    private Integer sendLithops;

    /**
     * 外部系统配置JSON
     */
    private String externalSystemJson;

    /**
     * 启用状态（0停用1启用）
     */
    private Integer enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;
}
