# 算法配置管理系统 PRD（Java 开发版）

## 1. 文档信息
- 文档名称：算法配置管理系统 PRD
- 面向角色：产品经理、Java 后端开发、前端开发、测试
- 原型来源：`figma/` 目录下 9 张页面原型图
- 版本：v1.0
- 日期：2026-02-09

## 2. 项目背景与目标
AI SEM 系统需要统一管理算法配置、运行资源和镜像配置，减少人工维护错误，支持算法快速上线、版本迭代和可追溯管理。

本期目标：
- 提供三大模块：`算法配置`、`资源分配`、`镜像管理`
- 支持新增、编辑、删除、启停、搜索
- 支持结构化 JSON 配置和外部系统集成参数
- 支持配置项可审计、可回溯

## 3. 范围定义
### 3.1 In Scope
- 算法配置管理（Recipe 与算法绑定）
- 算法资源分配管理（CPU/内存限制与请求）
- Docker 镜像管理（镜像元数据、关联算法、启停）
- 统一搜索（算法名称/Recipe ID/Product ID）

### 3.2 Out of Scope
- 算法文件内容在线编辑
- 镜像仓库实际推拉（仅记录配置，后续可扩展）
- 复杂权限系统（本期按管理员角色实现）

## 4. 用户角色
- 管理员（Admin）：拥有全部配置管理权限

## 5. 页面与信息架构
- 顶部：标题 + 搜索框
- 中部：Tab 切换
  - `算法配置`
  - `资源分配`
  - `镜像管理`
- 列表区：按模块展示数据 + 操作列
- 弹窗：新增/编辑表单

## 6. 功能需求

## 6.1 公共能力
### 6.1.1 搜索
- 输入关键字后，按当前模块维度筛选结果
- 占位提示：`搜索算法名称、Recipe ID、Product ID...`
- 支持回车触发与清空恢复

### 6.1.2 通用交互
- 列表默认按更新时间倒序
- 删除需二次确认
- 新增/编辑弹窗支持取消/确认
- 表单提交失败时展示明确错误信息

## 6.2 模块一：算法配置
### 6.2.1 列表展示
每条记录展示：
- 支持左侧箭头展开/收起详情
- Recipe ID（如 `RCP-CD-001`）
- 状态（启用/停用）
- EQP Type、EQP ID、产品ID、Case Name、Python 版本
- 操作：启停开关、编辑、删除

展开态展示：
- EQP ID（详情区）
- 算法名称、算法版本、Python版本
- 算法描述
- 算法文件（可下载链接）
- 算法配置(JSON)
- 配置开关摘要（执行聚合、外部系统集成）
- 创建时间、更新时间

### 6.2.2 新增算法配置
字段：
- Recipe ID*（唯一）
- EQP Type*（必填）
- EQP ID（选填）
- Python 版本（下拉）
- 产品ID
- Case Name
- 算法名称*（建议下拉+可输入）
- 算法版本*（建议下拉+可输入）
- 算法描述（多行）
- 算法文件(.py)（上传，后端存储文件路径）
- 算法配置(JSON)（文本 JSON）
- 执行聚合（开关）
- DC配置(JSON)（执行聚合开启时必填）
- 外部系统集成配置：
  - 发送KOV（开关）
  - 发送LIS（开关）
  - 发送Lithops（开关）
- 外部系统配置(JSON)（编辑态可见，建议新增态也支持）

### 6.2.3 编辑算法配置
- 与新增字段一致
- 允许修改启停状态与外部系统配置
- 更新后刷新列表并提示成功

### 6.2.4 业务规则
- `recipe_id` 全局唯一
- `algorithm_config_json`、`dc_config_json`、`external_system_json` 必须可解析为合法 JSON
- 当 `aggregate_enabled=true` 时，`dc_config_json` 必填
- 至少一个外部系统开关开启时，`external_system_json` 建议必填
- 展开态数据应由后端直接返回，前端不做字段拼装推导

