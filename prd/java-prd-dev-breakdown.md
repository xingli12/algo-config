# 算法配置管理开发任务拆分（Java）

## 1. 交付文件
- PRD：`java-prd-algo-config.md`
- 开发任务拆分：`java-prd-dev-breakdown.md`
- 数据库脚本：`sql/001_algo_config_schema.sql`
- 测试用例：`testcases/algo-config-testcases.md`

## 2. 里程碑建议
1. M1（第 1 周）：数据模型 + 算法配置模块 CRUD + 搜索
2. M2（第 2 周）：资源分配模块 + 镜像管理模块 + 依赖校验
3. M3（第 3 周）：日志审计 + 集成联调 + 回归测试 + UAT

## 3. 后端任务（BE）
| 任务ID | 模块 | 任务描述 | 输出物 | 依赖 |
|---|---|---|---|---|
| BE-001 | 基础 | 建立模块包结构（controller/service/repository/dto/entity） | 可编译工程结构 | 无 |
| BE-002 | 基础 | 统一返回体、全局异常、错误码枚举 | `ApiResponse`、`GlobalExceptionHandler` | 无 |
| BE-003 | 基础 | 参数校验框架（JSR-380）与 JSON 字段合法性校验器 | 自定义注解与校验器 | BE-002 |
| BE-004 | 基础 | 鉴权拦截（管理员角色） | 鉴权中间件/注解 | 无 |
| BE-005 | 基础 | 操作日志 AOP 拦截器 | `operation_log` 写入能力 | DB-005 |
| BE-010 | 算法配置 | `GET /api/algo-configs` 分页查询+关键字搜索 | 查询接口 | DB-001 |
| BE-011 | 算法配置 | `POST /api/algo-configs` 新增（含业务校验） | 新增接口 | BE-003, DB-001 |
| BE-012 | 算法配置 | `PUT /api/algo-configs/{id}` 编辑 | 编辑接口 | BE-011 |
| BE-013 | 算法配置 | `PUT /api/algo-configs/{id}/status` 启停 | 状态切换接口 | BE-011 |
| BE-014 | 算法配置 | `DELETE /api/algo-configs/{id}` 删除 | 删除接口 | BE-011 |
| BE-015 | 算法配置 | `POST /api/algo-configs/{id}/upload` 上传 `.py` 文件并落库路径 | 上传接口 | BE-011 |
| BE-016 | 算法配置 | `GET /api/algo-configs/{id}` 展开详情（含创建/更新时间、配置开关摘要） | 详情接口 | BE-010, DB-001 |
| BE-020 | 资源分配 | `GET /api/resource-configs` 列表查询 | 查询接口 | DB-004 |
| BE-021 | 资源分配 | `POST /api/resource-configs` 新增（请求值<=限制值） | 新增接口 | BE-003, DB-004 |
| BE-022 | 资源分配 | `PUT /api/resource-configs/{id}` 编辑 | 编辑接口 | BE-021 |
| BE-023 | 资源分配 | `POST /api/resource-configs/{id}/deploy` 启动/下发 | 执行接口 | BE-021 |
| BE-024 | 资源分配 | `POST /api/resource-configs/{id}/schedule` 定时任务创建 | 调度接口 | BE-021 |
| BE-025 | 资源分配 | `POST /api/resource-configs/{id}/rollback` 回滚到历史版本 | 回滚接口 | BE-021 |
| BE-026 | 资源分配 | `DELETE /api/resource-configs/{id}` 删除 | 删除接口 | BE-021 |
| BE-030 | 镜像管理 | `GET /api/image-configs` 列表查询 | 查询接口 | DB-002 |
| BE-031 | 镜像管理 | `POST /api/image-configs` 新增（name+tag 唯一） | 新增接口 | BE-003, DB-002 |
| BE-032 | 镜像管理 | `PUT /api/image-configs/{id}` 编辑 | 编辑接口 | BE-031 |
| BE-033 | 镜像管理 | `PUT /api/image-configs/{id}/status` 启停 | 状态接口 | BE-031 |
| BE-034 | 镜像管理 | `POST /api/image-configs/{id}/deploy` 部署触发 | 部署接口 | BE-031 |
| BE-035 | 镜像管理 | `DELETE /api/image-configs/{id}` 删除前依赖检查 | 删除接口 | BE-031, DB-004 |
| BE-040 | 联动 | 关联算法维护（image_algo_rel） | 关联维护逻辑 | DB-003 |
| BE-041 | 联动 | 跨模块搜索统一关键字策略 | 搜索能力一致性 | BE-010, BE-020, BE-030 |
| BE-050 | 文档 | OpenAPI/Swagger 完整接口文档 | `openapi.yaml` 或 swagger 页面 | 全部接口完成 |

## 4. 数据库任务（DB）
| 任务ID | 任务描述 | 输出物 |
|---|---|---|
| DB-001 | 建表 `algo_config` + 索引 | `sql/001_algo_config_schema.sql` |
| DB-002 | 建表 `image_config` + 唯一索引 `uk_image_name_tag` | 同上 |
| DB-003 | 建表 `image_algo_rel` + 外键 | 同上 |
| DB-004 | 建表 `resource_config` + 外键与索引 | 同上 |
| DB-005 | 建表 `operation_log` | 同上 |
| DB-006 | 初始化字典数据（可选：python 版本、状态枚举） | `sql/002_seed_data.sql`（后续） |

## 5. 联调与测试任务（QA）
| 任务ID | 测试范围 | 输出物 |
|---|---|---|
| QA-001 | 算法配置 CRUD + 启停 + JSON 校验 | API 测试报告 |
| QA-002 | 资源分配请求/限制规则校验 | API 测试报告 |
| QA-003 | 镜像唯一性与依赖删除阻断 | API 测试报告 |
| QA-004 | 搜索、分页、排序一致性 | 集成测试报告 |
| QA-005 | 权限与鉴权（未授权访问） | 安全测试记录 |
| QA-006 | 操作日志完整性 | 审计测试记录 |
| QA-007 | 回归与 UAT 场景 | `testcases/algo-config-testcases.md` 执行结果 |

## 6. 开发顺序（建议）
1. DB-001~DB-005
2. BE-001~BE-005（基础设施）
3. BE-010~BE-015（算法配置）
4. BE-030~BE-035（镜像管理）
5. BE-020~BE-026（资源分配）
6. BE-040~BE-041（跨模块联动）
7. BE-050 + QA-001~QA-007

## 7. Definition of Done
- 所有接口完成并通过单元测试、集成测试
- 关键业务规则已覆盖测试用例
- SQL 脚本可在全新库一键执行
- 接口文档与错误码文档齐全
- UAT 场景全部通过

## 8. 风险与阻塞项
- 资源分配模块图标语义（deploy/schedule/rollback）需产品最终确认
- 外部系统 JSON 结构未完全定稿，可能引起字段变更
- 文件上传存储方案（本地/MinIO/S3）需运维确认
