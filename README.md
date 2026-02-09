# 算法配置管理系统 - 项目脚手架

## 项目概述

本项目是基于 Spring Boot 2.6.5 + MyBatis-Plus + Oracle 的算法配置管理系统，提供统一的算法配置、资源分配和镜像配置管理能力。

## 技术栈

| 组件 | 技术 | 版本 |
|------|------|------|
| 应用框架 | Spring Boot | 2.6.5 |
| 服务发现 | Nacos | 2.1.0 |
| ORM 框架 | MyBatis-Plus | 3.5.2 |
| 数据库 | Oracle | 12c+ |
| JSON 处理 | fastjson | 1.2.83 |
| 工具库 | Lombok | - |
| 校验框架 | JSR-380 | - |

## 项目结构

```
algo-config/
├── src/main/java/com/company/algo/
│   ├── AlgoConfigApplication.java    # 主应用程序类
│   ├── aspect/                        # AOP 切面
│   │   ├── AuditLog.java              # 操作日志注解
│   │   └── AuditLogAspect.java        # 操作日志切面
│   ├── config/                        # 配置类
│   │   ├── I18nConfig.java            # 国际化配置
│   │   ├── WebMvcConfig.java          # Web MVC 配置
│   │   └── security/
│   │       ├── RequireAdmin.java      # 管理员鉴权注解
│   │       └── AdminAuthInterceptor.java  # 鉴权拦截器
│   ├── controller/                    # 控制器层（待实现）
│   ├── domain/                        # 领域层
│   │   ├── entity/                    # 实体类
│   │   │   ├── AlgoConfig.java        # 算法配置实体
│   │   │   └── OperationLog.java      # 操作日志实体
│   │   ├── dto/                       # 数据传输对象
│   │   │   └── AlgoConfigDTO.java     # 算法配置 DTO
│   │   ├── vo/                        # 视图对象
│   │   │   ├── ApiResponse.java       # 统一响应体
│   │   │   ├── AlgoConfigVO.java      # 算法配置 VO
│   │   │   └── AlgoConfigQueryVO.java # 查询条件 VO
│   │   └── enums/                     # 枚举类
│   │       ├── ErrorCode.java         # 错误码枚举
│   │       ├── ModuleType.java        # 模块类型枚举
│   │       └── OperationType.java     # 操作类型枚举
│   ├── exception/                     # 异常类
│   │   ├── BusinessException.java     # 业务异常
│   │   └── GlobalExceptionHandler.java  # 全局异常处理器
│   ├── repository/                    # 持久层
│   │   ├── AlgoConfigMapper.java      # 算法配置 Mapper
│   │   └── OperationLogMapper.java    # 操作日志 Mapper
│   ├── service/                       # 业务层
│   │   ├── AlgoConfigService.java     # 算法配置服务接口
│   │   └── impl/                      # 服务实现（待实现）
│   └── util/                          # 工具类
│       └── validation/                # 自定义校验器
│           ├── JsonFormat.java        # JSON 格式校验注解
│           ├── JsonFormatValidator.java
│           ├── CpuFormat.java         # CPU 格式校验注解
│           ├── CpuFormatValidator.java
│           ├── MemoryFormat.java      # 内存格式校验注解
│           └── MemoryFormatValidator.java
├── src/main/resources/
│   ├── application.yml                # 应用配置文件
│   ├── i18n/                          # 国际化资源
│   │   ├── messages_zh_CN.properties  # 中文错误信息
│   │   └── messages_en_US.properties  # 英文错误信息
│   └── mapper/                        # MyBatis XML
│       └── AlgoConfigMapper.xml       # 算法配置 Mapper XML
├── src/test/java/com/company/algo/    # 测试目录
├── pom.xml                            # Maven 配置
└── prd/sql/001_algo_config_schema_oracle.sql  # Oracle 建表脚本
```

## 已实现的功能

### ✅ 基础设施（100% 完成）

