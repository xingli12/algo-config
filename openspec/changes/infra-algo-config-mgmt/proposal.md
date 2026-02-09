# 提案：算法配置管理系统基础设施

## Why

AI SEM 系统当前缺乏统一的算法配置、资源分配和镜像配置管理能力，导致人工维护成本高、易出错、算法上线周期长且难以追溯。建立标准化的配置管理基础设施可以大幅提升算法运维效率和系统可维护性。

## What Changes

### 数据库层（Oracle 12c+）
- 创建 5 张核心业务表：
  - `algo_config` - 算法配置表（Recipe ID 唯一约束）
  - `image_config` - Docker 镜像配置表（镜像名称+标签唯一约束）
  - `image_algo_rel` - 镜像与算法关联表（外键级联删除）
  - `resource_config` - 资源配置表（CPU/内存限制与请求）
  - `operation_log` - 操作审计日志表（CLOB 字段存储 JSON 内容）
- 为每张表创建序列和触发器实现自增主键与自动时间戳

### Java 应用框架层
- **包结构设计**：controller/service/repository/domain 四层领域分层架构
- **统一响应体**：`ApiResponse<T>` 封装成功/失败响应（code + message + data）
- **全局异常处理**：`GlobalExceptionHandler` 统一捕获异常并转换为标准错误码
- **参数校验框架**：
  - JSR-380 注解校验（`@Valid`、`@NotNull` 等）
  - 自定义 JSON 字段校验器（使用 fastjson 校验 JSON 格式合法性）
  - 资源格式校验（CPU：`^[0-9]+m?$`，内存：`^[0-9]+(Mi|Gi)$`）
- **鉴权拦截器**：管理员角色校验（`@RequireAdmin` 注解）
- **操作日志 AOP**：自动记录所有增删改操作到 `operation_log` 表

### 错误处理体系
- 错误码枚举（A001~C001）
- 国际化支持（中英文双语，默认中文）
- `MessageSource` 配置（`classpath:i18n/messages_*.properties`）

## Capabilities

### New Capabilities
- `algo-config`: 算法配置管理能力（Recipe、算法文件、配置开关）
- `image-config`: Docker 镜像管理能力（镜像元数据、关联算法）
- `resource-config`: 资源分配管理能力（CPU/内存限制与请求、部署调度）
- `operation-audit`: 操作审计能力（日志记录、追溯查询）

### Modified Capabilities
- 无（全新项目，无现有能力修改）

## Impact

### 依赖组件
- **新增依赖**：
  - MyBatis-Plus（代码生成 + CRUD）
  - fastjson（JSON 校验与序列化）
  - Lombok（简化 Java 代码）
  - Nacos（服务发现 + 配置管理）
  - Spring Boot Starter Validation（JSR-380 校验）
  - Spring Boot AOP（操作日志拦截）

### 技术选型
- **应用框架**：Spring Boot 2.6.5
- **数据库**：Oracle 12c+（CLOB 存储 JSON 大字段）
- **文件存储**：CIFS 协议 NAS（数据库存储文件路径）
- **缓存**：Redis（可选，用于搜索结果缓存）

### 后续影响
- 为三个业务模块（算法配置、镜像管理、资源分配）提供统一的基础能力
- 所有 API 接口将遵循统一的响应格式和错误处理规范
- 所有操作均可追溯审计
