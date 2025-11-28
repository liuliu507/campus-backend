# 使用 OpenJDK 21 运行时镜像
FROM openjdk:21-jdk-slim

WORKDIR /app

# 将 jar 包复制进去（注意名称必须与你生成的完全一致）
COPY build/libs/ucampus-backend-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8081

# 启动 Spring Boot
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