## 6.3 模块二：资源分配
### 6.3.1 列表展示
字段：
- ID、算法名称、镜像地址、镜像版本、当前版本、NAS ID
- CPU限制、内存限制、更新时间
- 操作（原型图标）：
  - 启动/下发（▶）
  - 定时/计划（📅）
  - 历史/回滚（↺）
  - 编辑
  - 删除

说明：图标业务语义需产品最终确认，本 PRD 先按上述含义定义。

### 6.3.2 新增/编辑资源配置
字段：
- 算法名称*（应关联算法配置）
- 镜像地址*（应关联镜像管理）
- CPU限制（如 `2000m`）
- 内存限制（如 `4Gi`）
- CPU请求（如 `1000m`）
- 内存请求（如 `2Gi`）

### 6.3.3 业务规则
- 同一算法仅允许一个“生效中”资源配置
- 请求值不得大于限制值
- 资源格式校验：
  - CPU：`^[0-9]+m?$`
  - 内存：`^[0-9]+(Mi|Gi)$`

## 6.4 模块三：镜像管理
### 6.4.1 列表展示
字段：
- ID（如 `img_001`）
- 镜像名称
- 标签（tag）
- 仓库地址
- 关联算法（多个标签）
- 大小
- 创建时间
- 操作：部署/启用（▶）、编辑、删除

### 6.4.2 新增镜像配置
字段：
- 镜像名称*（如 `cd-measurement-algo`）
- 镜像标签*（如 `v2.3.1`）
- 镜像大小
- 仓库地址*
- 镜像描述
- 关联算法（逗号分隔，建议改为多选）

### 6.4.3 编辑镜像配置
新增字段：
- 镜像状态（启用/禁用）

### 6.4.4 业务规则
- `(image_name, image_tag)` 组合唯一
- 镜像禁用后不允许新建资源分配引用
- 若镜像被资源分配使用，删除需阻断并提示依赖关系

## 7. 数据模型（MySQL 建议）

## 7.1 表：`algo_config`
- `id` BIGINT PK
- `recipe_id` VARCHAR(64) UNIQUE NOT NULL
- `eqp_type` VARCHAR(64) NOT NULL
- `eqp_id` VARCHAR(64)
- `product_id` VARCHAR(64)
- `case_name` VARCHAR(128)
- `python_version` VARCHAR(32)
- `algorithm_name` VARCHAR(128) NOT NULL
- `algorithm_version` VARCHAR(64) NOT NULL
- `algorithm_desc` TEXT
- `algorithm_file_path` VARCHAR(512)
- `algorithm_config_json` JSON
- `aggregate_enabled` TINYINT(1) DEFAULT 0
- `dc_config_json` JSON
- `send_kov` TINYINT(1) DEFAULT 0
- `send_lis` TINYINT(1) DEFAULT 0
- `send_lithops` TINYINT(1) DEFAULT 0
- `external_system_json` JSON
- `enabled` TINYINT(1) DEFAULT 1
- `created_at` DATETIME
- `updated_at` DATETIME
- `created_by` VARCHAR(64)
- `updated_by` VARCHAR(64)

## 7.2 表：`image_config`
- `id` BIGINT PK
- `image_code` VARCHAR(64) UNIQUE（如 img_001）
- `image_name` VARCHAR(128) NOT NULL
- `image_tag` VARCHAR(64) NOT NULL
- `registry_url` VARCHAR(256) NOT NULL
- `image_size` VARCHAR(32)
- `image_desc` TEXT
- `status` VARCHAR(16) DEFAULT 'ENABLED'
- `created_at` DATETIME
- `updated_at` DATETIME
- 唯一索引：`uk_image_name_tag(image_name, image_tag)`

## 7.3 表：`image_algo_rel`
- `id` BIGINT PK
- `image_id` BIGINT NOT NULL
- `algorithm_name` VARCHAR(128) NOT NULL

