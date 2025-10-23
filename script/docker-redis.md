以下是 Docker 安装 Redis 并设置账号密码的最佳操作实践，涵盖安全配置、持久化存储、性能优化及生产环境建议：


### **一、环境准备**
- 确保 Docker 环境正常运行（推荐 Docker 20.10+）
- 检查 Docker 状态：`systemctl status docker`（Linux）或 `docker --version`


### **二、最佳安装步骤**

#### 1. 创建持久化目录与配置文件（核心）
Redis 数据默认存储在容器内，需通过目录挂载实现持久化，同时自定义配置增强安全性：
```bash
# 创建数据、配置、日志目录
mkdir -p /data/redis/{data,conf,logs}
# 设置权限（避免容器内权限不足，Redis 容器内用户 ID 为 999）
chown -R 999:999 /data/redis  # 生产环境推荐最小权限，而非 777

# 创建自定义配置文件（关键：设置密码、持久化策略等）
cat > /data/redis/conf/redis.conf << EOF
# 基础配置
bind 0.0.0.0                   # 允许所有IP访问（容器环境下安全，通过宿主机防火墙限制）
protected-mode yes             # 开启保护模式（需密码才能访问）
port 6379                      # 端口
timeout 300                    # 客户端闲置超时时间（秒）

# 安全配置
requirepass YourStrongRedisPwd123!  # 密码（必须复杂）
masterauth YourStrongRedisPwd123!   # 主从复制时的密码（若需主从架构）

# 持久化配置（根据需求选择）
save 900 1                     # 900秒内有1次修改则持久化
save 300 10                    # 300秒内有10次修改则持久化
save 60 10000                  # 60秒内有10000次修改则持久化
dbfilename dump.rdb            # RDB文件名
dir /data                      # 数据存储目录（对应容器内目录）
appendonly yes                 # 开启AOF持久化（双持久化更安全）
appendfilename "appendonly.aof"
appendfsync everysec           # 每秒同步AOF文件（平衡性能与安全性）

# 内存优化
maxmemory 1gb                  # 最大内存限制（根据服务器配置调整）
maxmemory-policy allkeys-lru   # 内存满时淘汰策略（优先删除最近最少使用的键）

# 日志配置
logfile /logs/redis.log        # 日志文件路径
loglevel notice                # 日志级别（notice：生产环境推荐）
EOF
```

**配置说明**：
- 密码设置：`requirepass` 必须包含大小写字母、数字、特殊字符（长度≥12位）
- 持久化：同时开启 RDB（快照）和 AOF（追加日志），避免数据丢失
- 内存限制：根据服务器内存设置 `maxmemory`，防止 Redis 耗尽资源


#### 2. 拉取 Redis 镜像
使用官方镜像，指定稳定版本（避免 `latest` 带来的兼容性问题）：
```bash
docker pull redis:7.2.4  # 推荐 7.x 版本（支持更多安全特性）
```


#### 3. 启动 Redis 容器（生产级配置）
```bash
docker run -d \
  --name redis \
  --restart=always \          # 容器退出后自动重启（生产必备）
  --net=bridge \              # 使用桥接网络（默认，便于端口控制）
  -p 6379:6379 \              # 端口映射（宿主机:容器，如需隐藏端口可修改宿主机端口）
  -v /data/redis/data:/data \ # 数据持久化（RDB/AOF文件存储）
  -v /data/redis/conf/redis.conf:/etc/redis/redis.conf \  # 挂载自定义配置
  -v /data/redis/logs:/logs \ # 日志持久化
  -e TZ=Asia/Shanghai \       # 设置时区（与宿主机一致）
  --memory=1.2g \             # 限制容器最大内存（略大于Redis配置的maxmemory）
  redis:7.2.4 \
  redis-server /etc/redis/redis.conf  # 指定使用自定义配置文件启动
```

**核心参数作用**：
- `--restart=always`：确保 Redis 服务意外停止后自动恢复
- 内存限制：`--memory=1.2g` 配合 Redis 配置的 `maxmemory 1gb`，预留缓冲空间
- 配置挂载：优先使用宿主机的 `redis.conf`，而非容器默认配置


