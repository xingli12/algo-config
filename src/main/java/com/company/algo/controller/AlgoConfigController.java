package com.company.algo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.company.algo.aspect.AuditLog;
import com.company.algo.config.security.RequireAdmin;
import com.company.algo.domain.dto.AlgoConfigDTO;
import com.company.algo.domain.enums.ModuleType;
import com.company.algo.domain.enums.OperationType;
import com.company.algo.domain.vo.ApiResponse;
import com.company.algo.domain.vo.AlgoConfigQueryVO;
import com.company.algo.domain.vo.AlgoConfigVO;
import com.company.algo.service.AlgoConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 算法配置控制器
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/algo-configs")
@RequireAdmin
@Validated
public class AlgoConfigController {

    @Resource
    private AlgoConfigService algoConfigService;

    /**
     * 分页查询算法配置（支持关键字搜索）
     *
     * @param queryVO 查询条件
     * @return 分页结果
     */
    @GetMapping
    public ApiResponse<IPage<AlgoConfigVO>> list(@Valid AlgoConfigQueryVO queryVO) {
        IPage<AlgoConfigVO> page = algoConfigService.queryPage(
                queryVO.getPage(),
                queryVO.getSize(),
                queryVO.getKeyword()
        );
        return ApiResponse.success(page);
    }

    /**
     * 查询算法配置详情（展开态）
     *
     * @param id 主键ID
     * @return 算法配置详情
     */
    @GetMapping("/{id}")
    public ApiResponse<AlgoConfigVO> detail(@PathVariable Long id) {
        AlgoConfigVO vo = algoConfigService.queryDetail(id);
        return ApiResponse.success(vo);
    }

    /**
     * 新增算法配置
     *
     * @param dto 算法配置DTO
     * @return 新增的主键ID
     */
    @PostMapping
    @AuditLog(module = "ALGO", operation = "CREATE")
    public ApiResponse<Long> create(@Valid @RequestBody AlgoConfigDTO dto) {
        Long id = algoConfigService.create(dto);
        return ApiResponse.success(id);
    }

    /**
     * 编辑算法配置
     *
     * @param id  主键ID
     * @param dto 算法配置DTO
     * @return 成功响应
     */
    @PutMapping("/{id}")
    @AuditLog(module = "ALGO", operation = "UPDATE")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody AlgoConfigDTO dto) {
        algoConfigService.update(id, dto);
        return ApiResponse.success();
    }

    /**
     * 切换算法配置启用/停用状态
     *
     * @param id      主键ID
     * @param enabled 启用状态（0停用1启用）
     * @return 成功响应
     */
    @PutMapping("/{id}/status")
    @AuditLog(module = "ALGO", operation = "#{@algoConfigController.getOperationType(#enabled)}")
    public ApiResponse<Void> toggleStatus(@PathVariable Long id, @RequestParam Integer enabled) {
        algoConfigService.toggleStatus(id, enabled);
        return ApiResponse.success();
    }

    /**
     * 删除算法配置
     *
     * @param id 主键ID
     * @return 成功响应
     */
    @DeleteMapping("/{id}")
    @AuditLog(module = "ALGO", operation = "DELETE")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        algoConfigService.delete(id);
        return ApiResponse.success();
    }

    /**
     * 上传算法文件（.py）
     *
     * @param id 主键ID
     * @return 文件路径
     */
    @PostMapping("/{id}/upload")
    @AuditLog(module = "ALGO", operation = "CREATE")
    public ApiResponse<String> uploadFile(@PathVariable Long id) {
        // TODO: 实现文件上传逻辑
        // 1. 接收 MultipartFile
        // 2. 验证文件格式（.py）
        // 3. 保存到 NAS
        // 4. 更新数据库路径

        String filePath = "/nas/algo-configs/" + id + "/" + System.currentTimeMillis() + ".py";
        algoConfigService.uploadFile(id, filePath);
        return ApiResponse.success(filePath);
    }

    /**
     * 根据启用状态获取操作类型（供 SpEL 表达式使用）
     */
    public String getOperationType(Integer enabled) {
        return (enabled != null && enabled == 1) ? "ENABLE" : "DISABLE";
    }
}
