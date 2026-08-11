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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            .padding(horizontal = 16.dp)
            .then(
                if (readOnly) {
                    Modifier.clickable { onBarClick?.invoke() }
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            if (readOnly) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color(0xFFF0F4F9),
                            RoundedCornerShape(
                                topStart = 4.dp,
                                bottomStart = 4.dp
                            )
                        )
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = searchQuery.ifEmpty { "상품명, 은행명 등으로 검색" },
                        fontSize = 14.sp,
                        color = if (searchQuery.isEmpty()) {
                            Color(0xFF919AA4)
                        } else {
                            Color.Black
                        }
                    )
                }
            } else {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxSize(),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Black
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color(0xFFF0F4F9),
                                    RoundedCornerShape(
                                        topStart = 4.dp,
                                        bottomStart = 4.dp
                                    )
                                )
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "상품명, 은행명 등으로 검색",
                                    fontSize = 14.sp,
                                    color = Color(0xFF919AA4)
                                )
                            }

                            innerTextField()
                        }
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF3C45F3).copy(alpha = 1f),
                            Color(0xFF3C45F3).copy(alpha = 0.5f)
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
