package com.company.algo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.algo.domain.dto.AlgoConfigDTO;
import com.company.algo.domain.entity.AlgoConfig;
import com.company.algo.domain.vo.AlgoConfigVO;

/**
 * 算法配置服务接口
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public interface AlgoConfigService {

    /**
     * 分页查询算法配置（支持关键字搜索）
     *
     * @param page    页码
     * @param size    每页大小
     * @param keyword 关键字
     * @return 分页结果
     */
    IPage<AlgoConfigVO> queryPage(Integer page, Integer size, String keyword);

    /**
     * 查询详情
     *
     * @param id 主键ID
     * @return 算法配置详情
     */
    AlgoConfigVO queryDetail(Long id);

    /**
     * 新增算法配置
     *
     * @param dto 算法配置DTO
     * @return 新增的主键ID
     */
    Long create(AlgoConfigDTO dto);

    /**
     * 编辑算法配置
     *
     * @param id  主键ID
     * @param dto 算法配置DTO
     */
    void update(Long id, AlgoConfigDTO dto);

    /**
     * 切换启用/停用状态
     *
     * @param id      主键ID
     * @param enabled 启用状态（0停用1启用）
     */
    void toggleStatus(Long id, Integer enabled);

    /**
     * 删除算法配置
     *
     * @param id 主键ID
     */
    void delete(Long id);

    /**
     * 上传算法文件
     *
     * @param id       主键ID
     * @param filePath 文件路径
     */
    void uploadFile(Long id, String filePath);
}
