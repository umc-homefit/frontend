package com.umc.homefit.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TopBarAction(
    val icon: Painter,
    val contentDescription: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleContent: (@Composable () -> Unit)? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: List<TopBarAction> = emptyList(),
    showDivider: Boolean = true
) {
    val dividerColor = Color(0xFFF0F4F9)

    val titleComposable: @Composable () -> Unit = {
        if (titleContent != null) {
            titleContent()
        } else {
            title?.let {
                Text(
                    text = it,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    val navigationIconComposable: @Composable () -> Unit = {
        if (showBackButton) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    }

    val actionsComposable: @Composable () -> Unit = {
        actions.forEach { action ->
            IconButton(onClick = action.onClick) {
                Icon(
                    painter = action.icon,
                    contentDescription = action.contentDescription
                )
            }
        }
    }

    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent
    )

    Column (modifier = modifier) {
        if (showBackButton) {
            CenterAlignedTopAppBar(
                title = titleComposable,
                navigationIcon = navigationIconComposable,
                actions = { actionsComposable() },
                colors = colors
            )
        } else {
            TopAppBar(
                title = titleComposable,
                navigationIcon = navigationIconComposable,
                actions = { actionsComposable() },
                colors = colors
            )
        }

        if (showDivider) {
            HorizontalDivider(
                color = dividerColor,
                thickness = 2.dp
            )
        }
    }
}
