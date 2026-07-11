package com.umc.homefit.navigation

import androidx.annotation.DrawableRes
import com.umc.homefit.R

sealed class BottomNavItem(
    val route: TabRoute,
    val title: String,
    @DrawableRes val iconRes: Int
) {
    data object Home : BottomNavItem(TabRoute.Home, "홈", R.drawable.ic_nav_home)
    data object Recruitment : BottomNavItem(TabRoute.RecruitmentList, "공고", R.drawable.ic_nav_recruitment)
    data object Analysis : BottomNavItem(TabRoute.Analysis, "분석", R.drawable.ic_nav_analysis)
    data object Finance : BottomNavItem(TabRoute.Finance, "금융", R.drawable.ic_nav_finance)
    data object MyPage : BottomNavItem(TabRoute.MyPage, "마이", R.drawable.ic_nav_my)

    companion object {
        val items = listOf(Home, Recruitment, Analysis, Finance, MyPage)
    }
}
