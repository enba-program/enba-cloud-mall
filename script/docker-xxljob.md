以下是 Docker 安装 XXL-Job（分布式任务调度平台）的最佳操作实践，涵盖 admin 调度中心和 executor 执行器的部署、数据库配置、安全加固及生产环境优化：


### **一、环境准备**
1. **依赖组件**：
    - MySQL 5.7+（XXL-Job 需存储任务配置等数据，推荐独立部署）
    - Docker 20.10+ 环境
2. **端口预留**：
    - 9000：XXL-Job Admin 调度中心端口
    - 9999：XXL-Job Executor 执行器端口（可自定义）


### **二、核心组件说明**
- **XXL-Job Admin**：调度中心，负责任务管理、调度触发、监控等（单节点部署，生产可集群）
- **XXL-Job Executor**：执行器，负责接收调度指令并执行任务（可多实例部署，与业务服务集成）


### **三、安装步骤**

#### 1. 初始化 MySQL 数据库
XXL-Job 依赖数据库存储元数据，需提前创建并初始化表结构：

```sql
-- 1. 创建数据库
CREATE DATABASE xxl_job CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 初始化表结构（使用官方脚本）
-- 脚本地址：https://github.com/xuxueli/xxl-job/blob/2.4.0/doc/db/tables_xxl_job.sql
-- 下载后执行，创建 8 张表（如 xxl_job_info、xxl_job_log 等）
```


#### 2. 部署 XXL-Job Admin（调度中心）


##### 2.1 拉取 Admin 镜像
XXL-Job 官方提供镜像，指定稳定版本（推荐 2.4.0，兼容性较好）：
```bash
docker pull xuxueli/xxl-job-admin:2.4.0
```


##### 2.2 启动 Admin 容器
```bash
docker run -e PARAMS="--spring.datasource.username=root --spring.datasource.password=123456  --spring.datasource.url=jdbc:mysql://172.17.0.2:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai" \
-p 8080:8080 \
-v /data/xxl-job/admin/logs:/data/applogs \
--name xxl-job-admin \
-d xuxueli/xxl-job-admin:2.4.0
```


#### 3. 部署 XXL-Job Executor（执行器，可选独立部署）
执行器可独立部署，或集成到业务服务中。以下是独立部署示例：

##### 3.1 创建 Executor 配置文件
```bash
# 创建 Executor 配置目录
mkdir -p /data/xxl-job/executor/conf

# 编写 application.properties 配置文件
cat > /data/xxl-job/executor/conf/application.properties << EOF
# 服务端口（执行器端口）
server.port=9999

# 日志配置
logging.config=classpath:logback.xml
logging.path=/data/logs/xxl-job/executor

# 执行器通讯TOKEN（需与 Admin 的 accessToken 一致）
xxl.job.accessToken=YourXXLJobToken!2024

# 执行器注册中心配置（指向 Admin 地址）
xxl.job.admin.addresses=http://192.168.1.100:9000/xxl-job-admin

# 执行器 AppName（唯一标识，Admin 控制台需对应）
xxl.job.executor.appname=xxl-job-executor-sample
# 执行器 IP（默认为空，自动获取，多网卡时可手动指定）
xxl.job.executor.ip=
# 执行器端口（默认 9999，与 server.port 一致）
xxl.job.executor.port=9999
# 执行器日志路径（与挂载目录一致）
xxl.job.executor.logpath=/data/logs/xxl-job/executor
# 执行器日志保留天数
xxl.job.executor.logretentiondays=30
EOF
```

**关键配置**：
- `xxl.job.admin.addresses`：必须指向 Admin 实际访问地址（宿主机 IP:9000）
- `xxl.job.accessToken`：需与 Admin 配置完全一致，否则无法通讯


##### 3.2 拉取 Executor 镜像
```bash
docker pull xuxueli/xxl-job-executor-sample:2.4.0
```


##### 3.3 启动 Executor 容器
```bash
docker run -d \
  --name xxl-job-executor \
  --restart=always \
  -p 9999:9999 \
  -v /data/xxl-job/executor/conf/application.properties:/xxl-job-executor-sample/application.properties \
  -v /data/xxl-job/executor/logs:/data/logs/xxl-job/executor \
  -e TZ=Asia/Shanghai \
  xuxueli/xxl-job-executor-sample:2.4.0
```


### **四、验证部署**

#### 1. 访问 Admin 控制台
- 地址：`http://宿主机IP:9000/xxl-job-admin`
- 默认账号密码：`admin/123456`（首次登录需修改密码！）
- 登录后进入「执行器管理」，若能看到 `xxl-job-executor-sample` 且状态为「在线」，说明 Executor 注册成功。


#### 2. 修改 Admin 默认密码（安全必备）
1. 登录控制台后，点击右上角「admin」→「个人中心」
2. 输入旧密码 `123456`，设置新密码（建议复杂度：大小写+数字+特殊字符，长度≥10位）


