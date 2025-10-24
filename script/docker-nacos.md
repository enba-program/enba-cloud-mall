以下是 Docker 安装 Nacos 2.0.2 并配置账号密码的最佳操作实践，涵盖安全加固、数据持久化、集群兼容及生产环境配置要点：


### **一、环境准备**
1. **系统要求**：
    - 推荐服务器配置：2核4G以上（Nacos 作为注册中心/配置中心，需保证稳定性）
    - Docker 环境：确保 Docker 20.10+ 正常运行（`docker --version` 验证）
2. **端口预留**：
    - 8848：Nacos 主端口（HTTP 访问）
    - 9848：客户端 gRPC 端口（2.0+ 新增，必须开放）
    - 9849：服务端 gRPC 端口（集群通信，单节点可选）


### **二、核心配置说明**
- **认证机制**：Nacos 2.0.x 支持内置认证，通过配置开启账号密码登录
- **数据存储**：默认使用嵌入式数据库（Derby），生产环境建议切换为 MySQL 实现数据持久化
- **安全加固**：除基础认证外，需限制访问来源并配置敏感信息加密


### **三、安装步骤（MySQL 持久化方案，推荐生产使用）**

#### 1. 准备 MySQL 环境（前提）
Nacos 需依赖 MySQL 存储配置和注册信息，先创建数据库及初始化表：
```sql
-- 1. 创建数据库
CREATE DATABASE nacos_config CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 初始化表结构（使用官方脚本）
-- 脚本地址：https://github.com/alibaba/nacos/blob/2.0.2/distribution/conf/nacos-mysql.sql
-- 下载后执行脚本，创建必要的表（如 config_info、service_info 等）
```


#### 2. 创建 Nacos 目录结构与配置文件
```bash
# 创建数据、配置、日志目录
mkdir -p /data/nacos/2.0.2/{conf,logs,data}
# 设置权限（避免容器内权限问题）
chmod -R 777 /data/nacos/2.0.2  # 生产环境可细化为容器用户（默认 root）
```

创建 Nacos 核心配置文件 `application.properties`，开启认证并配置 MySQL 连接：
```bash
cat > /data/nacos/2.0.2/conf/application.properties << EOF
# 服务器端口
server.port=8848

# 数据源配置（MySQL）
spring.datasource.platform=mysql
db.num=1
db.url.0=jdbc:mysql://192.168.1.100:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root  # MySQL 用户名
db.password.0=YourMySQLPwd123!  # MySQL 密码

# 开启认证（核心：启用账号密码登录）
nacos.core.auth.enabled=true
# 自定义密钥（必须修改，增强安全性，建议生成随机字符串）
nacos.core.auth.server.identity.key=ServerIdentity
nacos.core.auth.server.identity.value=YourServerSecretKey!
# 令牌密钥（用于生成登录令牌，必须修改，长度建议≥32位）
nacos.core.auth.plugin.nacos.token.secret.key=YourTokenSecretKey123456789012345678901234567890
# 关闭匿名访问（强制认证）
nacos.core.auth.anonymous.enabled=false

# 日志配置
logging.config=classpath:nacos-logback.xml
logging.path=/home/nacos/logs
EOF
```

**关键配置说明**：
- `nacos.core.auth.enabled=true`：开启登录认证（默认关闭，必须开启）
- `db.url.0`：替换为实际 MySQL 地址（若 MySQL 在容器内，用容器 IP 或桥接网络别名）
- 密钥配置（`server.identity.value` 和 `token.secret.key`）：必须修改默认值，建议使用随机复杂字符串（如通过 `openssl rand -hex 32` 生成）


#### 3. 拉取 Nacos 2.0.2 镜像
```bash
docker pull nacos/nacos-server:2.0.2
```


#### 4. 启动 Nacos 容器（生产级配置）
```bash
docker run -d \
  --name nacos2 \
  --restart=always \  # 容器退出后自动重启
  -p 8848:8848 \      # 主端口
  -p 9848:9848 \      # 客户端 gRPC 端口（2.0+ 必须映射，否则客户端连接失败）
  -p 9849:9849 \      # 服务端 gRPC 端口（集群用，单节点可选）
  -v /data/nacos/2.0.2/conf/application.properties:/home/nacos/conf/application.properties \
  -v /data/nacos/2.0.2/logs:/home/nacos/logs \
  -v /data/nacos/2.0.2/data:/home/nacos/data \
  -e MODE=standalone \  # 单节点模式（集群模式需修改为 cluster 并配置集群地址）
  -e JVM_XMS=512m \     # JVM 初始内存
  -e JVM_XMX=1g \       # JVM 最大内存（根据服务器配置调整）
  -e TZ=Asia/Shanghai \
  nacos/nacos-server:2.0.2
```


