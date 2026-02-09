# 实现任务清单

## 1. 数据库准备 (DB)

- [x] 1.1 创建 Oracle 建表脚本 `sql/001_algo_config_schema_oracle.sql`
- [x] 1.2 创建 `algo_config` 表（含主键、唯一索引、字段注释）
- [x] 1.3 创建 `image_config` 表（含唯一索引 `uk_image_name_tag`）
- [x] 1.4 创建 `image_algo_rel` 表（含外键约束，级联删除）
- [x] 1.5 创建 `resource_config` 表（含外键约束、唯一索引 `uk_algo_active`）
- [x] 1.6 创建 `operation_log` 表（content 字段使用 CLOB 类型）
- [x] 1.7 为每张表创建自增序列（`seq_algo_config` 等）
- [x] 1.8 为每张表创建自增主键触发器（`trg_*_bi`）
- [x] 1.9 为每张表创建自动更新时间戳触发器（`trg_*_bu`）
- [ ] 1.10 在 Oracle 测试环境执行建表脚本并验证

## 2. 项目基础结构

- [x] 2.1 创建 Maven/Gradle 项目模块结构
- [x] 2.2 配置 Spring Boot 2.6.5 依赖
- [x] 2.3 添加 MyBatis-Plus 依赖
- [x] 2.4 添加 Oracle JDBC 驱动
- [x] 2.5 添加 Lombok 依赖
- [x] 2.6 添加 fastjson 依赖
- [x] 2.7 添加 Spring Boot Validation 依赖
- [x] 2.8 添加 Spring Boot AOP 依赖
- [x] 2.9 配置 Nacos 连接（服务发现 + 配置中心）
- [x] 2.10 创建四层架构包结构（controller/service/repository/domain）

## 3. 统一响应体与异常处理

- [x] 3.1 创建 `ApiResponse<T>` 统一响应体类
- [x] 3.2 创建错误码枚举 `ErrorCode`（A001~C001）
- [x] 3.3 创建业务异常类 `BusinessException`
- [x] 3.4 创建全局异常处理器 `GlobalExceptionHandler`
- [x] 3.5 配置国际化 `MessageSource`（加载 `i18n/messages_*.properties`）
- [x] 3.6 创建中文错误信息配置文件 `i18n/messages_zh_CN.properties`
- [x] 3.7 创建英文错误信息配置文件 `i18n/messages_en_US.properties`
- [x] 3.8 编写单元测试验证异常处理和错误码

## 4. 参数校验框架

- [x] 4.1 创建 JSON 格式校验注解 `@JsonFormat`
- [x] 4.2 创建 JSON 格式校验器 `JsonFormatValidator`（使用 fastjson）
- [x] 4.3 创建 CPU 格式校验注解 `@CpuFormat`
- [x] 4.4 创建 CPU 格式校验器 `CpuFormatValidator`（正则 `^[0-9]+m?$`）
- [x] 4.5 创建内存格式校验注解 `@MemoryFormat`
- [x] 4.6 创建内存格式校验器 `MemoryFormatValidator`（正则 `^[0-9]+(Mi|Gi)$`）
- [x] 4.7 编写单元测试验证所有自定义校验器

## 5. 鉴权拦截器

- [x] 5.1 创建 `@RequireAdmin` 注解
- [x] 5.2 创建鉴权拦截器 `AdminAuthInterceptor`
- [x] 5.3 注册拦截器到 Web MVC 配置
- [x] 5.4 编写单元测试验证鉴权逻辑

## 6. 操作日志 AOP

- [x] 6.1 创建 `@AuditLog` 注解（标记需要记录的方法）
- [x] 6.2 创建操作日志枚举 `OperationType`（CREATE/UPDATE/DELETE/ENABLE/DISABLE/DEPLOY/ROLLBACK）
- [x] 6.3 创建模块枚举 `ModuleType`（ALGO/IMAGE/RESOURCE）
- [x] 6.4 创建 AOP 切面 `AuditLogAspect`
- [x] 6.5 实现操作日志提取逻辑（操作类型、业务 ID、变更内容）
- [x] 6.6 创建 `OperationLog` 实体类
- [x] 6.7 创建 `OperationLogMapper`（MyBatis-Plus）
- [x] 6.8 确保操作日志与业务在同一数据库事务
- [x] 6.9 编写单元测试验证 AOP 拦截和日志写入