#### 3. 测试任务调度
1. 进入「任务管理」→「新增」，配置一个测试任务：
    - 执行器：选择 `xxl-job-executor-sample`
    - 任务描述：测试任务
    - 调度类型：CRON
    - CRON 表达式：`0/10 * * * * ?`（每10秒执行一次）
    - 执行器路由策略：第一个
    - 执行器任务Handler：`demoJobHandler`（示例任务，已内置）
2. 启动任务，查看「调度日志」，若显示「执行成功」，则部署正常。


### **五、安全加固与最佳实践**

#### 1. 通讯安全
- **强制令牌验证**：确保 `xxl.job.accessToken` 非空且复杂度高（如通过 `openssl rand -hex 16` 生成）
- **网络隔离**：仅允许 Executor 所在服务器访问 Admin 的 9000 端口，通过防火墙限制：
  ```bash
  # 仅允许 192.168.1.0/24 网段访问 Admin 端口
  ufw allow from 192.168.1.0/24 to any port 9000
  ```


#### 2. 数据安全
- **定期备份 MySQL 数据库**：`xxl_job` 库存储任务配置和日志，需定时备份：
  ```bash
  mysqldump -u root -p'YourMySQLPwd123!' xxl_job > /data/backup/xxl_job_$(date +%Y%m%d).sql
  ```
- **清理日志**：通过 `xxl.job.executor.logretentiondays` 限制日志保留天数，或定期清理 `xxl_job_log` 表历史数据。


#### 3. 性能优化
- **Admin 集群部署**：高并发场景下，Admin 可部署多实例（共享同一 MySQL 数据库），通过负载均衡器分发请求
- **Executor 水平扩展**：同一 `appname` 可部署多个 Executor 实例，实现任务分片执行或负载均衡
- **JVM 配置**：根据服务器配置调整 Admin 和 Executor 的 JVM 内存（默认较小，生产环境建议增加）：
  ```bash
  # 启动时添加 JVM 参数（示例：Admin 内存设为 1G）
  docker run -d ... -e JAVA_OPTS="-Xms1g -Xmx1g" xuxueli/xxl-job-admin:2.4.0
  ```


#### 4. 集成业务服务（推荐）
实际生产中，Executor 通常集成到业务服务中，而非独立部署：
1. 业务服务添加依赖：
   ```xml
   <dependency>
       <groupId>com.xuxueli</groupId>
       <artifactId>xxl-job-core</artifactId>
       <version>2.4.0</version>
   </dependency>
   ```
2. 配置 `application.yml`（同 Executor 配置，指定 Admin 地址和令牌）
3. 配置类
```java
@ConditionalOnClass(XxlJobSpringExecutor.class)
@Configuration
public class XxlJobConfig {
private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

   @Value("${xxl.job.admin.addresses}")
   private String adminAddresses;
   
   @Value("${xxl.job.accessToken}")
   private String accessToken;
   
   @Value("${xxl.job.executor.appname}")
   private String appname;
   
   @Value("${xxl.job.executor.address}")
   private String address;
   
   @Value("${xxl.job.executor.ip}")
   private String ip;
   
   @Value("${xxl.job.executor.port}")
   private int port;
   
   @Value("${xxl.job.executor.logpath}")
   private String logPath;
   
   @Value("${xxl.job.executor.logretentiondays}")
   private int logRetentionDays;
   
   @Bean
   public XxlJobSpringExecutor xxlJobExecutor() {
      logger.info(">>>>>>>>>>> xxl-job config init.");
      XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
      xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
      xxlJobSpringExecutor.setAppname(appname);
      xxlJobSpringExecutor.setAddress(address);
      xxlJobSpringExecutor.setIp(ip);
      xxlJobSpringExecutor.setPort(port);
      xxlJobSpringExecutor.setAccessToken(accessToken);
      xxlJobSpringExecutor.setLogPath(logPath);
      xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
      return xxlJobSpringExecutor;
   }
}
```
4编写任务处理器（使用 `@XxlJob` 注解）


### **六、常见问题解决**
1. **Admin 启动失败（数据库连接错误）**：
    - 检查 `application.properties` 中 MySQL 地址、账号密码是否正确
    - 确认 MySQL 已初始化 `xxl_job` 表结构

2. **Executor 注册失败（Admin 控制台显示离线）**：
    - 检查 `xxl.job.admin.addresses` 是否正确（需包含 `http://` 和 Admin 端口）
    - 确认 `xxl.job.accessToken` 与 Admin 一致
    - 查看 Executor 日志：`docker logs xxl-job-executor`，排查「注册失败」原因（如网络不通、令牌不匹配）

3. **任务执行失败（日志显示无权限）**：
    - 检查 `accessToken` 是否匹配，或是否遗漏配置
    - 确认 Executor 中的 `demoJobHandler` 已正确定义（集成业务服务时需自定义 Handler）