以下是 Docker 安装 Elasticsearch 7.17.10 并配置账号密码的最佳操作实践，涵盖安全加固、数据持久化、性能优化及生产环境配置要点：


### **一、环境准备**
1. **系统要求**：
    - 推荐服务器配置：2核4G以上（Elasticsearch 对内存敏感）
    - 调整系统参数（避免启动失败）：
      ```bash
      # 临时调整虚拟内存（生产环境建议写入 /etc/sysctl.conf 持久化）
      sudo sysctl -w vm.max_map_count=262144
      # 确认修改生效
      sysctl -a | grep vm.max_map_count
      ```
2. **Docker 环境**：确保 Docker 20.10+ 正常运行，检查命令：`docker --version`


### **二、核心配置说明**
- **安全特性**：Elasticsearch 7.17.x 内置 X-Pack 安全模块，可通过配置启用账号密码认证
- **数据持久化**：需挂载数据目录，避免容器删除后数据丢失
- **集群模式**：单节点部署（适合测试/小型生产），后续可扩展为集群


### **三、安装步骤**

#### 1. 创建目录结构与权限配置
```bash
# 创建数据、配置、日志目录
mkdir -p /data/elasticsearch/7.17.10/{data,config,logs,plugins}
# 设置权限（Elasticsearch 容器内用户 ID 为 1000，需确保目录可写）
chown -R 1000:1000 /data/elasticsearch/7.17.10
chmod -R 755 /data/elasticsearch/7.17.10  # 生产环境最小权限原则
```


#### 2. 拉取官方镜像
指定 7.17.10 版本（避免 `latest` 版本兼容性问题）：
```bash
docker pull elasticsearch:7.17.10
```


#### 3. 配置 Elasticsearch 核心参数（启用安全认证）
创建自定义配置文件 `elasticsearch.yml`，启用密码认证并优化基础配置：
```bash
cat > /data/elasticsearch/7.17.10/config/elasticsearch.yml << EOF
# 集群名称（单节点可自定义）
cluster.name: es-cluster
# 节点名称
node.name: es-node-1
# 数据存储路径（对应容器内路径）
path.data: /usr/share/elasticsearch/data
# 日志路径
path.logs: /usr/share/elasticsearch/logs
# 绑定地址（容器环境下允许所有 IP 访问）
network.host: 0.0.0.0
# 初始主节点（单节点为当前节点）
# cluster.initial_master_nodes: ["es-node-1"]
# 允许跨域（如需 Kibana 或前端访问）
http.cors.enabled: true
http.cors.allow-origin: "*"
http.cors.allow-headers: Authorization,Content-Type

# 安全配置（核心）
xpack.security.enabled: true  # 启用安全模块
xpack.security.transport.ssl.enabled: true  # 启用传输层 SSL（集群内通信加密）
EOF
```

**安全配置说明**：
- `xpack.security.enabled: true`：开启账号密码认证（默认关闭）
- `xpack.security.transport.ssl.enabled: true`：加密节点间通信（单节点也建议开启，为集群扩展做准备）


#### 4. 启动 Elasticsearch 容器
```bash
docker run -d \
  --name es7 \
  --restart=always \  # 容器退出后自动重启
  -p 9200:9200 \      # HTTP 端口（客户端访问）
  -p 9300:9300 \      # 传输层端口（集群内通信）
  -v /data/elasticsearch/7.17.10/data:/usr/share/elasticsearch/data \
  -v /data/elasticsearch/7.17.10/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
  -v /data/elasticsearch/7.17.10/logs:/usr/share/elasticsearch/logs \
  -v /data/elasticsearch/7.17.10/plugins:/usr/share/elasticsearch/plugins \
  -e "ES_JAVA_OPTS=-Xms2g -Xmx2g" \  # JVM 内存（建议为服务器内存的 50%，且不超过 31G）
  -e "discovery.type=single-node" \  # 禁用GeoIP下载
  -e "ingest.geoip.downloader.enabled=false" \  # 禁用GeoIP下载
  -e TZ=Asia/Shanghai \
  elasticsearch:7.17.10
```

**关键参数**：
- `ES_JAVA_OPTS`：设置 JVM 堆内存，`-Xms` 和 `-Xmx` 建议保持一致（避免内存波动），生产环境至少 2G
- 端口映射：9200 用于 HTTP 访问（如 Kibana、API 调用），9300 用于节点间通信


#### 5. 验证容器启动状态
```bash
# 查看容器是否运行
docker ps | grep es7

# 查看日志（确认无 ERROR 信息，启动成功会显示 "started"）
docker logs -f es7
```


### **四、设置账号密码（核心安全步骤）**
Elasticsearch 启动后，需通过内置工具设置内置用户的密码（如 `elastic`、`kibana` 等）：

#### 1. 进入容器执行密码设置命令
```bash
# 进入容器
docker exec -it es7 bash

# 运行密码设置工具（交互式设置密码）
bin/elasticsearch-setup-passwords interactive
```

