package com.company.algo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.algo.domain.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
