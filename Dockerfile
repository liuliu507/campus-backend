FROM openjdk:21-jdk-slim

WORKDIR /app

# 复制构建文件
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 赋予执行权限
RUN chmod +x ./gradlew

# 下载依赖
RUN ./gradlew dependencies --no-daemon

# 复制源码
COPY src ./src

# 构建应用
RUN ./gradlew build -x test --no-daemon

# 运行应用（传递端口参数）
EXPOSE 8081
CMD ["java", "-jar", "build/libs/*.jar"]