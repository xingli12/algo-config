# 资源分配管理规范

## ADDED Requirements

### Requirement: 资源请求值不得大于限制值
系统 SHALL 确保每个算法配置的 CPU/内存请求值（request）不得大于对应的限制值（limit）。

#### Scenario: CPU 请求超过限制失败
- **WHEN** 用户提交资源配置且 `cpu_request` > `cpu_limit`
- **THEN** 系统返回错误码 R001，提示 "资源请求超过限制"

#### Scenario: 内存请求超过限制失败
- **WHEN** 用户提交资源配置且 `mem_request` > `mem_limit`
- **THEN** 系统返回错误码 R001，提示 "资源请求超过限制"

#### Scenario: 合理资源配置成功
- **WHEN** 用户提交资源配置且所有请求值 ≤ 限制值
- **THEN** 系统成功创建资源配置记录

### Requirement: 资源格式校验
系统 SHALL 校验 CPU 和内存字段的格式合法性，拒绝不符合规范的输入。

#### Scenario: CPU 格式校验
- **WHEN** 用户提交的 CPU 值不符合格式 `^[0-9]+m?$`
- **THEN** 系统返回错误码 C001，提示 "参数格式非法"
- **AND** 说明 CPU 格式要求：如 `2000m` 或 `4`

#### Scenario: 内存格式校验
- **WHEN** 用户提交的内存值不符合格式 `^[0-9]+(Mi|Gi)$`
- **THEN** 系统返回错误码 C001，提示 "参数格式非法"
- **AND** 说明内存格式要求：如 `4Gi` 或 `512Mi`

#### Scenario: 合法格式被接受
- **WHEN** 用户提交符合格式规范的 CPU 和内存值
- **THEN** 系统接受并保存资源配置

### Requirement: 每个算法仅允许一个生效中的资源配置
系统 SHALL 确保每个算法名称只能存在一个 `status=ACTIVE` 的资源配置。

#### Scenario: 创建第二个生效配置失败
- **WHEN** 用户尝试为已存在 ACTIVE 配置的算法创建新的 ACTIVE 配置
- **THEN** 系统返回错误提示 "该算法已存在生效中的资源配置"
- **AND** 建议先停用现有配置或切换到新配置

#### Scenario: 停用现有配置后创建新配置成功
- **WHEN** 用户先停用现有 ACTIVE 配置，再创建新的 ACTIVE 配置
- **THEN** 系统成功创建新的资源配置

### Requirement: 资源配置列表查询
系统 SHALL 提供资源配置列表查询接口，按 `updated_at` 倒序返回所有资源配置。

#### Scenario: 查询资源配置列表成功
- **WHEN** 用户请求 `GET /api/resource-configs`
- **THEN** 系统返回按 `updated_at` 倒序的所有资源配置
- **AND** 每条记录包含：算法名称、镜像地址、镜像版本、CPU/内存限制与请求、NAS ID、状态、更新时间

### Requirement: 资源配置部署
系统 SHALL 支持手动触发资源配置下发/启动操作。

#### Scenario: 触发资源配置部署成功
- **WHEN** 用户请求 `POST /api/resource-configs/{id}/deploy`
- **THEN** 系统记录部署操作到 `operation_log` 表
- **AND** 返回部署任务确认信息

### Requirement: 资源配置定时任务
系统 SHALL 支持为资源配置创建定时调度任务。

#### Scenario: 创建定时任务成功
- **WHEN** 用户请求 `POST /api/resource-configs/{id}/schedule` 并提供调度参数
- **THEN** 系统创建定时调度任务
- **AND** 记录操作到 `operation_log` 表

### Requirement: 资源配置回滚
系统 SHALL 支持回滚资源配置到历史版本。

#### Scenario: 回滚到历史版本成功
- **WHEN** 用户请求 `POST /api/resource-configs/{id}/rollback` 并指定目标版本
- **THEN** 系统将当前配置标记为历史版本
- **AND** 恢复目标版本的配置内容
- **AND** 记录回滚操作到 `operation_log` 表
- **AND** 返回回滚后的配置信息

#### Scenario: 回滚到不存在版本失败
- **WHEN** 用户请求回滚到一个不存在的版本号
- **THEN** 系统返回错误提示 "目标版本不存在"

### Requirement: 资源配置删除
系统 SHALL 支持删除资源配置记录。

#### Scenario: 删除资源配置成功
- **WHEN** 用户请求 `DELETE /api/resource-configs/{id}`
- **THEN** 系统从数据库删除该记录
- **AND** 记录操作日志到 `operation_log` 表
