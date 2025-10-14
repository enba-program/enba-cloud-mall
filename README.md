# enba-cloud-mall 微服务商城系统

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java Version](https://img.shields.io/badge/java-1.8-green.svg)
![Spring Boot Version](https://img.shields.io/badge/spring--boot-2.6.1-brightgreen.svg)

enba-cloud-mall 是一个基于 Spring Cloud Alibaba 生态的微服务商城系统，采用前后端分离架构，包含商品、订单、用户、购物车等核心电商模块，可作为企业级微服务项目的基础框架进行二次开发。

`注意!!!,项目依赖enba-boot脚手架，请先安装enba-boot脚手架(mvn clean install -DskipTests=true)`
* 码云地址：[enba-boot](https://gitee.com/mn_cxy/enba-boot)
* github地址：[enba-boot](https://github.com/enba-program/enba-boot)

## 项目架构

### 技术栈

- **核心框架**：Spring Boot 2.6.1、Spring Cloud Alibaba
- **服务注册与发现**：Nacos
- **配置中心**：Nacos Config
- **API网关**：Spring Cloud Gateway
- **数据存储**：MySQL 8.0、Redis 6.x
- **ORM框架**：MyBatis（通过动态数据源实现主从分离）
- **消息队列**：RocketMQ
- **部署环境**：Kubernetes

### 项目结构

```
enba-cloud-mall/
├── enba-cloud-mall-common          # 公共模块（工具类、常量、异常处理等）
├── enba-cloud-mall-biz-api         # 业务服务API接口定义
├── enba-cloud-mall-biz             # 核心业务服务
│   ├── enba-cloud-mall-area-biz    # 地区服务
│   ├── enba-cloud-mall-file-biz    # 文件服务
│   ├── enba-cloud-mall-goods-biz   # 商品服务
│   ├── enba-cloud-mall-orders-biz  # 订单服务
│   ├── enba-cloud-mall-shopping-biz # 购物车服务
│   └── enba-cloud-mall-users-biz   # 用户服务
├── enba-cloud-mall-facade          # 前端应用门面
│   └── enba-cloud-mall-h5-application # H5前端应用
└── enba-cloud-mall-gateway         # API网关服务
```

## 功能模块

### 1. 核心服务

- **用户服务（users-biz）**
    - 用户注册、登录、信息管理
    - 权限控制与认证

- **商品服务（goods-biz）**
    - 商品基础信息管理
    - 商品分类、库存管理
    - 商品搜索与推荐

- **订单服务（orders-biz）**
    - 订单创建与管理
    - 支付流程对接
    - 订单状态追踪

- **购物车服务（shopping-biz）**
    - 购物车项目管理
    - 结算功能

- **文件服务（file-biz）**
    - 文件上传下载
    - 图片处理

- **地区服务（area-biz）**
    - 行政区划管理
    - 地址解析

### 2. 支撑服务

- **API网关（gateway）**
    - 请求路由
    - 负载均衡
    - 统一认证

- **服务注册与配置（Nacos）**
    - 服务发现与健康检查
    - 配置集中管理

## 环境配置

### 开发环境

- JDK 1.8+
- MySQL 8.0+
- Redis 6.x+
- Nacos 2.x+
- Maven 3.6+

### 配置文件说明

各服务通过多环境配置文件区分不同部署环境：
- `dev`：开发环境
- `test`：测试环境
- `prod`：生产环境

配置文件路径：`src/main/resources/config/{环境}/application.yml`

核心配置项说明：
```yaml
# 服务端口
server:
  port: 8082

# 应用名称（服务注册标识）
spring:
  application:
    name: enba-cloud-goods-service
  # Nacos配置
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848  # 服务注册地址
      config:
        server-addr: localhost:8848  # 配置中心地址
  # 数据源配置
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8
    username: root
    password: 123456
  # Redis配置
  redis:
    host: 127.0.0.1
    port: 6379
```

## 部署方式

### 1. 本地开发部署

1. **启动依赖服务**
    - 启动 Nacos Server
    - 启动 MySQL 并初始化数据库脚本
    - 启动 Redis

2. **编译项目**
   ```bash
   mvn clean package -Dmaven.test.skip=true -P {环境}
   ```

3. **启动服务**
    - 依次启动各微服务（从注册中心、网关开始）
    - 或使用 IDE 直接运行各服务的主类

### 2. Kubernetes 部署

项目已提供各服务的 Kubernetes 部署配置文件，位于各服务的 `k8s/dev/app.yaml` 路径下。

1. **准备工作**
    - 搭建 Kubernetes 集群
    - 配置容器镜像仓库
    - 创建命名空间：`kubectl create namespace microservices`

2. **部署服务**
   ```bash
   # 部署网关服务
   kubectl apply -f enba-cloud-mall-gateway/k8s/dev/app.yaml
   
   # 部署用户服务
   kubectl apply -f enba-cloud-mall-biz/enba-cloud-mall-users-biz/k8s/dev/app.yaml
   
   # 部署其他服务...
   ```

3. **查看部署状态**
   ```bash
   kubectl get pods -n microservices
   kubectl get services -n microservices
   ```

## 服务间调用关系

- 前端应用通过 API 网关（gateway-service）访问后端服务
- 网关根据路由规则转发请求到对应微服务
- 微服务之间通过服务名进行调用（基于 Nacos 服务发现）

核心路由配置示例：
```yaml
# 网关路由配置
spring:
  cloud:
    gateway:
      routes:
        # 前台H5商城服务
        - id: facade-h5-application
          uri: lb://facade-h5-application
          predicates:
            - Path=/facade-h5/**,/facadeH5/**
          filters:
            - StripPrefix=1
```

## 开发规范

1. **代码规范**
    - 遵循阿里巴巴 Java 开发手册
    - 使用 Lombok 简化代码
    - 统一异常处理与返回格式

2. **接口规范**
    - 所有 API 接口通过 Swagger 文档暴露
    - 接口返回格式统一为：
      ```json
      {
        "code": 200,
        "message": "success",
        "data": {}
      }
      ```

3. **数据库规范**
    - 统一使用 MyBatis 作为 ORM 框架
    - 主从分离通过动态数据源实现

## 许可证

本项目基于 MIT 许可证开源 - 详情参见 [LICENSE](LICENSE) 文件

## 联系方式

- 项目维护者：恩爸编程
- 邮箱：contact@enba-cloud.com

欢迎提交 Issue 和 Pull Request 参与项目开发！