# 算法配置管理系统 API 文档

## 基础信息

- **服务名称**：algo-config-service
- **版本**：v1.0.0
- **基础路径**：/api
- **协议**：HTTP/HTTPS
- **数据格式**：JSON

## 统一响应格式

### 成功响应
```json
{
  "code": "0",
  "message": "ok",
  "messageEn": "ok",
  "data": { ... }
}
```

### 失败响应
```json
{
  "code": "A001",
  "message": "Recipe ID 已存在",
  "messageEn": "Recipe ID already exists",
  "data": null
}
```

## API 接口列表

### 系统管理

#### 1. 健康检查
```
GET /api/health
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": {
    "status": "UP",
    "service": "algo-config-service",
    "timestamp": "2026-02-09T23:59:59",
    "version": "1.0.0"
  }
}
```

---

### 算法配置管理 (Algo Config)

#### 1. 分页查询算法配置
```
GET /api/algo-configs?page=1&size=10&keyword=RCP-001
```

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认 1 |
| size | Integer | 否 | 每页大小，默认 10 |
| keyword | String | 否 | 搜索关键字（算法名称、Recipe ID、Product ID） |

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": {
    "records": [
      {
        "id": 1,
        "recipeId": "RCP-CD-001",
        "eqpType": "CD",
        "eqpId": "EQP001",
        "algorithmName": "CD Measurement Algorithm",
        "algorithmVersion": "v2.3.1",
        "enabled": 1,
        "createdAt": "2026-02-09T10:00:00",
        "updatedAt": "2026-02-09T15:30:00"
      }
    ],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

#### 2. 查询算法配置详情
```
GET /api/algo-configs/{id}
```

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 算法配置 ID |

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": {
    "id": 1,
    "recipeId": "RCP-CD-001",
    "eqpType": "CD",
    "eqpId": "EQP001",
    "productId": "PROD-001",
    "caseName": "Case-A",
    "pythonVersion": "3.8",
    "algorithmName": "CD Measurement Algorithm",
    "algorithmVersion": "v2.3.1",
    "algorithmDesc": "Critical Dimension measurement algorithm",
    "algorithmFilePath": "/nas/algo-configs/RCP-CD-001/algorithm.py",
    "algorithmConfigJson": "{\"threshold\": 0.5}",
    "aggregateEnabled": 1,
    "dcConfigJson": "{\"param\": 100}",
    "sendKov": 1,
    "sendLis": 0,
    "sendLithops": 1,
    "externalSystemJson": "{}",
    "enabled": 1,
    "createdAt": "2026-02-09T10:00:00",
    "updatedAt": "2026-02-09T15:30:00",
    "createdBy": "admin",
    "updatedBy": "admin"
  }
}
```

#### 3. 新增算法配置
```
POST /api/algo-configs
Content-Type: application/json
```

**请求体：**
```json
{
  "recipeId": "RCP-CD-002",
  "eqpType": "CD",
  "eqpId": "EQP002",
  "productId": "PROD-002",
  "caseName": "Case-B",
  "pythonVersion": "3.9",
  "algorithmName": "CD Measurement Algorithm",
  "algorithmVersion": "v2.3.1",
  "algorithmDesc": "Critical Dimension measurement algorithm",
  "algorithmConfigJson": "{\"threshold\": 0.5}",
  "aggregateEnabled": 1,
  "dcConfigJson": "{\"param\": 100}",
  "sendKov": 1,
  "sendLis": 0,
  "sendLithops": 1,
  "externalSystemJson": "{}",
  "enabled": 1
}
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": 123
}
```

**错误响应示例（Recipe ID 已存在）：**
```json
{
  "code": "A001",
  "message": "Recipe ID 已存在",
  "messageEn": "Recipe ID already exists",
  "data": null
}
```

#### 4. 编辑算法配置
```
PUT /api/algo-configs/{id}
Content-Type: application/json
```

**请求体：**（与新增相同）

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

#### 5. 切换算法配置状态
```
PUT /api/algo-configs/{id}/status?enabled=0
```

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 算法配置 ID |
| enabled | Integer | 是 | 状态（0 停用，1 启用） |

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

#### 6. 删除算法配置
```
DELETE /api/algo-configs/{id}
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

