package com.umc.homefit.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.umc.homefit.presentation.component.AppScaffold
import com.umc.homefit.presentation.component.AutoDismissInfoSnackbar

private val BannerBackground = Color(0xFFF0F4F9)
private val BannerText = Color(0xFF4A4F55)
private val ChipSelectedColor = Color(0xFF3C45F3)
private val ChipUnselectedBorder = Color(0xFFD2D9E2)
private val ChipUnselectedText = Color(0xFF919AA4)

@Composable
fun InterestedRegionScreenRoute(
    viewModel: InterestedRegionScreenViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    InterestedRegionScreen(
        uiState = uiState,
        onBack = onBack,
        onSelectRegion = viewModel::onSelectRegion,
        modifier = modifier
    )
}

@Composable
fun InterestedRegionScreen(
    uiState: InterestedRegionScreenUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onSelectRegion: (String) -> Unit = {},
) {
    var toastMessage by remember { mutableStateOf<String?>(null) }

    AppScaffold(
        title = "관심 지역 설정",
        showBackButton = true,
        onBackClick = onBack,
        showDivider = true,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is InterestedRegionScreenUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is InterestedRegionScreenUiState.Success -> {
                    InterestedRegionContent(
                        regions = uiState.regions,
                        selectedRegion = uiState.selectedRegion,
                        onSelectRegion = { region ->
                            onSelectRegion(region)
                            toastMessage = "관심 지역이 ${region}로 설정되었습니다."
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is InterestedRegionScreenUiState.Error -> {
                    Text(text = "Error: ${uiState.message}", modifier = Modifier.align(Alignment.Center))
                }
            }

            AutoDismissInfoSnackbar(
                visible = toastMessage != null,
                message = toastMessage.orEmpty(),
                onDismiss = { toastMessage = null }
            )
        }
    }
}

@Composable
private fun InterestedRegionContent(
    regions: List<String>,
    selectedRegion: String,
    onSelectRegion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 38.dp
            )
    ) {
        Text(
            text = "*선택한 지역에 신규 공고가 등록되면 알려드립니다.",
            fontSize = 12.sp,
            lineHeight = 14.sp,
            color = BannerText,
            modifier = Modifier
                .fillMaxWidth()
                .background(BannerBackground, RoundedCornerShape(4.dp))
                .padding(horizontal = 5.dp, vertical = 10.dp)
        )

        Text(
            text = "지역",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 37.dp, bottom = 21.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(regions) { region ->
                RegionChip(
                    region = region,
                    selected = region == selectedRegion,
                    onClick = { onSelectRegion(region) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun RegionChip(
    region: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Surface(onClick = ...) 오버로드는 minimumInteractiveComponentSize()(최소 48dp)를 강제로 적용해
    // 칩이 텍스트보다 훨씬 커지고 그리드 행 간격이 벌어져 보이므로, 클릭 없는 오버로드 + clickable로 대체
    Surface(
        shape = RoundedCornerShape(120.dp),
        color = Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) ChipSelectedColor else ChipUnselectedBorder
        ),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = region,
            color = if (selected) ChipSelectedColor else ChipUnselectedText,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            lineHeight = 17.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InterestedRegionScreenPreview() {
    InterestedRegionScreen(
        uiState = InterestedRegionScreenUiState.Success(
            regions = listOf("전체", "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구"),
            selectedRegion = "강남구"
        ),
        onBack = {}
    )
}
