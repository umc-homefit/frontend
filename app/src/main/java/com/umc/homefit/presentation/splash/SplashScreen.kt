package com.umc.homefit.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.homefit.R
import kotlinx.coroutines.delay

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

    LaunchedEffect(Unit) {
        delay(500)

        logoAlpha.animateTo(1f, tween(400, easing = LinearOutSlowInEasing))
        logoScale.animateTo(1f, tween(400, easing = LinearOutSlowInEasing))

        delay(200)

        textAlpha.animateTo(1f, tween(350, easing = LinearOutSlowInEasing))

        delay(700)

        onNavigateToMain()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_splash_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                    }
            )
            Text(
                text = "HomeFit",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF4B4B4B),
                modifier = Modifier.graphicsLayer { alpha = textAlpha.value }
            )
        }
    }
}
