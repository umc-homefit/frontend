package com.umc.homefit.util

/**
 * FinancialInfoScreen(소득/자산/부채/주택 정보 입력)에서 수집한 UI 입력값을
 * UpdateConditionProfileRequest 필드 값으로 변환하는 순수 함수 모음.
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
 * "만 원" 단위에서 먼저 12로 나누면(예: 4800/12=400) 12로 안 떨어지는 값(예: 4000/12=333.33)에서
 * 소수점이 버려져 왕복 변환 시 오차가 커진다(4000만 원 입력 -> 3996만 원으로 보이는 문제).
 * 그래서 반드시 "원" 단위로 먼저 환산한 뒤 12로 나누고, 반올림해서 오차를 최소화한다.
 * 예: "4000" (연 4,000만 원) -> 40,000,000원 / 12 = 3,333,333.33... -> 반올림 -> 3,333,333원
 *
 * 입력값이 비어있거나 숫자가 아니면 0을 반환한다.
 */
fun toMonthlyIncomeAmount(annualIncomeText: String): Long {
    val annualManWon = annualIncomeText.toLongOrNull() ?: 0L
    val annualWon = annualManWon * 10_000
    return Math.round(annualWon / 12.0)
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
 * "원"을 먼저 "만 원"으로 내림 처리해버리면(예: 3,330,000/10,000=333) 그다음 12를 곱해도
 * 원래 입력했던 값으로 못 돌아온다. 그래서 12를 먼저 곱해 "원" 단위로 정밀하게 계산한 뒤,
 * 맨 마지막에만 "만 원" 단위로 반올림한다.
 * 예: 3,333,333원 -> 3,333,333 * 12 = 39,999,996 -> / 10,000 = 3999.9996 -> 반올림 -> "4000"
 */
fun toAnnualIncomeText(monthlyIncomeAmount: Long): String {
    val annualWon = monthlyIncomeAmount * 12
    val annualManWon = Math.round(annualWon / 10_000.0)
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
