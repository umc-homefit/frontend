package com.umc.homefit.presentation.finance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umc.homefit.data.dto.finance.FinanceProductCategory
import com.umc.homefit.presentation.finance.component.RecommendedProductCard
import com.umc.homefit.presentation.theme.HomeFitTheme
import com.umc.homefit.presentation.finance.component.RecommendedProductSearchBar
import com.umc.homefit.presentation.component.AppScaffold
import com.umc.homefit.presentation.component.ErrorStateView
import com.umc.homefit.presentation.component.RefreshOnResume
import com.umc.homefit.presentation.finance.component.ConditionProfileRequiredContent

private val ProductAccent = Color(0xFF3C45F3)
private val ProductBorder = Color(0xFFDCE2E9)
private val ProductTextGray = Color(0xFF919AA4)

private enum class ProductSort(
    val label: String,
    val apiValue: String
) {
    RECOMMENDED("추천순", "RECOMMENDED"),
    LATEST("최신순", "LATEST"),
    LOWEST_RATE("금리 낮은순", "RATE_ASC"),
    HIGHEST_AMOUNT("대출한도 높은순", "LIMIT_DESC")
}

@Composable
fun RecommendedProductScreenRoute(
    viewModel: RecommendedProductScreenViewModel,
    searchQuery: String,
    onNavigateToSearch: () -> Unit,
    onNavigateToFinancialInfo: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RefreshOnResume(onResume = viewModel::refresh)

    RecommendedProductScreen(
        uiState = uiState,
        searchQuery = searchQuery,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToFinancialInfo = onNavigateToFinancialInfo,
        onNavigateToDetail = onNavigateToDetail,
        onFilterChanged = { sort, category, keyword ->
            viewModel.loadRecommendedProducts(
                sort = sort,
                category = category,
                keyword = keyword
            )
        },
        onRetry = viewModel::retry,
        modifier = modifier
    )
}

private data class ProductFilterOption(
    val label: String,
    val category: FinanceProductCategory?
)

private val productFilterOptions = listOf(
    ProductFilterOption(
        label = "전체",
        category = null
    ),
    ProductFilterOption(
        label = "주택담보대출",
        category = FinanceProductCategory.MORTGAGE_LOAN
    ),
    ProductFilterOption(
        label = "전세대출",
        category = FinanceProductCategory.JEONSE_LOAN
    )
    // TODO: 서버 /loan-products/match의 productCategory가 SUBSCRIPTION_SAVINGS를 지원하면 "청약저축" 칩 복원
)

