package com.company.algo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.algo.domain.entity.ImageAlgoRel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 镜像算法关联 Mapper
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Mapper
public interface ImageAlgoRelMapper extends BaseMapper<ImageAlgoRel> {
}
