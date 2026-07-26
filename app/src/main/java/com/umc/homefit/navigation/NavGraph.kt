package com.umc.homefit.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.collectAsState
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
import com.umc.homefit.presentation.auth.LoginFlowScreenRoute
import com.umc.homefit.presentation.auth.LoginScreenRoute
import com.umc.homefit.presentation.auth.SignUpScreenRoute
import com.umc.homefit.presentation.finance.*
import com.umc.homefit.presentation.home.*
import com.umc.homefit.presentation.mypage.*
import com.umc.homefit.presentation.recruitment.*
import com.umc.homefit.R
import com.umc.homefit.ui.component.AppScaffold

private const val FILTER_RESULT_KEY = "filter_result"

@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login,
        modifier = modifier.fillMaxSize()
    ) {
        composable<Route.Login> {
            LoginScreenRoute(
                onNavigateToHome = {
                    navController.navigate(Route.Main) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Route.SignUp) },
                onNavigateToLoginFlow = { navController.navigate(Route.LoginFlow) }
            )
        }

        composable<Route.LoginFlow> {
            LoginFlowScreenRoute(
                onBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Route.Main) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.SignUp> {
            SignUpScreenRoute(
                onBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Route.Main) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.Main> {
            MainScreen(rootNavController = navController)
        }

        composable<Route.RecruitmentFilter> {
            RecruitmentFilterScreenRoute(
                viewModel = hiltViewModel(),
                onApply = { filterState ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(FILTER_RESULT_KEY, filterState)
                    navController.popBackStack()
                },
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
                onBack = { navController.popBackStack() },
                onNavigateToAnalysis = {
                    navController.navigate(Route.FinancialInfo)
                }
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

        composable<Route.FinancialInfoEdit> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.FinancialInfoEdit>()
            FinancialInfoEditScreenRoute(
                step = args.step,
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
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
                onNavigateToEdit = { step -> navController.navigate(Route.FinancialInfoEdit(step)) }
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
    
    
    val rootBackStackEntry by rootNavController.currentBackStackEntryAsState()
    val filterResult = rootBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<FilterState?>(FILTER_RESULT_KEY, null)
        ?.collectAsState()
        ?.value

    val isRecommendedProductScreen =
        currentDestination?.route.orEmpty().contains(
            TabRoute.RecommendedProduct::class.qualifiedName.orEmpty()
        )

    val isProductSearchScreen =
        currentDestination?.route.orEmpty().contains(
            TabRoute.ProductSearch::class.qualifiedName.orEmpty()
        )


    val title: String? = when {
        currentDestination?.route?.contains(TabRoute.Home::class.qualifiedName.orEmpty()) == true -> "홈"
        currentDestination?.route?.contains(TabRoute.RecruitmentList::class.qualifiedName.orEmpty()) == true -> "공고"
        currentDestination?.route?.contains(TabRoute.Analysis::class.qualifiedName.orEmpty()) == true -> "입주 분석"
        currentDestination?.route?.contains(TabRoute.Finance::class.qualifiedName.orEmpty()) == true -> "금융 상품"
        isRecommendedProductScreen -> "추천 금융 상품"
        isProductSearchScreen -> "금융 상품 검색"
        currentDestination?.route?.contains(TabRoute.MyPage::class.qualifiedName.orEmpty()) == true -> "마이페이지"
        else -> "HomeFit"
    }


    AppScaffold(
        title = title,
        showBackButton = isProductSearchScreen,
        onBackClick = {
            tabNavController.popBackStack()
        },
        showDivider = isProductSearchScreen,
        bottomBar = {
            if (!isProductSearchScreen) {
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
                            val currentRoute =
                                currentDestination?.route.orEmpty()

                            val itemRouteName =
                                item.route::class.qualifiedName.orEmpty()

                            val isFinanceSubRoute =
                                currentRoute.contains(
                                    TabRoute.RecommendedProduct::class
                                        .qualifiedName
                                        .orEmpty()
                                )

                            val isFinanceItem =
                                item.route is TabRoute.Finance

                            val isSelected =
                                currentRoute.contains(itemRouteName) ||
                                    (
                                        isFinanceItem &&
                                            isFinanceSubRoute
                                        )

                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    if (item.route is TabRoute.Finance) {
                                        tabNavController.navigate(
                                            TabRoute.Finance
                                        ) {
                                            launchSingleTop = true

                                            popUpTo<TabRoute.Finance> {
                                                inclusive = false
                                                saveState = false
                                            }

                                            restoreState = false
                                        }
                                    } else {
                                        tabNavController.navigate(item.route) {
                                            popUpTo(
                                                tabNavController.graph
                                                    .findStartDestination()
                                                    .id
                                            ) {
                                                saveState = true
                                            }

                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(
                                            id = item.iconRes
                                        ),
                                        contentDescription = item.title,
                                        tint = if (isSelected) {
                                            Color.Black
                                        } else {
                                            Color.Gray
                                        },
                                        modifier = Modifier.size(30.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Normal
                                        }
                                    )
                                },
                                colors =
                                    NavigationBarItemDefaults.colors(
                                        selectedTextColor = Color.Black,
                                        unselectedTextColor = Color.Gray,
                                        indicatorColor = Color.Transparent
                                    )
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        val navHostPadding =
            if (
                isRecommendedProductScreen ||
                isProductSearchScreen
            ) {
                PaddingValues(
                    bottom = innerPadding.calculateBottomPadding()
                )
            } else {
                innerPadding
            }

        NavHost(
            navController = tabNavController,
            startDestination = TabRoute.Home,
            modifier = Modifier
                .fillMaxSize()
                .padding(navHostPadding)
        ) {
            composable<TabRoute.Home> {
                HomeScreenRoute(
                    viewModel = hiltViewModel(),
                    onNavigateToDetail = { recruitmentId ->
                        rootNavController.navigate(Route.RecruitmentDetail(recruitmentId))
                    }
                )
            }
            composable<TabRoute.RecruitmentList> {
                RecruitmentListScreenRoute(
                    viewModel = hiltViewModel(),
                    filterResult = filterResult,
                    onFilterConsumed = {
                        rootBackStackEntry?.savedStateHandle?.remove<FilterState>(FILTER_RESULT_KEY)
                    },
                    onNavigateToFilter = {
                        rootNavController.navigate(Route.RecruitmentFilter)
                    },
                    onNavigateToDetail = { recruitmentId ->
                        rootNavController.navigate(Route.RecruitmentDetail(recruitmentId))
                    }
                )
            }
            composable<TabRoute.Analysis> {
                AnalysisScreenRoute(
                    viewModel = hiltViewModel(),
                    onNavigateToFinancialInfo = {
                        rootNavController.navigate(Route.FinancialInfo)
                    }
                )
            }
            composable<TabRoute.Finance> {
                FinanceScreenRoute(
                    viewModel = hiltViewModel(),
                    onNavigateToRecommendedProducts = {
                        tabNavController.navigate(
                            TabRoute.RecommendedProduct
                        )
                    }
                )
            }
            composable<TabRoute.ProductSearch> {
                ProductSearchScreenRoute(
                    viewModel = hiltViewModel(),
                    onBack = {
                        tabNavController.popBackStack()
                    },
                    onSearchComplete = { keyword ->
                        tabNavController
                            .previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(
                                "productSearchQuery",
                                keyword
                            )

                        tabNavController.popBackStack()
                    }
                )
            }
            composable<TabRoute.RecommendedProduct> { backStackEntry ->
                val searchQuery by backStackEntry.savedStateHandle
                    .getStateFlow(
                        key = "productSearchQuery",
                        initialValue = ""
                    )
                    .collectAsState()

                RecommendedProductScreenRoute(
                    viewModel = hiltViewModel(),
                    searchQuery = searchQuery,
                    onBack = {
                        tabNavController.popBackStack()
                    },
                    onNavigateToSearch = {
                        tabNavController.navigate(
                            TabRoute.ProductSearch
                        )
                    },
                    onNavigateToDetail = { productId ->
                        rootNavController.navigate(
                            Route.ProductDetail(productId)
                        )
                    }
                )
            }
            composable<TabRoute.MyPage> {
                MyPageScreenRoute(
                    viewModel = hiltViewModel(),
                    onNavigateToSaved = {
                        rootNavController.navigate(Route.SavedRecruitment)
                    },
                    onNavigateToNotification = {
                        rootNavController.navigate(Route.NotificationSetting)
                    },
                    onNavigateToFinance = {
                        rootNavController.navigate(Route.MyFinance)
                    }
                )
            }

        }
    }
}
