package com.umc.homefit.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import com.umc.homefit.R
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
import androidx.compose.ui.res.painterResource
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
    showDivider: Boolean = false,
    centerTitle: Boolean = false
) {
    val dividerColor = Color(0xFFF0F4F9)

    val titleComposable: @Composable () -> Unit = {
        if (titleContent != null) {
            titleContent()
        } else {
            title?.let {
                Text(
                    text = it,
                    fontSize = if (showBackButton || centerTitle) 16.sp else 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    val navigationIconComposable: @Composable () -> Unit = {
        if (showBackButton) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_top_arrow_back),
                    contentDescription = "Back",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF919AA4)
                )
            }
        }
    }

    val actionsComposable: @Composable () -> Unit = {
        actions.forEach { action ->
            IconButton(onClick = action.onClick) {
                Icon(
                    painter = action.icon,
                    contentDescription = action.contentDescription,
                    tint = Color.Unspecified // 벡터 XML에 지정된 원래 색을 그대로 사용
                )
            }
        }
    }

    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent
    )

    Column (modifier = modifier) {
        if (showBackButton || centerTitle) {
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
