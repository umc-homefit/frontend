package com.umc.homefit.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.umc.homefit.presentation.analysis.*
import com.umc.homefit.presentation.finance.*
import com.umc.homefit.presentation.home.*
import com.umc.homefit.presentation.mypage.*
import com.umc.homefit.presentation.recruitment.*
import com.umc.homefit.ui.component.AppScaffold

@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Main,
        modifier = modifier.fillMaxSize()
    ) {
        composable<Route.Main> {
            MainScreen(rootNavController = navController)
        }

        composable<Route.RecruitmentFilter> {
            RecruitmentFilterScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.RecruitmentDetail> {
            RecruitmentDetailScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onNavigateToCompetition = { recruitmentId ->
                    navController.navigate(Route.Competition(recruitmentId))
                },
                onNavigateToAnalysis = {
                    navController.navigate(Route.FinancialInfo)
                }
            )
        }

        composable<Route.Competition> {
            CompetitionScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.FinancialInfo> {
            FinancialInfoScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onNavigateToResult = { analysisId ->
                    navController.navigate(Route.AnalysisResult(analysisId))
                }
            )
        }

        composable<Route.AnalysisResult> {
            AnalysisResultScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onNavigateToEstimatedCost = { resultId ->
                    navController.navigate(Route.EstimatedCost(resultId))
                }
            )
        }

        composable<Route.EstimatedCost> {
            EstimatedCostScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }


        composable<Route.ProductDetail> {
            ProductDetailScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.SavedRecruitment> {
            SavedRecruitmentScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.Notification> {
            NotificationScreenRoute(
                viewModel =
                    hiltViewModel<NotificationScreenViewModel>(),
                onBack = {
                    navController.popBackStack()
                },
                onSettingsClick = {
                    navController.navigate(
                        Route.NotificationSetting
                    )
                },
                onNotificationClick = { _ ->
                    // TODO 개별 알림 클릭 처리
                }
            )
        }

        composable<Route.NotificationSetting> {
            NotificationSettingScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.MyFinance> {
            MyFinanceScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate(Route.FinancialInfo) }
            )
        }
    }
}

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isHomeDestination =
        currentDestination?.route?.contains(
            TabRoute.Home::class.qualifiedName.orEmpty()
        ) == true

    val isRecruitmentSearchDestination =
        currentDestination?.route?.contains(
            TabRoute.RecruitmentSearch::class.qualifiedName.orEmpty()
        ) == true

    val title = when {
        currentDestination?.route?.contains(TabRoute.RecruitmentList::class.qualifiedName.orEmpty()) == true -> "공고 조회"
        currentDestination?.route?.contains(TabRoute.Analysis::class.qualifiedName.orEmpty()) == true -> "입주 분석"
        currentDestination?.route?.contains(TabRoute.Finance::class.qualifiedName.orEmpty()) == true -> "금융 상품 추천"
        currentDestination?.route?.contains(TabRoute.MyPage::class.qualifiedName.orEmpty()) == true -> "마이페이지"
        else -> "HomeFit"
    }

    AppScaffold(
        title = if (
            isHomeDestination ||
            isRecruitmentSearchDestination
            ) {
            null
        } else {
            title
        },
        showBackButton = false,
        bottomBar = {
            if (!isRecruitmentSearchDestination) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0f),
                                    Color.Black.copy(alpha = 0.05f)
                                )
                            )
                        )
                )

                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp,
                    modifier = Modifier.height(120.dp)
                ) {
                    BottomNavItem.items.forEach { item ->
                        val isSelected = currentDestination?.route
                            ?.contains(item.route::class.qualifiedName.orEmpty()) == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                when (item.route) {
                                    TabRoute.Home -> {
                                        val popped =
                                            tabNavController.popBackStack(
                                                route = TabRoute.Home,
                                                inclusive = false
                                            )

                                        if (!popped) {
                                            tabNavController.navigate(
                                                TabRoute.Home
                                            ) {
                                                launchSingleTop = true
                                            }
                                        }
                                    }

                                    else -> {
                                        tabNavController.navigate(item.route) {
                                            popUpTo<TabRoute.Home> {
                                                saveState = true
                                            }

                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.iconRes),
                                    contentDescription = item.title,
                                    tint = if (isSelected) Color.Black else Color.Gray,
                                    modifier = Modifier.size(30.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedTextColor = Color.Black,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        } },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = TabRoute.Home,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable<TabRoute.Home> {
                HomeScreenRoute(
                    viewModel = hiltViewModel(),
                    onNotificationClick = {
                        rootNavController.navigate(
                            Route.Notification
                        )
                    },
                    onNavigateToDetail = { recruitmentId ->
                        rootNavController.navigate(
                            Route.RecruitmentDetail(
                                recruitmentId
                            )
                        )
                    },

                    // 검색창 → 공고 검색 화면
                    onSearchClick = {
                        tabNavController.navigate(
                            TabRoute.RecruitmentSearch
                        )
                    },

                    // 전체 공고 보기 → 공고 목록
                    onAllAnnouncementClick = {
                        tabNavController.navigate(
                            TabRoute.RecruitmentList()
                        ) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    // 관심 공고 → 저장 공고 화면
                    onFavoriteClick = {
                        rootNavController.navigate(
                            Route.SavedRecruitment
                        )
                    },

                    // 입주 분석 → 분석 탭
                    onAnalysisClick = {
                        tabNavController.navigate(
                            TabRoute.Analysis
                        ) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    // 금융 상품 → 금융 탭
                    onFinanceClick = {
                        tabNavController.navigate(
                            TabRoute.Finance
                        ) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable<TabRoute.RecruitmentSearch> {
                RecruitmentSearchScreenRoute(
                    viewModel = hiltViewModel(),
                    onBack = {
                        tabNavController.popBackStack()
                    },
                    onSearchComplete = { searchQuery ->
                        tabNavController.navigate(
                            TabRoute.RecruitmentList(
                                searchQuery = searchQuery
                            )
                        ) {
                            popUpTo<TabRoute.Home> {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }


            composable<TabRoute.RecruitmentList> { backStackEntry ->
                val route =
                    backStackEntry.toRoute<TabRoute.RecruitmentList>()

                RecruitmentListScreenRoute(
                    viewModel = hiltViewModel(),
                    initialSearchQuery = route.searchQuery,
                    onNavigateToFilter = {
                        rootNavController.navigate(
                            Route.RecruitmentFilter
                        )
                    },
                    onNavigateToDetail = { recruitmentId ->
                        rootNavController.navigate(
                            Route.RecruitmentDetail(recruitmentId)
                        )
                    },
                    onNavigateToSearch = {
                        tabNavController.navigate(
                            TabRoute.RecruitmentSearch
                        ) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<TabRoute.Analysis> {
                AnalysisScreenRoute(
                    viewModel = hiltViewModel(),
                    onNavigateToFinancialInfo = {
                        rootNavController.navigate(
                            Route.FinancialInfo
                        )
                    }
                )
            }

            composable<TabRoute.Finance> {
                FinanceScreenRoute(
                    viewModel = hiltViewModel(),
                    onNavigateToRecommendedProducts = {
                        rootNavController.navigate(
                            Route.RecommendedProduct
                        )
                    }
                )
            }

            composable<TabRoute.MyPage> {
                MyPageScreenRoute(
                    viewModel = hiltViewModel(),
                    onNavigateToSaved = {
                        rootNavController.navigate(
                            Route.SavedRecruitment
                        )
                    },
                    onNavigateToNotification = {
                        rootNavController.navigate(
                            Route.NotificationSetting
                        )
                    },
                    onNavigateToFinance = {
                        rootNavController.navigate(
                            Route.MyFinance
                        )
                    }
                )
            }
        }
    }
}
