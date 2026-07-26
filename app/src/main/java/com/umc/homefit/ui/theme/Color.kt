package com.umc.homefit.ui.theme

// TODO: Figma 색상/폰트로 교체

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val PrimaryLight = Color(0xFF1E3A8A) // Deep Navy
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFDBEAFE)
val OnPrimaryContainerLight = Color(0xFF1E40AF)

val SecondaryLight = Color(0xFF0D9488) // Teal/Emerald
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFCCFBF1)
val OnSecondaryContainerLight = Color(0xFF115E59)

val BackgroundLight = Color(0xFFFFFFFF) // Off-white
val OnBackgroundLight = Color(0xFF0F172A)
val SurfaceLight = Color(0xFFFFFFFF)
val OnSurfaceLight = Color(0xFF0F172A)

// Dark Theme Colors
val PrimaryDark = Color(0xFF93C5FD)
val OnPrimaryDark = Color(0xFF1E3A8A)
val PrimaryContainerDark = Color(0xFF1E40AF)
val OnPrimaryContainerDark = Color(0xFFDBEAFE)

val SecondaryDark = Color(0xFF5EEAD4)
val OnSecondaryDark = Color(0xFF0F766E)
val SecondaryContainerDark = Color(0xFF115E59)
val OnSecondaryContainerDark = Color(0xFFCCFBF1)

val BackgroundDark = Color(0xFF0F172A)
val OnBackgroundDark = Color(0xFFF8FAFC)
val SurfaceDark = Color(0xFF1E293B)
val OnSurfaceDark = Color(0xFFF8FAFC)

// Recruitment 화면 Figma 색상
val RecruitmentAccent = Color(0xFF3C45F3)
val RecruitmentBorder = Color(0xFFD2D9E2)
val RecruitmentTextGray = Color(0xFF919AA4)
val SearchFieldBackground = Color(0xFFF0F4F9)

val StatusRecruitingBackground = Color(0xFFF1F0FF)
val StatusRecruitingText = Color(0xFF3C45F3)
val StatusScheduledBackground = Color(0xFFF0F4F9)
val StatusScheduledText = Color(0xFF4A4F55)
val StatusClosingSoonBackground = Color(0xFFFFF6F6)
val StatusClosingSoonText = Color(0xFFFF5659)

val CompetitionRateBackground = Color(0xFFF0F4F9)
val CompetitionRateText = Color(0xFF4A4F55)

val BookmarkActive = Color(0xFFFF5659)
val BookmarkInactive = Color(0xFFD2D9E2)

// 회원가입/로그인 단계별 검증 메시지 색상
val ValidationSuccessText = Color(0x8019A141)
val ValidationErrorText = Color(0x80FF5659)

// 공고 상세 화면 Figma 색상
val TextBlack = Color(0xFF161616)
val AnalysisButtonGradient = Brush.horizontalGradient(
    listOf(RecruitmentAccent, RecruitmentAccent.copy(alpha = 0.5f))
)
