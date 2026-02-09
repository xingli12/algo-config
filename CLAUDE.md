# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在此仓库中工作时提供指导。

## 语言规则

**重要**：后续生成的所有代码注释、文档、README 等内容必须使用**中文**（技术术语如类名、方法名、API路径等保持英文）。

## 项目概述

**算法配置管理系统** - 一个用于管理算法配置、资源分配和 Docker 镜像配置的 Spring Boot 应用。

### 核心模块

| 模块 | 描述 | 关键表 |
|------|------|------------|
| **算法配置** | 管理算法 Recipe、Python 版本、算法文件 | `algo_config` |
| **资源分配** | 管理 CPU/内存资源限制和请求 | `resource_config` |
| **镜像管理** | 管理 Docker 镜像配置和算法关联 | `image_config`, `image_algo_rel` |
| **操作日志** | 所有操作的审计追踪 | `operation_log` |

## 设计文档

位于 `prd/` 目录：
- `java-prd-algo-config.md` - 完整 PRD（包含需求和数据模型）
- `java-prd-dev-breakdown.md` - 开发任务拆分（BE/DB/QA 任务）
- `sql/001_algo_config_schema_oracle.sql` - Oracle 建表脚本（5张表）
- `testcases/algo-config-testcases.md` - API 测试用例

UI 原型图位于 `figma/` 目录（9 个 PNG 文件）。

## openspec规范

位于 `openspec/` 目录：
- 需遵守该目录下的规范约束


## 技术栈

| 组件 | 技术 | 版本/说明 |
|------|------|-----------|
| 应用框架 | Spring Boot | 2.6.5 |
| 配置中心 | Nacos | 服务发现 + 配置管理 |
| ORM 框架 | MyBatis-Plus | 代码生成 + CRUD |
| 数据库 | Oracle | 12c+ |
| 缓存 | Redis | 可选 |
| 工具库 | Lombok | 简化 Java 代码 |
| JSON 处理 | fastjson | 校验 + 序列化 |

### 文件存储
- 挂载 CIFS 协议 NAS
- 数据库存储文件路径（`algorithm_file_path` 字段）

### 领域分层
```
controller/  - 接口层（接收请求、参数校验）
service/     - 业务层（业务逻辑、事务管理）
repository/  - 持久层（MyBatis-Plus Mapper）
domain/      - 领域层（实体类、DTO、VO）
```

### 审计日志
- 使用 AOP 拦截器统一记录操作日志到 `operation_log` 表

### 错误信息
- 错误码 + 国际化（中英文，默认中文）

## 数据库架构

所有表使用 **Oracle 12c+**：

1. **algo_config** - 算法配置表，`recipe_id` 为唯一键
2. **image_config** - Docker 镜像配置表，`(image_name, image_tag)` 唯一约束
3. **image_algo_rel** - 镜像与算法关联表（级联删除）
4. **resource_config** - 资源配置表，外键关联 `image_config`（限制删除）
5. **operation_log** - 操作日志表，content 字段为 CLOB 类型

### Oracle 数据类型映射

| MySQL | Oracle | 说明 |
|-------|--------|------|
| `BIGINT` | `NUMBER(19)` | 主键 ID |
| `VARCHAR(n)` | `VARCHAR2(n CHAR)` | 字符串（按字符数） |
| `TEXT` | `CLOB` | 大文本 |
| `TINYINT(1)` | `NUMBER(1)` | 布尔值（0/1） |
| `DATETIME` | `TIMESTAMP` | 日期时间 |
| `JSON` | `CLOB` | JSON 字符串（应用层用 fastjson 校验） |

### 关键约束
- `algo_config.recipe_id` 全局唯一
- `resource_config` 每个算法只允许一个 ACTIVE 状态的配置
- 删除被 `resource_config` 引用的 `image_config` 会被外键阻断

### 自增主键
Oracle 不支持 `AUTO_INCREMENT`，使用**序列 + 触发器**实现：
```sql
CREATE SEQUENCE seq_algo_config START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE TRIGGER trg_algo_config_bi BEFORE INSERT ON algo_config ...
```

### 自动更新时间
Oracle 不支持 `ON UPDATE CURRENT_TIMESTAMP`，使用**触发器**实现：
```sql
CREATE TRIGGER trg_algo_config_bu BEFORE UPDATE ON algo_config ...
```

