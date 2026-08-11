package com.umc.homefit.data.dto.analysis

import kotlinx.serialization.Serializable

// 분석 생성(POST) 응답에서 실제로 쓰는 건 analysisId뿐 — 나머지 상세 항목은 결과 화면 진입 시
// GET /eligibility-analyses/{analysisId}(EligibilityAnalysisResultDto)로 다시 조회해서 채운다.
// ignoreUnknownKeys = true라 서버가 더 많은 필드를 내려줘도 여기선 무시된다.
@Serializable
data class RequestEligibilityAnalysisResultDto(
    val analysisId: Long
)
