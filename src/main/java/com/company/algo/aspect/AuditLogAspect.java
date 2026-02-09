package com.company.algo.aspect;

import com.alibaba.fastjson.JSON;
import com.company.algo.domain.entity.OperationLog;
import com.company.algo.repository.OperationLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 操作日志 AOP 切面
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
public class AuditLogAspect {

    @Resource
    private OperationLogMapper operationLogMapper;

    /**
     * 定义切点：所有带 @AuditLog 注解的方法
     */
    @Pointcut("@annotation(com.company.algo.aspect.AuditLog)")
    public void auditLogPointcut() {
    }

    /**
     * 环绕通知：记录操作日志
     */
    @Around("auditLogPointcut() && @annotation(auditLog)")
    public Object around(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String operator = "system";
        String bizId = null;
        String content = null;

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            operator = request.getHeader("X-User-Id");
            if (operator == null || operator.isEmpty()) {
                operator = "anonymous";
            }
        }

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            try {
                content = JSON.toJSONString(args);
            } catch (Exception e) {
                log.warn("序列化操作参数失败", e);
                content = "{}";
            }
        }

        Object result = null;
        try {
            // 执行业务方法
            result = joinPoint.proceed();

            // 尝试从返回结果中提取业务ID
            if (result != null) {
                bizId = extractBizId(result);
            }

            return result;
        } finally {
            // 记录操作日志（与业务在同一事务）
            saveOperationLog(auditLog.module(), auditLog.operation(), bizId, operator, content);
        }
    }

    /**
     * 从返回结果中提取业务ID
     */
    private String extractBizId(Object result) {
        try {
            if (result instanceof Number) {
                return result.toString();
            }
            // TODO: 根据实际返回类型提取业务ID
            return null;
        } catch (Exception e) {
            log.warn("提取业务ID失败", e);
            return null;
        }
    }

    /**
     * 保存操作日志
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOperationLog(String module, String operation, String bizId, String operator, String content) {
        OperationLog log = new OperationLog();
        log.setModule(module);
        log.setOperation(operation);
        log.setBizId(bizId);
        log.setOperator(operator);
        log.setContent(content);
        log.setCreatedAt(LocalDateTime.now());

        try {
            operationLogMapper.insert(log);
        } catch (Exception e) {
            log.error("保存操作日志失败: module={}, operation={}, bizId={}", module, operation, bizId, e);
            // 日志记录失败不影响业务
        }
    }
}
