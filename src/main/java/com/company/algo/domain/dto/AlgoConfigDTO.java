package com.company.algo.domain.dto;

import com.company.algo.util.validation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 算法配置 DTO（数据传输对象）
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
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

    public String getRecipeId() {
        return recipeId;
    }


    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }


    public String getEqpType() {
        return eqpType;
    }


    public void setEqpType(String eqpType) {
        this.eqpType = eqpType;
    }


    public String getEqpId() {
        return eqpId;
    }


    public void setEqpId(String eqpId) {
        this.eqpId = eqpId;
    }


    public String getProductId() {
        return productId;
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }


    public String getCaseName() {
        return caseName;
    }


    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }


    public String getPythonVersion() {
        return pythonVersion;
    }


    public void setPythonVersion(String pythonVersion) {
        this.pythonVersion = pythonVersion;
    }


    public String getAlgorithmName() {
        return algorithmName;
    }


    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }


    public String getAlgorithmVersion() {
        return algorithmVersion;
    }


    public void setAlgorithmVersion(String algorithmVersion) {
        this.algorithmVersion = algorithmVersion;
    }


    public String getAlgorithmDesc() {
        return algorithmDesc;
    }


    public void setAlgorithmDesc(String algorithmDesc) {
        this.algorithmDesc = algorithmDesc;
    }


    public String getAlgorithmConfigJson() {
        return algorithmConfigJson;
    }


    public void setAlgorithmConfigJson(String algorithmConfigJson) {
        this.algorithmConfigJson = algorithmConfigJson;
    }


    public Integer getAggregateEnabled() {
        return aggregateEnabled;
    }


    public void setAggregateEnabled(Integer aggregateEnabled) {
        this.aggregateEnabled = aggregateEnabled;
    }


    public String getDcConfigJson() {
        return dcConfigJson;
    }


    public void setDcConfigJson(String dcConfigJson) {
        this.dcConfigJson = dcConfigJson;
    }


    public Integer getSendKov() {
        return sendKov;
    }


    public void setSendKov(Integer sendKov) {
        this.sendKov = sendKov;
    }


    public Integer getSendLis() {
        return sendLis;
    }


    public void setSendLis(Integer sendLis) {
        this.sendLis = sendLis;
    }


    public Integer getSendLithops() {
        return sendLithops;
    }


    public void setSendLithops(Integer sendLithops) {
        this.sendLithops = sendLithops;
    }


    public String getExternalSystemJson() {
        return externalSystemJson;
    }


    public void setExternalSystemJson(String externalSystemJson) {
        this.externalSystemJson = externalSystemJson;
    }


    public Integer getEnabled() {
        return enabled;
    }


    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

}
