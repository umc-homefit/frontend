package com.umc.homefit.data.mock

/**
 * 인기 검색어. 백엔드 API가 아직 없어 프론트 고정값으로 둔다.
 * API가 생기면 이 자리를 실제 호출로 교체하면 된다.
 */
object RecruitmentSearchMockData {
    val popularSearches = listOf(
        "청년안심주택",
        "행복주택",
        "역세권",
        "강동구",
        "관악구"
    )
}
