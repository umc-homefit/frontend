package com.umc.homefit.presentation.recruitment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.presentation.theme.RecruitmentAccent
import com.umc.homefit.presentation.theme.RecruitmentTextGray
import com.umc.homefit.presentation.theme.SearchFieldBackground

private val SearchBarTextColor = Color(0xFF18191B)

/**
 * 공고 검색창. `readOnly = true`로 쓰면 입력은 막고 탭 시 [onBarClick]으로 검색 화면으로 이동시키는
 * 용도로 쓸 수 있다(공고 목록 화면 진입점), `readOnly = false`면 실제 입력/검색 화면에서 쓰인다.
 */
@Composable
fun RecruitmentSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onBarClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
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
                .fillMaxHeight()
                .background(
                    color = SearchFieldBackground,
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        bottomStart = 4.dp
                    )
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            if (readOnly) {
                Text(
                    text = searchQuery.ifEmpty { "공고명, 지하철역명, 단지명 등으로 검색" },
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = if (searchQuery.isEmpty()) RecruitmentTextGray else SearchBarTextColor,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = SearchBarTextColor,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    cursorBrush = SolidColor(RecruitmentAccent),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch(searchQuery)
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "공고명, 지하철역명, 단지명 등으로 검색",
                                    color = RecruitmentTextGray,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = LocalTextStyle.current.copy(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    )
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
                .width(48.dp)
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            RecruitmentAccent.copy(alpha = 1f),
                            RecruitmentAccent.copy(alpha = 0.5f)
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
                        onSearch(searchQuery)
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
