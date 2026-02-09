# 算法配置管理规范

## ADDED Requirements

### Requirement: 唯一 Recipe ID 约束
系统 SHALL 确保 `recipe_id` 在全局范围内唯一，不允许重复的 Recipe ID 创建。

#### Scenario: 创建重复 Recipe ID 失败
- **WHEN** 用户尝试创建一个已存在的 `recipe_id`
- **THEN** 系统返回错误码 A001，提示 "Recipe ID 已存在"
- **AND** 数据库不插入任何记录

#### Scenario: 创建唯一 Recipe ID 成功
- **WHEN** 用户创建一个不存在的 `recipe_id`
- **THEN** 系统成功创建记录并返回完整配置信息

### Requirement: 算法配置 JSON 格式校验
系统 SHALL 校验所有 JSON 字段（`algorithm_config_json`、`dc_config_json`、`external_system_json`）的格式合法性，拒绝非法 JSON 输入。

#### Scenario: 非法 JSON 被拒绝
- **WHEN** 用户提交的 JSON 字段包含格式错误（如缺少引号、括号不匹配）
- **THEN** 系统返回错误码 A002，提示 "JSON 格式错误"
- **AND** 数据库不插入任何记录

#### Scenario: 合法 JSON 被接受
- **WHEN** 用户提交符合 JSON 规范的字段内容
- **THEN** 系统成功保存并返回确认信息

### Requirement: DC 配置必填校验
当 `aggregate_enabled` 为 `true` 时，系统 SHALL 要求 `dc_config_json` 字段必填。

#### Scenario: 开启聚合但未填写 DC 配置失败
- **WHEN** 用户设置 `aggregate_enabled=true` 且 `dc_config_json` 为空
- **THEN** 系统返回错误码 A003，提示 "DC 配置缺失"
- **AND** 数据库不插入任何记录

#### Scenario: 开启聚合并填写 DC 配置成功
- **WHEN** 用户设置 `aggregate_enabled=true` 且提供合法的 `dc_config_json`
- **THEN** 系统成功保存记录

#### Scenario: 未开启聚合时 DC 配置可选
- **WHEN** 用户设置 `aggregate_enabled=false`
- **THEN** 系统允许 `dc_config_json` 为空或 null

### Requirement: 算法配置分页查询
系统 SHALL 支持按 `updated_at` 倒序的分页查询，并支持关键字搜索（算法名称、Recipe ID、Product ID）。

#### Scenario: 默认分页查询
- **WHEN** 用户请求 `GET /api/algo-configs?page=1&size=10`
- **THEN** 系统返回按 `updated_at` 倒序的前 10 条记录
- **AND** 响应包含总记录数和分页信息

#### Scenario: 关键字搜索
- **WHEN** 用户请求 `GET /api/algo-configs?keyword=RCP-CD-001`
- **THEN** 系统返回所有匹配的算法配置（算法名称、Recipe ID 或 Product ID 包含关键字）

### Requirement: 算法配置详情查询
系统 SHALL 提供展开态详情查询接口，返回完整的配置信息包括创建时间、更新时间和配置开关摘要。

#### Scenario: 查询详情成功
- **WHEN** 用户请求 `GET /api/algo-configs/{id}`
- **THEN** 系统返回该算法配置的完整信息
- **AND** 包含所有字段：EQP 信息、算法文件路径、JSON 配置、配置开关、时间戳等

### Requirement: 算法配置状态切换
系统 SHALL 支持通过独立接口切换算法配置的启用/停用状态。

#### Scenario: 启用算法配置
- **WHEN** 用户请求 `PUT /api/algo-configs/{id}/status` 并设置 `enabled=true`
- **THEN** 系统更新状态为启用
- **AND** 记录操作日志到 `operation_log` 表

#### Scenario: 停用算法配置
- **WHEN** 用户请求 `PUT /api/algo-configs/{id}/status` 并设置 `enabled=false`
- **THEN** 系统更新状态为停用
- **AND** 记录操作日志到 `operation_log` 表

### Requirement: 算法文件上传
系统 SHALL 支持上传 `.py` 算法文件，并将文件路径存储到数据库的 `algorithm_file_path` 字段。

#### Scenario: 上传算法文件成功
- **WHEN** 用户通过 `POST /api/algo-configs/{id}/upload` 上传 `.py` 文件
- **THEN** 系统将文件保存到 NAS 存储
- **AND** 更新 `algorithm_file_path` 字段
- **AND** 记录操作日志到 `operation_log` 表

#### Scenario: 上传非 .py 文件失败
- **WHEN** 用户尝试上传非 `.py` 格式的文件
- **THEN** 系统返回错误码 C001，提示 "参数格式非法"

### Requirement: 算法配置删除
系统 SHALL 支持删除算法配置，删除后不可恢复。

#### Scenario: 删除算法配置成功
- **WHEN** 用户请求 `DELETE /api/algo-configs/{id}`
- **THEN** 系统从数据库删除该记录
- **AND** 记录操作日志到 `operation_log` 表
