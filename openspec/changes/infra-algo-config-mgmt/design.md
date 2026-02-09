# 算法配置管理系统基础设施 - 技术设计

## Context

### 当前状态
AI SEM 系统目前缺乏统一的算法配置、资源分配和镜像配置管理能力。所有配置通过手工维护，存在以下问题：
- 配置分散，缺乏统一管理界面
- 无法追溯配置变更历史
- 缺乏统一的校验和错误处理
- 手工操作易出错，效率低下

### 约束条件
- **数据库**：必须使用 Oracle 12c+（公司标准）
- **应用框架**：Spring Boot 2.6.5（与现有系统保持一致）
- **服务发现**：使用 Nacos 进行服务注册与配置管理
- **文件存储**：使用 CIFS 协议挂载 NAS 存储算法文件
- **向后兼容**：所有接口设计需考虑未来扩展性

### 利益相关者
- 后端开发团队：负责实现和维护
- 测试团队：负责功能和集成测试
- 运维团队：负责部署和监控
- 产品团队：负责需求验收

## Goals / Non-Goals

**Goals:**
1. 建立统一的算法配置、镜像管理、资源分配三大核心模块
2. 提供完整的 CRUD 接口，支持分页、搜索、排序
3. 实现全面的参数校验和业务规则校验
4. 建立操作审计机制，所有配置变更可追溯
5. 统一错误处理和国际化支持

**Non-Goals:**
1. 不实现镜像仓库的实际推拉功能（仅记录配置元数据）
2. 不实现复杂的权限系统（本期仅支持管理员角色）
3. 不实现算法文件的在线编辑功能
4. 不实现自动化的 CI/CD 流水线集成（部署接口仅记录操作）

## Decisions

### D1: 数据库选型 - Oracle vs MySQL
**决策：使用 Oracle 12c+**

**理由：**
- 公司已有 Oracle 运维经验和许可证
- DBA 团队更熟悉 Oracle 的管理和优化
- 现有其他系统已使用 Oracle，便于统一维护

**替代方案：**
- MySQL：更轻量，但与公司技术栈不一致，增加运维成本

### D2: JSON 字段存储方案
**决策：使用 CLOB 类型存储 JSON 字符串，应用层使用 fastjson 校验**

**理由：**
- Oracle 12c 不支持原生 JSON 类型（19c+ 才支持）
- CLOB 类型可存储大文本（最多 4GB）
- fastjson 是成熟的 JSON 库，性能良好
- 应用层校验便于统一错误处理和国际化

**替代方案：**
- 使用 BLOB：二进制存储不便调试和查询
- 拆分到独立表：增加表数量和复杂度，JSON 字段结构可能变化

### D3: 自增主键实现方案
**决策：使用序列 + 触发器实现自增主键**

**理由：**
- Oracle 不支持 MySQL 的 `AUTO_INCREMENT`
- 序列（Sequence）是 Oracle 推荐的自增 ID 方案
- 触发器自动赋值，对应用层透明

**实现示例：**
```sql
CREATE SEQUENCE seq_algo_config START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE TRIGGER trg_algo_config_bi
BEFORE INSERT ON algo_config
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_algo_config.NEXTVAL INTO :NEW.id FROM dual;
  END IF;
END;
```

### D4: 统一响应体设计
**决策：使用泛型类 `ApiResponse<T>` 封装所有接口响应**

**理由：**
- 前端统一处理响应格式
- 便于全局异常处理和错误码转换
- 支持泛型，类型安全

**响应格式：**
```java
public class ApiResponse<T> {
    private String code;      // "0" 表示成功，非 0 表示错误码
    private String message;   // 中文错误信息
    private String messageEn; // 英文错误信息
    private T data;           // 业务数据
}
```

### D5: 参数校验方案
**决策：JSR-380 注解 + 自定义校验器组合**

**理由：**
- JSR-380（`@Valid`、`@NotNull` 等）是 Java 标准
- Spring Boot Starter Validation 原生支持
- 自定义校验器处理复杂业务规则（JSON 格式、资源格式等）

**校验层级：**
1. **Controller 层**：JSR-380 注解校验基本参数
2. **Service 层**：自定义校验器处理业务规则
   - `@JsonFormat`：JSON 字段格式校验
   - `@ResourceFormat`：CPU/内存格式校验
   - 业务规则：唯一性、依赖关系等

### D6: 操作日志实现方案
**决策：使用 AOP 拦截器 + 注解驱动**

**理由：**
- AOP 统一拦截所有配置变更操作
- 注解（`@AuditLog`）标记需要记录的方法
- 自动提取操作类型、业务 ID、变更内容
- 与业务事务在同一数据库事务，保证原子性

**实现示意：**
```java
@Aspect
@Component
public class AuditLogAspect {
    @Around("@annotation(auditLog)")
    public Object logOperation(ProceedingJoinPoint joinPoint, AuditLog auditLog) {
        // 提取操作信息
        // 执行业务方法
        // 写入操作日志
        return result;
    }
}
```