#### 4. 验证容器状态
```bash
# 查看容器是否运行
docker ps | grep redis

# 查看日志（确认启动正常，无配置错误）
docker logs redis

# 测试连接（需输入密码）
docker exec -it redis redis-cli
127.0.0.1:6379> AUTH YourStrongRedisPwd123!  # 输入配置文件中的密码
OK  # 返回OK表示认证成功
127.0.0.1:6379> set test 123
OK
127.0.0.1:6379> get test
"123"
```


### **三、密码管理最佳实践**

#### 1. 避免硬编码密码（生产环境进阶）
若需自动化部署，避免直接在配置文件中写密码，可通过环境变量注入：
```bash
# 1. 先创建环境变量文件
echo "REDIS_PASSWORD=YourStrongRedisPwd123!" > /data/redis/conf/env.txt

# 2. 修改启动命令，通过环境变量替换配置文件中的密码
docker run -d \
  --name redis \
  --restart=always \
  -p 6379:6379 \
  -v /data/redis/data:/data \
  -v /data/redis/conf/redis.conf:/etc/redis/redis.conf.template \  # 模板文件
  -v /data/redis/logs:/logs \
  --env-file /data/redis/conf/env.txt \  # 加载环境变量
  redis:7.2.4 \
  sh -c 'envsubst < /etc/redis/redis.conf.template > /etc/redis/redis.conf && redis-server /etc/redis/redis.conf'
```
（需确保原 `redis.conf` 中密码字段改为 `requirepass ${REDIS_PASSWORD}`）


#### 2. 定期更换密码
1. 进入容器修改密码（临时生效，需同步更新配置文件）：
   ```bash
   docker exec -it redis redis-cli
   127.0.0.1:6379> AUTH 旧密码
   OK
   127.0.0.1:6379> CONFIG SET requirepass 新密码  # 临时生效
   OK
   ```
2. 更新宿主机 `redis.conf` 中的 `requirepass` 和 `masterauth` 为新密码
3. 重启容器：`docker restart redis`（确保配置持久化）


### **四、安全加固措施**

#### 1. 限制网络访问（关键）
Redis 端口（6379）仅允许信任的 IP 访问，通过宿主机防火墙控制：
```bash
# 以 ufw 为例：仅允许 192.168.1.0/24 网段访问
ufw allow from 192.168.1.0/24 to any port 6379
ufw deny 6379  # 拒绝其他所有IP访问
```


#### 2. 禁用危险命令
在 `redis.conf` 中添加以下配置，禁止删除数据、修改配置的危险命令：
```ini
# 重命名危险命令（使其无法使用）
rename-command FLUSHALL ""
rename-command FLUSHDB ""
rename-command CONFIG ""
rename-command KEYS ""  # KEYS命令会阻塞Redis，建议禁用
```
重启容器生效：`docker restart redis`


#### 3. 禁止以 root 用户运行
Redis 容器默认以非 root 用户（ID 999）运行，无需额外配置，避免权限溢出风险。


### **五、日常维护与监控**

#### 1. 数据备份
```bash
# 手动备份RDB文件（AOF文件可直接复制）
cp /data/redis/data/dump.rdb /data/backup/redis_dump_$(date +%Y%m%d).rdb

# 定时备份（添加到crontab）
echo "0 2 * * * cp /data/redis/data/dump.rdb /data/backup/redis_dump_\$(date +\%Y\%m\%d).rdb" | crontab -
```


#### 2. 性能监控
使用 `redis-cli` 查看状态：
```bash
docker exec -it redis redis-cli -a YourStrongRedisPwd123! info
# 关注以下指标：
# - used_memory：已使用内存
# - connected_clients：连接数
# - keyspace_hits/misses：缓存命中率（越高越好）
# - latest_fork_usec：RDB持久化耗时（过长影响性能）
```


#### 3. 版本更新
```bash
# 停止并删除旧容器（数据已持久化，不会丢失）
docker stop redis && docker rm redis
# 拉取新版本镜像
docker pull redis:7.2.5
# 用原启动命令重新创建容器
```


### **六、常见问题解决**
- **连接失败**：检查密码是否正确、防火墙是否放行、容器是否运行（`docker ps`）
- **数据丢失**：确认 `appendonly yes` 已开启，且 `/data` 目录挂载正确
- **内存溢出**：降低 `maxmemory` 配置，或优化 `maxmemory-policy`（如 `volatile-lru` 仅淘汰带过期时间的键）
- **密码不生效**：确保启动时使用了自定义配置文件（`redis-server /etc/redis/redis.conf`）