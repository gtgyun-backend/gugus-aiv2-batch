# Build stage - Gradle을 사용해 애플리케이션 빌드
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Gradle Wrapper와 빌드 스크립트 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사
COPY src src

# Gradle Wrapper에 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 및 애플리케이션 빌드
RUN ./gradlew clean bootJar --no-daemon -x test

# Runtime stage - 실행을 위한 경량 이미지
FROM eclipse-temurin:21-jre

WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=builder /app/build/libs/batch-0.0.1-SNAPSHOT.jar app.jar

# Spring Profile 환경변수 설정
ARG SPRING_PROFILES_ACTIVE=local
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

# 포트 노출
EXPOSE 8008

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]