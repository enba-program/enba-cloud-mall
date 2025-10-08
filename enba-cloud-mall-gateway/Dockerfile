# 使用精简JRE基础镜像
FROM openjdk:8-jre-alpine

# 设置时区（Alpine需先安装tzdata）
RUN apk add --no-cache tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata

# 维护者信息
MAINTAINER enba

# 进入工作目录
WORKDIR /app

# 复制jar
COPY target/enba-cloud-mall-gateway.jar .

# 暴露端口
EXPOSE 8080

ENTRYPOINT ["java","-jar","enba-cloud-mall-gateway.jar"]