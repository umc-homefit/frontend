package com.umc.homefit.util

/**
 * FinancialInfoScreen(소득/자산/부채/주택 정보 입력)에서 수집한 UI 입력값을
 * UpdateConditionProfileRequestDto 필드 값으로 변환하는 순수 함수 모음.
 *
 * 프로젝트에 별도 도메인 모델/Mapper 클래스/UseCase 계층이 없으므로(CLAUDE.md 참고),
 * 상태 없는 top-level 함수로 두고 ViewModel/RepositoryImpl에서 바로 호출해서 쓴다.
 */

/**
 * "만 원" 단위의 UI 입력 문자열을 "원" 단위 Long으로 변환한다.
 * (백엔드 금액 필드는 모두 "원" 단위)
 *
 * 예: "4800" (4,800만 원) -> 48,000,000원
 *
 * 입력값이 비어있거나 숫자가 아니면 0을 반환한다.
 */
fun toApiAmount(uiAmount: String): Long {
    val manWon = uiAmount.toLongOrNull() ?: 0L
    return manWon * 10_000
}

/**
 * IncomeStep에서 입력받는 "연간 총소득"(만 원 단위) 텍스트를
 * 백엔드 monthlyIncomeAmount(월 총소득, 원 단위)로 변환한다.
 *
 * 연간(만 원) -> 월(만 원)으로 먼저 12로 나눈 뒤, "원" 단위로 맞추기 위해 10,000을 곱한다.
 * 예: "4800" (연 4,800만 원) -> 4800 / 12 = 400(월 400만 원) -> 400 * 10,000 = 4,000,000원
 *
 * 입력값이 비어있거나 숫자가 아니면 0을 반환한다.
 * 12로 나눈 나머지(연 소득이 12로 안 떨어지는 경우)는 버림 처리된다.
 */
fun toMonthlyIncomeAmount(annualIncomeText: String): Long {
    val annualManWon = annualIncomeText.toLongOrNull() ?: 0L
    val monthlyManWon = annualManWon / 12
    return monthlyManWon * 10_000
}

/**
 * HouseStep 라디오 옵션 텍스트를 백엔드 housingOwnershipStatus enum 값으로 매핑한다.
 *
 * - "완전 무주택" -> "HOMELESS"
 * - "본인 무주택, 세대원 유주택" -> "FAMILY_OWNED"
 * - "본인 유주택" -> "OWNED"
 *
 * 매칭되는 옵션이 없으면 "UNKNOWN"을 반환한다.
 * "UNKNOWN"은 백엔드 enum에 존재하지 않는 방어용 값이므로,
 * 실제로 API 요청을 보내기 전에 호출부에서 별도 검증이 필요하다.
 */
fun mapToHousingStatus(selectedOption: String): String = when (selectedOption) {
    "완전 무주택" -> "HOMELESS"
    "본인 무주택, 세대원 유주택" -> "FAMILY_OWNED"
    "본인 유주택" -> "OWNED"
    else -> "UNKNOWN"
}

/**
 * housingOwnershipStatus 값으로부터 isHomeless(무주택 여부)를 파생시킨다.
 *
 * housingStatus가 "HOMELESS"(본인 및 세대원 모두 무주택)일 때만 true를 반환하고,
 * 그 외("FAMILY_OWNED", "OWNED", "UNKNOWN" 등)는 모두 false를 반환한다.
 */
fun calculateIsHomeless(housingStatus: String): Boolean = housingStatus == "HOMELESS"

// ----- 조회(GET) 결과를 UI 표시용으로 되돌리는 역변환 함수들 -----

/**
 * 백엔드 "원" 단위 금액을 "만 원" 단위 UI 표시 문자열로 변환한다. (toApiAmount의 반대 방향)
 *
 * 예: 48,000,000원 -> "4800" (4,800만 원)
 * 10,000으로 나눈 나머지(만 원 단위로 안 떨어지는 금액)는 버림 처리된다.
 */
fun toDisplayAmount(apiAmount: Long): String = (apiAmount / 10_000).toString()

/**
 * 백엔드 monthlyIncomeAmount(월 총소득, 원 단위)를
 * IncomeStep의 "연간 총소득"(만 원 단위) 입력란에 채울 문자열로 변환한다. (toMonthlyIncomeAmount의 반대 방향)
 *
 * 먼저 "원"을 "만 원"으로 바꾸기 위해 10,000으로 나눈 뒤, 월 -> 연으로 12를 곱한다. (순서 주의)
 * 예: 4,000,000원 -> 4,000,000 / 10,000 = 400(월 400만 원) -> 400 * 12 = 4800(연 4,800만 원) -> "4800"
 */
fun toAnnualIncomeText(monthlyIncomeAmount: Long): String {
    val monthlyManWon = monthlyIncomeAmount / 10_000
    val annualManWon = monthlyManWon * 12
    return annualManWon.toString()
}

/**
 * 백엔드 housingOwnershipStatus 값을 HouseStep 라디오 옵션 텍스트로 매핑한다. (mapToHousingStatus의 반대 방향)
 *
 * - "HOMELESS" -> "완전 무주택"
 * - "FAMILY_OWNED" -> "본인 무주택, 세대원 유주택"
 * - "OWNED" -> "본인 유주택"
 *
 * 매칭되는 값이 없으면 null을 반환한다 (라디오 옵션 중 아무것도 미리 선택하지 않음).
 */
fun mapToHouseOption(housingStatus: String): String? = when (housingStatus) {
    "HOMELESS" -> "완전 무주택"
    "FAMILY_OWNED" -> "본인 무주택, 세대원 유주택"
    "OWNED" -> "본인 유주택"
    else -> null
}
