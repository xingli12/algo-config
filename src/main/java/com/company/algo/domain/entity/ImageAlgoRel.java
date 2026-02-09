package com.company.algo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 镜像与算法关联关系实体类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Data
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
}
