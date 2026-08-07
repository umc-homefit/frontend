package com.umc.homefit.presentation.component

import com.umc.homefit.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 여러 화면(금융정보 입력, 로그인, 회원가입 등)에서 공통으로 쓰이는
 * "상단 타이틀 + 콘텐츠 + 하단 링크 + 하단 버튼" 레이아웃
 * FinancialInfoScreen의 각 Step에서도 동일하게 사용
 */
@Composable
fun StepBaseLayout(
    title: String,
    onNext: () -> Unit,
    isNextEnabled: Boolean,
    bottomLinkText: String,
    onBottomLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String = "다음",
    contentSpacing: Dp = 76.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val primaryColor = Color(0xFF3C45F3)
    val disabledButtonColor = Color(0xFFF0F4F9)
    val disabledButtonTextColor = Color(0xFF919AA4)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(contentSpacing)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF161616)
            )
            content()
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (bottomLinkText.isNotEmpty()) {
                Text(
                    text = bottomLinkText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4A4F55),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onBottomLinkClick() }
                )
            }

            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        brush = if (isNextEnabled) {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = 1f),
                                    primaryColor.copy(alpha = 0.5f)
                                )
                            )
                        } else {
                            Brush.horizontalGradient(
                                colors = listOf(disabledButtonColor, disabledButtonColor)
                            )
                        },
                        shape = RoundedCornerShape(4.dp)
                    ),
                enabled = isNextEnabled,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = disabledButtonTextColor
                )
            ) {
                Text(
                    text = buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * 완료 화면 공용 컴포넌트
 * 금융정보 입력 완료, 로그인 완료, 회원가입 완료 등에서
 * title / buttonText / onButtonClick 만 바꿔서 재사용
 * 애니메이션과 레이아웃은 항상 동일
 */
@Composable
fun CompletionStep(
    title: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    StepBaseLayout(
        title = title,
        onNext = onButtonClick,
        isNextEnabled = true,
        bottomLinkText = "",
        onBottomLinkClick = {},
        modifier = modifier,
        buttonText = buttonText,
        contentSpacing = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            // 캐릭터
            Image(
                painter = painterResource(id = R.drawable.ic_analysis_result),
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.Center)
            )

            val starTargets = listOf(
                80.dp to (-165).dp,     // 우상단
                (-70).dp to (-240).dp,  // 좌상단
                (-140).dp to 60.dp,     // 좌하단
                140.dp to 100.dp,       // 우하단
                (-20).dp to 260.dp,     // 버튼 위
            )

            val startPoint = 0.dp to 320.dp

            starTargets.forEachIndexed { index, target ->
                FireworkStar(
                    startOffset = startPoint,
                    targetOffset = target,
                    delayMillis = index * 150,
                )
            }
        }
    }
}

@Composable
internal fun BoxScope.FireworkStar(
    startOffset: Pair<Dp, Dp>,
    targetOffset: Pair<Dp, Dp>,
    delayMillis: Int,
) {
    val offsetX = remember { Animatable(startOffset.first.value) }
    val offsetY = remember { Animatable(startOffset.second.value) }
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        while (true) {
            launch {
                offsetX.animateTo(
                    targetOffset.first.value,
                    animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing)
                )
            }
            launch {
                offsetY.animateTo(
                    targetOffset.second.value,
                    animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing)
                )
            }
            launch {
                alpha.animateTo(1f, animationSpec = tween(durationMillis = 300))
            }
            scale.animateTo(
                1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            delay(1200)

            launch {
                offsetX.animateTo(
                    startOffset.first.value,
                    animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
                )
            }
            launch {
                offsetY.animateTo(
                    startOffset.second.value,
                    animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
                )
            }
            launch {
                scale.animateTo(0.3f, animationSpec = tween(durationMillis = 450))
            }
            alpha.animateTo(0f, animationSpec = tween(durationMillis = 400))

            delay(400)
        }
    }

    Image(
        painter = painterResource(id = R.drawable.ic_analysis_star),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .align(Alignment.Center)
            .size(40.dp)
            .graphicsLayer {
                translationX = offsetX.value.dp.toPx()
                translationY = offsetY.value.dp.toPx()
                this.alpha = alpha.value
                scaleX = scale.value
                scaleY = scale.value
            }
    )
}
