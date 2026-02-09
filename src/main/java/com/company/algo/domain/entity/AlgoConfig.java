package com.company.algo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 算法配置实体类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
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

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


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


    public String getAlgorithmFilePath() {
        return algorithmFilePath;
    }


    public void setAlgorithmFilePath(String algorithmFilePath) {
        this.algorithmFilePath = algorithmFilePath;
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


    public String getCreatedBy() {
        return createdBy;
    }


    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    public String getUpdatedBy() {
        return updatedBy;
    }


    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

}