#### 5. 验证容器启动状态
```bash
# 查看容器是否运行
docker ps | grep nacos2

# 查看日志（确认启动成功，无 ERROR 信息）
docker logs -f nacos2
# 成功标志：Nacos started successfully in standalone mode.
```


### **四、账号密码管理（核心安全配置）**

#### 1. 初始账号与修改密码
- **默认账号**：Nacos 初始管理员账号为 `nacos`，密码为 `nacos`（必须修改！）
- **修改默认密码**：
    1. 访问 Nacos 控制台：`http://宿主机IP:8848/nacos`
    2. 使用 `nacos/nacos` 登录
    3. 进入「控制台 → 权限控制 → 用户列表」
    4. 点击「编辑」修改 `nacos` 用户的密码（建议复杂度：大小写+数字+特殊字符，长度≥12位）


#### 2. 创建业务专用账号（遵循最小权限原则）
1. 登录 Nacos 控制台，进入「权限控制 → 用户列表 → 新建用户」
2. 填写用户名、密码（如 `app_user`/`AppUser@123!`）
3. 进入「角色列表 → 新建角色」（如 `app_role`）
4. 关联用户与角色：「用户列表 → 编辑角色」，将 `app_user` 关联到 `app_role`
5. 分配权限：「权限管理」中为 `app_role` 分配指定命名空间的权限（如仅允许读写 `prod` 命名空间）


#### 3. 密码重置（若遗忘密码）
若管理员密码遗忘，可通过 MySQL 直接修改数据库：
```sql
-- 登录 MySQL，切换到 nacos_config 库
USE nacos_config;

-- 更新 admin 角色用户的密码（以默认用户 nacos 为例）
-- 密码需用 BCrypt 加密（可通过在线工具生成，如 https://bcrypt-generator.com/）
UPDATE users SET password='$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx' WHERE username='nacos';
```
- 注意：Nacos 密码存储使用 BCrypt 加密，不可直接存储明文。


### **五、安全加固与最佳实践**

#### 1. 网络访问控制
仅允许信任的 IP 访问 Nacos 端口（8848、9848）：
```bash
# ufw 防火墙示例（仅允许 192.168.1.0/24 网段）
ufw allow from 192.168.1.0/24 to any port 8848
ufw allow from 192.168.1.0/24 to any port 9848
ufw deny 8848
ufw deny 9848
```


#### 2. 敏感配置加密
Nacos 支持对配置中心的敏感信息（如数据库密码、API 密钥）加密存储：
1. 在 `application.properties` 中添加加密配置：
   ```properties
   # 启用加密
   nacos.cmdb.encryption.data.key=YourEncryptionKey  # 加密密钥（自定义）
   ```
2. 控制台配置敏感信息时，使用 `${cipher:加密后的内容}` 格式存储（需先通过 Nacos 加密工具生成密文）。


#### 3. 定期备份数据
定期备份 MySQL 中 `nacos_config` 数据库（核心数据存储）：
```bash
# 示例：每日凌晨 2 点备份
mysqldump -u root -p'YourMySQLPwd123!' nacos_config > /data/backup/nacos_config_$(date +%Y%m%d).sql
```


#### 4. 集群部署注意事项（扩展）
若需部署集群，需：
- 修改 `MODE=cluster`
- 配置 `nacos.server.ip` 和集群节点列表（`conf/cluster.conf`）
- 确保所有节点的 `token.secret.key` 和认证配置一致
- 增加 JVM 内存（如 `-e JVM_XMS=2g -e JVM_XMX=4g`）


### **六、常见问题解决**
1. **客户端连接失败（报 403 或认证错误）**：
    - 检查客户端是否配置了账号密码（如 Spring Cloud 项目需在 `application.yml` 中添加 `username` 和 `password`）
    - 确认 `9848` 端口已开放（2.0+ 客户端通过 gRPC 连接此端口）

2. **启动失败（数据库连接错误）**：
    - 检查 `application.properties` 中 MySQL 地址、用户名、密码是否正确
    - 确认 MySQL 已创建 `nacos_config` 数据库并执行初始化脚本

3. **控制台登录提示「用户名或密码错误」**：
    - 检查是否已修改默认密码，或通过 MySQL 重置密码
    - 确认 `nacos.core.auth.enabled=true` 已开启（未开启时无需登录，存在安全风险）