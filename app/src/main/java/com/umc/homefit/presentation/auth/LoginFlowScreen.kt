package com.umc.homefit.presentation.auth

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.homefit.R
import com.umc.homefit.presentation.auth.component.LastCharVisibleTransformation
import com.umc.homefit.presentation.component.AppScaffold
import com.umc.homefit.presentation.component.CompletionStep
import com.umc.homefit.presentation.component.StepBaseLayout
import com.umc.homefit.presentation.theme.RecruitmentAccent
import com.umc.homefit.presentation.theme.RecruitmentBorder
import com.umc.homefit.presentation.theme.RecruitmentTextGray
import com.umc.homefit.presentation.theme.SearchFieldBackground
import com.umc.homefit.presentation.theme.ValidationErrorText
import com.umc.homefit.presentation.theme.ValidationSuccessText

@Composable
fun LoginFlowScreenRoute(
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LoginFlowScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onEmailNext = viewModel::onEmailNext,
        onPasswordNext = viewModel::onPasswordNext,
        onStepBack = viewModel::onStepBack,
        onBack = onBack,
        onNavigateToHome = onNavigateToHome,
        modifier = modifier
    )
}

@Composable
fun LoginFlowScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailNext: () -> Unit,
    onPasswordNext: () -> Unit,
    onStepBack: () -> Unit,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val handleBackClick = {
        when (uiState.step) {
            LoginStep.EMAIL -> onBack()
            LoginStep.PASSWORD -> onStepBack()
            LoginStep.COMPLETE -> Unit
        }
    }

    BackHandler(enabled = uiState.step != LoginStep.COMPLETE) {
        handleBackClick()
    }

    BackHandler(enabled = uiState.step == LoginStep.COMPLETE) {
        onNavigateToHome()
    }

    val progress = when (uiState.step) {
        LoginStep.EMAIL -> 0.5f
        LoginStep.PASSWORD -> 1f
        LoginStep.COMPLETE -> 1f
    }

    AppScaffold(
        title = if (uiState.step != LoginStep.COMPLETE) "로그인" else null,
        titleContent = if (uiState.step == LoginStep.COMPLETE) { {} } else null,
        showBackButton = true,
        onBackClick = if (uiState.step == LoginStep.COMPLETE) onNavigateToHome else handleBackClick,
        showDivider = true,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (uiState.step != LoginStep.COMPLETE) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp),
                        color = RecruitmentAccent,
                        trackColor = SearchFieldBackground,
                        drawStopIndicator = {}
                    )
                }

                Crossfade(
                    targetState = uiState.step,
                    label = "LoginStepTransition",
                    modifier = Modifier.weight(1f)
                ) { step ->
                    when (step) {
                        LoginStep.EMAIL -> {
                            EmailStep(
                                email = uiState.email,
                                isValidFormat = uiState.isEmailValid,
                                onEmailChange = onEmailChange,
                                onNext = onEmailNext
                            )
                        }

                        LoginStep.PASSWORD -> {
                            PasswordStep(
                                password = uiState.password,
                                isValid = uiState.isPasswordValid,
                                isLoading = uiState.isLoading,
                                errorMessage = uiState.errorMessage,
                                onPasswordChange = onPasswordChange,
                                onNext = onPasswordNext
                            )
                        }

                        LoginStep.COMPLETE -> {
                            CompletionStep(
                                title = "로그인이 완료되었습니다",
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
    email: String,
    isValidFormat: Boolean,
    onEmailChange: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    StepBaseLayout(
        title = "이메일을 입력해주세요",
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
                color = RecruitmentTextGray
            )

            Spacer(modifier = Modifier.height(21.dp))

            TextField(
                value = email,
                onValueChange = onEmailChange,
                placeholder = {
                    Text(
                        text = "example@email.com",
                        fontSize = 16.sp,
                        color = RecruitmentBorder
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onEmailChange("") }) {
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
                    focusedIndicatorColor = RecruitmentBorder,
                    unfocusedIndicatorColor = RecruitmentBorder
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

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
                        color = ValidationSuccessText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
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
                        color = ValidationErrorText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun PasswordStep(
    password: String,
    isValid: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onPasswordChange: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    StepBaseLayout(
        title = "비밀번호를 입력해주세요",
        onNext = onNext,
        isNextEnabled = isValid && !isLoading,
        bottomLinkText = "",
        onBottomLinkClick = {},
        modifier = modifier,
        buttonText = if (isLoading) "로그인 중..." else "다음"
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "비밀번호",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = RecruitmentTextGray
            )

            Spacer(modifier = Modifier.height(21.dp))

            TextField(
                value = password,
                onValueChange = onPasswordChange,
                visualTransformation = remember { LastCharVisibleTransformation() },
                placeholder = {
                    Text(
                        text = "Home2026#",
                        fontSize = 16.sp,
                        color = RecruitmentBorder
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onPasswordChange("") }) {
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
                    focusedIndicatorColor = RecruitmentBorder,
                    unfocusedIndicatorColor = RecruitmentBorder
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = errorMessage ?: "비밀번호를 입력해주세요",
                color = if (errorMessage != null) ValidationErrorText else RecruitmentTextGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