### D7: 文件上传方案
**决策：数据库存储文件路径 + NAS 存储实际文件**

**理由：**
- 数据库仅存储路径字符串，便于备份和迁移
- NAS 挂载到应用服务器，通过文件系统访问
- 支持分布式部署（多个应用实例共享同一 NAS）

**文件路径格式：**
```
/nas/algo-configs/{recipe_id}/{timestamp}.py
```

### D8: 错误码设计
**决策：字母前缀 + 三位数字编号**

**编码规则：**
- `A001~A999`：算法配置模块（Algo）
- `I001~I999`：镜像管理模块（Image）
- `R001~R999`：资源配置模块（Resource）
- `C001~C999`：通用错误（Common）

**国际化处理：**
- 使用 Spring `MessageSource`
- 配置文件：`classpath:i18n/messages_zh_CN.properties`、`messages_en_US.properties`
- 默认返回中文，同时返回英文便于前端切换

### D9: 鉴权方案
**决策：基于注解的简单角色校验**

**理由：**
- 本期仅支持管理员角色，无需复杂权限系统
- 注解（`@RequireAdmin`）清晰标记需要鉴权的接口
- 可在未来扩展为更复杂的 RBAC 系统

**实现示意：**
```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAdmin {
}
```

### D10: 领域分层架构
**决策：四层架构（Controller → Service → Repository → Domain）**

**职责划分：**
- **Controller 层**：接收请求、参数校验、调用 Service
- **Service 层**：业务逻辑、事务管理、调用 Repository
- **Repository 层**：数据访问（MyBatis-Plus Mapper）
- **Domain 层**：实体类（Entity）、DTO、VO、枚举

**包结构：**
```
com.company.algo
├── controller/     # 接口层
├── service/        # 业务层
│   └── impl/
├── repository/     # 持久层（Mapper 接口）
├── domain/         # 领域层
│   ├── entity/     # 数据库实体
│   ├── dto/        # 数据传输对象
│   └── vo/         # 视图对象
├── config/         # 配置类
├── exception/      # 异常类
├── util/           # 工具类
└── aspect/         # AOP 切面
```

## Risks / Trade-offs

| 风险 | 缓解措施 |
|------|----------|
| **R1**: Oracle CLOB 字段查询性能可能不如 VARCHAR | 使用索引优化常用查询字段，避免对 CLOB 字段进行 `WHERE` 条件过滤 |
| **R2**: AOP 操作日志可能影响性能 | 异步写入日志（可选），或优化日志内容大小 |
| **R3**: 文件上传到 NAS 可能成为性能瓶颈 | 限制文件大小上限（如 10MB），使用 CDN 缓存静态文件 |
| **R4**: JSON 字段结构变化可能导致兼容性问题 | 使用 fastjson 的 `@JSONField(ordinal = N)` 控制字段顺序，向后兼容新增字段 |
| **R5**: 外键级联删除可能导致误删数据 | 删除前检查依赖关系，提供确认提示和回滚机制 |
| **R6**: 并发更新可能导致数据不一致 | 使用乐观锁（version 字段）或悲观锁（`SELECT FOR UPDATE`） |

## Migration Plan

### 部署步骤
1. **数据库准备**（DBA 执行）
   - 在 Oracle 创建数据库和用户
   - 执行建表脚本 `sql/001_algo_config_schema_oracle.sql`
   - 验证序列和触发器创建成功

2. **应用部署**（DevOps 执行）
   - 配置 Nacos 连接信息
   - 配置数据库连接池（HikariCP）
   - 配置 NAS 挂载路径
   - 启动 Spring Boot 应用
   - 验证健康检查接口

3. **验证测试**（QA 执行）
   - 执行测试用例 `testcases/algo-config-testcases.md`
   - 验证所有 CRUD 接口功能正常
   - 验证操作日志记录完整
   - 验证错误码和国际化信息正确

### 回滚策略
1. **数据库回滚**：保留表结构，仅 `TRUNCATE` 业务数据
2. **应用回滚**：使用蓝绿部署，保留旧版本应用快速切换
3. **配置回滚**：Nacos 配置中心支持版本回退

## Open Questions

1. **Q1**: NAS 存储的具体挂载路径和访问协议？
   - **待确认**：运维团队提供 NAS 配置详情

2. **Q2**: 资源分配模块的部署/调度/回滚操作是否需要对接真实的调度系统？
   - **待确认**：产品团队明确接口实现逻辑（仅记录操作 vs 实际调用）

3. **Q3**: 是否需要实现 Redis 缓存提升查询性能？
   - **建议**：先上线观察性能指标，再决定是否引入缓存

4. **Q4**: 操作日志保留周期是多少？
   - **待确认**：运维团队定义数据保留策略

5. **Q5**: 是否需要对接现有的统一鉴权系统（如 OAuth2/LDAP）？
   - **待确认**：安全团队提供鉴权集成方案
