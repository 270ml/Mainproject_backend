# Fashion API (NineOunce E-Commerce Backend)

> 패션 이커머스 서비스(**NineOunce**)를 위한 **Spring Boot 3 + Java 21 기반 백엔드 API 서버**입니다.  
> AI 딥러닝 모델로 추출한 **512/768차원 이미지 벡터**를 활용해 시각적 유사 상품 검색(Visual Search)과 스타일 추천을 제공하며,  
> **네이버 데이터랩 쇼핑인사이트 API**와 자체 판매 데이터를 결합한 실시간 패션 트렌드 분석, 소셜 로그인(Google·Naver·Kakao) 기반의 무상태 인증 아키텍처를 갖춘 풀스택 백엔드 시스템입니다.

## 🚀 주요 기술 스택 (Tech Stack)

### Backend Core
- **Framework**: <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white"/> Spring Boot 3.5.10
- **Language**: <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white"/> Java 21
- **Build Tool**: <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white"/> Gradle
- **Database**: <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white"/> PostgreSQL (Supabase 환경 연동)
- **ORM**: <img src="https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white"/> Spring Data JPA / Hibernate 6

### Security & Authentication
- **Spring Security** & **OAuth2 Client**: <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=Spring-Security&logoColor=white"/> (Google, Naver, Kakao 소셜 로그인 지원)
- **JWT (JSON Web Token)**: <img src="https://img.shields.io/badge/JWT-black?style=flat-square&logo=JSON%20web%20tokens"/> (`com.auth0:java-jwt`) 세션리스 인증 아키텍처 구현

### AI & Integration
- **FastAPI 연동**: <img src="https://img.shields.io/badge/FastAPI-009688?style=flat-square&logo=FastAPI&logoColor=white"/> Python 기반 외부 분석 서버(포트 8000, 8001) 통신 (512D/768D 벡터 임베딩)
- **pgvector**: <img src="https://img.shields.io/badge/pgvector-4169E1?style=flat-square&logo=postgresql&logoColor=white"/> 벡터 검색(Vector Similarity Search) 기반 코사인 유사도 연산

### Data & External API
- **Naver Datalab API**: <img src="https://img.shields.io/badge/Naver-03C75A?style=flat-square&logo=naver&logoColor=white"/> 쇼핑인사이트 API 기반 실시간 패션 트렌드 스코어 산출
- **Jackson (ObjectMapper)**: <img src="https://img.shields.io/badge/Jackson-000000?style=flat-square&logo=json&logoColor=white"/> 외부 API JSON 응답 파싱 및 직렬화/역직렬화

### Storage & Utility
- **Storage**: <img src="https://img.shields.io/badge/Supabase-3ECF8E?style=flat-square&logo=supabase&logoColor=white"/> Supabase Storage — 회원 프로필 사진 및 원본 분석 이미지 보관
- **API Docs**: <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=black"/> springdoc-openapi — `@Tag`/`@Operation` 어노테이션으로 API 명세 자동화 및 인터랙티브 테스트 UI (`/swagger-ui.html`)
- **P6Spy**: <img src="https://img.shields.io/badge/P6Spy-FF6600?style=flat-square&logoColor=white"/> 실행 SQL 및 바인딩 파라미터 실시간 모니터링
- **MapStruct**: <img src="https://img.shields.io/badge/MapStruct-FF6A00?style=flat-square&logoColor=white"/> MapStruct 1.6 — DTO ↔ Entity 간 컴파일 타임 자동 매핑 (런타임 리플렉션 비용 제거)
- **Caffeine Cache**: <img src="https://img.shields.io/badge/Caffeine-6DB33F?style=flat-square&logoColor=white"/> 인메모리 로컬 캐시 (10분 TTL, 최대 100 항목) — 트렌드·추천 조회 성능 최적화

### DevOps & Environment
- **Docker**: <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white"/> 멀티 스테이지 빌드 (JDK → JRE 분리) 기반 경량 컨테이너 배포
- **spring-dotenv**: <img src="https://img.shields.io/badge/.env-ECD53F?style=flat-square&logo=dotenv&logoColor=black"/> `.env` 파일 자동 로드 — 로컬/Docker/클라우드 환경변수 통합 관리
- **Lombok**: <img src="https://img.shields.io/badge/Lombok-BC4521?style=flat-square&logoColor=white"/> `@Getter`/`@Builder`/`@RequiredArgsConstructor` 등 보일러플레이트 코드 최소화
- **Bean Validation**: <img src="https://img.shields.io/badge/Validation-6DB33F?style=flat-square&logo=spring&logoColor=white"/> `spring-boot-starter-validation` — 입력 데이터 검증 자동화

