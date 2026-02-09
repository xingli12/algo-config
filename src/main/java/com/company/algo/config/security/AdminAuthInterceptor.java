package com.company.algo.config.security;

import com.company.algo.domain.enums.ErrorCode;
import com.company.algo.domain.vo.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员鉴权拦截器
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Slf4j
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 仅处理方法处理器
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 检查是否有 @RequireAdmin 注解
        RequireAdmin requireAdmin = handlerMethod.getMethodAnnotation(RequireAdmin.class);
        if (requireAdmin == null) {
            requireAdmin = handlerMethod.getBeanType().getAnnotation(RequireAdmin.class);
        }

        // 不需要管理员权限
        if (requireAdmin == null) {
            return true;
        }

        // TODO: 实现实际的用户鉴权逻辑
        // 这里简单示例：从请求头或 Session 中获取用户角色信息
        String userRole = request.getHeader("X-User-Role");

        if (!"admin".equalsIgnoreCase(userRole)) {
            log.warn("未授权访问: path={}, method={}", request.getRequestURI(), request.getMethod());

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            ApiResponse<Void> errorResponse = ApiResponse.error(
                    ErrorCode.C002.getCode(),
                    ErrorCode.C002.getMessage(),
                    ErrorCode.C002.getMessageEn()
            );
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return false;
        }

        return true;
    }
}
