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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umc.homefit.data.dto.RecommendedProductDto
import com.umc.homefit.data.mock.FinanceMockData
import com.umc.homefit.presentation.finance.component.RecommendedProductCard
import com.umc.homefit.presentation.finance.component.RecommendedProductSearchBar
import com.umc.homefit.ui.component.AppScaffold

private val ProductAccent = Color(0xFF3C45F3)
private val ProductBorder = Color(0xFFDCE2E9)
private val ProductTextGray = Color(0xFF919AA4)

private enum class ProductSort(
    val label: String
) {
    RECOMMENDED("추천순"),
    LATEST("최신순"),
    LOWEST_RATE("금리 낮은순"),
    HIGHEST_AMOUNT("대출한도 높은순")
}

@Composable
fun RecommendedProductScreenRoute(
    viewModel: RecommendedProductScreenViewModel,
    searchQuery: String,
    onNavigateToSearch: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecommendedProductScreen(
        uiState = uiState,
        searchQuery = searchQuery,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier
    )
}

private data class ProductFilterOption(
    val label: String,
    val keyword: String?
)

private val productFilterOptions = listOf(
    ProductFilterOption(
        label = "전체",
        keyword = null
    ),
    ProductFilterOption(
        label = "주택담보대출",
        keyword = "주택"
    ),
    ProductFilterOption(
        label = "전세대출",
        keyword = "전세"
    ),
    ProductFilterOption(
        label = "청약저축",
        keyword = "청약"
    )
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
                start = 20.dp,
                end = 20.dp,
                top = 25.dp,
                bottom = 3.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "총 ${totalCount}개 상품",
            color = ProductTextGray,
            style = MaterialTheme.typography.bodyMedium
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
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFD2D9E2),
                        shape = RoundedCornerShape(8.dp)
                    ),
                containerColor = Color(0xFFFFFFFF),
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 0.dp,
                tonalElevation = 0.dp
            ) {
                ProductSort.entries.forEach { sort ->
                    val isSelected = sort == selectedSort

                    DropdownMenuItem(
                        text = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = sort.label,
                                    textAlign = TextAlign.Center,
                                    color = if (isSelected) {
                                        Color(0xFF4A4F55)
                                    } else {
                                        ProductTextGray
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        onClick = {
                            onSortSelected(sort)
                            onExpandedChange(false)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp),
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
    modifier: Modifier = Modifier
) {
    var selectedKeyword by remember {
        mutableStateOf<String?>(null)
    }

    var selectedSort by remember {
        mutableStateOf(ProductSort.RECOMMENDED)
    }

    var expanded by remember {
        mutableStateOf(false)
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
                    top = 12.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(17.dp)
        ) {
            items(
                items = productFilterOptions,
                key = { option ->
                    option.label
                }
            ) { option ->
                ProductFilterChip(
                    label = option.label,
                    selected = selectedKeyword == option.keyword,
                    onClick = {
                        selectedKeyword = option.keyword
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
                val filteredProducts =
                    uiState.products.filter { product ->
                        val matchesCategory =
                            selectedKeyword == null ||
                                productMatchesKeyword(
                                    product = product,
                                    keyword = selectedKeyword.orEmpty()
                                )

                        val matchesSearch =
                            searchQuery.isBlank() ||
                                productMatchesKeyword(
                                    product = product,
                                    keyword = searchQuery
                                )

                        matchesCategory && matchesSearch
                    }

                val sortedProducts: List<RecommendedProductDto> =
                    when (selectedSort) {
                        ProductSort.RECOMMENDED,
                        ProductSort.LATEST -> {
                            filteredProducts
                        }

                        ProductSort.LOWEST_RATE -> {
                            filteredProducts.sortedBy { product ->
                                parseMinimumInterestRate(
                                    product.interestRate
                                )
                            }
                        }

                        ProductSort.HIGHEST_AMOUNT -> {
                            filteredProducts.sortedByDescending { product ->
                                parseLoanAmount(
                                    product.amountDescription
                                )
                            }
                        }
                    }

                if (sortedProducts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "조건에 맞는 금융 상품이 없습니다.",
                            color = ProductTextGray
                        )
                    }
                } else {
                    ProductListHeader(
                        totalCount = sortedProducts.size,
                        selectedSort = selectedSort,
                        expanded = expanded,
                        onExpandedChange = { isExpanded ->
                            expanded = isExpanded
                        },
                        onSortSelected = { sort ->
                            selectedSort = sort
                        }
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 20.dp,
                            vertical = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = sortedProducts,
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

            is RecommendedProductScreenUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${uiState.message}"
                    )
                }
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
        shape = RoundedCornerShape(50),
        color = Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            modifier = Modifier.padding(
                horizontal = 13.dp,
                vertical = 5.dp
            )
        )
    }
}

private fun productMatchesKeyword(
    product: RecommendedProductDto,
    keyword: String
): Boolean {
    return product.title.contains(
        other = keyword,
        ignoreCase = true
    ) ||
        product.productType.contains(
            other = keyword,
            ignoreCase = true
        ) ||
        product.interestRate.contains(
            other = keyword,
            ignoreCase = true
        ) ||
        product.amountDescription.contains(
            other = keyword,
            ignoreCase = true
        ) ||
        product.targetDescription.contains(
            other = keyword,
            ignoreCase = true
        ) ||
        product.tags.any { tag ->
            tag.contains(
                other = keyword,
                ignoreCase = true
            )
        }
}

private fun parseMinimumInterestRate(
    interestRate: String
): Double {
    return Regex("""\d+(?:\.\d+)?""")
        .find(interestRate)
        ?.value
        ?.toDoubleOrNull()
        ?: Double.MAX_VALUE
}

private fun parseLoanAmount(
    amountDescription: String
): Long {
    val amountText = amountDescription
        .substringAfter("|", amountDescription)
        .replace(",", "")
        .replace(" ", "")

    var totalAmount = 0L

    Regex("""(\d+)억""")
        .find(amountText)
        ?.groupValues
        ?.getOrNull(1)
        ?.toLongOrNull()
        ?.let { value ->
            totalAmount += value * 100_000_000L
        }

    Regex("""(\d+)천만""")
        .find(amountText)
        ?.groupValues
        ?.getOrNull(1)
        ?.toLongOrNull()
        ?.let { value ->
            totalAmount += value * 10_000_000L
        }

    Regex("""(\d+)백만""")
        .find(amountText)
        ?.groupValues
        ?.getOrNull(1)
        ?.toLongOrNull()
        ?.let { value ->
            totalAmount += value * 1_000_000L
        }

    if (
        "억" !in amountText &&
        "천만" !in amountText &&
        "백만" !in amountText
    ) {
        Regex("""(\d+)만""")
            .find(amountText)
            ?.groupValues
            ?.getOrNull(1)
            ?.toLongOrNull()
            ?.let { value ->
                totalAmount += value * 10_000L
            }
    }

    return totalAmount
}

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun RecommendedProductScreenPreview() {
    RecommendedProductScreen(
        uiState = RecommendedProductScreenUiState.Success(
            products = FinanceMockData.recommendedProducts
        ),
        searchQuery = "",
        onNavigateToSearch = {},
        onNavigateToDetail = {}
    )
}