---

## 🛠 아키텍처 및 핵심 기술 심층 분석 (Technical Deep Dive)

단순히 라이브러리를 조립하는 수준을 넘어, 대용량 트래픽과 AI 기반 시각 정보 처리의 성능 최적화, 그리고 유지보수성을 모두 고려하여 다음과 같은 최신 기술을 코드 레벨에 깊이 있게 적용했습니다.

### 1. ⚡ Java 21 가상 스레드 (Virtual Threads) 기반 논블로킹 I/O
- **도입 배경:** AI 이미지 분석과 같이 무거운 연산을 외부 FastAPI 서버로 위임하는 구조에서, 응답 대기로 인한 수 초의 네트워크 연결 지연(Latency)이 동반되었습니다. 기존 시스템에서는 이러한 구간에서 플랫폼 동시접속 스레드가 블로킹되어 빠르게 스레드 풀이 고갈되는 병목 현상이 발생했습니다.
- **해결 방안:** Java 21의 가벼운 '가상 스레드(Virtual Threads)' 기능을 `application.properties`에 즉시 도입하고 논블로킹 패러다임을 활성화했습니다.
- **기대 효과:** 서버 램(Memory) 고갈 없이 수천 명의 동시 접속자가 AI 검색을 요청해도 안정적으로 응답을 대기하고 분배할 수 있게 되어, 시스템 동시성(Throughput) 성능이 사실상 무제한에 가깝게 비약적으로 향상되었습니다.

### 2. 🧠 PostgreSQL `pgvector`를 활용한 대규모 벡터 엔진 조인
- **도입 배경:** 각 상품의 이미지를 512차원 혹은 768차원 다차원 배열 벡터로 인코딩한 방대한 데이터를 애플리케이션(Java단) 메모리로 가져와 반복 컴퓨팅으로 유사도를 비교할 경우, 즉각적인 `Out Of Memory` 발생과 응답 마비가 심각했습니다. 
- **해결 방안:** 상품 간 유사성 척도 및 스타일 클러스터링을 DB 엔진 단락에서 즉시 연산·해결하도록 PostgreSQL의 강력한 `pgvector` 확장을 활용하여 데이터 계층의 조인 네이티브 쿼리를 직접 구축했습니다.
- **기대 효과:** 불필요한 네트워크 대역폭 송수신 부하를 모두 분리했습니다. 검색 속도가 수십 배 이상 최적화되었을 뿐 아니라, 기존의 고정형(Static) 데이터베이스 관계 모델에서 AI가 지속 매핑하는 `top1_style` 동적 추천 모델링으로 시스템을 유연하게 진화시켰습니다.

### 3. 🛡️ 완전 분산 보안(Stateless)을 위한 Cookie 기반 OAuth2 및 Filter 체계
- **도입 배경:** 폭발적인 트래픽 대응용 스케일 아웃(Scale-out) 시, 기존 HTTP Session을 유지하려면 막대한 비용의 Redis와 같은 세션 클러스터링 인프라가 필수적입니다. 또한 Spring Security의 소셜 로그인 흐름 통제 자체에선 일부 데이터 단락의 세션 저장이 강제됩니다. 
- **해결 방안:** 백엔드 로그인 검증에 개입되는 세션 메커니즘을 100% 암호화된 `HTTP-Only` 쿠키 단으로 우회 재구현(`AuthorizationRequestRepository`) 하였으며, 성공 직후 JWT 식별 토큰만을 교환하는 필터단을 전면 설계했습니다.
- **기대 효과:** 단 1%의 세션도 서버에 남기지 않는 완전한 무상태(Stateless) 클라우드 로직을 완성했습니다. 악의적 요청 등을 비즈니스 로직이 아닌 서비스 진입 맨 처음 시점인 필터 단에서 신속하게 차단하여 백엔드 리소스 누수를 최소화하고 보안 레벨을 견고히 잡았습니다.

