package com.umc.homefit.data.mock

import com.umc.homefit.data.dto.recruitment.RecruitmentDto
import com.umc.homefit.data.dto.recruitment.RecruitmentStatus

object RecruitmentMockData {

    fun getRecruitments(): List<RecruitmentDto> {
        return listOf(
            RecruitmentDto(
                id = "1",
                title = "2026년 행복주택 입주자 모집공고",
                company = "한국토지주택공사",
                location = "서울특별시 강남구",
                rentType = "월세",
                depositMin = 30000000,
                depositMax = 30000000,
                monthlyRentMin = 350000,
                monthlyRentMax = 350000,
                announcementDate = "2026-07-13",
                announcementNumber = "2026-강남-001",
                area = 39.87,
                applicationStartDate = "2026-07-14",
                applicationEndDate = "2026-07-18",
                status = RecruitmentStatus.RECRUITING,
                competitionRate = "12.3:1",
                isBookmarked = true
            ),
            RecruitmentDto(
                id = "2",
                title = "청년 매입임대주택 입주자 모집공고",
                company = "서울주택도시공사",
                location = "서울특별시 마포구",
                rentType = "전세",
                depositMin = 80000000,
                depositMax = 80000000,
                monthlyRentMin = 0,
                monthlyRentMax = 0,
                announcementDate = "2026-07-10",
                announcementNumber = "2026-마포-014",
                area = 29.5,
                applicationStartDate = "2026-07-20",
                applicationEndDate = "2026-07-25",
                status = RecruitmentStatus.SCHEDULED,
                competitionRate = "-",
                isBookmarked = false
            ),
            RecruitmentDto(
                id = "3",
                title = "신혼부부 전세임대주택 입주자 모집공고",
                company = "한국토지주택공사",
                location = "서울특별시 송파구",
                rentType = "전세",
                depositMin = 65000000,
                depositMax = 65000000,
                monthlyRentMin = 0,
                monthlyRentMax = 0,
                announcementDate = "2026-07-05",
                announcementNumber = "2026-송파-007",
                area = 49.94,
                applicationStartDate = "2026-07-11",
                applicationEndDate = "2026-07-14",
                status = RecruitmentStatus.CLOSING_SOON,
                competitionRate = "34.7:1",
                isBookmarked = true
            ),
            RecruitmentDto(
                id = "4",
                title = "국민임대주택 입주자 모집공고",
                company = "인천도시공사",
                location = "인천광역시 연수구",
                rentType = "월세",
                depositMin = 15000000,
                depositMax = 15000000,
                monthlyRentMin = 180000,
                monthlyRentMax = 180000,
                announcementDate = "2026-06-28",
                announcementNumber = "2026-연수-022",
                area = 59.92,
                applicationStartDate = "2026-07-01",
                applicationEndDate = "2026-07-05",
                status = RecruitmentStatus.RECRUITING,
                competitionRate = "5.1:1",
                isBookmarked = false
            )
        )
    }
}
