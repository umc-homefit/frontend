package com.umc.homefit.presentation.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.umc.homefit.presentation.splash.SplashScreenRoute
import com.umc.homefit.presentation.component.AppScaffold

private const val FILTER_RESULT_KEY = "filter_result"
private const val NAVIGATE_TO_TAB_KEY = "navigate_to_tab"
private const val PRODUCT_SEARCH_RESULT_KEY = "product_search_result"

@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Splash,
        modifier = modifier.fillMaxSize()
    ) {
        composable<Route.Splash> {
            SplashScreenRoute(
                onNavigateToMain = {
                    navController.navigate(Route.Main) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                }
            )
        }

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
            val currentFilter = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.getStateFlow<FilterState?>(FILTER_RESULT_KEY, null)
                ?.collectAsState()
                ?.value

            RecruitmentFilterScreenRoute(
                viewModel = hiltViewModel(),
                initialFilter = currentFilter ?: FilterState(),
                onApply = { filterState ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(FILTER_RESULT_KEY, filterState)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.RecruitmentDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.RecruitmentDetail>()
            RecruitmentDetailScreenRoute(
                viewModel = hiltViewModel(),
                analysisId = args.analysisId,
                onBack = { navController.popBackStack() },
                onNavigateToAnalysis = { noticeId, unitId ->
                    navController.navigate(Route.FinancialInfo(noticeId = noticeId.toLongOrNull(), unitId = unitId))
                },
                onNavigateToAnalysisResult = { analysisId ->
                    navController.navigate(Route.AnalysisResult(analysisId = analysisId, fromRecord = true))
                }
            )
        }

        composable<Route.Competition> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.Competition>()
            CompetitionScreenRoute(
                viewModel = hiltViewModel(),
                analysisId = args.analysisId,
                onBack = { navController.popBackStack() },
                onNavigateToAnalysis = { noticeId, unitId ->
                    navController.navigate(Route.FinancialInfo(noticeId = noticeId.toLongOrNull(), unitId = unitId))
                },
                onNavigateToAnalysisResult = { analysisId ->
                    navController.navigate(Route.AnalysisResult(analysisId = analysisId, fromRecord = true))
                }
            )
        }

        composable<Route.FinancialInfo> {
            FinancialInfoScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onNavigateToResult = { analysisId -> navController.navigate(Route.AnalysisResult(analysisId)) }
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

        composable<Route.AnalysisResult> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.AnalysisResult>()
            AnalysisResultScreenRoute(
                viewModel = hiltViewModel(),
                fromRecord = args.fromRecord,
                onBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Route.Main) {
                        popUpTo(Route.Main) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRecommendedProduct = {
                    navController.navigate(Route.Main) {
                        popUpTo(Route.Main) { inclusive = true }
                        launchSingleTop = true
                    }
                    navController.currentBackStackEntry?.savedStateHandle?.set(NAVIGATE_TO_TAB_KEY, "recommendedProduct")
                }
            )
        }

        composable<Route.EstimatedCost> {
            EstimatedCostScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.ProductDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.ProductDetail>()
            ProductDetailScreenRoute(
                productId = route.productId,
                viewModel = hiltViewModel<ProductDetailScreenViewModel>(),
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.Notification> {
            NotificationScreenRoute(
                viewModel = hiltViewModel<NotificationScreenViewModel>(),
                onBack = { navController.popBackStack() },
                onSettingsClick = { navController.navigate(Route.NotificationSetting) },
                onNotificationClick = { _ -> /* TODO 개별 알림 클릭 처리 */ }
            )
        }

        composable<Route.NotificationSetting> {
            NotificationSettingScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.ProductSearch> {
            ProductSearchScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onSearchComplete = { keyword ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(PRODUCT_SEARCH_RESULT_KEY, keyword)
                    navController.popBackStack()
                }
            )
        }

        composable<Route.SavedRecruitment> {
            SavedRecruitmentScreenRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onNavigateToDetail = { recruitmentId -> navController.navigate(Route.RecruitmentDetail(recruitmentId)) }
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
    val currentRoute = currentDestination?.route.orEmpty()
    val rootBackStackEntry by rootNavController.currentBackStackEntryAsState()

    val filterResult = rootBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<FilterState?>(FILTER_RESULT_KEY, null)
        ?.collectAsState()
        ?.value

    val requestedTab = rootBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>(NAVIGATE_TO_TAB_KEY, null)
        ?.collectAsState()
        ?.value

    val productSearchResult = rootBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>(PRODUCT_SEARCH_RESULT_KEY, null)
        ?.collectAsState()
        ?.value

    LaunchedEffect(requestedTab) {
        when (requestedTab) {
            "recruitment" -> tabNavController.navigateToTab(TabRoute.RecruitmentList())
            "analysis" -> tabNavController.navigateToTab(TabRoute.Analysis)
            "finance" -> tabNavController.navigateToTab(TabRoute.Finance)
            "recommendedProduct" -> tabNavController.navigateToTab(TabRoute.RecommendedProduct)
            "mypage" -> tabNavController.navigateToTab(TabRoute.MyPage)
        }

        if (requestedTab != null) {
            rootBackStackEntry?.savedStateHandle?.remove<String>(NAVIGATE_TO_TAB_KEY)
        }
    }

    val isHomeDestination = currentRoute.contains(TabRoute.Home::class.qualifiedName.orEmpty())
    val isRecruitmentSearchDestination = currentRoute.contains(TabRoute.RecruitmentSearch::class.qualifiedName.orEmpty())
    val isAnalysisTab = currentRoute.contains(TabRoute.Analysis::class.qualifiedName.orEmpty())

    val title = when {
        isHomeDestination -> null
        isRecruitmentSearchDestination -> null
        currentRoute.contains(TabRoute.RecruitmentList::class.qualifiedName.orEmpty()) -> "공고"
        isAnalysisTab -> "분석"
        currentRoute.contains(TabRoute.Finance::class.qualifiedName.orEmpty()) -> "금융 상품"
        currentRoute.contains(TabRoute.RecommendedProduct::class.qualifiedName.orEmpty()) -> "추천 금융 상품"
        currentRoute.contains(TabRoute.MyPage::class.qualifiedName.orEmpty()) -> "마이페이지"
        else -> "HomeFit"
    }

    AppScaffold(
        title = title,
        showBackButton = false, // 필요없을 것 같으면 코드 생략
        onBackClick = { tabNavController.popBackStack() },
        centerTitle = isAnalysisTab,
        showDivider = isAnalysisTab,
        bottomBar = {
            if (!isRecruitmentSearchDestination) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp,
                    modifier = Modifier.height(120.dp)
                ) {
                    BottomNavItem.items.forEach { item ->
                        val itemRouteName = item.route::class.qualifiedName.orEmpty()
                        val isFinanceSubRoute = currentRoute.contains(TabRoute.RecommendedProduct::class.qualifiedName.orEmpty())
                        val isFinanceItem = item.route is TabRoute.Finance
                        val isSelected = currentRoute.contains(itemRouteName) || (isFinanceItem && isFinanceSubRoute)

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                // 금융 탭을 벗어나는 경우에만 추천 상품 검색어를 초기화한다.
                                // (금융 탭을 다시 누르는 건 같은 섹션에 머무는 것이므로 지우지 않음)
                                if (item.route !is TabRoute.Finance) {
                                    rootBackStackEntry?.savedStateHandle
                                        ?.set<String?>(PRODUCT_SEARCH_RESULT_KEY, null)
                                }
                                // 공고 탭을 벗어나는 경우에만 적용해둔 필터를 초기화한다(검색어와 동일한 이유).
                                if (item.route !is TabRoute.RecruitmentList) {
                                    rootBackStackEntry?.savedStateHandle
                                        ?.set<FilterState?>(FILTER_RESULT_KEY, null)
                                }

                                when (item.route) {
                                    TabRoute.Home -> {
                                        val popped = tabNavController.popBackStack(route = TabRoute.Home, inclusive = false)
                                        if (!popped) {
                                            tabNavController.navigateToTab(TabRoute.Home)
                                        }
                                    }
                                    else -> tabNavController.navigateToTab(item.route)
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
        },
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
                    onNotificationClick = { rootNavController.navigate(Route.Notification) },
                    onNavigateToDetail = { recruitmentId -> rootNavController.navigate(Route.RecruitmentDetail(recruitmentId)) },
                    onSearchClick = { tabNavController.navigate(TabRoute.RecruitmentSearch) },
                    onAllAnnouncementClick = { tabNavController.navigateToTab(TabRoute.RecruitmentList()) },
                    onFavoriteClick = { rootNavController.navigate(Route.SavedRecruitment) },
                    onAnalysisClick = { tabNavController.navigateToTab(TabRoute.Analysis) },
                    onFinanceClick = { tabNavController.navigateToTab(TabRoute.Finance) }
                )
            }

            composable<TabRoute.RecruitmentList> { backStackEntry ->
                val route = backStackEntry.toRoute<TabRoute.RecruitmentList>()

                RecruitmentListScreenRoute(
                    viewModel = hiltViewModel(),
                    filterResult = filterResult,
                    // 적용된 필터 값은 지우지 않고 남겨둔다 — 필터 화면을 다시 열었을 때 이 값으로 미리 채워야 하기 때문.
                    // (탭을 벗어날 때는 바텀탭 onClick에서 별도로 초기화한다)
                    onFilterConsumed = {},
                    onNavigateToFilter = { rootNavController.navigate(Route.RecruitmentFilter) },
                    onNavigateToDetail = { recruitmentId -> rootNavController.navigate(Route.RecruitmentDetail(recruitmentId)) },
                    onNavigateToSearch = { tabNavController.navigate(TabRoute.RecruitmentSearch) },
                    initialSearchQuery = route.searchQuery
                )
            }

            composable<TabRoute.RecruitmentSearch> {
                RecruitmentSearchScreenRoute(
                    viewModel = hiltViewModel(),
                    onBack = { tabNavController.popBackStack() },
                    onSearchComplete = { searchQuery ->
                        tabNavController.navigate(TabRoute.RecruitmentList(searchQuery = searchQuery)) {
                            popUpTo<TabRoute.Home> { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<TabRoute.Analysis> {
                AnalysisScreenRoute(
                    viewModel = hiltViewModel(),
                    onNavigateToEdit = { step -> rootNavController.navigate(Route.FinancialInfoEdit(step)) },
                    onNavigateToDetail = { noticeId, analysisId ->
                        rootNavController.navigate(Route.RecruitmentDetail(recruitmentId = noticeId, analysisId = analysisId))
                    }
                )
            }

            composable<TabRoute.Finance> {
                FinanceScreenRoute(
                    viewModel = hiltViewModel(),
                    onNavigateToRecommendedProducts = { tabNavController.navigate(TabRoute.RecommendedProduct) },
                    onNavigateToFinancialInfo = { rootNavController.navigate(Route.FinancialInfo()) },
                    onNavigateToDetail = { productId -> rootNavController.navigate(Route.ProductDetail(productId = productId)) }
                )
            }

            composable<TabRoute.RecommendedProduct> {
                RecommendedProductScreenRoute(
                    viewModel = hiltViewModel(),
                    searchQuery = productSearchResult.orEmpty(),
                    onNavigateToSearch = { rootNavController.navigate(Route.ProductSearch) },
                    onNavigateToFinancialInfo = { rootNavController.navigate(Route.FinancialInfo()) },
                    onNavigateToDetail = { productId -> rootNavController.navigate(Route.ProductDetail(productId = productId)) },
                    onClearSearch = { rootBackStackEntry?.savedStateHandle?.set<String?>(PRODUCT_SEARCH_RESULT_KEY, null) },
                    onBack = { tabNavController.popBackStack() }
                )
            }

            composable<TabRoute.MyPage> {
                MyPageScreenRoute(
                    viewModel = hiltViewModel(),
                    onNavigateToSaved = { rootNavController.navigate(Route.SavedRecruitment) },
                    onNavigateToNotification = { rootNavController.navigate(Route.NotificationSetting) },
                    onNavigateToFinance = { rootNavController.navigate(Route.MyFinance) }
                )
            }
        }
    }
}

private fun NavHostController.navigateToTab(route: TabRoute) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        // 금융 탭과 공고 탭은 바텀탭으로 재진입할 때 항상 새 상태(검색어 없음)로 시작한다.
        // restoreState = true면 이전에 저장된 백스택(이전 검색어 포함)이 새로 넘긴 인자를 무시하고
        // 그대로 복원되기 때문에, 탭을 나갔다가 돌아왔을 때만 검색어가 초기화되도록 여기서 막는다.
        restoreState = route !is TabRoute.Finance && route !is TabRoute.RecruitmentList
    }
}