### 4. 🔗 Spring 6 선언적 HTTP 클라이언트 (Declarative HTTP Interfaces)
- **도입 배경:** 외부 FastAPI (AI 모듈 서버) 통신 로직에 기존 `RestTemplate`나 복잡한 빈 매핑 `FeignClient`를 사용하면 런타임 시 보일러플레이트 코드가 기하급수적으로 늘어나고 타임아웃 방어에 어려움이 발생합니다. 
- **해결 방안:** Spring 6의 가장 강력한 최신 통신 기능인 100% 논블로킹 엔진 Reactor Netty 기반의 **Declarative HTTP Interfaces (`@HttpExchange`)** 어댑터를 커스텀 튜닝(커넥션 풀 500개 확장, 120s 장기 타임아웃 세션 방어)하여 연동했습니다. 
- **기대 효과:** 통신 구조의 모든 직관적 결합도를 느슨하고 깔끔한 어노테이션 기반 인터페이스 단위로 압축하여 코드 추적 및 유지보수성을 극대화했습니다. 아무리 무거운 AI 연산이 길어져도 끊임없이 안전하게 데이터 소켓을 방어합니다. 

### 5. 🚀 MapStruct 매핑 자동화 및 로컬 파이프라인 (Caffeine Cache) 구축
- **도입 배경:** 클라이언트로 전송될 DTO 간 계속된 변경과 직렬/역직렬화에 기존 런타임 리플렉션 기술을 적용하면 구조적 응답 지연을 초래합니다. 더불어, 고정적인 메인 추천 트렌드 상품들을 매번 DB 통신으로 연산하는 막대한 I/O 낭비가 존재했습니다.
- **해결 방안:** 컴파일 빌드 단계 시점에 매핑 최적화 코드를 프레임워크가 자동 생성하는 `MapStruct` 도입 및 빠른 인메모리 반응형 데이터를 전달할 목적의 로컬 한정 `Caffeine Cache`를 동시 기용했습니다. 
- **기대 효과:** 애플리케이션의 런타임 객체 변환 지연율(Latency 오버헤드)을 'Zero' 단상으로 차단하였으며 실시간 트렌드 조회에 있어 DB 콜 발생 부담을 거의 완벽히 덜어냈습니다.

### 6. 🏞️ 구조적 영속성 분리 (Supabase Storage 독립 파일 연동망)
- **도입 배경:** 유저 프로필 등 사용자에 의해 등록된 무거운 대용량 바이너리 파일을 직접 RDBMS의 Byte Blob으로 관리하면 인프라 마이그레이션 백업 및 트랜잭션 용량에 곧바로 치명상을 입힙니다.
- **해결 방안:** Supabase Storage 버킷 전용 API 연동망 규격을 통해, 바이너리 팩 저장소는 별도 외부 퍼블릭 스토리지로 던지고 백엔드 테이블에는 해당 자산의 공개 HTTP URL(`default.svg` 등) 텍스트 명칭 하나만 기록되도록 로직을 분리시켰습니다. 
- **기대 효과:** RDBMS 비대화를 단번에 종식시켰습니다. 클라우드 스토리지를 응용하여 가볍고 초고속인 글로벌 응답을 제공, 프론트엔드에서 즉각 이미지가 스트리밍 로딩 렌더링되는 체감 성능 향상을 크게 이끌어 냈습니다.

### 7. 🐳 Docker 멀티 스테이지 빌드(Multi-stage Build) 및 협업 생태계 최적화
- **도입 배경:** 소스 코드와 조립 도구(JDK) 전체를 하나의 배포 컨테이너에 우겨넣으면 이미지 용량이 수백 MB 이상 비대해지며, 해커 침입 시 컴파일러가 그대로 노출되는 보안 위협이 뒤따릅니다. 더불어 프론트엔드 팀과의 기민한 병렬 개발을 위해 CORS 정책과 API 명세 동기화의 유연함이 절실했습니다.
- **해결 방안:** `Dockerfile`을 계층별로 나누어 1단계(Build Stage)에서 무거운 JDK로 소스를 컴파일(`app.jar`)하고, 2단계 최종 컨테이너(Production)에는 가볍고 통제된 **JRE 환경**만 옮겨 담는 **멀티 스테이지 빌드(Multi-stage Build)** 아키텍처를 도입했습니다. 나아가 Swagger(OpenAPI), P6Spy 네이티브 쿼리 추적망을 통합했습니다.
- **기대 효과:** 최종 릴리즈 도커 컨테이너의 크기를 비약적으로 다이어트시키고 부팅 속도를 최적화함은 물론 운영 환경 공격 벡터를 줄였습니다. 클라이언트(프론트) 개발자는 실시간으로 구동되는 대화형 Swagger 산출물과 방해 없는 API 규격을 통해 지연 없는 즉각적인 프론트-백 연동 통합이 가능해집니다.

