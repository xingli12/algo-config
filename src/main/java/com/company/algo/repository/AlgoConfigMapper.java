package com.company.algo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.algo.domain.entity.AlgoConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 算法配置 Mapper
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Mapper
public interface AlgoConfigMapper extends BaseMapper<AlgoConfig> {

    /**
     * 分页查询（支持关键字搜索）
     *
     * @param page    分页对象
     * @param keyword 关键字（算法名称、Recipe ID、Product ID）
     * @return 分页结果
     */
    IPage<AlgoConfig> selectPageWithKeyword(Page<AlgoConfig> page, @Param("keyword") String keyword);
}
