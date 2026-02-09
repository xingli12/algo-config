# 操作审计规范

## ADDED Requirements

### Requirement: 自动记录所有配置变更操作
系统 SHALL 通过 AOP 拦截器自动记录所有模块的增删改操作到 `operation_log` 表。

#### Scenario: 记录算法配置新增操作
- **WHEN** 用户创建新的算法配置
- **THEN** 系统在 `operation_log` 表插入一条记录
- **AND** `module` 字段为 `ALGO`
- **AND** `operation` 字段为 `CREATE`
- **AND** `biz_id` 字段为新建记录的 ID
- **AND** `content` 字段包含创建的字段内容（JSON 格式）
- **AND** `operator` 字段为当前操作用户
- **AND** `created_at` 字段为当前时间戳

#### Scenario: 记录镜像配置编辑操作
- **WHEN** 用户编辑镜像配置
- **THEN** 系统在 `operation_log` 表插入一条记录
- **AND** `module` 字段为 `IMAGE`
- **AND** `operation` 字段为 `UPDATE`
- **AND** `biz_id` 字段为被编辑记录的 ID
- **AND** `content` 字段包含变更前后的字段对比（JSON 格式）

#### Scenario: 记录资源配置删除操作
- **WHEN** 用户删除资源配置
- **THEN** 系统在 `operation_log` 表插入一条记录
- **AND** `module` 字段为 `RESOURCE`
- **AND** `operation` 字段为 `DELETE`
- **AND** `biz_id` 字段为被删除记录的 ID
- **AND** `content` 字段包含被删除记录的完整内容（JSON 格式）

### Requirement: 记录状态切换操作
系统 SHALL 记录所有配置项的启用/停用状态切换操作。

#### Scenario: 记录算法配置启用操作
- **WHEN** 用户启用算法配置
- **THEN** 系统在 `operation_log` 表插入一条记录
- **AND** `operation` 字段为 `ENABLE`

#### Scenario: 记录镜像配置禁用操作
- **WHEN** 用户禁用镜像配置
- **THEN** 系统在 `operation_log` 表插入一条记录
- **AND** `operation` 字段为 `DISABLE`

### Requirement: 记录部署和回滚操作
系统 SHALL 记录资源配置和镜像配置的部署、调度、回滚操作。

#### Scenario: 记录资源配置部署操作
- **WHEN** 用户触发资源配置部署
- **THEN** 系统在 `operation_log` 表插入一条记录
- **AND** `module` 字段为 `RESOURCE`
- **AND** `operation` 字段为 `DEPLOY`
- **AND** `content` 字段包含部署目标信息和参数

#### Scenario: 记录镜像配置部署操作
- **WHEN** 用户触发镜像配置部署
- **THEN** 系统在 `operation_log` 表插入一条记录
- **AND** `module` 字段为 `IMAGE`
- **AND** `operation` 字段为 `DEPLOY`

#### Scenario: 记录资源配置回滚操作
- **WHEN** 用户回滚资源配置到历史版本
- **THEN** 系统在 `operation_log` 表插入一条记录
- **AND** `operation` 字段为 `ROLLBACK`
- **AND** `content` 字段包含目标版本信息和回滚原因

### Requirement: 操作日志内容格式
系统 SHALL 将操作日志的 `content` 字段存储为 JSON 格式的 CLOB 类型，支持复杂的结构化数据。

#### Scenario: 存储简单字段变更
- **WHEN** 操作涉及少量字段变更
- **THEN** `content` 字段包含变更的字段名、旧值、新值

#### Scenario: 存储大文本内容
- **WHEN** 操作涉及 JSON 配置字段或大文本内容
- **THEN** `content` 字段使用 CLOB 类型存储完整内容
- **AND** 内容保持 JSON 格式的可读性

### Requirement: 操作员身份记录
系统 SHALL 在所有操作日志中记录当前操作用户的身份信息。

#### Scenario: 记录管理员操作
- **WHEN** 管理员用户执行任何配置变更操作
- **THEN** `operator` 字段记录管理员的用户名或 ID

#### Scenario: 匿名操作被拒绝
- **WHEN** 未登录用户尝试执行配置变更操作
- **THEN** 系统拒绝操作并返回鉴权失败错误
- **AND** 不记录任何操作日志

### Requirement: 操作日志持久化保证
系统 SHALL 确保操作日志写入的原子性，如果业务操作失败则不记录日志。

#### Scenario: 业务操作失败不记录日志
- **WHEN** 用户尝试创建算法配置但因校验失败（如 Recipe ID 重复）
- **THEN** 系统不写入任何操作日志

#### Scenario: 业务操作成功必定记录日志
- **WHEN** 用户成功创建算法配置
- **THEN** 系统在同一个数据库事务内完成业务记录创建和日志写入
- **AND** 两者要么同时成功，要么同时失败