### 8. 🔀 CompletableFuture 기반 병렬 벡터 검색 파이프라인
- **도입 배경:** 사용자가 이미지를 업로드하면 FastAPI로부터 768차원 임베딩 벡터를 수신한 뒤, 이 벡터로 **내부 자체 상품 DB**와 **네이버 외부 상품 DB** 두 곳에서 동시에 유사 상품을 검색해야 합니다. 이 두 검색을 순차적으로 실행하면 총 응답 시간이 두 배로 늘어나는 병목이 발생합니다.
- **해결 방안:** `RecommandService`에서 `CompletableFuture.supplyAsync()`로 내부 상품 검색과 네이버 상품 검색을 **동시에 비동기 실행**하고, `CompletableFuture.allOf().join()`으로 두 결과가 모두 도착할 때까지만 대기하는 **Fork-Join 병렬 파이프라인**을 구축했습니다.
- **기대 효과:** 두 개의 독립적인 벡터 유사도 검색이 동시에 수행되므로, 응답 시간이 단일 검색 수준으로 단축됩니다. Java 21 Virtual Threads와 결합하여 수천 건의 동시 분석 요청에서도 스레드 풀 고갈 없이 안정적으로 병렬 처리됩니다.

### 9. 📊 네이버 데이터랩 쇼핑인사이트 API 실시간 연동 및 상대 트렌드 스코어 산출
- **도입 배경:** 자체 판매 데이터만으로는 전체 패션 시장의 실시간 트렌드 흐름을 파악하기 어렵습니다. 특히 10가지 스타일(트래디셔널, 캐주얼, 스포츠 등) 간의 상대적 인기도를 정량화할 객관적 지표가 필요했습니다.
- **해결 방안:** `TrendService`에서 **네이버 데이터랩 쇼핑인사이트 API**를 `CompletableFuture` 기반으로 병렬 호출하고, 각 스타일 키워드의 검색 비율(ratio) 합산값을 기준 스타일(페미닌) 대비 **상대적 트렌드 스코어**로 환산하는 독자적인 알고리즘을 구현했습니다. 최종 결과는 퍼센트 비중(`value`)과 표시용 문자열(`percentStr`) 두 가지 포맷으로 프론트엔드에 전달됩니다.
- **기대 효과:** 단순한 검색량 나열이 아닌, 스타일 간의 **상대적 선호도 비율**을 실시간으로 산출하여 프론트엔드 차트(레이더/도넛)가 곧바로 렌더링할 수 있는 정규화된 데이터를 제공합니다. 외부 빅데이터와 내부 판매 데이터를 결합한 하이브리드 트렌드 분석 체계를 완성했습니다.

### 10. 🎯 Spring Data Interface Projection 및 읽기 전용 트랜잭션 최적화
- **도입 배경:** 벡터 유사도 검색 결과를 반환할 때, 무거운 JPA Entity 전체를 로딩하면 불필요한 컬럼까지 모두 메모리에 올라가고 Hibernate의 변경 감지(Dirty Checking) 오버헤드도 발생합니다.
- **해결 방안:** `SimilarProductProjection`이라는 **인터페이스 기반 Projection**을 정의하여, Native Query 결과에서 필요한 6개 필드(`productId`, `title`, `price`, `imageUrl`, `productLink`, `similarityScore`)만 정확하게 추출합니다. 또한 조회 전용 서비스 메서드에는 `@Transactional(readOnly = true)`를 적용하여 Hibernate의 스냅샷 저장·변경 감지를 완전히 비활성화했습니다.
- **기대 효과:** 불필요한 데이터 전송과 메모리 할당을 원천 차단하여 대규모 벡터 검색 결과 처리 시 GC 부담과 응답 지연을 크게 줄였습니다. 읽기 전용 트랜잭션은 DB 커넥션 레벨에서도 `SET TRANSACTION READ ONLY`를 통해 락(Lock) 경합을 최소화합니다.


---

## 🔌 시스템 모듈 및 주요 API 명세 (Key Features & Endpoints)

