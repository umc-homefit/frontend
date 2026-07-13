# Convention

HomeFit Android 프로젝트 코드 및 협업 컨벤션입니다.

---

## 브랜치 전략

| 브랜치 | 설명 |
|---|---|
| main | 최종 배포 브랜치 |
| develop | 개발 통합 브랜치 |
| feature/* | 기능 개발 브랜치 |
| fix/* | 버그 수정 브랜치 |

### 브랜치 네이밍

```
feature/이슈번호-내용
fix/이슈번호-내용
```

예시
```
feature/12-home-screen
fix/20-button-overflow
```

---

## 작업 흐름

```bash
# 1. develop 최신화
git checkout develop
git pull origin develop

# 2. 작업 브랜치 생성
git checkout -b feature/12-home-screen

# 3. 작업 & 커밋
git add .
git commit -m "Feat: 홈 화면 구현"

# 5. push
git push origin feature/12-home-screen

# 6. GitHub에서 PR 생성 (feature/12-home-screen → develop)
# 7. 리뷰 & Approve
# 8. Squash and merge
# 9. 브랜치 삭제
```

---

## 커밋 메시지

```
<type>: <내용>
```

| type | 설명 |
|---|---|
| feat | 새로운 기능 추가 |
| fix | 버그 수정 |
| refactor | 리팩토링 |
| design | CSS 등 사용자 UI 디자인 변경 |
| comment | 필요한 주석 추가 및 변경 |
| style | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 |
| test | 테스트 코드 추가, 수정, 삭제 |
| chore | 빌드 스크립트 수정, assets, 패키지 매니저 등 기타 변경사항 |
| init | 프로젝트 초기 생성 |
| rename | 파일 혹은 폴더명 수정 또는 이동 |
| remove | 파일 삭제 |

예시
```
feat: 홈 화면 UI 구현
fix: 공고 목록 크래시 수정
rename: HomeViewModel 파일 이동
```

---

## 이슈 규칙

- 기능 개발 전 Issue 생성
- 담당자(Assignee) 지정
- Label 지정
- 작업 완료 후 PR에서 closes #번호 작성

---

## PR 규칙

- feature/fix → develop 으로 PR 생성
- PR 템플릿 필수 작성
- 관련 이슈 연결 (closes #이슈번호)
- 리뷰어 최소 1명 지정
- Approve 1명 이상 후 Merge
- Merge 방식은 항상 Squash and merge

---

## Kotlin 네이밍 규칙

| 대상 | 규칙 | 예시 |
|---|---|---|
| 클래스/인터페이스 | PascalCase | RecruitmentRepository |
| 함수/변수 | camelCase | getRecruitmentList() |
| 상수 | UPPER_SNAKE_CASE | MAX_PAGE_SIZE |
| 패키지 | 소문자 | com.homefit.home |

---

## Compose 네이밍 규칙

- 화면 단위: `XxxScreen`
- 공통 컴포넌트: 명사형 `PascalCase`
- Preview: `XxxPreview`

```kotlin
@Composable fun RecruitmentDetailScreen(...)
@Composable fun RecruitmentCard(...)
@Preview @Composable fun RecruitmentDetailScreenPreview()
```

---

## Compose 규칙

- UI 상태는 ViewModel에서 관리
- Screen에서는 비즈니스 로직 작성 금지
- 재사용 가능한 UI는 component 폴더 분리

---

## ViewModel / UiState 규칙

- ViewModel: `XxxViewModel`, 화면과 1:1 대응
- UiState: `XxxUiState` data class로 정의
- StateFlow로 단방향 State 노출

```kotlin
data class RecruitmentListUiState(
    val isLoading: Boolean = false,
    val recruitments: List<RecruitmentDto> = emptyList(),
    val errorMessage: String? = null
)
```

---

## 패키지 구조 규칙

```
presentation/recruitment/
├── RecruitmentListScreen.kt
├── RecruitmentListViewModel.kt
├── RecruitmentListUiState.kt
└── component/
    └── RecruitmentCard.kt
```

---

## 리소스 네이밍

| 리소스 | 규칙 | 예시 |
|---|---|---|
| Drawable | ic_, img_, bg_ + snake_case | ic_arrow_back |
| String | 화면명_설명 | recruitment_detail_title |
| Color | snake_case | color_primary |

---

## 주의사항

- develop, main 직접 커밋 ❌
- 이슈 없이 작업 시작 ❌
- PR 없이 merge ❌
- 한 브랜치에 PR 여러 개 ❌
- Merge commit 사용 ❌ (항상 Squash and merge)
