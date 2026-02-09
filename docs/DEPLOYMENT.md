# 部署文档

## 环境要求

### 系统要求
- **操作系统**: Linux (CentOS 7+/Ubuntu 18.04+)
- **Java**: JDK 8 或 JDK 11
- **Maven**: 3.6+
- **Oracle**: 12c 或更高版本
- **Nacos**: 2.0+ (服务发现和配置中心)

### 硬件要求
- **CPU**: 2 核心及以上
- **内存**: 4GB 及以上
- **磁盘**: 20GB 及以上

## 数据库初始化

### 1. 创建数据库用户

```sql
-- 使用 sysdba 用户登录
CREATE USER algo_config IDENTIFIED BY "your_password";
GRANT CONNECT, RESOURCE TO algo_config;
GRANT CREATE VIEW, CREATE SEQUENCE, CREATE TRIGGER TO algo_config;
GRANT EXECUTE ON SYS.DBMS_LOB TO algo_config;
ALTER USER algo_config DEFAULT TABLESPACE USERS;
ALTER USER algo_config QUOTA UNLIMITED ON USERS;
COMMIT;
```

### 2. 执行建表脚本

```bash
# 将 SQL 脚本上传到服务器
scp prd/sql/001_algo_config_schema_oracle.sql user@server:/tmp/

# 登录数据库服务器
ssh user@server

# 使用 sqlplus 执行脚本
sqlplus algo_config/your_password@localhost:1521/XE @/tmp/001_algo_config_schema_oracle.sql
```

或使用 SQL Developer 等图形工具直接执行脚本。

### 3. 验证表创建

```sql
-- 检查表是否创建成功
SELECT table_name FROM user_tables WHERE table_name IN (
    'ALGO_CONFIG',
    'IMAGE_CONFIG',
    'IMAGE_ALGO_REL',
    'RESOURCE_CONFIG',
    'OPERATION_LOG'
);

-- 检查序列是否创建成功
SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'SEQ_%';

-- 检查触发器是否创建成功
SELECT trigger_name FROM user_triggers WHERE trigger_name LIKE 'TRG_%';
```

## 配置文件

### 1. application.yml 配置

在部署环境中，需要修改以下配置项：

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@your-oracle-host:1521:your-sid
    username: algo_config
    password: ${DB_PASSWORD}  # 从环境变量读取

  cloud:
    nacos:
      discovery:
        server-addr: your-nacos-server:8848
        namespace: your-namespace
        group: DEFAULT_GROUP
      config:
        server-addr: your-nacos-server:8848
        namespace: your-namespace
        group: DEFAULT_GROUP

file:
  upload:
    nas-path: /nas/algo-configs  # NAS 挂载路径
    base-url: http://your-file-server/api/files
```

### 2. 环境变量配置

创建启动脚本 `start.sh`:

```bash
#!/bin/bash

# Java 配置
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export PATH=$JAVA_HOME/bin:$PATH

# 数据库密码
export DB_PASSWORD=your_secure_password

# Nacos 配置
export NACOS_SERVER_ADDR=nacos.example.com:8848
export NACOS_NAMESPACE=production

# 文件存储路径
export NAS_PATH=/nas/algo-configs
export FILE_BASE_URL=http://files.example.com/api/files

# JVM 参数
JAVA_OPTS="-Xms2g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 启动应用
java $JAVA_OPTS -jar algo-config-1.0.0.jar \
  --spring.profiles.active=production
```

## 部署步骤

### 1. 编译打包

```bash
# 在开发机器上执行
mvn clean package -DskipTests

# 生成的 jar 包位于 target/algo-config-1.0.0.jar
```

### 2. 上传部署包

```bash
# 上传 jar 包到服务器
scp target/algo-config-1.0.0.jar user@server:/opt/app/
scp start.sh user@server:/opt/app/
scp stop.sh user@server:/opt/app/
```

### 3. 创建日志目录

```bash
ssh user@server
mkdir -p /var/log/algo-config
chown appuser:appgroup /var/log/algo-config
```

### 4. 启动应用

```bash
ssh user@server
cd /opt/app
chmod +x start.sh stop.sh

# 启动应用
./start.sh
```

### 5. 验证部署

```bash
# 检查进程
ps aux | grep algo-config

# 查看日志
tail -f /var/log/algo-config/algo-config.log

# 健康检查
curl http://localhost:8080/api/health

# 查看 Swagger 文档
open http://localhost:8080/swagger-ui/
```

## 停止服务

创建 `stop.sh` 脚本:

```bash
#!/bin/bash

PID=$(ps aux | grep algo-config-1.0.0.jar | grep -v grep | awk '{print $2}')

if [ -n "$PID" ]; then
    echo "Stopping algo-config (PID: $PID)..."
    kill $PID
    sleep 5

    # 强制停止
    if ps -p $PID > /dev/null; then
        echo "Force stopping..."
        kill -9 $PID
    fi

    echo "Service stopped."
else
    echo "Service is not running."
fi
```

## 故障排查

### 1. 应用启动失败

**检查点**:
- Java 版本是否正确
- 数据库连接是否正常
- Nacos 服务是否可访问
- 日志文件: `/var/log/algo-config/algo-config.log`

**常见问题**:
```
# 数据库连接失败
ORA-12541: TNS:no listener
→ 检查 Oracle 监听器是否启动: lsnrctl status

# Nacos 连接失败
java.net.ConnectException: Connection refused
→ 检查 Nacos 服务是否启动: curl nacos-server:8848/nacos
```

### 2. API 返回 500 错误

**检查点**:
- 查看应用日志
- 检查数据库连接池状态
- 验证配置文件加载

```bash
# 查看最近的错误日志
grep -i "error" /var/log/algo-config/algo-config.log | tail -50
```

### 3. Swagger 无法访问

**解决方案**:
```yaml
# 在 application.yml 中确认 springfox 配置正确
springfox:
  documentation:
    enabled: true
```

## 回滚

如果新版本出现问题，可以快速回滚到上一版本：

```bash
cd /opt/app

# 停止当前服务
./stop.sh

# 恢复上一版本的 jar 包
mv algo-config-1.0.0.jar algo-config-1.0.0.jar.backup
mv algo-config-1.0.0-previous.jar algo-config-1.0.0.jar

# 启动服务
./start.sh
```

## 监控建议

### 1. 应用监控

- **JVM 监控**: 使用 JConsole 或 VisualVM
- **日志监控**: 使用 ELK Stack 或类似工具
- **APM**: SkyWalking 或 Pinpoint

### 2. 业务监控

- API 响应时间 (P95 < 500ms)
- 错误率 (< 0.1%)
- 数据库连接池使用率

### 3. 告警配置

建议配置以下告警：
- 应用宕机告警
- API 错误率超过阈值
- 数据库连接异常
- 磁盘空间不足
