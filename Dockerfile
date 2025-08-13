# Stage 1: Build ứng dụng với Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy file pom.xml và tải dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy toàn bộ mã nguồn và build JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng với JDK nhẹ
FROM eclipse-temurin:17-jdk-slim

WORKDIR /app

# Copy JAR đã build từ stage trước
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
