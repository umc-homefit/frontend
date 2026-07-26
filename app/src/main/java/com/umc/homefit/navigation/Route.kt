package com.umc.homefit.navigation

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
    data class RecruitmentDetail(val recruitmentId: String) : Route

    @Serializable
    data class Competition(val recruitmentId: String) : Route

    @Serializable
    data object FinancialInfo : Route

    @Serializable
    data class FinancialInfoEdit(val step: com.umc.homefit.presentation.analysis.FinancialInfoStep) : Route

    @Serializable
    data class AnalysisResult(val analysisId: String) : Route

    @Serializable
    data class EstimatedCost(val resultId: String) : Route

    @Serializable
    data class ProductDetail(val productId: Long) : Route

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
    data class RecruitmentList(
        val searchQuery: String = ""
    ) : TabRoute

    @Serializable
    data object RecruitmentSearch : TabRoute

    @Serializable
    data object Analysis : TabRoute

    @Serializable
    data object Finance : TabRoute
  
    @Serializable
    data object ProductSearch : TabRoute

    @Serializable
    data object RecommendedProduct : TabRoute

    @Serializable
    data object MyPage : TabRoute
}
