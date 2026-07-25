package com.umc.homefit.presentation.analysis

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umc.homefit.ui.component.AppScaffold
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.size
import com.umc.homefit.ui.component.StepBaseLayout
import com.umc.homefit.ui.component.CompletionStep
import kotlinx.serialization.Serializable

@Serializable
enum class FinancialInfoStep {
    INCOME,   // 소득
    ASSET,    // 자산
    DEBT,     // 부채
    HOUSE,    // 주택
    COMPLETE  // 완료
}

@Composable
fun FinancialInfoScreenRoute(
    viewModel: FinancialInfoScreenViewModel,
    onBack: () -> Unit,
    onNavigateToResult: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    FinancialInfoScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToResult = onNavigateToResult,
        modifier = modifier
    )
}

@Composable
fun FinancialInfoScreen(
    uiState: FinancialInfoScreenUiState,
    onBack: () -> Unit,
    onNavigateToResult: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(FinancialInfoStep.INCOME) }

    val progress = when (currentStep) {
        FinancialInfoStep.INCOME -> 0.25f
        FinancialInfoStep.ASSET -> 0.50f
        FinancialInfoStep.DEBT -> 0.75f
        FinancialInfoStep.HOUSE -> 1.00f
        FinancialInfoStep.COMPLETE -> 1.00f
    }

    val handleBackClick = {
        when (currentStep) {
            FinancialInfoStep.INCOME -> onBack()
            FinancialInfoStep.ASSET -> currentStep = FinancialInfoStep.INCOME
            FinancialInfoStep.DEBT -> currentStep = FinancialInfoStep.ASSET
            FinancialInfoStep.HOUSE -> currentStep = FinancialInfoStep.DEBT
            FinancialInfoStep.COMPLETE -> currentStep = FinancialInfoStep.HOUSE
        }
    }

    AppScaffold(
        title = null,
        showBackButton = true,
        onBackClick = handleBackClick,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(3.dp),
                color = Color(0xFF3C45F3),
                trackColor = Color(0xFFF0F4F9)
            )

            when (uiState) {
                is FinancialInfoScreenUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is FinancialInfoScreenUiState.Success -> {
                    Crossfade(targetState = currentStep, label = "StepTransition") { step ->
                        when (step) {
                            FinancialInfoStep.INCOME -> {
                                IncomeStep(
                                    onNext = { currentStep = FinancialInfoStep.ASSET }
                                )
                            }

                            FinancialInfoStep.ASSET -> {
                                AssetStep(
                                    onNext = { currentStep = FinancialInfoStep.DEBT }
                                )
                            }

                            FinancialInfoStep.DEBT -> {
                                DebtStep(
                                    onNext = { currentStep = FinancialInfoStep.HOUSE }
                                )
                            }

                            FinancialInfoStep.HOUSE -> {
                                HouseStep(
                                    onNext = { currentStep = FinancialInfoStep.COMPLETE }
                                )
                            }

                            FinancialInfoStep.COMPLETE -> {
                                CompletionStep(
                                    title = "입주 분석이 완료되었습니다",
                                    buttonText = "분석 결과 확인하기",
                                    onButtonClick = { onNavigateToResult("결과ID") }
                                )
                            }
                        }
                    }
                }

                is FinancialInfoScreenUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${uiState.message}")
                    }
                }
            }
        }
    }
}

