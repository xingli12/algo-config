package com.company.algo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.algo.domain.entity.ResourceConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源配置 Mapper
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Mapper
public interface ResourceConfigMapper extends BaseMapper<ResourceConfig> {
}