#### 2. 按提示设置密码（重要）
工具会列出需要设置密码的内置用户，包括：
- `elastic`：超级管理员（拥有所有权限，必须设置强密码）
- `kibana`：Kibana 连接 ES 专用（若后续安装 Kibana 需使用）
- `logstash_system`：Logstash 连接专用
- `beats_system`：Beats 连接专用
- `apm_system`：APM 连接专用

**密码要求**：
- 长度至少 8 位
- 包含大小写字母、数字、特殊字符（如 `Elastic@123!`）
- 不同用户使用不同密码，避免复用


#### 3. 验证密码是否生效
```bash
# 宿主机执行，测试 elastic 用户认证
curl -u elastic:你的elastic密码 https://localhost:9200 -k
# 成功返回：包含集群名称、版本等信息的 JSON（注意：7.17 默认开启 HTTPS，需加 -k 忽略证书验证）
```


### **五、安全加固与最佳实践**

#### 1. 证书配置（替代 -k 忽略验证，生产必备）
默认情况下，Elasticsearch 会生成自签名证书，但客户端访问需忽略证书验证（`-k`）。生产环境建议配置自定义证书：

1. 在容器内生成证书（或使用已有证书）：
   ```bash
   docker exec -it es7 bash
   # 生成证书到 config 目录
   bin/elasticsearch-certutil cert -out config/elastic-certificates.p12 -pass ""
   # 授权证书文件
   chmod 644 config/elastic-certificates.p12
   ```

2. 修改 `elasticsearch.yml` 增加证书配置：
   ```yaml
   xpack.security.transport.ssl.keystore.path: elastic-certificates.p12
   xpack.security.transport.ssl.truststore.path: elastic-certificates.p12
   xpack.security.http.ssl.enabled: true  # 开启 HTTP 层 SSL（可选，强化传输安全）
   xpack.security.http.ssl.keystore.path: elastic-certificates.p12
   xpack.security.http.ssl.truststore.path: elastic-certificates.p12
   ```

3. 重启容器：`docker restart es7`


#### 2. 创建业务专用账号（避免直接使用 elastic）
使用 `elastic` 超级管理员创建低权限业务账号，遵循最小权限原则：
```bash
# 登录 ES（输入 elastic 密码）
curl -u elastic:密码 -XPOST "https://localhost:9200/_security/user/app_user" -k -H "Content-Type: application/json" -d '
{
  "password" : "AppUser@456!",
  "roles" : ["editor"],  # 预定义角色（editor 拥有索引读写权限）
  "full_name" : "Application User"
}
'
```
- 角色说明：`editor`（索引读写）、`viewer`（只读）、`superuser`（等价于 elastic）
- 可自定义角色：通过 Kibana 或 API 创建精细化权限角色


#### 3. 网络访问控制
仅允许信任的 IP 访问 9200 端口（如应用服务器、Kibana 服务器）：
```bash
# ufw 防火墙示例（仅允许 192.168.1.0/24 网段）
ufw allow from 192.168.1.0/24 to any port 9200
ufw deny 9200  # 拒绝其他所有 IP
```


#### 4. 数据备份
定期备份 ES 数据目录（`/data/elasticsearch/7.17.10/data`），或使用快照 API：
```bash
# 创建快照仓库（需先在 es.yml 配置 path.repo）
curl -u elastic:密码 -XPUT "https://localhost:9200/_snapshot/my_backup" -k -H "Content-Type: application/json" -d '
{
  "type": "fs",
  "settings": {
    "location": "/usr/share/elasticsearch/backup"
  }
}
'
# 执行快照
curl -u elastic:密码 -XPUT "https://localhost:9200/_snapshot/my_backup/snapshot_1" -k
```


### **六、性能优化建议**
1. **JVM 内存**：`-Xms` 和 `-Xmx` 设为服务器物理内存的 50%（最大不超过 31G，因 JVM 对大内存处理效率下降）
2. **索引优化**：
    - 合理设置分片数（单节点建议 1-3 个分片）
    - 配置索引生命周期管理（ILM），自动删除过期数据
3. **磁盘选择**：使用 SSD 存储提升读写性能（ES 对磁盘 IO 敏感）


### **七、常见问题解决**
1. **启动失败（max virtual memory areas 错误）**：执行 `sysctl -w vm.max_map_count=262144` 并写入 `/etc/sysctl.conf` 持久化
2. **权限不足**：确保宿主机目录属主为 1000:1000（`chown -R 1000:1000 /data/elasticsearch`）
3. **密码遗忘**：删除数据目录下的 `.security` 索引文件，重启后重新执行 `elasticsearch-setup-passwords`（会丢失所有用户配置，谨慎操作）
4. **HTTPS 访问警告**：配置自定义证书并在客户端信任证书，替代 `-k` 参数