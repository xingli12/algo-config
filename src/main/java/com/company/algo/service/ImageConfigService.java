package com.company.algo.service;

import com.company.algo.domain.dto.ImageConfigDTO;
import com.company.algo.domain.vo.ImageConfigVO;

import java.util.List;

/**
 * 镜像配置服务接口
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public interface ImageConfigService {

    /**
     * 查询所有镜像配置（含关联算法）
     *
     * @return 镜像配置列表
     */
    List<ImageConfigVO> listAll();

    /**
     * 查询详情
     *
     * @param id 主键ID
     * @return 镜像配置详情
     */
    ImageConfigVO queryDetail(Long id);

    /**
     * 新增镜像配置
     *
     * @param dto 镜像配置DTO
     * @return 新增的主键ID
     */
    Long create(ImageConfigDTO dto);

    /**
     * 编辑镜像配置
     *
     * @param id  主键ID
     * @param dto 镜像配置DTO
     */
    void update(Long id, ImageConfigDTO dto);

    /**
     * 切换启用/禁用状态
     *
     * @param id     主键ID
     * @param status 状态（ENABLED/DISABLED）
     */
    void toggleStatus(Long id, String status);

    /**
     * 删除镜像配置（检查依赖，级联删除关联）
     *
     * @param id 主键ID
     */
    void delete(Long id);

    /**
     * 触发镜像部署
     *
     * @param id 主键ID
     */
    void deploy(Long id);
}