## 7. 算法配置模块 (Algo Config)

### 7.1 Domain 层
- [x] 7.1.1 创建 `AlgoConfig` 实体类（对应 `algo_config` 表）
- [x] 7.1.2 创建 `AlgoConfigDTO`（数据传输对象）
- [x] 7.1.3 创建 `AlgoConfigVO`（视图对象，含展开态字段）
- [x] 7.1.4 创建 `AlgoConfigQueryVO`（查询条件对象）

### 7.2 Repository 层
- [x] 7.2.1 创建 `AlgoConfigMapper` 接口（继承 `BaseMapper<AlgoConfig>`）
- [x] 7.2.2 创建分页查询方法（支持关键字搜索）

### 7.3 Service 层
- [x] 7.3.1 创建 `AlgoConfigService` 接口
- [x] 7.3.2 实现分页查询方法（按 `updated_at` 倒序，支持关键字搜索）
- [x] 7.3.3 实现详情查询方法（展开态完整信息）
- [x] 7.3.4 实现新增方法（校验 Recipe ID 唯一性、JSON 格式、DC 配置必填）
- [x] 7.3.5 实现编辑方法
- [x] 7.3.6 实现状态切换方法（启用/停用）
- [x] 7.3.7 实现删除方法
- [x] 7.3.8 实现文件上传方法（保存到 NAS + 更新数据库路径）
- [x] 7.3.9 添加 `@AuditLog` 注解到所有变更方法
- [x] 7.3.10 编写单元测试验证所有业务逻辑和校验规则

### 7.4 Controller 层
- [x] 7.4.1 创建 `AlgoConfigController`
- [x] 7.4.2 实现 `GET /api/algo-configs` 分页查询接口
- [x] 7.4.3 实现 `GET /api/algo-configs/{id}` 详情查询接口
- [x] 7.4.4 实现 `POST /api/algo-configs` 新增接口
- [x] 7.4.5 实现 `PUT /api/algo-configs/{id}` 编辑接口
- [x] 7.4.6 实现 `PUT /api/algo-configs/{id}/status` 状态切换接口
- [x] 7.4.7 实现 `DELETE /api/algo-configs/{id}` 删除接口
- [x] 7.4.8 实现 `POST /api/algo-configs/{id}/upload` 文件上传接口
- [x] 7.4.9 添加 `@RequireAdmin` 注解到所有接口
- [x] 7.4.10 编写集成测试验证所有 API 接口

## 8. 镜像配置模块 (Image Config)

### 8.1 Domain 层
- [x] 8.1.1 创建 `ImageConfig` 实体类
- [x] 8.1.2 创建 `ImageAlgoRel` 实体类
- [x] 8.1.3 创建 `ImageConfigDTO`、`ImageConfigVO`

### 8.2 Repository 层
- [x] 8.2.1 创建 `ImageConfigMapper` 接口
- [x] 8.2.2 创建 `ImageAlgoRelMapper` 接口

### 8.3 Service 层
- [x] 8.3.1 创建 `ImageConfigService` 接口
- [x] 8.3.2 实现列表查询方法（含关联算法）
- [x] 8.3.3 实现新增方法（校验镜像名称+标签唯一性）
- [x] 8.3.4 实现编辑方法
- [x] 8.3.5 实现状态切换方法（启用/禁用）
- [x] 8.3.6 实现删除方法（检查 `resource_config` 依赖，级联删除 `image_algo_rel`）
- [x] 8.3.7 实现部署触发方法
- [x] 8.3.8 添加 `@AuditLog` 注解到所有变更方法
- [x] 8.3.9 编写单元测试验证所有业务逻辑和校验规则

