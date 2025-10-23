以下是 Docker 安装 RocketMQ 4.9.1 并配置账号密码的最佳操作实践，涵盖命名服务器（Namesrv）、 broker 部署、权限控制及持久化配置，适合生产环境参考：


### **一、环境准备**
- 确保 Docker 环境正常运行（推荐 Docker 20.10+）
- 建议服务器配置：2核4G以上（RocketMQ 对内存有一定要求）
- 检查端口可用性：Namesrv 默认 9876，Broker 默认 10911（主）、10909（从）、10912（控制台）


### **二、核心概念说明**
- **Namesrv**：命名服务器，负责路由管理（轻量级，无状态）
- **Broker**：消息服务器，负责存储和转发消息（需持久化数据）
- **权限控制**：RocketMQ 4.9.x 通过 `plain_acl.yml` 配置账号密码及权限策略


### **三、安装步骤**

#### 1. 创建目录结构（持久化与配置）
```bash
# 创建主目录
mkdir -p /data/rocketmq/4.9.1/{namesrv,broker/{conf,data,logs}}
# 设置权限（避免容器内权限问题）
chmod -R 777 /data/rocketmq  # 生产环境可细化为 RocketMQ 容器用户（默认 root，可自定义）
```


#### 2. 拉取官方镜像
RocketMQ 官方提供镜像，指定 4.9.1 版本：
```bash
docker pull apache/rocketmq:4.9.1
```


#### 3. 配置权限控制（账号密码核心）
RocketMQ 通过 `plain_acl.yml` 实现权限管理，需挂载到 broker 配置目录：

```bash
# 创建 ACL 配置文件（定义账号、密码及权限）
cat > /data/rocketmq/4.9.1/broker/conf/plain_acl.yml << EOF
globalWhiteRemoteAddresses:
- 127.0.0.1  # 允许本地地址无需认证（可删除，强制所有连接认证）

accounts:
- accessKey: rocketmqAdmin  # 管理员账号
  secretKey: Admin@123456!  # 管理员密码（复杂密码）
  whiteRemoteAddress:        # 允许的来源地址（留空表示所有）
  admin: true                # 管理员权限（可操作所有资源）

- accessKey: appUser        # 普通业务账号
  secretKey: User@67890!    # 普通用户密码
  whiteRemoteAddress:
  admin: false
  defaultTopicPerm: DENY    # 默认主题权限（拒绝）
  defaultGroupPerm: SUB     # 默认消费组权限（订阅）
  topicPerms:               # 具体主题权限（格式：topicName=权限，权限包括 DENY/PUB/SUB/PUB_SUB）
  - TopicTest=PUB_SUB
  groupPerms:               # 具体消费组权限（格式：groupName=权限，权限包括 DENY/SUB）
  - GroupTest=SUB
EOF
```

**权限说明**：
- `accessKey`/`secretKey`：客户端连接时需使用的账号密码
- `admin: true`：拥有所有资源的操作权限（谨慎分配）
- `topicPerms`/`groupPerms`：精细化控制指定主题/消费组的权限（遵循最小权限原则）


#### 4. 配置 Broker 核心参数
创建 broker 配置文件，启用 ACL 权限控制：
```bash
cat > /data/rocketmq/4.9.1/broker/conf/broker.conf << EOF
# 集群名称（单节点可自定义）
brokerClusterName = DefaultCluster
# Broker 名称（单节点用 DEFAULT）
brokerName = broker-a
# Broker ID（0 为主节点，>0 为从节点）
brokerId = 0
# 外网访问地址（替换为宿主机 IP，客户端需用此 IP 连接）
brokerIP1 = 192.168.1.100  # 必须修改为实际宿主机 IP！
# 存储路径（对应容器内路径，已通过挂载持久化）
storePathRootDir = /root/store/data
storePathCommitLog = /root/store/data/commitlog
# 权限控制开关（必须开启）
aclEnable = true
# 自动创建主题/消费组（生产环境建议关闭，由管理员提前创建）
autoCreateTopicEnable = false
autoCreateSubscriptionGroup = false
EOF
```

**关键配置**：
- `brokerIP1`：必须设置为宿主机可访问的 IP，否则客户端无法连接
- `aclEnable = true`：启用 ACL 权限控制（依赖 `plain_acl.yml`）


#### 5. 启动 Namesrv（命名服务器）
```bash
docker run -d \
  --name rmqnamesrv \
  --restart=always \
  -p 9876:9876 \
  -v /data/rocketmq/4.9.1/namesrv/logs:/root/logs \
  -e MAX_HEAP_SIZE=512m \  # 最大堆内存（根据服务器配置调整）
  -e HEAP_NEWSIZE=128m \    # 新生代内存
  -e TZ=Asia/Shanghai \
  apache/rocketmq:4.9.1 \
  sh mqnamesrv
```


