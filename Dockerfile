# 使用正确的 OpenJDK 21 镜像
FROM openjdk:21-jdk

WORKDIR /app

# 复制构建文件
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 赋予执行权限
RUN chmod +x ./gradlew

# 下载依赖（使用更快的镜像）
RUN ./gradlew dependencies --no-daemon --refresh-dependencies

# 复制源码
COPY src ./src

# 构建应用
RUN ./gradlew build -x test --no-daemon

# 运行应用
EXPOSE 8081
CMD ["java", "-jar", "build/libs/*.jar"]