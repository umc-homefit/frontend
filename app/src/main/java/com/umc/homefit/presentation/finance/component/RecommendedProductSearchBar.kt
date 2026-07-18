package com.umc.homefit.presentation.finance.component


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RecommendedProductSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onBarClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    if (!readOnly) {
                        onSearchQueryChange(it)
                    }
                },
                modifier = Modifier.fillMaxSize(),
                placeholder = {
                    Text(
                        text = "상품명, 은행명 등으로 검색"
                    )
                },
                readOnly = readOnly,
                singleLine = true,
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    bottomStart = 4.dp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F4F9),
                    unfocusedContainerColor = Color(0xFFF0F4F9),
                    disabledContainerColor = Color(0xFFF0F4F9),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color(0xFF919AA4),
                    unfocusedPlaceholderColor = Color(0xFF919AA4)
                )
            )

            if (readOnly && onBarClick != null) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            onBarClick()
                        }
                )
            }
        }

        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF3C45F3).copy(alpha = 0.8f),
                            Color(0xFF3C45F3).copy(alpha = 0.4f)
                        ),
                        start = Offset.Zero,
                        end = Offset(
                            x = 0f,
                            y = Float.POSITIVE_INFINITY
                        )
                    ),
                    shape = RoundedCornerShape(
                        topEnd = 4.dp,
                        bottomEnd = 4.dp
                    )
                )
                .clickable {
                    if (readOnly) {
                        onBarClick?.invoke()
                    } else {
                        onSearchClick()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색",
                tint = Color.White
            )
        }
    }
}