## API 接口

### 算法配置 (`/api/algo-configs`)
- `GET /api/algo-configs` - 分页列表 + 搜索
- `GET /api/algo-configs/{id}` - 详情查询（展开态）
- `POST /api/algo-configs` - 新增（校验 `recipe_id` 唯一性、JSON 字段）
- `PUT /api/algo-configs/{id}` - 编辑
- `PUT /api/algo-configs/{id}/status` - 启停切换
- `DELETE /api/algo-configs/{id}` - 删除
- `POST /api/algo-configs/{id}/upload` - 上传 `.py` 算法文件

### 资源分配 (`/api/resource-configs`)
- `GET /api/resource-configs` - 列表（按 `updated_at` 倒序）
- `POST /api/resource-configs` - 新增（校验请求值 ≤ 限制值）
- `PUT /api/resource-configs/{id}` - 编辑
- `DELETE /api/resource-configs/{id}` - 删除
- `POST /api/resource-configs/{id}/deploy` - 触发部署
- `POST /api/resource-configs/{id}/schedule` - 创建定时任务
- `POST /api/resource-configs/{id}/rollback` - 回滚到历史版本

### 镜像管理 (`/api/image-configs`)
- `GET /api/image-configs` - 列表
- `POST /api/image-configs` - 新增（校验 `image_name + image_tag` 唯一性）
- `PUT /api/image-configs/{id}` - 编辑
- `PUT /api/image-configs/{id}/status` - 启停切换
- `DELETE /api/image-configs/{id}` - 删除（检查依赖）
- `POST /api/image-configs/{id}/deploy` - 触发部署

## 错误码

| 错误码 | 描述 |
|--------|------|
| A001 | Recipe ID 已存在 |
| A002 | JSON 格式错误 |
| A003 | DC 配置缺失（当 `aggregate_enabled=true` 时） |
| R001 | 资源请求超过限制 |
| I001 | 镜像名称 + 标签重复 |
| I002 | 镜像存在依赖，无法删除 |
| C001 | 参数格式非法 |

## 校验规则

### 资源格式校验
- CPU：`^[0-9]+m?$`（如 `2000m`、`4`）
- 内存：`^[0-9]+(Mi\|Gi)$`（如 `4Gi`、`512Mi`）
- 请求值不得大于限制值

### 业务规则
1. 当 `aggregate_enabled=1` 时，`dc_config_json` 必填
2. 当任一外部系统开关开启时，建议提供 `external_system_json`
3. 每个算法只能有一个 ACTIVE 状态的资源配置
4. 禁用状态的镜像不能被新的资源配置引用
5. 所有新增/编辑/删除/启停操作必须记录到 `operation_log`

### JSON 字段处理
- **数据库存储**：使用 CLOB 类型存储 JSON 字符串
- **应用层校验**：使用 `fastjson` 校验 JSON 格式合法性
- **字段清单**：
  - `algorithm_config_json` - 算法配置
  - `dc_config_json` - DC 配置
  - `external_system_json` - 外部系统配置
  - `operation_log.content` - 操作日志内容

**校验示例**：
```java
// 使用 fastjson 校验
JSON.parse(jsonString);  // 抛出异常则格式非法
```

## 标准响应格式

成功响应：
```json
{
  "code": 0,
  "message": "ok",
  "data": { ... }
}
```

错误响应：
```json
{
  "code": "A001",
  "message": "可读的错误信息",
  "message_en": "Readable error message",
  "data": null
}
```

### 国际化处理
- 默认使用中文错误信息
- 同时返回中英文双语 `message` 和 `message_en`
- 使用 Spring `MessageSource` 实现国际化
- 配置文件路径：`classpath:i18n/messages_*.properties`

## 开发流程（建议）

1. **阶段一**：数据库 + 算法配置模块（BE-010~BE-016）
2. **阶段二**：镜像管理模块（BE-030~BE-035）
3. **阶段三**：资源分配模块（BE-020~BE-026）
4. **阶段四**：跨模块功能 + 审计（BE-040~BE-041）
5. **阶段五**：文档 + 测试（BE-050 + QA）

详细任务拆分见 `prd/java-prd-dev-breakdown.md`。
