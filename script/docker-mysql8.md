以下是 Docker 安装 MySQL 8 并设置账号密码的最佳操作实践，涵盖安全配置、持久化存储及最佳实践建议：


### **一、环境准备**
- 确保已安装 Docker 环境（推荐 Docker 20.10+）
- 检查 Docker 状态：`systemctl status docker`（Linux）或 `docker --version`


### **二、最佳安装步骤**

#### 1. 创建持久化目录（重要）
为避免容器删除后数据丢失，建议在宿主机创建数据目录并设置权限：
```bash
# 创建数据存储目录和配置目录
mkdir -p /data/mysql8/{data,conf,logs}
# 设置权限（避免容器内权限不足）
chmod -R 777 /data/mysql8  # 生产环境可根据需求细化权限（如属主为 999:999，对应 MySQL 容器内用户）
```


#### 2. 拉取 MySQL 8 镜像
使用官方镜像，指定版本避免兼容性问题：
```bash
docker pull mysql:8.0.36  # 推荐使用具体小版本，而非 latest
```


#### 3. 启动 MySQL 容器（核心配置）
通过 `docker run` 命令启动，重点配置账号密码、端口映射、持久化及安全参数：
```bash
docker run -d \
  --name mysql8 \
  --restart=always \  # 容器退出后自动重启
  -p 3306:3306 \      # 端口映射（宿主机:容器）
  -v /data/mysql8/data:/var/lib/mysql \  # 数据持久化
  -v /data/mysql8/conf:/etc/mysql/conf.d \  # 配置文件挂载
  -v /data/mysql8/logs:/var/log/mysql \    # 日志持久化
  -e MYSQL_ROOT_PASSWORD="YourStrongRootPwd123!" \  # root 密码（必须复杂）
  -e MYSQL_DATABASE="app_db" \  # 初始化时创建的数据库（可选）
  -e MYSQL_USER="app_user" \    # 初始化时创建的普通用户（可选）
  -e MYSQL_PASSWORD="AppUserPwd456!" \  # 普通用户密码（可选）
  -e TZ="Asia/Shanghai" \  # 设置时区
  mysql:8.0.36 \
  --character-set-server=utf8mb4 \  # 全局字符集（支持 emoji）
  --collation-server=utf8mb4_unicode_ci \
```

**参数说明**：
- `--restart=always`：确保服务稳定运行，适合生产环境
- 字符集设置：`utf8mb4` 支持所有 Unicode 字符（包括 emoji），优于 `utf8`
- 认证插件：若需兼容旧版本客户端（如 MySQL 5.7 及以下），添加 `--default-authentication-plugin=mysql_native_password`，否则使用默认的 `caching_sha2_password`（更安全）


#### 4. 验证容器状态
```bash
# 查看容器是否运行
docker ps | grep mysql8

# 查看日志（排查启动问题）
docker logs mysql8
```


### **三、账号密码管理最佳实践**

#### 1. 初始化后修改 root 密码（推荐）
即使启动时设置了密码，建议进入容器后重新修改，增强安全性：
```bash
# 进入容器
docker exec -it mysql8 bash

# 登录 MySQL（使用启动时的 root 密码）
mysql -u root -p

# 修改 root 密码（MySQL 8 语法）
ALTER USER 'root'@'%' IDENTIFIED BY 'NewStrongerPwd789!';
FLUSH PRIVILEGES;
```


#### 2. 创建专用业务账号（避免直接使用 root）
```sql
-- 创建数据库（若启动时未指定）
CREATE DATABASE IF NOT EXISTS app_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户并限制访问主机（仅允许指定 IP 连接，增强安全）
CREATE USER 'app_user'@'192.168.%.%' IDENTIFIED BY 'AppUserPwd456!';  # 仅允许内网 IP
-- 或允许所有主机（不推荐生产环境）：CREATE USER 'app_user'@'%' ...

-- 授权用户仅访问指定数据库
GRANT SELECT, INSERT, UPDATE, DELETE ON app_db.* TO 'app_user'@'192.168.%.%';
FLUSH PRIVILEGES;
```

**安全原则**：
- 最小权限：仅授予业务所需权限（避免 `ALL PRIVILEGES`）
- 限制访问源：通过 `@'IP段'` 限制用户仅能从指定主机连接


#### 3. 密码复杂度要求
- 长度至少 12 位
- 包含大小写字母、数字、特殊字符（如 `!@#$%`）
- 定期更换密码（可通过脚本或数据库工具实现）


### **四、额外安全配置（推荐）**

#### 1. 配置 MySQL 安全参数
在宿主机 `/data/mysql8/conf` 目录下创建 `my.cnf`，添加以下配置：
```ini
[mysqld]
# 禁用符号链接（防止路径遍历攻击）
symbolic-links=0
# 限制连接数（根据服务器性能调整）
max_connections=1000
# 日志配置（审计和排障）
slow_query_log=1
slow_query_log_file=/var/log/mysql/slow.log
long_query_time=2  # 慢查询阈值（秒）
# 禁用不必要的功能
skip_name_resolve=1  # 禁用 DNS 解析（加快连接速度，避免解析问题）
```
重启容器生效：`docker restart mysql8`


#### 2. 防火墙限制
仅允许必要的 IP 访问宿主机 3306 端口（以 Linux ufw 为例）：
```bash
# 仅允许 192.168.1.0/24 网段访问 3306
ufw allow from 192.168.1.0/24 to any port 3306
# 禁止其他 IP 访问
ufw deny 3306
```


### **五、日常维护**
1. **备份数据**：
   ```bash
   # 定时备份数据库到宿主机（示例：每天凌晨 2 点备份 app_db）
   docker exec mysql8 mysqldump -u root -p'NewStrongerPwd789!' --databases app_db > /data/backup/mysql_$(date +%Y%m%d).sql
   ```

2. **更新镜像**：
   ```bash
   # 停止并删除旧容器
   docker stop mysql8 && docker rm mysql8
   # 拉取新版本镜像
   docker pull mysql:8.0.37
   # 用原命令重新启动（依赖持久化目录，数据不丢失）
   ```


### **六、常见问题**
- **连接失败**：检查容器是否运行、端口映射是否正确、防火墙规则、密码是否正确
- **字符集问题**：确认启动参数或配置文件中已设置 `utf8mb4`
- **认证插件错误**：若客户端提示 `caching_sha2_password` 不支持，重新创建用户时指定插件：
  ```sql
  CREATE USER 'app_user'@'%' IDENTIFIED WITH mysql_native_password BY '密码';
  ```