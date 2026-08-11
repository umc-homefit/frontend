package com.umc.homefit.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Main : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object SignUp : Route

    @Serializable
    data object LoginFlow : Route

    @Serializable
    data object RecruitmentFilter : Route

    @Serializable
    data class RecruitmentDetail(val recruitmentId: String, val analysisId: String? = null) : Route

    @Serializable
    data class Competition(val recruitmentId: String, val analysisId: String? = null) : Route

    @Serializable
    data class FinancialInfo(val noticeId: Long? = null, val unitId: Long? = null) : Route

    @Serializable
    data class FinancialInfoEdit(val step: com.umc.homefit.presentation.analysis.FinancialInfoStep) : Route

    @Serializable
    data class AnalysisResult(val analysisId: String, val fromRecord: Boolean = false) : Route

    @Serializable
    data class EstimatedCost(val resultId: String) : Route

    @Serializable
    data class ProductDetail(val productId: Long) : Route

    @Serializable
    data object ProductSearch : Route

    @Serializable
    data object SavedRecruitment : Route

    @Serializable
    data object Notification : Route

    @Serializable
    data object NotificationSetting : Route

    @Serializable
    data object MyFinance : Route
}

sealed interface TabRoute {
    @Serializable
    data object Home : TabRoute

    @Serializable
    data class RecruitmentList(val searchQuery: String = "") : TabRoute

    @Serializable
    data object RecruitmentSearch : TabRoute

    @Serializable
    data object Analysis : TabRoute

    @Serializable
    data object Finance : TabRoute

    @Serializable
    data object RecommendedProduct : TabRoute

    @Serializable
    data object MyPage : TabRoute
}
