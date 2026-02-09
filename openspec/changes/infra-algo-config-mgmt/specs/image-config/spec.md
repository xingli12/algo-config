# Docker 镜像配置管理规范

## ADDED Requirements

### Requirement: 镜像名称与标签唯一性约束
系统 SHALL 确保 `(image_name, image_tag)` 组合在全局范围内唯一，不允许重复的组合创建。

#### Scenario: 创建重复镜像名称和标签失败
- **WHEN** 用户尝试创建一个已存在的 `(image_name, image_tag)` 组合
- **THEN** 系统返回错误码 I001，提示 "镜像名称+标签重复"
- **AND** 数据库不插入任何记录

#### Scenario: 创建唯一镜像名称和标签成功
- **WHEN** 用户创建一个不存在的 `(image_name, image_tag)` 组合
- **THEN** 系统成功创建记录并返回完整镜像配置信息

### Requirement: 镜像与算法关联维护
系统 SHALL 维护镜像与算法的关联关系，支持一个镜像关联多个算法。

#### Scenario: 创建镜像时关联算法
- **WHEN** 用户创建镜像配置并指定关联的算法名称列表
- **THEN** 系统在 `image_config` 表创建镜像记录
- **AND** 在 `image_algo_rel` 表创建关联记录

#### Scenario: 删除镜像时级联删除关联
- **WHEN** 用户删除镜像配置
- **THEN** 系统从 `image_algo_rel` 表自动删除所有该镜像的关联记录
- **AND** 数据库保持一致性

### Requirement: 镜像状态切换
系统 SHALL 支持切换镜像的启用/禁用状态。

#### Scenario: 启用镜像
- **WHEN** 用户请求 `PUT /api/image-configs/{id}/status` 并设置 `status=ENABLED`
- **THEN** 系统更新状态为启用
- **AND** 记录操作日志到 `operation_log` 表

#### Scenario: 禁用镜像
- **WHEN** 用户请求 `PUT /api/image-configs/{id}/status` 并设置 `status=DISABLED`
- **THEN** 系统更新状态为禁用
- **AND** 记录操作日志到 `operation_log` 表

### Requirement: 禁用镜像的资源配置引用限制
系统 SHALL 不允许新的资源配置引用被禁用的镜像。

#### Scenario: 新建资源配置引用禁用镜像失败
- **WHEN** 用户尝试创建资源配置并引用状态为 `DISABLED` 的镜像
- **THEN** 系统返回错误提示 "镜像已被禁用，无法引用"
- **AND** 数据库不插入任何记录

#### Scenario: 新建资源配置引用启用镜像成功
- **WHEN** 用户创建资源配置并引用状态为 `ENABLED` 的镜像
- **THEN** 系统成功创建资源配置记录

### Requirement: 镜像删除前的依赖检查
系统 SHALL 在删除镜像前检查是否被 `resource_config` 表引用，如果存在依赖则阻断删除。

#### Scenario: 删除被依赖的镜像失败
- **WHEN** 用户请求 `DELETE /api/image-configs/{id}` 且该镜像被 `resource_config` 引用
- **THEN** 系统返回错误码 I002，提示 "镜像存在依赖，无法删除"
- **AND** 显示依赖关系详情（哪些资源配置正在使用该镜像）

#### Scenario: 删除未被依赖的镜像成功
- **WHEN** 用户请求 `DELETE /api/image-configs/{id}` 且该镜像未被任何资源配置引用
- **THEN** 系统从数据库删除该镜像记录
- **AND** 级联删除 `image_algo_rel` 表中的关联记录
- **AND** 记录操作日志到 `operation_log` 表

### Requirement: 镜像配置列表查询
系统 SHALL 提供镜像配置列表查询接口，返回所有镜像的基本信息和关联的算法列表。

#### Scenario: 查询镜像列表成功
- **WHEN** 用户请求 `GET /api/image-configs`
- **THEN** 系统返回所有镜像配置
- **AND** 每条记录包含：镜像 ID、名称、标签、仓库地址、关联算法列表、大小、创建时间

### Requirement: 镜像部署触发
系统 SHALL 支持手动触发镜像部署操作。

#### Scenario: 触发镜像部署成功
- **WHEN** 用户请求 `POST /api/image-configs/{id}/deploy`
- **THEN** 系统记录部署操作到 `operation_log` 表
- **AND** 返回部署任务确认信息
