package com.umc.homefit.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val showTopBar = title != null || titleContent != null || showBackButton
    Scaffold(
        topBar = {
            if (showTopBar) {
                AppTopBar(
                    title = title,
                    titleContent = titleContent,
                    showBackButton = showBackButton,
                    onBackClick = onBackClick,
                    actions = actions,
                    showDivider = showDivider
                )
            }
        },
        bottomBar = bottomBar,
        contentWindowInsets = WindowInsets.systemBars,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content(innerPadding)
        }
    }
}
