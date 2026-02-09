# 运维手册

## 系统概述

算法配置管理系统是一个基于 Spring Boot 的后端服务，用于管理算法配置、资源分配和 Docker 镜像配置。

### 技术栈
- **框架**: Spring Boot 2.6.5
- **数据库**: Oracle 12c+
- **服务发现**: Nacos 2.0+
- **ORM**: MyBatis-Plus 3.5.2

### 服务端口
- **应用端口**: 8080
- **上下文路径**: /api
- **健康检查**: GET /api/health

## 日常运维

### 1. 服务启停

#### 启动服务
```bash
cd /opt/app
./start.sh

# 或使用 systemd（推荐）
systemctl start algo-config
```

#### 停止服务
```bash
cd /opt/app
./stop.sh

# 或使用 systemd
systemctl stop algo-config
```

#### 重启服务
```bash
systemctl restart algo-config

# 查看服务状态
systemctl status algo-config
```

#### 查看日志
```bash
# 实时日志
tail -f /var/log/algo-config/algo-config.log

# 最近 100 行
tail -n 100 /var/log/algo-config/algo-config.log

# 搜索错误日志
grep -i "error" /var/log/algo-config/algo-config.log
```

### 2. 健康检查

#### 基础健康检查
```bash
# 检查服务是否启动
curl http://localhost:8080/api/health

# 预期响应
{
  "code": "0",
  "message": "ok",
  "messageEn": "ok",
  "data": {
    "status": "UP"
  }
}
```

#### 数据库连接检查
```bash
# 检查数据库连接池状态
curl http://localhost:8080/api/actuator/health/db

# 需要启用 Spring Boot Actuator
```

### 3. 性能监控

#### JVM 监控
```bash
# 查看 JVM 进程
ps aux | grep algo-config

# 使用 jstat 监控
jstat -gcutil <PID> 1000

# 使用 jmap 查看堆内存
jmap -heap <PID>
```

#### API 性能
```bash
# 使用 ab 进行压力测试
ab -n 1000 -c 10 http://localhost:8080/api/algo-configs

# 检查响应时间
curl -w "@curl-format.txt" -o /dev/null -s http://localhost:8080/api/algo-configs
```

创建 `curl-format.txt`:
```
time_namelookup:  %{time_namelookup}\n
time_connect:     %{time_connect}\n
time_appconnect:  %{time_appconnect}\n
time_pretransfer: %{time_pretransfer}\n
time_starttransfer: %{time_starttransfer}\n
time_total:       %{time_total}\n
```

## 常见问题处理

### 1. 服务无法启动

#### 问题现象
```bash
systemctl start algo-config
# 服务启动失败
```

#### 排查步骤

1. **检查 Java 版本**
```bash
java -version
# 应该是 Java 8 或 Java 11
```

2. **检查配置文件**
```bash
# 验证 YAML 格式
java -jar /path/to/algo-config.jar --spring.config.location=/path/to/application.yml --debug
```

3. **检查端口占用**
```bash
netstat -tuln | grep 8080
lsof -i :8080
```

4. **查看详细错误**
```bash
journalctl -u algo-config -n 50 --no-pager
```

### 2. 数据库连接异常

#### 问题现象
```
java.sql.SQLException: Listener refused the connect
ORA-12541: TNS:no listener
```

#### 解决方案

1. **检查 Oracle 监听器**
```bash
# 在数据库服务器上执行
lsnrctl status

# 如果未启动，启动监听器
lsnrctl start
```

2. **验证连接字符串**
```yaml
# 检查 application.yml
spring:
  datasource:
    url: jdbc:oracle:thin:@host:port:sid
    # 确认 host、port、sid 正确
```

3. **测试连接**
```bash
# 从应用服务器测试数据库连接
sqlplus algo_config/password@//host:port/sid
```

### 3. Nacos 连接失败

#### 问题现象
```
java.net.ConnectException: Connection refused
Failed to connect to Nacos server
```

#### 解决方案

1. **检查 Nacos 服务**
```bash
curl http://nacos-server:8848/nacos/
```

2. **检查网络连通性**
```bash
telnet nacos-server 8848
```

3. **验证配置**
```bash
# 检查 Nacos 地址配置
grep "nacos" /opt/app/application.yml
```

### 4. 内存溢出

#### 问题现象
```
java.lang.OutOfMemoryError: Java heap space
```

#### 解决方案

1. **查看 JVM 内存配置**
```bash
ps aux | grep algo-config | grep -o "Xmx[0-9a-zA-Z]*"
```

2. **增加 JVM 堆内存**
```bash
# 修改 start.sh
JAVA_OPTS="-Xms4g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

3. **分析内存泄漏**
```bash
# 生成堆转储
jmap -dump:format=b,file=/tmp/algo-config-heap.hprof <PID>

