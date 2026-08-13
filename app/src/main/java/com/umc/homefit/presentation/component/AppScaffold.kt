package com.umc.homefit.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppScaffold(
    title: String?,
    modifier: Modifier = Modifier,
    titleContent: (@Composable () -> Unit)? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: List<TopBarAction> = emptyList(),
    showDivider: Boolean = false,
    centerTitle: Boolean = false,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            if (title != null || titleContent != null || actions.isNotEmpty() ) {
                AppTopBar(
                    title = title,
                    titleContent = titleContent,
                    showBackButton = showBackButton,
                    onBackClick = onBackClick,
                    actions = actions,
                    showDivider = showDivider,
                    centerTitle = centerTitle
                )
            }
        },
        bottomBar = bottomBar,
        contentWindowInsets = WindowInsets.systemBars,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        // Box에서 padding을 먼저 적용하지 않고, innerPadding을 그대로 content에 넘겨서
        // 각 화면이 필요에 맞게 한 번만 적용하도록 한다 (Scaffold의 일반적인 사용 패턴)
        // 이 부분 코드 건들지 마세요...!
        content(innerPadding)
    }
}
