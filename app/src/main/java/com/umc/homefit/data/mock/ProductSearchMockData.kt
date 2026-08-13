package com.umc.homefit.data.mock

/**
 * 인기 검색어. 백엔드 API가 아직 없어 프론트 고정값으로 둔다.
 * API가 생기면 이 자리를 실제 호출로 교체하면 된다.
 */
object ProductSearchMockData {
    val popularSearches = listOf(
        "디딤돌 대출",
        "버팀목 전세대출",
        "청년 주택드림 청약통장",
        "주택청약종합저축",
        "국민은행"
    )
}