#### 7. 上传算法文件
```
POST /api/algo-configs/{id}/upload
Content-Type: multipart/form-data
```

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | File | 是 | .py 格式的算法文件 |

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": "/nas/algo-configs/1/1707529200000.py"
}
```

---

### 镜像配置管理 (Image Config)

#### 1. 查询所有镜像配置
```
GET /api/image-configs
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": [
    {
      "id": 1,
      "imageCode": "img_001",
      "imageName": "cd-measurement-algo",
      "imageTag": "v2.3.1",
      "registryUrl": "registry.example.com",
      "imageSize": "2.5GB",
      "imageDesc": "Critical Dimension measurement algorithm image",
      "status": "ENABLED",
      "algorithmNames": ["CD Measurement Algorithm", "Overlay Algorithm"],
      "createdAt": "2026-02-09T10:00:00",
      "updatedAt": "2026-02-09T15:30:00"
    }
  ]
}
```

#### 2. 查询镜像配置详情
```
GET /api/image-configs/{id}
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": {
    "id": 1,
    "imageCode": "img_001",
    "imageName": "cd-measurement-algo",
    "imageTag": "v2.3.1",
    "registryUrl": "registry.example.com",
    "imageSize": "2.5GB",
    "imageDesc": "Critical Dimension measurement algorithm image",
    "status": "ENABLED",
    "algorithmNames": ["CD Measurement Algorithm", "Overlay Algorithm"],
    "createdAt": "2026-02-09T10:00:00",
    "updatedAt": "2026-02-09T15:30:00"
  }
}
```

#### 3. 新增镜像配置
```
POST /api/image-configs
Content-Type: application/json
```

**请求体：**
```json
{
  "imageCode": "img_002",
  "imageName": "overlay-algo",
  "imageTag": "v1.0.0",
  "registryUrl": "registry.example.com",
  "imageSize": "1.8GB",
  "imageDesc": "Overlay detection algorithm image",
  "algorithmNames": ["Overlay Algorithm", "Defect Detection Algorithm"]
}
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": 456
}
```

**错误响应示例（镜像名称+标签重复）：**
```json
{
  "code": "I001",
  "message": "镜像名称+标签重复",
  "messageEn": "Image name and tag already exist",
  "data": null
}
```

#### 4. 编辑镜像配置
```
PUT /api/image-configs/{id}
Content-Type: application/json
```

**请求体：**（与新增相同）

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

#### 5. 切换镜像状态
```
PUT /api/image-configs/{id}/status?status=DISABLED
```

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 镜像配置 ID |
| status | String | 是 | 状态（ENABLED/DISABLED） |

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

#### 6. 删除镜像配置
```
DELETE /api/image-configs/{id}
```

**错误响应示例（镜像存在依赖）：**
```json
{
  "code": "I002",
  "message": "镜像存在依赖，无法删除",
  "messageEn": "Image has dependencies, cannot delete",
  "data": null
}
```

#### 7. 触发镜像部署
```
POST /api/image-configs/{id}/deploy
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

---

### 资源配置管理 (Resource Config)

#### 1. 分页查询资源配置
```
GET /api/resource-configs?page=1&size=10
```

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认 1 |
| size | Integer | 否 | 每页大小，默认 10 |

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": {
    "records": [
      {
        "id": 1,
        "algorithmName": "CD Measurement Algorithm",
        "imageId": 1,
        "imageUrl": "registry.example.com/cd-measurement-algo:v2.3.1",
        "imageVersion": "v2.3.1",
        "currentVersion": "v2.3.1",
        "nasId": "nas-001",
        "cpuLimit": "2000m",
        "memLimit": "4Gi",
        "cpuRequest": "1000m",
        "memRequest": "2Gi",
        "status": "ACTIVE",
        "updatedAt": "2026-02-09T15:30:00"
      }
    ],
    "total": 50,
    "size": 10,
    "current": 1,
    "pages": 5
  }
}
```

#### 2. 查询资源配置详情
```
GET /api/resource-configs/{id}
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": {
    "id": 1,
    "algorithmName": "CD Measurement Algorithm",
    "imageId": 1,
    "imageUrl": "registry.example.com/cd-measurement-algo:v2.3.1",
    "imageVersion": "v2.3.1",
    "currentVersion": "v2.3.1",
    "nasId": "nas-001",
    "cpuLimit": "2000m",
    "memLimit": "4Gi",
    "cpuRequest": "1000m",
    "memRequest": "2Gi",
    "status": "ACTIVE",
    "updatedAt": "2026-02-09T15:30:00"
  }
}
```

#### 3. 新增资源配置
```
POST /api/resource-configs
Content-Type: application/json
```

**请求体：**
```json
{
  "algorithmName": "CD Measurement Algorithm",
  "imageId": 1,
  "imageVersion": "v2.3.1",
  "nasId": "nas-001",
  "cpuLimit": "2000m",
  "memLimit": "4Gi",
  "cpuRequest": "1000m",
  "memRequest": "2Gi",
  "status": "ACTIVE"
}
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok",
  "data": 789
}
```

**错误响应示例（资源请求超过限制）：**
```json
{
  "code": "R001",
  "message": "资源请求超过限制",
  "messageEn": "Resource request exceeds limit",
  "data": null
}
```

**错误响应示例（算法已存在生效配置）：**
```json
{
  "code": "R002",
  "message": "该算法已存在生效中的资源配置",
  "messageEn": "Active resource config already exists for this algorithm",
  "data": null
}
```

**错误响应示例（镜像已禁用）：**
```json
{
  "code": "I003",
  "message": "镜像已被禁用，无法引用",
  "messageEn": "Image is disabled, cannot reference",
  "data": null
}
```

#### 4. 编辑资源配置
```
PUT /api/resource-configs/{id}
Content-Type: application/json
```

**请求体：**（与新增相同）

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

#### 5. 删除资源配置
```
DELETE /api/resource-configs/{id}
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

