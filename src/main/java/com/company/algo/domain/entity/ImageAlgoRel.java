package com.company.algo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 镜像与算法关联关系实体类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@TableName("image_algo_rel")
public class ImageAlgoRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 镜像ID
     */
    private Long imageId;

    /**
     * 算法名称
     */
    private String algorithmName;

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getImageId() {
        return imageId;
    }


    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }


    public String getAlgorithmName() {
        return algorithmName;
    }


    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

}
