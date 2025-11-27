# -------- Build Stage --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# 复制所有所需工程文件
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 给 gradlew 执行权限
RUN chmod +x gradlew

# 下载依赖
RUN ./gradlew dependencies --no-daemon || true

# 复制源码
COPY src ./src

# 构建 jar
RUN ./gradlew build -x test --no-daemon

# -------- Run Stage --------
FROM eclipse-temurin:21-jre
WORKDIR /app

# 从 build stage 复制 jar 文件
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
