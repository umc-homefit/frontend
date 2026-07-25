package com.umc.homefit.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // 1. 위에서 by 키워드 사용을 위해 필수 임포트
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.umc.homefit.R

@Composable
fun SplashScreenRoute(
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()

    SplashScreen(
        onAnimationFinished = {
            when (loginState) {
                is SplashState.Authenticated -> onNavigateToMain()
                is SplashState.Unauthenticated, SplashState.Loading -> onNavigateToLogin()
            }
        },
        modifier = modifier
    )
}

@Composable
fun SplashScreen(
    onAnimationFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.7f) }
    val textAlpha = remember { Animatable(0f) }

    val textWidthTarget = 95.dp
    val textWidth = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(500)

        logoAlpha.animateTo(1f, tween(400, easing = LinearOutSlowInEasing))
        logoScale.animateTo(1f, tween(400, easing = LinearOutSlowInEasing))

        delay(200)

        launch {
            textWidth.animateTo(textWidthTarget.value, tween(350, easing = LinearOutSlowInEasing))
        }
        launch {
            textAlpha.animateTo(1f, tween(350, easing = LinearOutSlowInEasing))
        }

        delay(700)

        onAnimationFinished()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(if (textWidth.value > 0f) 5.dp else 0.dp)
        ) {
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

            Box(
                modifier = Modifier
                    .height(28.dp)
                    .width(textWidth.value.dp)
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