@Composable
private fun ProductListHeader(
    totalCount: Int,
    selectedSort: ProductSort,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSortSelected: (ProductSort) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "총 ${totalCount}개 상품",
            color = ProductTextGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 17.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Box {

            Row(
                modifier = Modifier
                    .clickable {
                        onExpandedChange(true)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = selectedSort.label,
                    color = ProductTextGray,
                    style = MaterialTheme.typography.bodyMedium
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF919AA4)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    onExpandedChange(false)
                },
                modifier = Modifier
                    .width(140.dp)
                    .background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFD2D9E2),
                        shape = RoundedCornerShape(4.dp)
                    ),
                containerColor = Color(0xFFFFFFFF),
                shape = RoundedCornerShape(4.dp),
                shadowElevation = 0.dp,
                tonalElevation = 0.dp
            ) {
                ProductSort.entries.forEach { sort ->
                    val isSelected = sort == selectedSort

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = sort.label,
                                textAlign = TextAlign.Center,
                                color = if (isSelected) {
                                    Color(0xFF4A4F55)
                                } else {
                                    ProductTextGray
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {
                            onSortSelected(sort)
                            onExpandedChange(false)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        colors = MenuDefaults.itemColors(
                            textColor = ProductTextGray
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedProductScreen(
    uiState: RecommendedProductScreenUiState,
    searchQuery: String,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToFinancialInfo: () -> Unit,
    onFilterChanged: (sort: String, category: String?, keyword: String?) -> Unit,
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = null,
        modifier = modifier,
        showBackButton = false
    ) { innerPadding ->
        RecommendedProductContent(
            uiState = uiState,
            searchQuery = searchQuery,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToFinancialInfo = onNavigateToFinancialInfo,
            onFilterChanged = onFilterChanged,
            onRetry = onRetry,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}


@Composable
private fun RecommendedProductContent(
    uiState: RecommendedProductScreenUiState,
    searchQuery: String,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToFinancialInfo: () -> Unit,
    onFilterChanged: (sort: String, category: String?, keyword: String?) -> Unit,
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember {
        mutableStateOf<FinanceProductCategory?>(null)
    }

    var selectedSort by remember {
        mutableStateOf(ProductSort.RECOMMENDED)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var isInitialized by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(searchQuery) {
        if (isInitialized) {
            onFilterChanged(
                selectedSort.apiValue,
                selectedCategory?.name,
                searchQuery.ifBlank { null }
            )
        } else {
            isInitialized = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        RecommendedProductSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = {},
            onSearchClick = onNavigateToSearch,
            readOnly = true,
            onBarClick = onNavigateToSearch
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 4.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = productFilterOptions,
                key = { option ->
                    option.label
                }
            ) { option ->
                ProductFilterChip(
                    label = option.label,
                    selected = selectedCategory == option.category,
                    onClick = {
                        selectedCategory = option.category
                        onFilterChanged(
                            selectedSort.apiValue,
                            option.category?.name,
                            searchQuery.ifBlank { null }
                        )
                    }
                )
            }
        }

        when (uiState) {
            is RecommendedProductScreenUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is RecommendedProductScreenUiState.Success -> {
                val filteredProducts = uiState.products

                if (filteredProducts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "조건에 맞는 금융 상품을 찾지 못했어요",
                                color = Color(0xFF18191B),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "다른 카테고리나 검색어로 다시 찾아보세요",
                                color = ProductTextGray,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    ProductListHeader(
                        totalCount = filteredProducts.size,
                        selectedSort = selectedSort,
                        expanded = expanded,
                        onExpandedChange = { isExpanded ->
                            expanded = isExpanded
                        },
                        onSortSelected = { sort ->
                            selectedSort = sort
                            onFilterChanged(
                                sort.apiValue,
                                selectedCategory?.name,
                                searchQuery.ifBlank { null }
                            )
                        }
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = filteredProducts,
                            key = { product ->
                                product.productId
                            }
                        ) { product ->
                            RecommendedProductCard(
                                product = product,
                                onClick = {
                                    onNavigateToDetail(
                                        product.productId
                                    )
                                }
                            )
                        }
                    }
                }
            }

            RecommendedProductScreenUiState.ConditionProfileRequired -> {
                ConditionProfileRequiredContent(
                    onNavigateToFinancialInfo = onNavigateToFinancialInfo
                )
            }

            is RecommendedProductScreenUiState.Error -> {
                ErrorStateView(message = uiState.message, onRetry = onRetry)
            }
        }
    }
}

@Composable
private fun ProductFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) {
        ProductAccent
    } else {
        ProductBorder
    }

    val textColor = if (selected) {
        ProductAccent
    } else {
        ProductTextGray
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(120.dp),
        color = Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 5.dp
            )
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun RecommendedProductScreenPreview() {
    HomeFitTheme {
        RecommendedProductScreen(
            uiState = RecommendedProductScreenUiState.Success(
                products = listOf(
                    FinanceRecommendedProductUiModel(
                        productId = 1,
                        title = "디딤돌 대출",
                        productType = "정부지원",
                        interestRate = "연 2.15% ~ 3.00%",
                        amountDescription = "대출한도 | 최대 2억 5,000만 원",
                        targetDescription = "연소득 | 6,000만 원 이하",
                        tags = listOf("무주택자", "생애최초")
                    )
                )
            ),
            searchQuery = "",
            onNavigateToSearch = {},
            onNavigateToFinancialInfo = {},
            onNavigateToDetail = {},
            onFilterChanged = { _, _, _ -> }
        )
    }
}
