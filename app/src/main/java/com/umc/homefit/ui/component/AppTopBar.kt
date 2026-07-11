package com.umc.homefit.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: List<TopBarAction> = emptyList()
) {
    val titleComposable: @Composable () -> Unit = {
        title?.let {
            Text(
                text = it,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
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

    if (showBackButton) {
        CenterAlignedTopAppBar(
            title = titleComposable,
            navigationIcon = navigationIconComposable,
            actions = { actionsComposable() },
            colors = colors,
            modifier = modifier
        )
    } else {
        TopAppBar(
            title = titleComposable,
            navigationIcon = navigationIconComposable,
            actions = { actionsComposable() },
            colors = colors,
            modifier = modifier
        )
    }
}