프로젝트는 주요 도메인 및 용도별 컨트롤러(`*Controller`)로 깔끔하게 분리되어 있습니다. 총 **10개 컨트롤러**, **30여개 엔드포인트**로 구성됩니다.

### 1. 👕 AI 시각 검색 및 상품 추천 (`/api/recommand`)
| Method | Endpoint | Description |
|:---:|---|---|
| **GET** | `/api/recommand/{productId}` | 512D 벡터 기준 코사인 유사도 측정 및 네이버/내부 유사 상품 Top 10 반환 |
| **GET** | `/api/recommand/768/{productId}` | 고밀도 768D 벡터 모델을 이용한 고정밀 유사 상품 추천 |
| **POST** | `/api/recommand/analyze` | (Multipart) 업로드 이미지 → FastAPI(8000) 512D 분석 → 유사 상품 추천 |
| **POST** | `/api/recommand/768/analyze` | (Multipart) 업로드 이미지 → FastAPI(8001) 768D 분석 → 내부/네이버 병렬 추천 |

### 2. 👥 회원 관리 및 소셜 인증 (`/api/members`)
| Method | Endpoint | Description |
|:---:|---|---|
| **POST** | `/api/members/signup` | 신규 회원가입 (Multipart: 아이디, 비밀번호, 닉네임, 프로필 이미지) |
| **POST** | `/api/members/login` | 로컬 로그인 처리 및 JWT Access Token 발급 |
| **POST** | `/api/members/logout` | 로그아웃 처리 (서버 세션 무효화) |
| **GET** | `/api/members/me` | JWT 토큰 기반 현재 로그인 사용자 정보 조회 |
| **GET** | `/api/members/list` | 전체 회원 목록 조회 (관리자용) |
| **PATCH** | `/api/members/update` | 닉네임, 비밀번호 등 회원 정보 선택적 수정 |
| **POST** | `/api/members/profile` | (Multipart) 프로필 이미지 Supabase Storage 업로드 및 회원 정보 갱신 |
| **DELETE** | `/api/members/withdraw` | 비밀번호 확인 후 회원 탈퇴 처리 |

### 3. 🛍️ 관심 상품 — 위시리스트 (`/api/save-products`)
| Method | Endpoint | Description |
|:---:|---|---|
| **POST** | `/api/save-products` | 네이버 상품 ID + 스타일 속성을 관심 목록에 등록 (JWT 필수) |
| **GET** | `/api/save-products` | 현재 로그인 회원의 관심 상품 전체 조회 |
| **DELETE** | `/api/save-products` | `List<String>` Body로 여러 건의 관심 상품 일괄 삭제 |

### 4. � 내부 상품 관리 (`/api/products`)
| Method | Endpoint | Description |
|:---:|---|---|
| **GET** | `/api/products/list` | 전체 또는 카테고리별 필터링된 나인온스 상품 목록 조회 |
| **GET** | `/api/products/detail?productId=` | 특정 상품 ID의 상세 정보 조회 |
| **GET** | `/api/products/map/512` | 512D UMAP 기반 3D Scatter Plot 렌더링용 X/Y/Z 좌표 반환 |
| **GET** | `/api/products/map/768` | 768D UMAP 기반 3D Scatter Plot 렌더링용 X/Y/Z 좌표 반환 |
| **GET** | `/api/products/style-count/512` | 512D 벡터 보유 상품의 스타일별 분포 통계 |
| **GET** | `/api/products/style-count/768` | 768D 벡터 보유 상품의 스타일별 분포 통계 |
| **GET** | `/api/products/internal-count` | 나인온스 자체 상품 총 개수 |
| **GET** | `/api/products/naver-count` | 네이버 크롤링 상품 총 개수 |

### 5. 🌐 네이버 외부 상품 (`/api/naver-products`)
| Method | Endpoint | Description |
|:---:|---|---|
| **GET** | `/api/naver-products/list` | 네이버 크롤링 데이터 기반 전체 외부 상품 목록 조회 |

### 6. 📈 트렌드 분석 (`/api/trends`)
| Method | Endpoint | Description |
|:---:|---|---|
| **GET** | `/api/trends/shopping-insight` | 네이버 쇼핑인사이트 API 기반 10대 스타일 실시간 트렌드 스코어 산출 |
| **GET** | `/api/trends/by-year?year=` | 연도별 월간 스타일 판매량 트렌드 조회 (파라미터 없으면 전체 연도) |

