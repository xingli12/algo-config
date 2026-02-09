package com.company.algo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.algo.domain.dto.ResourceConfigDTO;
import com.company.algo.domain.vo.ResourceConfigVO;

/**
 * 资源配置服务接口
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public interface ResourceConfigService {

    /**
     * 分页查询资源配置（按 updated_at 倒序）
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    IPage<ResourceConfigVO> queryPage(Integer page, Integer size);

    /**
     * 查询详情
     *
     * @param id 主键ID
     * @return 资源配置详情
     */
    ResourceConfigVO queryDetail(Long id);

    /**
     * 新增资源配置
     *
     * @param dto 资源配置DTO
     * @return 新增的主键ID
     */
    Long create(ResourceConfigDTO dto);

    /**
     * 编辑资源配置
     *
     * @param id  主键ID
     * @param dto 资源配置DTO
     */
    void update(Long id, ResourceConfigDTO dto);

    /**
     * 删除资源配置
     *
     * @param id 主键ID
     */
    void delete(Long id);

    /**
     * 触发资源配置下发/启动
     *
     * @param id 主键ID
     */
    void deploy(Long id);

    /**
     * 创建定时调度任务
     *
     * @param id          主键ID
     * @param scheduleCron 定时表达式
     */
    void schedule(Long id, String scheduleCron);

    /**
     * 回滚到历史版本
     *
     * @param id           主键ID
     * @param targetVersion 目标版本号
     */
    void rollback(Long id, String targetVersion);
}
