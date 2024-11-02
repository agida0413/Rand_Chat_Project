# 1단계: Gradle 빌드를 위한 빌드 이미지
FROM gradle:7.6.0-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /backend

# Gradle Wrapper 파일을 먼저 복사하여 캐시 활용
COPY gradle/wrapper/gradle-wrapper.jar ./gradle/wrapper/
COPY gradle/wrapper/gradle-wrapper.properties ./gradle/wrapper/
COPY gradlew ./
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사
COPY src ./src
# gradlew 실행 권한 설정
RUN chmod +x gradlew
# 필요한 종속성을 다운로드 (캐시 활용)
RUN ./gradlew build --no-daemon

RUN ./gradlew test --no-daemon  # 테스트를 실행하는 단계 추가
# 2단계: 최종 이미지
FROM openjdk:17-jdk-slim

# JAR 파일을 /app.jar로 복사
COPY --from=builder /backend/build/libs/*.jar app.jar
# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]