#### 6. 启动 Broker（消息服务器）
```bash
docker run -d \
  --name rmqbroker \
  --restart=always \
  --link rmqnamesrv:namesrv \  # 关联 namesrv 容器，内部可通过 namesrv 访问
  -p 10911:10911 \  # 主服务端口
  -p 10909:10909 \  # 从服务端口（单节点也需映射）
  -p 10912:10912 \  # 控制台端口
  -v /data/rocketmq/4.9.1/broker/conf:/opt/rocketmq-4.9.1/conf \  # 挂载配置（含 ACL）
  -v /data/rocketmq/4.9.1/broker/data:/root/store/data \          # 数据持久化
  -v /data/rocketmq/4.9.1/broker/logs:/root/logs \                # 日志持久化
  -e NAMESRV_ADDR=namesrv:9876 \  # 指向 namesrv 地址
  -e MAX_HEAP_SIZE=1g \           # Broker 内存（建议 1g+）
  -e HEAP_NEWSIZE=256m \
  -e TZ=Asia/Shanghai \
  apache/rocketmq:4.9.1 \
  sh mqbroker -c /opt/rocketmq-4.9.1/conf/broker.conf  # 指定自定义配置
```

#### 7. 启动控制台（可选）
RocketMQ 4.9.x 默认不提供控制台，需手动拉取镜像并启动：
```bash 
 docker pull apacherocketmq/rocketmq-console:2.0.0
```

```bash 
docker run -e "JAVA_OPTS=-Drocketmq.namesrv.addr=127.0.0.1:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8080:8080 -t apacherocketmq/rocketmq-console:2.0.0
```


### **四、验证部署与权限**

#### 1. 检查容器状态
```bash
docker ps | grep rmq  # 确保 rmqnamesrv 和 rmqbroker 均为 Up 状态
docker logs rmqbroker  # 查看 broker 日志，确认无 ERROR 信息
```


#### 2. 测试权限控制（通过容器内工具）
```bash
# 进入 broker 容器
docker exec -it rmqbroker bash

# 尝试无密码发送消息（应失败）
sh bin/tools.sh org.apache.rocketmq.example.quickstart.Producer
# 预期结果：抛出异常（No accessKey found）

# 使用管理员账号发送消息（成功）
sh bin/tools.sh org.apache.rocketmq.example.quickstart.Producer -n namesrv:9876 -k rocketmqAdmin -s Admin@123456!
# 预期结果：发送成功（SendResult [sendStatus=SEND_OK]）

# 使用普通账号消费消息（成功）
sh bin/tools.sh org.apache.rocketmq.example.quickstart.Consumer -n namesrv:9876 -k appUser -s User@67890!
# 预期结果：消费到消息
```


### **五、客户端连接配置（关键）**
客户端需指定账号密码才能连接，以 Java 客户端为例：
```java
// 生产者配置
DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup", new AclClientRPCHook(
  new SessionCredentials("appUser", "User@67890!")  // 普通账号
));
producer.setNamesrvAddr("192.168.1.100:9876");  // 宿主机 IP:9876
producer.start();

// 消费者配置
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerGroup", new AclClientRPCHook(
  new SessionCredentials("appUser", "User@67890!")
), new AllocateMessageQueueAveragely());
consumer.setNamesrvAddr("192.168.1.100:9876");
```


### **六、安全加固与最佳实践**

#### 1. 账号密码管理
- 定期更换 `plain_acl.yml` 中的密码（修改后需重启 broker：`docker restart rmqbroker`）
- 避免使用默认账号（如 `rocketmqAdmin` 可改为业务相关名称）
- 密码复杂度要求：长度≥12位，包含大小写字母、数字、特殊字符


#### 2. 网络安全
- 仅开放必要端口（9876、10911）给信任的客户端 IP（通过防火墙限制）：
  ```bash
  # ufw 示例：仅允许 192.168.1.0/24 网段访问
  ufw allow from 192.168.1.0/24 to any port 9876
  ufw allow from 192.168.1.0/24 to any port 10911
  ```
- 生产环境建议部署在私有网络，避免直接暴露公网


#### 3. 持久化与备份
- 定期备份 `/data/rocketmq/4.9.1/broker/data` 目录（消息存储数据）
- 启用 broker 配置中的 `deleteWhen` 和 `fileReservedTime` 控制日志保留时间（避免磁盘占满）


#### 4. 性能优化
- 根据业务量调整 JVM 内存（`MAX_HEAP_SIZE` 和 `HEAP_NEWSIZE`）
- 开启 broker 配置中的 `transientStorePoolEnable=true`（利用堆外内存提升性能，需服务器内存充足）


### **七、常见问题解决**
- **客户端连接失败**：检查 `brokerIP1` 是否为宿主机 IP、端口是否开放、账号密码是否正确
- **权限异常**：查看 broker 日志（`/data/rocketmq/4.9.1/broker/logs/broker.log`），确认 `plain_acl.yml` 配置是否正确
- **内存溢出**：降低 JVM 内存配置，或升级服务器配置
- **数据丢失**：确保 `broker.conf` 中持久化参数正确，且数据目录挂载正常