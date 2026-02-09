package com.company.algo.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 算法配置 VO（视图对象）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Data
public class AlgoConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * Recipe ID
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
     * 是否启用聚合
     */
    private Integer aggregateEnabled;

    /**
     * DC配置JSON
     */
    private String dcConfigJson;

    /**
     * 是否发送KOV
     */
    private Integer sendKov;

    /**
     * 是否发送LIS
     */
    private Integer sendLis;

    /**
     * 是否发送Lithops
     */
    private Integer sendLithops;

    /**
     * 外部系统配置JSON
     */
    private String externalSystemJson;

    /**
     * 启用状态
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
