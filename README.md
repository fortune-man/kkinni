# 애플리케이션 개요

## 프로젝트명
끼니 - 직장인 도시락 구독 서비스를 위한 주문 관리 시스템

## 목적
- 간단한 주문 관리 기능을 통해 주문 생성, 조회, 수정, 삭제 처리
- 백엔드 설계와 테스트 자동화를 학습 및 실습하기 위한 프로젝트

## 주요 기능
1. **주문 생성**: 사용자 정보와 상품 정보를 기반으로 새로운 주문을 생성합니다.
2. **주문 조회**: 특정 ID의 주문, 또는 모든 주문 정보를 조회합니다.
3. **주문 수정**: 주문 상태 및 세부 정보를 수정합니다.
4. **주문 삭제**: 특정 주문 ID를 기반으로 주문을 삭제합니다.
5. **유효성 검증**: 필수 입력값 누락이나 잘못된 상태값에 대해 적절한 에러를 반환합니다.

---

# 아키텍처 설계

## 구조 개요
- **Controller**: 현재 미사용 (API 노출 필요 시 구현 예정)
- **Service**: 비즈니스 로직 처리
- **Repository**: 데이터베이스 CRUD 처리
- **Domain**: 도메인 모델 정의
- **Validation**: 입력 데이터 검증

### 디렉토리 구조
```
/src
  ├── main
  │    ├── java
  │    │    ├── eat.kkinni
  │    │    │    ├── repository
  │    │    │    │    └── OrderRepository.java
  │    │    │    ├── service
  │    │    │    │    ├── domain
  │    │    │    │    │    └── Order.java
  │    │    │    │    ├── validation
  │    │    │    │    │    ├── ErrorMessage.java
  │    │    │    │    │    └── OrderValidator.java
  │    │    │    │    └── OrderService.java
  │    │    │    └── KkinniApplication.java
  └── test
       ├── java
       │    ├── eat.kkinni
       │    │    └── service
       │    │         └── OrderServiceTest.java
```

### 데이터베이스
MariaDB를 사용하며, 주문 정보를 저장하기 위한 테이블을 설계했습니다.

| Column Name   | Type       | Constraints            |
|---------------|------------|------------------------|
| id            | BIGINT     | Primary Key, Auto Increment |
| user_name     | VARCHAR(255) | NOT NULL             |
| item          | VARCHAR(255) | NOT NULL             |
| status        | VARCHAR(50) | NOT NULL             |

---

# 구현된 기능 및 테스트 결과

## 테스트 커버리지 결과
- **총 커버리지**: 85% (Jacoco 플러그인을 사용하여 측정)
- **Missed Coverage**: 일부 예외 처리 및 branch coverage 부족

| Component                     | Coverage |
|-------------------------------|----------|
| Service (OrderService)        | 84%      |
| Validation (OrderValidator)   | 96%      |
| Domain (Order)                | 79%      |
| Repository (OrderRepository)  | 테스트 간접 검증됨 |

## 주요 테스트 사례
- 주문 생성 성공 및 실패 테스트 (입력값 검증 포함)
- 특정 ID 및 전체 주문 조회 테스트
- 주문 상태 및 세부 정보 수정 테스트
- 주문 삭제 성공 및 실패 테스트

---

# 문제 해결 과정

1. **테스트 기반 개발(TDD)**
    - 모든 기능을 구현하기 전에 테스트를 작성하여 기능 완성도를 보장했습니다.

2. **입력 검증**
    - `OrderValidator` 클래스를 활용하여 필수 입력값 및 상태값 검증을 분리, 재사용성을 높였습니다.

3. **Mocking을 활용한 테스트**
    - `Mockito`를 사용해 Repository 계층을 Mock 처리하여 서비스 계층의 로직을 검증했습니다.

4. **코드 리팩토링**
    - 중복되는 로직을 서비스 및 검증 계층으로 분리하여 SRP(Single Responsibility Principle)를 준수했습니다.

---

# 개선이 필요한 포인트

1. **Controller 계층 구현**
    - RESTful API를 통해 실제 HTTP 요청을 처리하고 JSON 응답을 반환하는 기능 추가

2. **엔터프라이즈 확장성 설계**
    - Redis와 같은 캐시 시스템 도입으로 조회 성능 최적화
    - 데이터베이스 샤딩 또는 리플리케이션을 통한 확장성 확보

3. **CI/CD 파이프라인 구축**
    - GitHub Actions 또는 Jenkins를 활용해 자동 빌드, 테스트 및 배포 환경 구축

4. **100% 테스트 커버리지 목표**
    - 현재 누락된 branch coverage를 보완하고 예외 처리를 더욱 강화

5. **추가 비즈니스 로직**
    - 주문 이력 관리, 사용자 인증 및 권한 관리 기능 추가



