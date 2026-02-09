package com.company.algo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.company.algo.aspect.AuditLog;
import com.company.algo.config.security.RequireAdmin;
import com.company.algo.domain.dto.ResourceConfigDTO;
import com.company.algo.domain.vo.ApiResponse;
import com.company.algo.domain.vo.ResourceConfigVO;
import com.company.algo.service.ResourceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 资源配置控制器
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/resource-configs")
@RequireAdmin
@Validated
@Api(tags = "资源配置管理")
public class ResourceConfigController {

    @Resource
    private ResourceConfigService resourceConfigService;

    /**
     * 分页查询资源配置（按 updated_at 倒序）
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping
    @ApiOperation(value = "分页查询资源配置", notes = "按更新时间倒序返回资源配置列表")
    public ApiResponse<IPage<ResourceConfigVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<ResourceConfigVO> resultPage = resourceConfigService.queryPage(page, size);
        return ApiResponse.success(resultPage);
    }

    /**
     * 查询资源配置详情
     *
     * @param id 主键ID
     * @return 资源配置详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询资源配置详情", notes = "返回资源配置的完整信息")
    public ApiResponse<ResourceConfigVO> detail(@PathVariable Long id) {
        ResourceConfigVO vo = resourceConfigService.queryDetail(id);
        return ApiResponse.success(vo);
    }

    /**
     * 新增资源配置
     *
     * @param dto 资源配置DTO
     * @return 新增的主键ID
     */
    @PostMapping
    @AuditLog(module = "RESOURCE", operation = "CREATE")
    @ApiOperation(value = "新增资源配置", notes = "校验资源请求 ≤ 限制、每个算法仅一个 ACTIVE 配置")
    public ApiResponse<Long> create(@Valid @RequestBody ResourceConfigDTO dto) {
        Long id = resourceConfigService.create(dto);
        return ApiResponse.success(id);
    }

    /**
     * 编辑资源配置
     *
     * @param id  主键ID
     * @param dto 资源配置DTO
     * @return 成功响应
     */
    @PutMapping("/{id}")
    @AuditLog(module = "RESOURCE", operation = "UPDATE")
    @ApiOperation(value = "编辑资源配置", notes = "更新资源配置的参数和状态")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody ResourceConfigDTO dto) {
        resourceConfigService.update(id, dto);
        return ApiResponse.success();
    }

    /**
     * 删除资源配置
     *
     * @param id 主键ID
     * @return 成功响应
     */
    @DeleteMapping("/{id}")
    @AuditLog(module = "RESOURCE", operation = "DELETE")
    @ApiOperation(value = "删除资源配置", notes = "删除指定的资源配置")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        resourceConfigService.delete(id);
        return ApiResponse.success();
    }

    /**
     * 触发资源配置下发/启动
     *
     * @param id 主键ID
     * @return 成功响应
     */
    @PostMapping("/{id}/deploy")
    @AuditLog(module = "RESOURCE", operation = "DEPLOY")
    @ApiOperation(value = "触发资源配置下发", notes = "手动触发资源配置下发/启动操作")
    public ApiResponse<Void> deploy(@PathVariable Long id) {
        resourceConfigService.deploy(id);
        return ApiResponse.success();
    }

    /**
     * 创建定时调度任务
     *
     * @param id           主键ID
     * @param scheduleCron 定时表达式
     * @return 成功响应
     */
    @PostMapping("/{id}/schedule")
    @AuditLog(module = "RESOURCE", operation = "CREATE")
    @ApiOperation(value = "创建定时调度任务", notes = "为资源配置创建定时调度任务")
    public ApiResponse<Void> schedule(
            @PathVariable Long id,
            @RequestParam String scheduleCron) {
        resourceConfigService.schedule(id, scheduleCron);
        return ApiResponse.success();
    }

    /**
     * 回滚到历史版本
     *
     * @param id           主键ID
     * @param targetVersion 目标版本号
     * @return 成功响应
     */
    @PostMapping("/{id}/rollback")
    @AuditLog(module = "RESOURCE", operation = "ROLLBACK")
    @ApiOperation(value = "回滚到历史版本", notes = "将资源配置回滚到指定的历史版本")
    public ApiResponse<Void> rollback(
            @PathVariable Long id,
            @RequestParam String targetVersion) {
        resourceConfigService.rollback(id, targetVersion);
        return ApiResponse.success();
    }
}
