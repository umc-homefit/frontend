# HomeFit Android

> "이 집, 내가 실제로 들어갈 수 있을까?"  

HomeFit은 사용자가 공공 청년주택 공고를 조회하고, 자신의 조건을 기반으로 입주 가능성을 분석하며, 조건에 맞는 금융상품을 추천받을 수 있도록 돕는 주거 금융 플랫폼입니다.

---

## 🛠 기술 스택

| 영역 | 기술 |
|---|---|
| 언어 | Kotlin |
| UI | Jetpack Compose, Material3 |
| 아키텍처 | MVVM |
| DI | Hilt |
| Navigation | Navigation Compose |
| 네트워크 | Retrofit |
| 로컬 DB | Room |
| 비동기 | Coroutines, Flow |
| 이미지 로딩 | Coil |

---

## 👥 팀원 소개

| 이름 | GitHub |
|---|---|
| 릴리/김혜민 |  |
| 제이/박유진 |  |
| 양고/전서영 |  |
| 리비/홍지원 |  |

---

## 📁 프로젝트 폴더 구조

```
app/
├── data/
│   ├── datasource/
│   ├── dto/
│   └── repository/
├── presentation/
│   ├── home/
│   ├── recruitment/
│   ├── analysis/
│   ├── finance/
│   ├── mypage/
│   └── common/
├── navigation/
├── di/
├── ui/
└── util/
```

---

## 🚀 빌드 및 실행 방법

1. Repository Clone
```bash
git clone https://github.com/[org이름]/homefit-android.git
```
2. Android Studio에서 프로젝트 Open
3. `local.properties`에 필요한 환경 변수 설정
4. Gradle Sync 진행
5. `app` 모듈 실행 (▶ Run)

---

## 🖼 화면 목록

| 화면 이름 | 스크린 ID | 진입 경로 | 담당자 | 구현 여부 |
|---|---|---|---|---|
| 홈 | HomeScreen | 앱 최초 진입 | 제이/박유진 | 완료 |
| 알림 | NotificationScreen | 홈 → 알림 | 제이/박유진 | 완료 |
| 공고 검색 | RecruitmentSearchScreen | 홈 → 검색창 | 제이/박유진 | 완료 |
| 공고 목록 | RecruitmentListScreen | 하단 탭 → 공고 | 릴리/김혜민 | 완료 |
| 공고 필터 | RecruitmentFilterScreen | 공고 목록 → 필터 | 릴리/김혜민 | 완료 |
| 공고 상세 | RecruitmentDetailScreen | 공고 목록 → 공고 선택 | 릴리/김혜민 | 완료 |
| 경쟁률 정보 | CompetitionScreen | 공고 상세 → 경쟁률 보기 | 릴리/김혜민 | 완료 |
| 입주 분석 | AnalysisScreen | 하단 탭 → 분석 | 리비/홍지원 | 완료 |
| 금융 정보 입력 | FinancialInfoScreen | 입주 분석 → 금융 정보 입력 | 리비/홍지원 | 완료 |
| 입주 가능성 결과 | AnalysisResultScreen | 금융 정보 입력 → 분석 완료 | 리비/홍지원 | 완료 |
| 금융 상품 | FinanceScreen | 하단 탭 → 금융 | 제이/박유진 | 완료 |
| 추천 상품 목록 | RecommendedProductScreen | 금융 상품 → 추천 상품 전체 보기 | 제이/박유진 | 완료 |
| 상품 상세 | ProductDetailScreen | 추천 상품 목록 → 상품 선택 | 제이/박유진 | 완료 |
| 금융 상품 검색 | ProductSearchScreen | 추천 금융 상품 → 검색창 | 제이/박유진 | 수정 필요 |
| 마이페이지 | MyPageScreen | 하단 탭 → 마이 | 양고/전서영 | 완료 |
| 저장 공고 관리 | SavedRecruitmentScreen | 마이페이지 → 저장 공고 | 양고/전서영 | 완료 |
| 알림 설정 | NotificationSettingScreen | 마이페이지 → 알림 설정 | 양고/전서영 | 완료 |
| 내 금융 정보 관리 | MyFinanceScreen | 마이페이지 → 내 금융 정보 | 양고/전서영 | 완료 |

---

## 🧭 Navigation Flow

```

Home
├── 공고
│   ├── 공고 필터
│   └── 공고 상세
│       ├── 경쟁률 정보
│       ├── 공고 저장
│       └── 입주 분석 요청
├── 분석
│   ├── 금융 정보 입력
│   └── 입주 가능성 결과
│       
├── 금융
│   └── 금융 상품
│       └── 추천 상품 목록
│           └── 상품 상세
└── 마이
    ├── 저장 공고 관리
    ├── 알림 설정
    └── 내 금융 정보 관리
```

---

## 📏 Convention

- [GIT_CONVENTION.md](./GIT_CONVENTION.md)

## 🎨 Figma

- Figma URL: [https://www.figma.com/design/bK4utyX0yVnQptcXGXjDlw/%ED%99%88%ED%95%8F?node-id=241-8737&t=5QssZ6X6T5bqFdfd-0]
