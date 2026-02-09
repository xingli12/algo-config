package com.company.algo.controller;

import com.company.algo.domain.vo.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
@Api(tags = "系统管理")
public class HealthController {

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    @ApiOperation(value = "健康检查", notes = "返回服务运行状态信息")
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "algo-config-service");
        health.put("timestamp", LocalDateTime.now());
        health.put("version", "1.0.0");
        return ApiResponse.success(health);
    }
}
