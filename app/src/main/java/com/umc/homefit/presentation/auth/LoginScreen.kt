package com.umc.homefit.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.width
import com.umc.homefit.R

private val KakaoYellow = Color(0xFFFEE500)
private val GoogleBorderColor = Color(0xFFD2D9E2)
private val PrimaryColor = Color(0xFF3C45F3)

@Composable
fun LoginScreenRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToLoginFlow: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoginScreen(
        onNavigateToHome = onNavigateToHome,
        onNavigateToSignUp = onNavigateToSignUp,
        onNavigateToLoginFlow = onNavigateToLoginFlow,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToLoginFlow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_main_icon),
                contentDescription = null,
                modifier = Modifier.size(95.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

        }

        Spacer(modifier = Modifier.weight(1f))

        // 카카오로 로그인
        Button(
            onClick = onNavigateToHome,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = KakaoYellow,
                contentColor = Color.Black
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_kakao_icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "카카오로 로그인", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Google로 로그인
        OutlinedButton(
            onClick = onNavigateToHome,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, GoogleBorderColor),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Google로 로그인", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 이메일로 로그인
        Button(
            onClick = onNavigateToLoginFlow,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PrimaryColor, PrimaryColor.copy(alpha = 0.5f))
                    ),
                    shape = RoundedCornerShape(6.dp)
                ),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        ) {
            Text(text = "이메일로 로그인", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(27.dp))

        Text(
            text = "계정이 없으신가요?",
            fontSize = 14.sp,
            color = Color.Gray,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = onNavigateToSignUp)
        )

        Spacer(modifier = Modifier.height(39.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onNavigateToHome = {},
        onNavigateToSignUp = {},
        onNavigateToLoginFlow = {}
    )
}
