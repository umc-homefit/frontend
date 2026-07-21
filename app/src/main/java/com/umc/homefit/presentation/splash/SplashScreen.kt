package com.umc.homefit.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.umc.homefit.R

@Composable
fun SplashScreenRoute(
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier
) {
    SplashScreen(
        onNavigateToMain = onNavigateToMain,
        modifier = modifier
    )
}

@Composable
fun SplashScreen(
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.7f) }
    val textAlpha = remember { Animatable(0f) }

    // 텍스트가 차지할 가로 너비를 제어하는 애니메이션 (0dp -> 텍스트 실제 너비)
    // 이미지 너비에 맞게 적절히 조절하십시오. 여기서는 임의로 100dp로 가정했습니다.
    val textWidthTarget = 100.dp
    val textWidth = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(500)

        // 1. 로고 중앙에서 등장
        logoAlpha.animateTo(1f, tween(400, easing = LinearOutSlowInEasing))
        logoScale.animateTo(1f, tween(400, easing = LinearOutSlowInEasing))

        delay(200)

        // 2. 텍스트가 너비를 확장하며 로고를 왼쪽으로 밀어냄과 동시에 페이드인
        launch {
            textWidth.animateTo(textWidthTarget.value, tween(350, easing = LinearOutSlowInEasing))
        }
        launch {
            textAlpha.animateTo(1f, tween(350, easing = LinearOutSlowInEasing))
        }

        delay(700)
        onNavigateToMain()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            // 텍스트 너비가 0일 때는 간격이 생기지 않도록 가변적으로 조절
            horizontalArrangement = Arrangement.spacedBy(if (textWidth.value > 0f) 5.dp else 0.dp)
        ) {
            // 로고 이미지 (Row 내부의 너비 변화에 따라 자연스럽게 왼쪽으로 밀림)
            Image(
                painter = painterResource(id = R.drawable.ic_splash_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(62.dp)
                    .graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                    }
            )

            // 텍스트를 감싸는 Box의 너비를 0에서부터 서서히 키움
            Box(
                modifier = Modifier
                    .height(28.dp)
                    .width(textWidth.value.dp) // 애니메이션되는 너비 적용
                    .graphicsLayer { alpha = textAlpha.value }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_splash_text),
                    contentDescription = null,
                    modifier = Modifier.height(28.dp)
                )
            }
        }
    }
}