## 7.4 表：`resource_config`
- `id` BIGINT PK
- `algorithm_name` VARCHAR(128) NOT NULL
- `image_id` BIGINT NOT NULL
- `image_version` VARCHAR(64)
- `current_version` VARCHAR(64)
- `nas_id` VARCHAR(64)
- `cpu_limit` VARCHAR(32)
- `mem_limit` VARCHAR(32)
- `cpu_request` VARCHAR(32)
- `mem_request` VARCHAR(32)
- `status` VARCHAR(16) DEFAULT 'ACTIVE'
- `updated_at` DATETIME
- 唯一索引：`uk_algo_active(algorithm_name, status)`

## 7.5 表：`operation_log`
- `id` BIGINT PK
- `module` VARCHAR(32)（ALGO/RESOURCE/IMAGE）
- `operation` VARCHAR(32)（CREATE/UPDATE/DELETE/ENABLE/DISABLE/DEPLOY/ROLLBACK）
- `biz_id` VARCHAR(64)
- `operator` VARCHAR(64)
- `content` JSON
- `created_at` DATETIME

## 8. 接口设计（REST）

## 8.1 算法配置
- `GET /api/algo-configs`：分页查询/搜索
- `GET /api/algo-configs/{id}`：详情查询（用于展开态）
- `POST /api/algo-configs`：新增
- `PUT /api/algo-configs/{id}`：编辑
- `PUT /api/algo-configs/{id}/status`：启停
- `DELETE /api/algo-configs/{id}`：删除
- `POST /api/algo-configs/{id}/upload`：上传 `.py`

## 8.2 资源分配
- `GET /api/resource-configs`
- `POST /api/resource-configs`
- `PUT /api/resource-configs/{id}`
- `POST /api/resource-configs/{id}/deploy`
- `POST /api/resource-configs/{id}/schedule`
- `POST /api/resource-configs/{id}/rollback`
- `DELETE /api/resource-configs/{id}`

## 8.3 镜像管理
- `GET /api/image-configs`
- `POST /api/image-configs`
- `PUT /api/image-configs/{id}`
- `PUT /api/image-configs/{id}/status`
- `POST /api/image-configs/{id}/deploy`
- `DELETE /api/image-configs/{id}`

## 8.4 通用响应
- 成功：`{ "code": 0, "message": "ok", "data": ... }`
- 失败：`{ "code": 非0, "message": "可读错误信息", "data": null }`

## 9. 校验与错误码建议
- `A001`：Recipe ID 已存在
- `A002`：JSON 格式错误
- `A003`：DC 配置缺失
- `R001`：资源请求超过限制
- `I001`：镜像名称+标签重复
- `I002`：镜像存在依赖，禁止删除
- `C001`：参数非法

## 10. 非功能需求
- 安全：接口需登录态和管理员权限校验
- 审计：新增/编辑/删除/启停必须记录操作日志
- 性能：单模块列表查询 P95 < 500ms（10k 数据量）
- 可用性：关键操作需幂等（防重复提交）
- 可维护性：JSON 字段变更需向后兼容

## 11. 验收标准（UAT）
- 能完成三模块新增/编辑/删除/查询
- 启停开关状态可持久化并实时反映在列表
- JSON 非法输入会被前后端双重拦截
- 镜像依赖阻断策略生效
- 资源请求与限制校验生效
- 操作日志可按模块与时间追踪

## 12. 待确认项
- 资源分配页 3 个图标（📅/↺/▶）的最终业务定义
- `外部系统配置(JSON)` 在“新增算法配置”是否必填
- NAS ID 来源（手填/字典/自动绑定）
- “当前版本”字段更新机制（手动切换或自动回写）
- 镜像大小是自动解析还是手工录入

## 13. 技术实现（Java）
- 技术栈：Spring Boot2.6.5 + Nacos + Lombok + MyBatis-Plus + Oracle + Redis（可选）
- 文件上传：挂载cifs协议Nas + 数据库存路径
- JSON 字段：使用fastJson 校验并落库存 `JSON` 类型
- 领域分层：controller/service/repository/domain
- 事件日志：AOP 或统一审计拦截器
- 报错信息: 错误码+ 国际化中英文（默认中文） 
