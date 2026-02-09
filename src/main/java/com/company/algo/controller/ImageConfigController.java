package com.company.algo.controller;

import com.company.algo.aspect.AuditLog;
import com.company.algo.config.security.RequireAdmin;
import com.company.algo.domain.dto.ImageConfigDTO;
import com.company.algo.domain.vo.ApiResponse;
import com.company.algo.domain.vo.ImageConfigVO;
import com.company.algo.service.ImageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 镜像配置控制器
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/image-configs")
@RequireAdmin
@Validated
public class ImageConfigController {

    @Resource
    private ImageConfigService imageConfigService;

    /**
     * 查询所有镜像配置（含关联算法）
     *
     * @return 镜像配置列表
     */
    @GetMapping
    public ApiResponse<List<ImageConfigVO>> list() {
        List<ImageConfigVO> list = imageConfigService.listAll();
        return ApiResponse.success(list);
    }

    /**
     * 查询镜像配置详情
     *
     * @param id 主键ID
     * @return 镜像配置详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ImageConfigVO> detail(@PathVariable Long id) {
        ImageConfigVO vo = imageConfigService.queryDetail(id);
        return ApiResponse.success(vo);
    }

    /**
     * 新增镜像配置
     *
     * @param dto 镜像配置DTO
     * @return 新增的主键ID
     */
    @PostMapping
    @AuditLog(module = "IMAGE", operation = "CREATE")
    public ApiResponse<Long> create(@Valid @RequestBody ImageConfigDTO dto) {
        Long id = imageConfigService.create(dto);
        return ApiResponse.success(id);
    }

    /**
     * 编辑镜像配置
     *
     * @param id  主键ID
     * @param dto 镜像配置DTO
     * @return 成功响应
     */
    @PutMapping("/{id}")
    @AuditLog(module = "IMAGE", operation = "UPDATE")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody ImageConfigDTO dto) {
        imageConfigService.update(id, dto);
        return ApiResponse.success();
    }

    /**
     * 切换镜像启用/禁用状态
     *
     * @param id     主键ID
     * @param status 状态（ENABLED/DISABLED）
     * @return 成功响应
     */
    @PutMapping("/{id}/status")
    @AuditLog(module = "IMAGE", operation = "#{@imageConfigController.getOperationType(#status)}")
    public ApiResponse<Void> toggleStatus(@PathVariable Long id, @RequestParam String status) {
        imageConfigService.toggleStatus(id, status);
        return ApiResponse.success();
    }

    /**
     * 删除镜像配置（检查依赖，级联删除关联）
     *
     * @param id 主键ID
     * @return 成功响应
     */
    @DeleteMapping("/{id}")
    @AuditLog(module = "IMAGE", operation = "DELETE")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        imageConfigService.delete(id);
        return ApiResponse.success();
    }

    /**
     * 触发镜像部署
     *
     * @param id 主键ID
     * @return 成功响应
     */
    @PostMapping("/{id}/deploy")
    @AuditLog(module = "IMAGE", operation = "DEPLOY")
    public ApiResponse<Void> deploy(@PathVariable Long id) {
        imageConfigService.deploy(id);
        return ApiResponse.success();
    }

    /**
     * 根据状态获取操作类型（供 SpEL 表达式使用）
     */
    public String getOperationType(String status) {
        return "ENABLED".equals(status) ? "ENABLE" : "DISABLE";
    }
}