# 使用 MAT 分析堆转储文件
```

### 5. API 响应慢

#### 排查步骤

1. **检查数据库查询**
```sql
-- 查看慢查询
SELECT sql_text, elapsed_time, cpu_time
FROM v$sql
WHERE elapsed_time > 1000
ORDER BY elapsed_time DESC;
```

2. **检查数据库连接池**
```bash
# 使用 actuator 端点
curl http://localhost:8080/api/actuator/metrics/hikaricp
```

3. **分析线程状态**
```bash
# 查看线程堆栈
jstack <PID> > /tmp/jstack.log

# 检查是否有 BLOCKED 线程
grep -A 5 "BLOCKED" /tmp/jstack.log
```

## 日志管理

### 日志位置
- **应用日志**: `/var/log/algo-config/algo-config.log`
- **访问日志**: 通常由 Nginx/Apache 记录
- **错误日志**: 应用日志中包含 ERROR 级别

### 日志轮转

创建 `/etc/logrotate.d/algo-config`:
```
/var/log/algo-config/*.log {
    daily
    rotate 30
    compress
    delaycompress
    notifempty
    create 644 appuser appgroup
    postrotate
        systemctl reload algo-config > /dev/null 2>&1 || true
    endscript
}
```

### 日志级别调整

在 `application.yml` 中调整：
```yaml
logging:
  level:
    root: INFO
    com.company.algo: DEBUG  # 开发环境使用 DEBUG，生产环境使用 INFO
    com.company.algo.mapper: DEBUG
```

## 备份与恢复

### 数据库备份

#### 逻辑备份
```sql
-- 使用 expdp 导出数据
expdp algo_config/password DIRECTORY=/backup DUMPFILE=algo_config_$(date +%Y%m%d).dmp LOGFILE=expdp.log

-- 恢复
impdp algo_config/password DIRECTORY=/backup DUMPFILE=algo_config_20240101.dmp LOGFILE=impdp.log
```

#### RMAN 备份
```bash
# 由 DBA 执行 RMAN 增量备份
rman target sys/password << EOF
BACKUP INCREMENTAL LEVEL 1 DATABASE;
EOF
```

### 配置文件备份

```bash
# 备份配置文件
tar -czf algo-config-config-$(date +%Y%m%d).tar.gz \
    /opt/app/application.yml \
    /opt/app/start.sh \
    /etc/logrotate.d/algo-config

# 恢复
tar -xzf algo-config-config-20240101.tar.gz -C /
```

## 安全加固

### 1. 配置管理员角色

确保所有 API 接口都有管理员权限验证：

```java
// Controller 类上应有 @RequireAdmin 注解
@RequireAdmin
public class AlgoConfigController {
    // ...
}
```

### 2. 启用 HTTPS

使用 Nginx 反向代理并启用 SSL：

```nginx
server {
    listen 443 ssl;
    server_name api.example.com;

    ssl_certificate /etc/nginx/ssl/cert.pem;
    ssl_certificate_key /etc/nginx/ssl/key.pem;

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 3. 数据库密码管理

- 使用强密码（大小写字母、数字、特殊字符）
- 定期更换密码
- 使用 Oracle Wallet 或加密配置存储密码

### 4. 网络安全

- 配置防火墙只允许必要的端口
- 使用 VLAN 隔离应用层和数据层
- 定期更新依赖包版本

## 监控告警

### 建议监控指标

| 指标 | 阈值 | 告警级别 |
|------|------|----------|
| API 响应时间 P95 | > 500ms | Warning |
| 错误率 | > 0.1% | Critical |
| JVM 堆内存使用率 | > 85% | Warning |
| 数据库连接池使用率 | > 90% | Warning |
| 服务可用性 | < 99.9% | Critical |

### 告警通知方式

- 邮件告警（主要）
- 短信告警（紧急）
- 即时通讯工具（企业微信/钉钉）

## 容量规划

### 并发量评估

假设：
- 平均 API 响应时间: 100ms
- 目标 TPS: 1000
- 单机容量估算: TPS = 1000ms / 响应时间 = 1000 / 0.1 = 10000

实际建议：
- 单机可承载: 1000-2000 TPS（考虑业务复杂度）
- 建议集群部署: 2-3 台服务器

### 扩容策略

#### 垂直扩展
增加服务器资源（CPU、内存）

#### 水平扩展
部署多个应用实例，使用负载均衡

```nginx
upstream algo-config {
    server 10.0.0.1:8080 weight=1;
    server 10.0.0.2:8080 weight=1;
    server 10.0.0.3:8080 weight=1 backup;
}

server {
    location /api/ {
        proxy_pass http://algo-config;
    }
}
```

## 联系方式

- **开发团队**: algo-config@example.com
- **运维团队**: ops@example.com
- **值班电话**: +86-xxx-xxxx-xxxx
