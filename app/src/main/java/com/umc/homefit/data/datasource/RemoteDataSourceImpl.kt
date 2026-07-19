package com.umc.homefit.data.datasource

import com.umc.homefit.R
import com.umc.homefit.data.dto.Attachment
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

    override suspend fun getRecruitmentDetail(id: String): RecruitmentDto {
        return RecruitmentDto(
            id = id,
            title = "강동구 청년안심주택 2025-03호",
            company = "한국토지주택공사",
            location = "서울 강동구 천호동 123-4",
            rentType = "청년안심주택 (임대)",
            unitSummary = "전용 24㎡ 18세대 / 전용 33㎡ 12세대",
            depositMin = 32000000,
            depositMax = 48000000,
            monthlyRentMin = 280000,
            monthlyRentMax = 410000,
            announcementDate = "2026-07-13",
            announcementNumber = "2026-강남-001",
            area = 39.87,
            // TODO: API 연동 시 ISO 8601 → 한글 날짜 포맷 변환 필요
            applicationStartDate = "2025년 6월 9일 (월) 오전 10:00",
            applicationEndDate = "2025년 6월 13일 (금) 오후 6:00",
            status = RecruitmentStatus.RECRUITING,
            competitionRate = "12.3:1",
            isBookmarked = true,
            tags = listOf("청년우선공급", "역세권"),
            attachments = listOf(
                Attachment(fileName = "2025-03호 공고문 (PDF)", registeredDate = "2025.06.02 등록"),
                Attachment(fileName = "입주자 모집 안내 책자", registeredDate = "2025.06.02 등록"),
                Attachment(fileName = "서울주택도시공사 청약 신청 매뉴얼", registeredDate = "2025.05.28 등록")
            ),
            moveInDate = "2025년 9월",
            ageRange = "만 19세 ~ 39세",
            incomeStandard = "도시근로자 월평균 소득 100% 이하",
            assetStandard = "총 자산 3억 6,100만 원 이하",
            housingOwnership = "무주택 세대구성원",
            residencyRequirement = "서울시 거주 또는 직장 소재",
            winnerAnnouncementDate = "2025년 7월 4일 (금)",
            contractPeriod = "2025년 7월 14일 ~ 7월 18일",
            photoResIds = listOf(
                R.drawable.img_recruitment_1,
                R.drawable.img_recruitment_2,
                R.drawable.img_recruitment_3,
                R.drawable.img_recruitment_4
            )
        )
    }
}
