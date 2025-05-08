FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew \
 && ./gradlew clean bootJar -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /

COPY --from=builder /workspace/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]