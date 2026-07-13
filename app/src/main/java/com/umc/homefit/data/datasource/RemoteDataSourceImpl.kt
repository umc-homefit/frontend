package com.umc.homefit.data.datasource

import com.umc.homefit.data.dto.RecruitmentDto
import com.umc.homefit.data.dto.RecruitmentStatus
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    // TODO: Retrofit API 서비스 인터페이스 추가 시 여기에 주입
) : RemoteDataSource {

    override suspend fun getRecruitments(): List<RecruitmentDto> {
        return listOf(
            RecruitmentDto(
                id = "1",
                title = "2026년 행복주택 입주자 모집공고",
                company = "한국토지주택공사",
                location = "서울특별시 강남구",
                rentType = "월세",
                deposit = 30000000,
                monthlyRent = 350000,
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
                deposit = 80000000,
                monthlyRent = 0,
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
                deposit = 65000000,
                monthlyRent = 0,
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
                deposit = 15000000,
                monthlyRent = 180000,
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

    override suspend fun getRecruitmentDetail(id: String): RecruitmentDto {
        return RecruitmentDto(
            id = id,
            title = "2026년 행복주택 입주자 모집공고",
            company = "한국토지주택공사",
            location = "서울특별시 강남구",
            rentType = "월세",
            deposit = 30000000,
            monthlyRent = 350000,
            announcementDate = "2026-07-13",
            announcementNumber = "2026-강남-001",
            area = 39.87,
            applicationStartDate = "2026-07-14",
            applicationEndDate = "2026-07-18",
            status = RecruitmentStatus.RECRUITING,
            competitionRate = "12.3:1",
            isBookmarked = true
        )
    }
}
