# 阶段1：构建JAR包（使用官方Maven镜像，含Java 17）
FROM maven:3.9-eclipse-temurin-17 AS builder
# 设置工作目录
WORKDIR /app
# 复制pom.xml和源码
COPY pom.xml .
COPY src ./src
# 打包（跳过测试，加快构建）
RUN mvn clean package -DskipTests


# 阶段2：运行JAR包（使用轻量的JRE镜像，减小体积）
FROM eclipse-temurin:17-jre-alpine
# 设置工作目录
WORKDIR /app
# 从构建阶段复制JAR包到当前镜像
COPY --from=builder /app/target/*.jar app.jar
# 暴露端口（和你Spring Boot配置的PORT一致，默认8080）
EXPOSE 8080
# 启动命令（读取环境变量PORT，适配Render的端口映射）