1. **项目结构**：标准 Maven 项目 + 四层架构包结构
2. **统一响应体**：`ApiResponse<T>` 封装所有接口响应
3. **全局异常处理**：`GlobalExceptionHandler` 统一处理异常
4. **错误码枚举**：完整的错误码定义（A/I/R/C 前缀）
5. **参数校验框架**：JSR-380 + 自定义校验器（JSON/CPU/内存格式）
6. **鉴权拦截器**：`@RequireAdmin` 注解 + 拦截器实现
7. **操作日志 AOP**：`@AuditLog` 注解 + AOP 切面
8. **国际化支持**：中英文双语错误信息

### ✅ 算法配置模块（50% 完成）

- **Domain 层**：实体类、DTO、VO 已创建
- **Repository 层**：Mapper 接口 + XML 已创建
- **Service 层**：服务接口已定义
- **Controller 层**：待实现

### ⏳ 镜像配置模块（0% 完成）

### ⏳ 资源配置模块（0% 完成）

## 快速开始

### 1. 数据库初始化

执行 Oracle 建表脚本：

```bash
sqlplus username/password@localhost:1521/XE @prd/sql/001_algo_config_schema_oracle.sql
```

### 2. 配置数据库

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:XE
    username: algo_config
    password: your_password
```

### 3. 配置 Nacos

确保 Nacos 服务已启动，或修改配置：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

### 4. 启动应用

```bash
mvn spring-boot:run
```

访问：`http://localhost:8080/api`

## 待完成任务

基于 `openspec/changes/infra-algo-config-mgmt/tasks.md`，剩余任务包括：

### 阶段 2：项目基础结构（已完成 2.1-2.8）
- ⏳ 2.9 配置 Nacos 连接
- ✅ 2.10 创建四层架构包结构

### 阶段 3：统一响应体与异常处理（已完成 3.1-3.7）
- ⏳ 3.8 编写单元测试

### 阶段 4：参数校验框架（已完成 4.1-4.6）
- ⏳ 4.7 编写单元测试

### 阶段 5：鉴权拦截器（已完成 5.1-5.3）
- ⏳ 5.4 编写单元测试

### 阶段 6：操作日志 AOP（已完成 6.1-6.7）
- ⏳ 6.8 确保操作日志与业务在同一数据库事务
- ⏳ 6.9 编写单元测试

### 阶段 7-11：业务模块实现（待开发）

详细任务列表请参考：`openspec/changes/infra-algo-config-mgmt/tasks.md`

## 开发指南

### 添加新的 Controller

```java
@RestController
@RequestMapping("/api/xxx-configs")
@RequireAdmin  // 需要管理员权限
public class XxxConfigController {

    @Resource
    private XxxConfigService xxxConfigService;

    @GetMapping
    public ApiResponse<Page<XxxConfigVO>> list(@Valid XxxConfigQueryVO query) {
        // 实现...
    }

    @PostMapping
    @AuditLog(module = "XXX", operation = "CREATE")  // 记录操作日志
    public ApiResponse<Long> create(@Valid @RequestBody XxxConfigDTO dto) {
        // 实现...
    }
}
```

### 使用自定义校验器

```java
@Data
public class ResourceConfigDTO {
    @CpuFormat(message = "CPU 格式不正确")
    private String cpuLimit;

    @MemoryFormat(message = "内存格式不正确")
    private String memLimit;

    @JsonFormat(message = "JSON 格式不正确")
    private String configJson;
}
```

### 抛出业务异常

```java
@Service
public class AlgoConfigServiceImpl implements AlgoConfigService {

    @Override
    public Long create(AlgoConfigDTO dto) {
        // 校验 Recipe ID 唯一性
        if (existsByRecipeId(dto.getRecipeId())) {
            throw new BusinessException(ErrorCode.A001);
        }
        // ...
    }
}
```

## 文档

- **PRD**：`prd/java-prd-algo-config.md`
- **开发任务拆分**：`prd/java-prd-dev-breakdown.md`
- **API 测试用例**：`prd/testcases/algo-config-testcases.md`
- **OpenSpec 工件**：`openspec/changes/infra-algo-config-mgmt/`

## License

Copyright © 2026 Algo Config Team. All rights reserved.
