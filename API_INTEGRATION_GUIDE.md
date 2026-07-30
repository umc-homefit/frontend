# API 연동 가이드

HomeFit Android에서 새로운 화면에 API를 연동할 때 따라야 하는 구조와 순서를 정리한 문서입니다.

---

## 전체 흐름

```
Screen
 ↓
ViewModel
 ↓
Repository Interface (domain)
 ↓
Repository Impl (data)
 ↓
Retrofit ApiService
 ↓
Backend API
```

**RemoteDataSource, UseCase 계층은 만들지 않습니다.** Repository Impl이 ApiService를 직접 주입받아 호출합니다. 대학생 팀 프로젝트 규모에 맞춰 단순한 MVVM + Repository 구조를 유지하기 위함이며, 임의로 계층을 추가하지 않습니다.

---

## 패키지 구조

```
data/
├── api/                 # Retrofit 인터페이스 (feature별)
│   ├── auth/
│   ├── common/          # health check 등 공통 엔드포인트
│   ├── home/
│   ├── finance/
│   └── recruitment/
├── dto/                 # 요청/응답 DTO (feature별)
├── local/               # DataStore, TokenProvider, UserPreferencesDataSource
├── mock/                # 아직 API가 없는 기능의 임시 데이터 (object)
├── remote/              # NetworkResult, SafeApiCall
└── repository/          # Repository 구현체 (feature별)

domain/
└── repository/          # Repository 인터페이스 (feature별)

presentation/
└── {feature}/            # Screen, ViewModel, UiState
```

---

## 연동 순서 (7단계)

| 단계 | 내용 | 위치 |
|---|---|---|
| 1 | DTO 작성 | `data/dto/{feature}/` |
| 2 | ApiService(Retrofit 인터페이스) 작성 + `NetworkModule`에 provide 추가 | `data/api/{feature}/` |
| 3 | Repository 인터페이스 작성 | `domain/repository/{feature}/` |
| 4 | Repository 구현체 작성 (ApiService 직접 주입, `safeApiCall`로 호출) | `data/repository/{feature}/` |
| 5 | Hilt Module에 `@Binds`로 인터페이스-구현체 연결 | `di/{Feature}RepositoryModule.kt` |
| 6 | ViewModel에서 Repository 주입받아 `UiState` 갱신 | `presentation/{feature}/` |
| 7 | Screen에서 `collectAsStateWithLifecycle()`로 상태 관찰 | `presentation/{feature}/` |

---

## 예시 (Auth 기준)

**1. DTO** — `data/dto/auth/LoginRequest.kt`, `LoginResponse.kt`

**2. ApiService** — `data/api/auth/AuthApiService.kt`
```kotlin
interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>
}
```

**3. Domain Repository** — `domain/repository/auth/AuthRepository.kt`
```kotlin
interface AuthRepository {
    suspend fun login(email: String, password: String): NetworkResult<LoginResponse>
}
```

**4. Repository Impl** — `data/repository/auth/AuthRepositoryImpl.kt`
```kotlin
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): NetworkResult<LoginResponse> {
        val result = safeApiCall { authApiService.login(LoginRequest(email, password)) }
        if (result is NetworkResult.Success) {
            userPreferencesDataSource.updateAccessToken(result.data.accessToken)
        }
        return result
    }
}
```

**5. Hilt Module** — `di/AuthRepositoryModule.kt`
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
```

---

## Mock 데이터 사용 규칙

아직 백엔드 API가 준비되지 않은 기능(recruitment, home 등)은 `data/mock/{Feature}MockData.kt`에 `object`로 임시 데이터를 두고, Repository Impl이 이를 직접 참조합니다.

```kotlin
class RecruitmentRepositoryImpl @Inject constructor() : RecruitmentRepository {
    override suspend fun getRecruitments(): List<RecruitmentDto> {
        return RecruitmentMockData.getRecruitments()
    }
}
```

실제 API가 준비되면:
1. `data/api/{feature}/`에 ApiService 추가
2. `NetworkModule`에 provide 함수 추가
3. Repository Impl 생성자에 ApiService 주입, mock 호출을 `safeApiCall` 호출로 교체

**RemoteDataSource, mock용 인터페이스를 새로 만들 필요 없습니다.** Repository Impl 하나만 수정하면 됩니다.

---

## 주의사항

- ❌ RemoteDataSource 계층 추가 금지
- ❌ UseCase 계층 추가 금지
- ❌ Screen/ViewModel에서 Retrofit·DTO 직접 참조 금지 (Repository를 거쳐야 함)
- ✅ 네트워크 에러 처리는 `safeApiCall`(`data/remote/SafeApiCall.kt`) 하나로 통일
- ✅ 인증 토큰이 필요한 요청은 `NetworkModule`의 auth interceptor가 자동으로 헤더에 붙임 (`TokenProvider` 경유)
