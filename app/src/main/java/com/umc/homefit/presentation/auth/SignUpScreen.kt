package com.umc.homefit.presentation.auth

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.umc.homefit.ui.component.AppScaffold
import com.umc.homefit.ui.component.CompletionStep
import com.umc.homefit.ui.component.StepBaseLayout
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import com.umc.homefit.R
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.runtime.saveable.rememberSaveable

enum class SignUpStep {
    EMAIL,
    PASSWORD,
    PASSWORD_CONFIRM,
    COMPLETE
}

@Composable
fun SignUpScreenRoute(
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    SignUpScreen(
        onBack = onBack,
        onNavigateToHome = onNavigateToHome,
        modifier = modifier
    )
}

@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by rememberSaveable { mutableStateOf(SignUpStep.EMAIL) }
    var savedPassword by rememberSaveable { mutableStateOf("") }

    val handleBackClick = {
        when (currentStep) {
            SignUpStep.EMAIL -> onBack()
            SignUpStep.PASSWORD -> currentStep = SignUpStep.EMAIL
            SignUpStep.PASSWORD_CONFIRM -> currentStep = SignUpStep.PASSWORD
            SignUpStep.COMPLETE -> currentStep = SignUpStep.PASSWORD_CONFIRM
        }
    }

    val progress = when (currentStep) {
        SignUpStep.EMAIL -> 0f
        SignUpStep.PASSWORD -> 0.5f
        SignUpStep.PASSWORD_CONFIRM -> 1.0f
        SignUpStep.COMPLETE -> 1.0f
    }

    AppScaffold(
        title = "회원가입",
        showBackButton = true,
        onBackClick = handleBackClick,
        showDivider = true,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (currentStep != SignUpStep.COMPLETE) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp),
                        color = Color(0xFF3C45F3),
                        trackColor = Color(0xFFF0F4F9)
                    )
                }

                Crossfade(
                    targetState = currentStep,
                    label = "SignUpStepTransition",
                    modifier = Modifier.weight(1f)
                ) { step ->
                    when (step) {
                        SignUpStep.EMAIL -> {
                            EmailStep(
                                onNext = { currentStep = SignUpStep.PASSWORD }
                            )
                        }

                        SignUpStep.PASSWORD -> {
                            PasswordStep(
                                onNext = { enteredPassword ->
                                    savedPassword = enteredPassword
                                    currentStep = SignUpStep.PASSWORD_CONFIRM
                                }
                            )
                        }

                        SignUpStep.PASSWORD_CONFIRM -> {
                            PasswordConfirmStep(
                                originalPassword = savedPassword,
                                onNext = { currentStep = SignUpStep.COMPLETE }
                            )
                        }

                        SignUpStep.COMPLETE -> {
                            CompletionStep(
                                title = "회원가입이 완료되었습니다",
                                buttonText = "홈으로",
                                onButtonClick = onNavigateToHome
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmailStep(
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    val emailRegex = remember { Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") }
    val isValidFormat = emailRegex.matches(email)

    StepBaseLayout(
        title = "회원가입을 시작해볼까요?",
        onNext = onNext,
        isNextEnabled = isValidFormat,
        bottomLinkText = "",
        onBottomLinkClick = {},
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "이메일",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF919AA4)
            )

            Spacer(modifier = Modifier.height(21.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = "example@email.com",
                        fontSize = 16.sp,
                        color = Color(0xFFD2D9E2)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { email = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_signup_clear),
                            contentDescription = "지우기",
                            tint = Color.Unspecified
                        )
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFD2D9E2),
                    unfocusedIndicatorColor = Color(0xFFD2D9E2)
                )
            )

            if (isValidFormat) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_signup_correct),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "올바른 이메일 형식입니다",
                        color = Color(0x8019A141),
                        fontSize = 12.sp
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_signup_wrong),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "이메일 형식이 맞지 않습니다",
                        color = Color(0x80FF5659),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PasswordStep(
    onNext: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var password by rememberSaveable { mutableStateOf("") }
    val passwordRegex = remember {
        Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$")
    }
    val isValidFormat = passwordRegex.matches(password)

    StepBaseLayout(
        title = "비밀번호를 입력해주세요",
        onNext = { onNext(password) },
        isNextEnabled = isValidFormat,
        bottomLinkText = "",
        onBottomLinkClick = {},
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "비밀번호",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF919AA4)
            )

            Spacer(modifier = Modifier.height(21.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(
                        text = "Home2026#",
                        fontSize = 16.sp,
                        color = Color(0xFFD2D9E2)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { password = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_signup_clear),
                            contentDescription = "지우기",
                            tint = Color.Unspecified
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFD2D9E2),
                    unfocusedIndicatorColor = Color(0xFFD2D9E2)
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            when {
                password.isEmpty() -> {
                    Text(
                        text = "8자 이상 · 영문·숫자·특수문자 포함",
                        color = Color(0xFF919AA4),
                        fontSize = 12.sp
                    )
                }
                isValidFormat -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_signup_correct),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "올바른 비밀번호 형식입니다",
                            color = Color(0x8019A141),
                            fontSize = 12.sp
                        )
                    }
                }
                else -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_signup_wrong),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "비밀번호 형식이 맞지 않습니다",
                            color = Color(0x80FF5659),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PasswordConfirmStep(
    originalPassword: String,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordConfirm by rememberSaveable { mutableStateOf("") }
    val isMatching = passwordConfirm.isNotEmpty() && passwordConfirm == originalPassword

    StepBaseLayout(
        title = "비밀번호를 확인해주세요",
        onNext = onNext,
        isNextEnabled = isMatching,
        bottomLinkText = "",
        onBottomLinkClick = {},
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "비밀번호 확인",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF919AA4)
            )

            Spacer(modifier = Modifier.height(21.dp))

            TextField(
                value = passwordConfirm,
                onValueChange = { passwordConfirm = it },
                placeholder = {
                    Text(
                        text = "Home2026#",
                        fontSize = 16.sp,
                        color = Color(0xFFD2D9E2)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordConfirm = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_signup_clear),
                            contentDescription = "지우기",
                            tint = Color.Unspecified
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFD2D9E2),
                    unfocusedIndicatorColor = Color(0xFFD2D9E2)
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (passwordConfirm.isNotEmpty()) {
                if (isMatching) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_signup_correct),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "비밀번호가 일치합니다",
                            color = Color(0x8019A141),
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_signup_wrong),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "비밀번호가 일치하지 않습니다",
                            color = Color(0x80FF5659),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
