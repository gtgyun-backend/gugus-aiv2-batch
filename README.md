# Gugus Batch Project

## 프로젝트 소개

Spring Boot 기반의 배치 프로젝트로, 외부 API를 통해 카테고리, 브랜드, 모델 정보를 동기화하는 서버 애플리케이션입니다.

## 주요 기능

### 1. 외부 API 동기화
- **카테고리 API**: 전체 카테고리 목록 조회 및 DB 동기화
- **브랜드 API**: 전체 브랜드 목록 조회 및 DB 동기화  
- **모델 API**: 브랜드-카테고리 조합별 모델 목록 조회 및 DB 동기화

### 2. 스케줄링
- **통합 스케줄러**: 매일 새벽 2시 30분에 순차적으로 실행
  - 1단계: 카테고리 동기화
  - 2단계: 브랜드 동기화
  - 3단계: 모든 브랜드-카테고리 조합에 대한 모델 동기화
- **개별 스케줄러**: 각각 독립적으로 실행 가능 (현재 비활성화)

### 3. 데이터 처리
- **Upsert 로직**: 기존 데이터는 업데이트, 신규 데이터는 삽입
- **페이징 처리**: 대용량 데이터 처리 지원
- **에러 처리**: 개별 조합 실패 시에도 전체 프로세스 계속 진행
- **테스트 데이터**: 방화벽 등으로 API 호출이 불가능한 상황에서 JSON 파일로 테스트 가능

## 기술 스택

### Backend

- Java 21
- Spring Boot 3.3.11
- Spring Data JPA
- QueryDSL 5.0.0
- Spring Security
- Spring WebFlux (외부 API 호출)
- Spring Scheduling (배치 스케줄링)
- Lombok
- Reactor (비동기 처리)

### Database

- MySQL 8.0
- HikariCP (커넥션 풀)

### Development Tools

- Gradle
- JUnit 5
- Mockito
- TestContainers

## 프로젝트 구조

src/main/java/com/gugus/batch/  
├─ config/ # 애플리케이션 설정  
├─ database/ # 데이터베이스 관련  
│  ├─ entities/ # 도메인 엔티티  
│  └─ repositories/ # 데이터 접근 계층  
├─ externals/ # 외부 API 관련  
│  ├─ dto/ # 외부 API DTO  
│  └─ LetsurExternalClient.java # 외부 API 클라이언트  
├─ job/ # 배치 작업  
├─ scheduler/ # 스케줄러  
├─ service/ # 비즈니스 로직  
└─ util/ # 유틸리티 클래스



## 설정 가이드

### 환경 설정

프로젝트는 세 가지 환경 프로필을 지원합니다:

- local: 로컬 개발 환경
- dev: 개발 서버 환경
- prod: 운영 서버 환경

### 데이터베이스 설정

```yaml
spring:
  datasource:
    url: jdbc:mysql://[host]:[port]/[database]
    username: [username]
    password: [password]
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 애플리케이션 속성

```properties
server.port=8080

# JPA 설정
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.properties.hibernate.default_batch_fetch_size=100

# 외부 API 설정
externals.letsur.api-key=${LETSUR_API_KEY:dev-key}
externals.letsur.base-url=https://api.partner.com

# 스케줄링 설정
sync.category.enabled=true
sync.brand.enabled=true
sync.model.enabled=true
```

## 빌드 및 실행

### 요구사항

- JDK 21
- MySQL 8.0

### 로컬 빌드

```bash
./gradlew clean build
```

### 애플리케이션 실행

```bash
java -jar -Dspring.profiles.active=[profile] build/libs/batch-0.0.1-SNAPSHOT.jar
```

### Docker 실행

```bash
docker build -t gugus-batch .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=[profile] gugus-batch
```

## 개발 가이드

### 배치 작업 실행

#### 수동 실행
```java
// SyncJob을 통한 전체 동기화
@Autowired
private SyncJob syncJob;

syncJob.runAll(200); // pageSize 200으로 실행
```

**HTTP API를 통한 수동 실행:**
```bash
# 실제 API를 사용한 전체 동기화 실행
curl -X POST "http://localhost:8080/api/test/sync-all?pageSize=200"

# 테스트 데이터를 사용한 전체 동기화 실행
curl -X POST "http://localhost:8080/api/test/sync-all-test?pageSize=200"
```

### API 문서화 (Swagger)

프로젝트에는 Swagger UI가 포함되어 있어 API 문서를 쉽게 확인할 수 있습니다.

**Swagger UI 접속:**
- 로컬 환경: http://localhost:8080/swagger-ui.html
- API 문서 JSON: http://localhost:8080/api-docs

**주요 API 엔드포인트:**
- `POST /api/test/sync-all` - 실제 API를 사용한 전체 동기화
- `POST /api/test/sync-all-test` - 테스트 데이터를 사용한 전체 동기화
- `GET /health` - 시스템 상태 확인

#### 스케줄링
- **통합 스케줄러**: 매일 새벽 2시 30분 자동 실행 (실제 API 호출)
- **테스트용 스케줄러**: 매일 새벽 3시 자동 실행 (JSON 파일 사용)
- **개별 스케줄러**: 각각 독립적으로 실행 가능

### 외부 API 연동

#### API 엔드포인트
- 카테고리: `GET /v1/externals/letsur/category`
- 브랜드: `GET /v1/externals/letsur/brand`
- 모델: `GET /v1/externals/letsur/management-models`

#### 인증
- x-api-key 헤더를 통한 인증
- 환경변수 `LETSUR_API_KEY`로 설정

#### 테스트 데이터
방화벽 등으로 실제 API 호출이 불가능한 상황에서 테스트용 JSON 파일을 사용할 수 있습니다:
- `src/main/resources/test-data/categories-response.json`
- `src/main/resources/test-data/brands-response.json`
- `src/main/resources/test-data/models-response.json`

**테스트용 메서드:**
- `CategorySyncService.syncAllWithTestData()`
- `BrandSyncService.syncAllWithTestData()`
- `ModelSyncService.syncPairWithTestData()`
- `SyncJob.runAllWithTestData()`

**테스트용 API 엔드포인트:**
- `POST /api/test/sync-all-test` - 테스트 데이터로 전체 동기화

**테스트용 스케줄러:**
- 매일 새벽 3시에 테스트 데이터로 자동 실행

### 데이터 처리

#### Upsert 로직
- 기존 데이터: `updateNameByAPI()` 메서드로 업데이트
- 신규 데이터: `createByAPI()` 메서드로 생성

#### 페이징 처리
- 기본 페이지 크기: 200
- 설정으로 조정 가능

### 로깅
- 각 단계별 상세 로그 출력
- 에러 발생 시 개별 조합 실패 로그
- 전체 프로세스 완료 로그

## 기여 가이드

1. 코드 컨벤션을 준수해주세요
2. 모든 변경사항은 테스트를 동반해야 합니다
3. 커밋 메시지는 명확하게 작성해주세요
