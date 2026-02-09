package com.company.algo.aspect;

import com.alibaba.fastjson.JSON;
import com.company.algo.domain.entity.OperationLog;
import com.company.algo.repository.OperationLogMapper;
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
@Aspect
@Component
public class AuditLogAspect {


    private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AuditLogAspect.class);
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
     * 使用 REQUIRED 传播级别，确保与业务方法在同一事务中
     */
    @Transactional(rollbackFor = Exception.class, propagation = org.springframework.transaction.annotation.Propagation.REQUIRED)
    public void saveOperationLog(String module, String operation, String bizId, String operator, String content) {
        OperationLog operationLog = new OperationLog();
        operationLog.setModule(module);
        operationLog.setOperation(operation);
        operationLog.setBizId(bizId);
        operationLog.setOperator(operator);
        operationLog.setContent(content);
        operationLog.setCreatedAt(LocalDateTime.now());

        try {
            operationLogMapper.insert(operationLog);
        } catch (Exception e) {
            log.error("保存操作日志失败: module=" + module + ", operation=" + operation + ", bizId=" + bizId);
            // 日志记录失败不影响业务
        }
    }
}
