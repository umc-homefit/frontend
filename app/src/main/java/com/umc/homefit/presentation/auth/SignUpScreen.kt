package com.umc.homefit.presentation.auth

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umc.homefit.presentation.component.AppScaffold
import com.umc.homefit.presentation.component.CompletionStep
import com.umc.homefit.presentation.component.StepBaseLayout
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
    modifier: Modifier = Modifier,
    viewModel: SignUpScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SignUpScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToHome = onNavigateToHome,
        onSignup = viewModel::signup,
        onEmailChanged = viewModel::resetState,
        modifier = modifier
    )
}

@Composable
fun SignUpScreen(
    uiState: SignUpScreenUiState,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onSignup: (String, String) -> Unit,
    onEmailChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by rememberSaveable { mutableStateOf(SignUpStep.EMAIL) }
    var savedEmail by rememberSaveable { mutableStateOf("") }
    var savedPassword by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState) {
        when (uiState) {
            is SignUpScreenUiState.Success -> currentStep = SignUpStep.COMPLETE
            is SignUpScreenUiState.EmailDuplicateError -> currentStep = SignUpStep.EMAIL
            else -> Unit
        }
    }

    val emailErrorMessage = when (uiState) {
        is SignUpScreenUiState.EmailDuplicateError -> "이미 가입된 이메일입니다"
        else -> null
    }

    val signupErrorMessage = (uiState as? SignUpScreenUiState.Error)?.message

    val handleBackClick = {
        when (currentStep) {
            SignUpStep.EMAIL -> onBack()
            SignUpStep.PASSWORD -> currentStep = SignUpStep.EMAIL
            SignUpStep.PASSWORD_CONFIRM -> currentStep = SignUpStep.PASSWORD
            SignUpStep.COMPLETE -> currentStep = SignUpStep.PASSWORD_CONFIRM
        }
    }

    BackHandler(enabled = currentStep != SignUpStep.COMPLETE) {
        handleBackClick()
    }

    BackHandler(enabled = currentStep == SignUpStep.COMPLETE) {
        onNavigateToHome()
    }

    val progress = when (currentStep) {
        SignUpStep.EMAIL -> 1f / 3f
        SignUpStep.PASSWORD -> 2f / 3f
        SignUpStep.PASSWORD_CONFIRM -> 1f
        SignUpStep.COMPLETE -> 1f
    }

    AppScaffold(
        title = if (currentStep != SignUpStep.COMPLETE) "회원가입" else null,
        titleContent = if (currentStep == SignUpStep.COMPLETE) { {} } else null,
        showBackButton = true,
        onBackClick = if (currentStep == SignUpStep.COMPLETE) onNavigateToHome else handleBackClick,
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
                        trackColor = Color(0xFFF0F4F9),
                        drawStopIndicator = {}
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
                                onNext = { enteredEmail ->
                                    savedEmail = enteredEmail
                                    currentStep = SignUpStep.PASSWORD
                                },
                                serverErrorMessage = emailErrorMessage,
                                onEmailChanged = onEmailChanged
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
                                isLoading = uiState is SignUpScreenUiState.Loading,
                                errorMessage = signupErrorMessage,
                                onNext = { onSignup(savedEmail, savedPassword) }
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
    onNext: (String) -> Unit,
    modifier: Modifier = Modifier,
    serverErrorMessage: String? = null,
    onEmailChanged: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    val emailRegex = remember { Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") }
    val isValidFormat = emailRegex.matches(email)

    StepBaseLayout(
        title = "회원가입을 시작해볼까요?",
        onNext = { onNext(email) },
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
                onValueChange = {
                    email = it
                    onEmailChanged()
                },
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

            Spacer(modifier = Modifier.height(4.dp))

            if (serverErrorMessage != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_signup_wrong),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = serverErrorMessage,
                        color = Color(0x80FF5659),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else if (isValidFormat) {
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
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else if (email.isNotEmpty()) {
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
    onNext: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var password by rememberSaveable { mutableStateOf("") }
    val passwordRegex = remember {
        Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?!.*\\s).{8,}$")
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
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
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
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
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
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
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
    isLoading: Boolean,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    var passwordConfirm by rememberSaveable { mutableStateOf("") }
    val isMatching = passwordConfirm.isNotEmpty() && passwordConfirm == originalPassword

    StepBaseLayout(
        title = "비밀번호를 확인해주세요",
        onNext = onNext,
        isNextEnabled = isMatching && !isLoading,
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
                            text = "비밀번호가 일치하지 않습니다",
                            color = Color(0x80FF5659),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_signup_wrong),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = errorMessage,
                        color = Color(0x80FF5659),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        uiState = SignUpScreenUiState.Idle,
        onBack = {},
        onNavigateToHome = {},
        onSignup = { _, _ -> },
        onEmailChanged = {}
    )
}