### 7. 💰 판매 통계 (`/api/sales`)
| Method | Endpoint | Description |
|:---:|---|---|
| **GET** | `/api/sales/rank` | 매장별·기간별 판매 순위 Top 5 조회 (전체/온라인/특정 매장) |

### 8. 🏬 매장 정보 (`/api/store`)
| Method | Endpoint | Description |
|:---:|---|---|
| **GET** | `/api/store/list` | 등록된 전체 매장(오프라인/온라인) 목록 조회 |

### 9. 📋 시스템 로그 모니터링 (`/api/logs`)
| Method | Endpoint | Description |
|:---:|---|---|
| **GET** | `/api/logs/view` | 브라우저 내장 실시간 서버 로그 대시보드 (HTML 반환) |
| **GET** | `/api/logs/raw` | 서버 로그 파일 마지막 1000줄 Raw 텍스트 반환 |

---

## 📂 프로젝트 아키텍처 레이어 (Layer Architecture)

```text
src/main/java/com/kdt03/fashion_api
 ├── config/        # CORS 통신, JWT, OAuth2 보안 정책, 가상 스레드, WebClient 전역 설정 모듈
 ├── controller/    # HTTP 기반 RESTful API 엔드포인트 진입 경로 및 문서 매핑 매개체
 ├── domain/        # 데이터베이스 엔티티 (JPA Entities) + 입체 XYZ 벡터 좌표계 및 도메인 모델
 │   └── dto/       # 클라이언트 단방향 전송 객체 모음집 (불변성 보장)
 ├── repository/    # JpaRepository 확장 인터페이스 (Vector 유사도 계산 Query 및 Native Join)
 ├── service/       # 트랜잭션(@Transactional) 처리를 중심으로 비즈니스 플로우를 통제하는 로직
 └── util/          # JWT 커스텀 디코딩 및 발급 토큰화(Payload 분석) 등 필수 공통 함수 집합
```

---

## 💻 실행 및 배포 가이드 (How to run locally & Deploy)

### 필요 환경 변수 구축 (.env 작성 요령)
현재 사용 중인 루트 경로 폴더에 `.env` 파일을 만들거나 시스템 혹은 런타임 환경 변수에 아래의 항목들을 반드시 설정해야 데이터 연결과 소셜 통신이 구동됩니다. (로컬과 Docker 환경 모두 공용 필수)

```ini
# Database & General Security Auth
DB_PASSWORD=your_supabase_postgres_password
JWT_SECRET=your_super_secret_jwt_signature_key

# OAuth2 External Providers
OAUTH2_GOOGLE_CLIENT_ID=your_google_id
OAUTH2_GOOGLE_CLIENT_SECRET=your_google_secret
OAUTH2_NAVER_CLIENT_ID=your_naver_id
OAUTH2_NAVER_CLIENT_SECRET=your_naver_secret
OAUTH2_KAKAO_CLIENT_ID=your_kakao_id
OAUTH2_KAKAO_CLIENT_SECRET=your_kakao_secret
```

### 🐳 Docker 통합 컨테이너를 이용한 배포와 실행 (권장 방안)
성능 저하 및 레이어 분리를 극대화하기 위해 제공 중인 **Multi-stage Build (멀티 스테이지 빌드 최적화)** 방식의 `Dockerfile`입니다 (JDK로 무거운 용량의 빌드 컴파일을 진행한 후 마지막엔 매우 가벼운 JRE 환경만 넘겨서 자원을 절약합니다).

1. **Docker 최적화 이미지 빌드 시작**
   ```bash
   docker build -t fashion-api:latest .
   ```
2. **배포용 컨테이너 실행**
   준비한 환경 변수를 넘겨주며 내장 톰캣 전용 8080 포트를 점유하여 컨테이너를 구동 및 백그라운드 활성화합니다.
   ```bash
   docker run -d -p 8080:8080 \
     --env-file .env \
     --name fashion-backend \
     fashion-api:latest
   ```
3. 부팅 상태 확인 및 에러 검색: `docker logs -f fashion-backend`

### 💻 OS 시스템 터미널 구동 가이드 (Docker 미사용 / 로컬 테스트용)
플랫폼 제약이나 종속성 다운로드를 예방하기 위한 `gradlew` 래퍼 파일이 코드에 동봉되어 있으므로, Java 21이 이미 인스톨되어 있다면 즉각 테스트가 가능합니다. 터미널 경로를 본 프로젝트(fashion-api)로 이동하십시오.

