package com.company.algo.domain.dto;

import com.company.algo.util.validation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 算法配置 DTO（数据传输对象）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Data
public class AlgoConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Recipe ID（唯一）
     */
    @NotBlank(message = "Recipe ID 不能为空")
    private String recipeId;

    /**
     * 设备类型
     */
    @NotBlank(message = "设备类型不能为空")
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
    @NotBlank(message = "算法名称不能为空")
    private String algorithmName;

    /**
     * 算法版本
     */
    @NotBlank(message = "算法版本不能为空")
    private String algorithmVersion;

    /**
     * 算法描述
     */
    private String algorithmDesc;

    /**
     * 算法配置JSON
     */
    @JsonFormat
    private String algorithmConfigJson;

    /**
     * 是否启用聚合（0否1是）
     */
    private Integer aggregateEnabled;

    /**
     * DC配置JSON
     */
    @JsonFormat(nullable = true)
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
    @JsonFormat
    private String externalSystemJson;

    /**
     * 启用状态（0停用1启用）
     */
    private Integer enabled;
}
