package com.umc.homefit.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umc.homefit.R

/**
 * 아직 서버에서 데이터를 제공하지 않는 화면/영역에 표시하는 "준비 중" 안내 뷰.
 * (예: 경쟁률 탭 - 백엔드 API 준비 전)
 */
@Composable
fun PreparingStateView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_recruitment_empty),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_recruitment_preparing_text),
            contentDescription = "준비 중"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreparingStateViewPreview() {
    PreparingStateView()
}
