package com.company.algo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.algo.domain.entity.ImageConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 镜像配置 Mapper
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Mapper
public interface ImageConfigMapper extends BaseMapper<ImageConfig> {
}
