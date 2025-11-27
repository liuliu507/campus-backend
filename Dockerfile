# 使用正确的 JDK 21 镜像
FROM eclipse-temurin:21-jdk

WORKDIR /app

# 复制 Gradle Wrapper
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# 先下载依赖（加快构建速度）
RUN ./gradlew dependencies --no-daemon

# 复制源码
COPY src ./src

# 构建应用
RUN ./gradlew clean build -x test --no-daemon

# 暴露端口
EXPOSE 8081

# 运行应用 - 使用 8081 端口
CMD ["java", "-jar", "-Dserver.port=8081", "build/libs/ucampus-backend-0.0.1-SNAPSHOT.jar"]