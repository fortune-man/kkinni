# 끼니: 직장인 구독 배달 서비스 백엔드 프로젝트

> **직장인 도시락 구독 배달 서비스의 백엔드 시스템**  
- 동시 접속 처리 능력이 요구되는 주문 관리 시스템 개발 프로젝트를 위한 문서입니다.
- 실무에서 마주칠 법한 문제 해결 과정과 기술적 선택을 강조하고, 개인 포트폴리오 작성에 참고할 수 있도록 구성되었습니다. 
- 저와 비슷한 고민을 하는 누군가가 있다면 작으나마 도움이 되기를 기대합니다.

---

## 1. 프로젝트 개요

### 1.1 목표
- 동시 접속 최소 **50명 이상 안정적으로 처리** 가능한 시스템 개발.
- **Redis 캐싱**을 활용해 데이터베이스 부하 최소화.
- **QueryDSL**을 이용한 동적 쿼리 설계 및 성능 최적화.
- **TDD(Test-Driven Development)**를 기반으로 높은 신뢰성을 확보한 개발.

### 1.2 아키텍처
![Backend Architecture](https://velog.velcdn.com/images/urtimeislimited/post/78e28ab3-3eff-467b-af34-8f36dffb411d/image.png)

---

## 2. 전체 일정 및 단계별 진행

### 2.1 채용 시장 조사 및 요구 사항 수집
- **목표 회사 및 기술 스택 분석**:
- Redis, QueryDSL, Docker, AWS 등의 기술 선정.
- 지속 성장 가능한 대규모 트래픽을 처리하는 서비스 회사의 요구사항 기반.
- **백엔드 아키텍처 설계**:
- 아키텍처 다이어그램을 기준으로 데이터 흐름 및 트래픽 경로 확인.

---

### 2.2 환경 설정 및 초기화

1. **필수 도구 설치**:
- Java 17, Gradle, Docker, Redis, MariaDB.
2. **Spring Boot 프로젝트 생성**:
- Spring Initializr에서 **Spring Web**, **Spring Data JPA**, **Redis**, **QueryDSL** 추가.
3. **Docker 설정**:
- MariaDB와 Redis를 컨테이너로 실행.

#### Docker Compose 예제
```application.properties
version: '3.8'
services:
  mariadb:
    image: mariadb:10.5
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: orders_db
    ports:
      - "3306:3306"
    volumes:
      - mariadb_data:/var/lib/mysql

  redis:
    image: redis:6.2
    ports:
      - "6379:6379"

volumes:
  mariadb_data:
```

### 2.3 기능 개발 및 테스트

1. 주문 도메인 설계
   •	Order 엔티티, Repository, Service, Controller 계층 구현.
   •	TDD를 기반으로 서비스 레이어부터 개발.

2. Redis 캐싱 적용
   •	@Cacheable 어노테이션과 RedisConfig로 TTL(Time-to-Live) 설정.
   •	RedisConfig 예제:
```java
@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10));
        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(config)
            .build();
    }
}
```
3. QueryDSL 적용
   •	동적 쿼리 설계와 MariaDB 인덱스 최적화.
```java
QOrder order = QOrder.order;
JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

List<Order> results = queryFactory.selectFrom(order)
    .where(order.status.eq("COMPLETED"))
    .fetch();
```
### 2.4 테스트 및 검증

1. TDD(Test-Driven Development)
- JUnit과 MockMvc를 활용하여 유닛 테스트 및 통합 테스트 작성.
- Jacoco를 사용해 테스트 커버리지 확인.
2. k6 부하 테스트
- 50명 동시 접속을 테스트하기 위한 k6 시나리오 작성.
```k6
import http from 'k6/http';
import { check } from 'k6';

export let options = {
  vus: 50,
  duration: '10s',
};

export default function () {
  let res = http.get('http://localhost:8080/orders/1');
  check(res, { 'status is 200': (r) => r.status === 200 });
}
```
---

### 2.5 배포
1.	Docker로 애플리케이션 빌드 및 배포:
- Dockerfile 작성 및 이미지 빌드.
- AWS EC2에 배포하여 URL로 접속 가능하도록 설정.
2.	운영 환경 최적화:
- Tomcat 스레드 풀 크기 조정.
- Redis 캐시 히트율 모니터링.

---
### 3. 문제 해결 및 배운 점

주요 문제 해결
- Redis 설정 오류: CacheManager 명시적 설정으로 해결.
- 동적 쿼리 성능 저하: QueryDSL을 통해 최적화.
- 부하 테스트 실패: Redis 캐싱 및 Tomcat 스레드 최적화로 해결.

배운 점
- Redis와 QueryDSL의 장단점 학습.
- Docker와 AWS 클라우드 배포 경험.
- 데이터베이스 인덱스 설계의 중요성 체득.
---

### 4. 비즈니스 밸류 창출 요소

1. 시스템 안정성 강화
- Redis 캐싱 적용으로 DB 부하 감소.
- 평균 응답 시간 90ms로 단축 (도입 전 800ms).

2. 운영 비용 절감
- DB 요청 수 감소로 MariaDB 인스턴스 비용 절감.
- QueryDSL로 비효율적 풀스캔 제거.

3. 고객 만족도 향상
- 주문 처리 평균 응답 시간을 100ms 이하로 유지.
- 50명 동시 접속을 안정적으로 처리.

---

### 5. 후임 개발자를 위한 제안
1.	문제 해결 중심의 학습:
- 기술을 단순히 배우는 것이 아닌, 실제 비즈니스 문제를 해결할 수 있는 역량을 강조하세요.
2.	비즈니스 밸류 우선 접근:
- 기술 도입 이유와 가치를 항상 비즈니스와 연결 지어 설명할 수 있도록 연습하세요.
3.	데이터 기반 성과 증명:
- 성능 개선 전후 데이터를 체계적으로 기록하고, 이를 활용해 회사에 가치를 증명하세요.

---

### 6. 앞으로의 발전 방향
1.	Redis 성능 최적화 (캐시 정책 세분화).
2.	QueryDSL 활용 강화 (복잡한 동적 쿼리 처리).
  2.1 디자인패턴 활용 고려
3.	AWS 인프라 확장 (로드 밸런싱 및 스케일링 적용).


---

#### 실행 방법
1.	Docker Compose 실행
```docker-compose up -d```
2. Spring Boot 실행
```./gradlew bootRun```
3. k6 부하 테스트 실행
- 테스트 스크립트 작성
- load-test.js 파일 생성
- ```nano load-test.js```
```import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 50, // 동시 접속 사용자 수
  duration: '5s', // 테스트 실행 시간
};

export default function () {
  const res = http.get('http://localhost:8080/orders/1'); // API 엔드포인트
  check(res, { 'status is 200': (r) => r.status === 200 });
  sleep(1);
}
```
```k6 run load-test.js```
- 터미널에 요청 성공률, 평균 응답 시간, 에러 비율 등이 출력

4.	배포:
- Docker 이미지를 빌드하고 AWS EC2에 배포.
- URL로 접속하여 서비스 테스트.