```bash
# Windows (CMD, PowerShell 등의 환경)
> gradlew.bat build -x test
> gradlew.bat bootRun

# macOS / Linux (Mac의 경우 만약 권한 거부 시 사전에 권한 부여: chmod +x gradlew)
$ ./gradlew build -x test
$ ./gradlew bootRun
```

### 🔗 백엔드 API 서비스 접속 및 Swagger 디버깅 테스트
- **Swagger 인터페이스 UI**: 서버 구동이 완료된 후 브라우저 메인 주소창에 `http://localhost:8080/swagger-ui.html` URL을 명시하여 이동하시면, 내부에 코딩된 모든 백엔드 API 들을 간편하고 직관적으로 파라미터를 넘겨주며 직접 호출하고 테스트해 볼 수 있습니다.

---

## 🗄️ 데이터베이스 스키마 구조 (Entity Relationship Diagram)

서비스에서 운용 중인 Supabase(PostgreSQL)의 전체 테이블 및 연관 관계를 요약한 다이어그램입니다. `pgvector`가 적용된 모델(`_vectors_512`, `_vectors_768`) 및 3D 시각화가 지원되는 모델(`_xyz_`) 등이 포함되어 있습니다.

```mermaid
erDiagram
    %% ==========================================
    %% 1. 사용자 및 인증 도메인 (User & Auth)
    %% ==========================================
    member {
        int seq PK
        string id UK
        string password
        string nickname
        string provider "OAuth2 로그인(Local, Google 등)"
        string profile
    }
    stores {
        string store_id PK
        string store_name
    }

    %% ==========================================
    %% 2. 메타데이터 도메인 (Category & Style)
    %% ==========================================
    categories {
        string category_id PK
        string category_name
    }
    styles {
        string style_id PK
        string style_name
    }

    %% ==========================================
    %% 3. 상품 도메인 (Products)
    %% ==========================================
    nineounce_products {
        string product_id PK
        string product_name
        int price
        string image_url
        string category_id FK
        string style_id FK
    }
    naver_products {
        string product_id PK
        string title
        int price
        string product_link
        string image_url
        string category_id FK
        string style_id FK
    }

    categories ||--o{ nineounce_products : "분류"
    categories ||--o{ naver_products : "분류"
    styles ||--o{ nineounce_products : "스타일 지정"
    styles ||--o{ naver_products : "스타일 지정"

    %% ==========================================
    %% 4. 트랜잭션 도메인 (Action & Log)
    %% ==========================================
    save_products {
        bigint save_id PK
        string member_id FK
        string naver_product_id FK
        string style_id FK 
        timestamp created_at
    }
    sales {
        int sale_id PK
        string product_id FK
        int sale_quantity
        int sale_price
    }
    sales_log {
        bigint seq PK
        string store_id FK
        string product_id FK
        int sale_quantity
        date sale_date
    }

    member ||--o{ save_products : "추가"
    naver_products ||--o{ save_products : "대상"
    nineounce_products ||--o{ sales : "판매"
    nineounce_products ||--o{ sales_log : "기록"
    stores ||--o{ sales_log : "통계"

    %% ==========================================
    %% 5. AI 모델링 도메인 (Vectors & 3D)
    %% ==========================================
    naver_product_vectors_512 {
        string product_id FK
        float top1_score
        string top1_style FK
    }
    naver_product_vectors_768 {
        string product_id FK
        float style_score1
        string style_top1 FK
    }
    nineounce_product_vectors_512 {
        string product_id FK
        float top1_score
        string top1_style FK
    }
    nineounce_product_vectors_768 {
        string product_id FK
        float style_score1
        string style_top1 FK
    }
    nineounce_xyz_512 {
        string product_id FK
        float x 
        float y 
        float z 
        string top1_style FK
    }
    nineounce_xyz_768 {
        string product_id FK
        float x 
        float y 
        float z 
        string top1_style FK
    }

    naver_products ||--o| naver_product_vectors_512 : "512D"
    naver_products ||--o| naver_product_vectors_768 : "768D"
    nineounce_products ||--o| nineounce_product_vectors_512 : "512D"
    nineounce_products ||--o| nineounce_product_vectors_768 : "768D"
    nineounce_products ||--o| nineounce_xyz_512 : "3D(512)"
    nineounce_products ||--o| nineounce_xyz_768 : "3D(768)"
```
