# 使用官方的 Java 11 运行时作为基础镜像
FROM openjdk:11-jdk-slim

# 设置工作目录
WORKDIR /app

# 将项目的 jar 文件复制到镜像中 源文件 目标文件
COPY target/backend-1.0.jar app.jar

# 暴露应用程序运行的端口
EXPOSE 17010

# 运行应用程序
ENTRYPOINT ["java", "-jar", "app.jar"]