#### 6. 触发资源配置下发
```
POST /api/resource-configs/{id}/deploy
```

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

#### 7. 创建定时任务
```
POST /api/resource-configs/{id}/schedule?scheduleCron=0/5 * * * ?
```

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 资源配置 ID |
| scheduleCron | String | 是 | Cron 表达式 |

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

#### 8. 回滚到历史版本
```
POST /api/resource-configs/{id}/rollback?targetVersion=v2.3.0
```

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 资源配置 ID |
| targetVersion | String | 是 | 目标版本号 |

**响应示例：**
```json
{
  "code": "0",
  "message": "ok"
}
```

---

## 错误码说明

| 错误码 | 描述 | HTTP 状态码 |
|--------|------|-------------|
| 0 | 成功 | 200 |
| A001 | Recipe ID 已存在 | 200 |
| A002 | JSON 格式错误 | 200 |
| A003 | DC 配置缺失 | 200 |
| A004 | 算法配置不存在 | 200 |
| I001 | 镜像名称+标签重复 | 200 |
| I002 | 镜像存在依赖，无法删除 | 200 |
| I003 | 镜像已被禁用，无法引用 | 200 |
| I004 | 镜像配置不存在 | 200 |
| R001 | 资源请求超过限制 | 200 |
| R002 | 该算法已存在生效中的资源配置 | 200 |
| R003 | 资源配置不存在 | 200 |
| C001 | 参数格式非法 | 200 |
| C002 | 未授权访问 | 200 |
| C003 | 系统内部错误 | 200 |

---

## 数据模型

### 算法配置 (AlgoConfig)

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| recipeId | String | 是 | Recipe ID（全局唯一） |
| eqpType | String | 是 | 设备类型 |
| algorithmName | String | 是 | 算法名称 |
| algorithmVersion | String | 是 | 算法版本 |
| algorithmConfigJson | String | 否 | 算法配置 JSON |
| aggregateEnabled | Integer | 否 | 是否启用聚合（0/1） |
| dcConfigJson | String | 条件 | DC 配置 JSON（启用聚合时必填） |
| enabled | Integer | 否 | 启用状态（0 停用，1 启用） |

### 镜像配置 (ImageConfig)

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| imageName | String | 是 | 镜像名称 |
| imageTag | String | 是 | 镜像标签 |
| registryUrl | String | 是 | 仓库地址 |
| algorithmNames | Array | 否 | 关联的算法名称列表 |
| status | String | 否 | 状态（ENABLED/DISABLED） |

### 资源配置 (ResourceConfig)

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| algorithmName | String | 是 | 算法名称 |
| imageId | Long | 是 | 镜像 ID |
| cpuLimit | String | 否 | CPU 限制（如 2000m） |
| memLimit | String | 否 | 内存限制（如 4Gi） |
| cpuRequest | String | 否 | CPU 请求（如 1000m） |
| memRequest | String | 否 | 内存请求（如 2Gi） |
| status | String | 否 | 状态（ACTIVE/INACTIVE） |

---

## 访问 Swagger UI

启动应用后，访问以下地址查看交互式 API 文档：

```
http://localhost:8080/swagger-ui/
```

或获取 OpenAPI JSON：

```
http://localhost:8080/v3/api-docs
```