// 퀵 금액 버튼 칩 그룹 컴포넌트
@Composable
fun QuickAmountChipGroup(
    onAmountClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val chipBackgroundColor = Color(0xFFF0F4F9)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        val quickAmounts = listOf(
            "+1,000만 원" to 1000L,
            "+100만 원" to 100L,
            "+10만 원" to 10L
        )
        quickAmounts.forEachIndexed { index, (label, amount) ->
            Surface(
                onClick = { onAmountClick(amount) },
                shape = RoundedCornerShape(20.dp),
                color = chipBackgroundColor,
                modifier = Modifier.padding(start = if (index > 0) 8.dp else 0.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4A4F55),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

// 금액 입력 필드 컴포넌트
@Composable
fun FinancialInputField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onQuickAmountClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    showHelpIcon: Boolean = true,
) {
    val dividerColor = Color(0xFFD2D9E2)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            if (showHelpIcon) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFEEF1F6),
                    modifier = Modifier.size(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "?",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF9EA4AA),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        TextField(
            value = value,
            onValueChange = { input ->
                if (input.all { it.isDigit() }) onValueChange(input)
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Text(
                    text = "만 원",
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 4.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = dividerColor,
                unfocusedIndicatorColor = dividerColor
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        QuickAmountChipGroup(onAmountClick = onQuickAmountClick)
    }
}

// 금융정보 입력_소득
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeStep(
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    initialAmount: String = "",
    initialIncomeType: String = "근로소득",
    buttonText: String = "다음"
) {
    var annualIncomeText by remember { mutableStateOf(initialAmount) }
    var incomeType by remember { mutableStateOf(initialIncomeType) }
    var hasSelectedIncomeType by remember { mutableStateOf(initialAmount.isNotEmpty()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val incomeTypes = listOf("근로소득", "사업소득", "기타소득")
    val dividerColor = Color(0xFFD2D9E2)

    @Suppress("AssignedValueIsNeverRead")
    StepBaseLayout(
        title = "내 소득 정보를 입력해주세요",
        onNext = onNext,
        isNextEnabled = annualIncomeText.isNotEmpty(),
        bottomLinkText = "소득이 없어요",

        onBottomLinkClick = { annualIncomeText = "0" },
        modifier = modifier,
        buttonText = buttonText
    ) {
        // 연간 총소득 입력 필드
        FinancialInputField(
            title = "연간 총소득",
            value = annualIncomeText,
            onValueChange = { annualIncomeText = it },
            onQuickAmountClick = { amount ->
                val current = annualIncomeText.toLongOrNull() ?: 0L
                annualIncomeText = (current + amount).toString()
            },
            showHelpIcon = false,
        )

        // 소득 유형 드롭다운 메뉴
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "소득 유형",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFEEF1F6),
                    modifier = Modifier.size(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "?",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF9EA4AA),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = incomeType,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = if (isDropdownExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = dividerColor,
                        unfocusedBorderColor = dividerColor,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = if (hasSelectedIncomeType) Color(0xFF1A1A1A) else dividerColor, // ★ 선택 전엔 테두리와 동일 색
                        unfocusedTextColor = if (hasSelectedIncomeType) Color(0xFF1A1A1A) else dividerColor // ★ 선택 전엔 테두리와 동일 색
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    incomeTypes.forEachIndexed { index, type ->
                        DropdownMenuItem(
                            text = { Text(text = type, color = Color(0xFF4A4F55)) },
                            onClick = {
                                incomeType = type
                                hasSelectedIncomeType = true
                                isDropdownExpanded = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color(0xFF4A4F55),
                                leadingIconColor = Color.Transparent,
                                trailingIconColor = Color.Transparent,
                                disabledTextColor = Color.Gray,
                                disabledLeadingIconColor = Color.Transparent,
                                disabledTrailingIconColor = Color.Transparent
                            )
                        )
                        if (index != incomeTypes.lastIndex) {
                            HorizontalDivider(color = dividerColor)
                        }
                    }
                }
            }
        }
    }
}

// 금융정보 입력_자산
@Composable
fun AssetStep(
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    initialTotalAsset: String = "",
    initialFinancialAsset: String = "",
    buttonText: String = "다음"
) {
    var totalAssetText by remember { mutableStateOf(initialTotalAsset) }
    var financialAssetText by remember { mutableStateOf(initialFinancialAsset) }

    @Suppress("AssignedValueIsNeverRead")
    StepBaseLayout(
        title = "내 자산 정보를 입력해주세요",
        onNext = onNext,
        isNextEnabled = totalAssetText.isNotEmpty() && financialAssetText.isNotEmpty(),
        bottomLinkText = "자산이 없어요",
        onBottomLinkClick = {
            totalAssetText = "0"
            financialAssetText = "0"
        },
        modifier = modifier,
        buttonText = buttonText
    ) {
        // 총 보유 자산 입력 필드
        FinancialInputField(
            title = "총 보유 자산",
            value = totalAssetText,
            onValueChange = { totalAssetText = it },
            onQuickAmountClick = { amount ->
                val current = totalAssetText.toLongOrNull() ?: 0L
                totalAssetText = (current + amount).toString()
            }
        )

        // 금융 자산 입력 필드
        FinancialInputField(
            title = "금융 자산",
            value = financialAssetText,
            onValueChange = { financialAssetText = it },
            onQuickAmountClick = { amount ->
                val current = financialAssetText.toLongOrNull() ?: 0L
                financialAssetText = (current + amount).toString()
            }
        )
    }
}

// 금융정보 입력_부채
@Composable
fun DebtStep(
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    initialTotalDebt: String = "",
    initialMonthlyRepayment: String = "",
    buttonText: String = "다음"
) {
    var totalDebtText by remember { mutableStateOf(initialTotalDebt) }
    var monthlyRepaymentText by remember { mutableStateOf(initialMonthlyRepayment) }

    @Suppress("AssignedValueIsNeverRead")
    StepBaseLayout(
        title = "내 부채 정보를 입력해주세요",
        onNext = onNext,
        isNextEnabled = totalDebtText.isNotEmpty() && monthlyRepaymentText.isNotEmpty(),
        bottomLinkText = "부채가 없어요",
        onBottomLinkClick = {
            totalDebtText = "0"
            monthlyRepaymentText = "0"
        },
        modifier = modifier,
        buttonText = buttonText
    ) {
        // 총 부채 금액 입력 필드
        FinancialInputField(
            title = "총 부채 금액",
            value = totalDebtText,
            onValueChange = { totalDebtText = it },
            onQuickAmountClick = { amount ->
                val current = totalDebtText.toLongOrNull() ?: 0L
                totalDebtText = (current + amount).toString()
            }
        )

        // 월 상환액 입력 필드
        FinancialInputField(
            title = "월 상환액",
            value = monthlyRepaymentText,
            onValueChange = { monthlyRepaymentText = it },
            onQuickAmountClick = { amount ->
                val current = monthlyRepaymentText.toLongOrNull() ?: 0L
                monthlyRepaymentText = (current + amount).toString()
            }
        )
    }
}


// 금융정보 입력_주택
@Composable
fun HouseStep(
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    initialOption: String? = null,
    buttonText: String = "다음"
) {
    val options = listOf("본인 무주택", "세대원 전원 무주택", "주택 보유 (유주택)")
    var selectedOption by remember { mutableStateOf(initialOption) }

    StepBaseLayout(
        title = "주택 보유 여부를 알려주세요",
        onNext = onNext,
        isNextEnabled = selectedOption != null,
        bottomLinkText = "",
        onBottomLinkClick = {},
        modifier = modifier,
        buttonText = buttonText
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEach { option ->
                HouseOptionRow(
                    text = option,
                    isSelected = selectedOption == option,
                    onSelect = { selectedOption = option }
                )
            }
        }
    }
}

@Composable
private fun HouseOptionRow(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF3C45F3),
                unselectedColor = Color(0xFFC2C6CC)
            )
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = Color(0xFF1A1A1A),
            modifier = Modifier.padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // 우측 도움말 아이콘
        Surface(
            shape = RoundedCornerShape(50),
            color = Color(0xFFEEF1F6),
            modifier = Modifier.size(16.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "?",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9EA4AA),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FinancialInfoEditScreenRoute(
    step: FinancialInfoStep,
    viewModel: FinancialInfoScreenViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    FinancialInfoEditScreen(
        step = step,
        uiState = uiState,
        onSave = onBack,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun FinancialInfoEditScreen(
    step: FinancialInfoStep,
    uiState: FinancialInfoScreenUiState,
    onSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = null,
        showBackButton = true,
        onBackClick = onBack,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is FinancialInfoScreenUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is FinancialInfoScreenUiState.Success -> {
                    // TODO: 실제 금융 정보 API 연동 후 아래 초기값을 서버 데이터로 교체
                    when (step) {
                        FinancialInfoStep.INCOME -> IncomeStep(
                            onNext = onSave,
                            buttonText = "완료",
                            initialAmount = "4800",
                            initialIncomeType = "근로소득"
                        )
                        FinancialInfoStep.ASSET -> AssetStep(
                            onNext = onSave,
                            buttonText = "완료",
                            initialTotalAsset = "6500",
                            initialFinancialAsset = "2800"
                        )
                        FinancialInfoStep.DEBT -> DebtStep(
                            onNext = onSave,
                            buttonText = "완료",
                            initialTotalDebt = "1800",
                            initialMonthlyRepayment = "35"
                        )
                        FinancialInfoStep.HOUSE -> HouseStep(
                            onNext = onSave,
                            buttonText = "완료",
                            initialOption = "본인 무주택"
                        )
                        FinancialInfoStep.COMPLETE -> Unit
                    }
                }
                is FinancialInfoScreenUiState.Error -> {
                    Text(text = "Error: ${uiState.message}", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