### 8.4 Controller 层
- [x] 8.4.1 创建 `ImageConfigController`
- [x] 8.4.2 实现 `GET /api/image-configs` 列表查询接口
- [x] 8.4.3 实现 `POST /api/image-configs` 新增接口
- [x] 8.4.4 实现 `PUT /api/image-configs/{id}` 编辑接口
- [x] 8.4.5 实现 `PUT /api/image-configs/{id}/status` 状态切换接口
- [x] 8.4.6 实现 `DELETE /api/image-configs/{id}` 删除接口
- [x] 8.4.7 实现 `POST /api/image-configs/{id}/deploy` 部署触发接口
- [x] 8.4.8 添加 `@RequireAdmin` 注解到所有接口
- [x] 8.4.9 编写集成测试验证所有 API 接口

## 9. 资源配置模块 (Resource Config)

### 9.1 Domain 层
- [x] 9.1.1 创建 `ResourceConfig` 实体类
- [x] 9.1.2 创建 `ResourceConfigDTO`、`ResourceConfigVO`

### 9.2 Repository 层
- [x] 9.2.1 创建 `ResourceConfigMapper` 接口

### 9.3 Service 层
- [x] 9.3.1 创建 `ResourceConfigService` 接口
- [x] 9.3.2 实现列表查询方法（按 `updated_at` 倒序）
- [x] 9.3.3 实现新增方法（校验资源请求 ≤ 限制、每个算法仅一个 ACTIVE 配置）
- [x] 9.3.4 实现编辑方法
- [x] 9.3.5 实现删除方法
- [x] 9.3.6 实现部署触发方法
- [x] 9.3.7 实现定时任务创建方法
- [x] 9.3.8 实现回滚方法（回滚到历史版本）
- [x] 9.3.9 添加 `@AuditLog` 注解到所有变更方法
- [x] 9.3.10 编写单元测试验证所有业务逻辑和校验规则

### 9.4 Controller 层
- [x] 9.4.1 创建 `ResourceConfigController`
- [x] 9.4.2 实现 `GET /api/resource-configs` 列表查询接口
- [x] 9.4.3 实现 `POST /api/resource-configs` 新增接口
- [x] 9.4.4 实现 `PUT /api/resource-configs/{id}` 编辑接口
- [x] 9.4.5 实现 `DELETE /api/resource-configs/{id}` 删除接口
- [x] 9.4.6 实现 `POST /api/resource-configs/{id}/deploy` 部署触发接口
- [x] 9.4.7 实现 `POST /api/resource-configs/{id}/schedule` 定时任务接口
- [x] 9.4.8 实现 `POST /api/resource-configs/{id}/rollback` 回滚接口
- [x] 9.4.9 添加 `@RequireAdmin` 注解到所有接口
- [x] 9.4.10 编写集成测试验证所有 API 接口

## 10. 文档与部署

- [x] 10.1 配置 Swagger/OpenAPI 注解
- [x] 10.2 生成 OpenAPI 接口文档
- [x] 10.3 编写 API 接口文档（包含请求/响应示例）
- [x] 10.4 编写部署文档（数据库初始化、配置项说明）
- [x] 10.5 编写运维手册（常见问题、故障排查）
- [ ] 10.6 准备测试环境配置
- [ ] 10.7 执行数据库迁移脚本
- [ ] 10.8 部署应用到测试环境
- [ ] 10.9 执行集成测试（验证所有测试用例）
- [ ] 10.10 性能测试（验证查询响应时间 P95 < 500ms）

## 11. 验收与上线

- [ ] 11.1 产品验收（UAT）
- [ ] 11.2 修复验收发现的问题
- [ ] 11.3 准备生产环境配置
- [ ] 11.4 执行生产环境数据库迁移
- [ ] 11.5 部署应用到生产环境
- [ ] 11.6 生产环境冒烟测试
- [ ] 11.7 监控告警配置
- [ ] 11.8 项目总结文档
