package com.umc.homefit.presentation.analysis

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umc.homefit.presentation.component.AppScaffold
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.umc.homefit.presentation.finance.component.HelpTerm
import com.umc.homefit.presentation.component.StepBaseLayout
import com.umc.homefit.presentation.component.CompletionStep
import com.umc.homefit.util.mapToHouseOption
import com.umc.homefit.util.mapToHousingStatus
import kotlinx.serialization.Serializable
import com.umc.homefit.R

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
        onIncomeNext = viewModel::onIncomeNext,
        onAssetNext = viewModel::onAssetNext,
        onDebtNext = viewModel::onDebtNext,
        onHouseNext = viewModel::onHouseNextAndSubmit,
        modifier = modifier
    )
}

@Composable
fun FinancialInfoScreen(
    uiState: FinancialInfoScreenUiState,
    onBack: () -> Unit,
    onNavigateToResult: (String) -> Unit,
    onIncomeNext: (annualIncomeText: String) -> Unit,
    onAssetNext: (totalAssetText: String, financialAssetText: String) -> Unit,
    onDebtNext: (totalDebtText: String, monthlyRepaymentText: String) -> Unit,
    onHouseNext: (housingStatus: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(FinancialInfoStep.INCOME) }

    LaunchedEffect(uiState) {
        if (uiState is FinancialInfoScreenUiState.Success && uiState.isSubmitted) {
            currentStep = FinancialInfoStep.COMPLETE
        }
    }

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
            FinancialInfoStep.COMPLETE -> currentStep = FinancialInfoStep.INCOME
        }
    }

    AppScaffold(
        title = "",
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
                    val draft = uiState.draft
                    Crossfade(targetState = currentStep, label = "StepTransition") { step ->
                        when (step) {
                            FinancialInfoStep.INCOME -> {
                                IncomeStep(
                                    onNext = { annualIncome ->
                                        onIncomeNext(annualIncome)
                                        currentStep = FinancialInfoStep.ASSET
                                    },
                                    initialAmount = draft.annualIncomeText
                                )
                            }

                            FinancialInfoStep.ASSET -> {
                                AssetStep(
                                    onNext = { totalAsset, financialAsset ->
                                        onAssetNext(totalAsset, financialAsset)
                                        currentStep = FinancialInfoStep.DEBT
                                    },
                                    initialTotalAsset = draft.totalAssetText,
                                    initialFinancialAsset = draft.financialAssetText
                                )
                            }

                            FinancialInfoStep.DEBT -> {
                                DebtStep(
                                    onNext = { totalDebt, monthlyRepayment ->
                                        onDebtNext(totalDebt, monthlyRepayment)
                                        currentStep = FinancialInfoStep.HOUSE
                                    },
                                    initialTotalDebt = draft.totalDebtText,
                                    initialMonthlyRepayment = draft.monthlyRepaymentText
                                )
                            }

                            FinancialInfoStep.HOUSE -> {
                                HouseStep(
                                    onNext = { housingStatus -> onHouseNext(housingStatus) },
                                    initialOption = draft.housingStatus?.let { mapToHouseOption(it) }
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
        // 우측 정렬 및 칩 간 간격 12.dp 적용
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
    ) {
        val quickAmounts = listOf(
            "+1,000만 원" to 1000L,
            "+100만 원" to 100L,
            "+10만 원" to 10L
        )
        quickAmounts.forEach { (label, amount) ->
            Surface(
                onClick = { onAmountClick(amount) },
                shape = RoundedCornerShape(200.dp),
                color = chipBackgroundColor
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4A4F55),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

private object ThousandsSeparatorVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        if (original.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val formatted = buildString {
            original.forEachIndexed { index, char ->
                val digitsFromEnd = original.length - index
                if (index != 0 && digitsFromEnd % 3 == 0) append(',')
                append(char)
            }
        }

        val originalToTransformed = IntArray(original.length + 1)
        var originalIndex = 0
        formatted.forEachIndexed { formattedIndex, char ->
            if (char != ',') {
                originalIndex++
                originalToTransformed[originalIndex] = formattedIndex + 1
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                originalToTransformed[offset.coerceIn(0, original.length)]

            override fun transformedToOriginal(offset: Int): Int =
                formatted.take(offset.coerceIn(0, formatted.length)).count { it != ',' }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
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
    helpTerms: List<HelpTerm> = emptyList(),
) {
    val dividerColor = Color(0xFFD2D9E2)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF161616)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (showHelpIcon) {
                HelpIconButton(terms = helpTerms)
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
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFD2D9E2),
                    modifier = Modifier.padding(end = 4.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = ThousandsSeparatorVisualTransformation,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A4F55)
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = dividerColor,
                unfocusedIndicatorColor = dividerColor
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickAmountChipGroup(onAmountClick = onQuickAmountClick)
    }
}

@Composable
fun HelpIconButton(
    terms: List<HelpTerm>,
    modifier: Modifier = Modifier,
    iconSize: Dp = 20.dp
) {
    var showDialog by remember { mutableStateOf(false) }

    Image(
        painter = painterResource(id = R.drawable.ic_question_mark),
        contentDescription = "도움말",
        modifier = modifier
            .size(iconSize)
            .clickable { showDialog = true }
    )

    if (showDialog) {
        TermsHelpDialog(
            terms = terms,
            onDismissRequest = { showDialog = false }
        )
    }
}

@Composable
fun TermsHelpDialog(
    terms: List<HelpTerm>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = modifier
                .width(315.dp)
                .height(325.dp)
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.bg_terms_help
                ),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 14.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onDismissRequest),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "×",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFA9B6C5),
                    fontSize = 32.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )
            }

            val topPadding = if (terms.size >= 3) 100.dp else 132.dp
            val itemSpacing = if (terms.size >= 3) 10.dp else 15.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 24.dp,
                        top = topPadding,
                        end = 24.dp,
                        bottom = 24.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(
                    space = itemSpacing,
                    alignment = Alignment.CenterVertically
                )
            ) {
                terms.forEach { term ->
                    TermDescription(
                        title = term.title,
                        description = term.description
                    )
                }
            }
        }
    }
}

@Composable
private fun TermDescription(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = Color(0xFF4A4F55),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = description,
            color = Color(0xFF919AA4),
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}

// 금융정보 입력_소득
@Composable
fun IncomeStep(
    onNext: (annualIncomeText: String) -> Unit,
    modifier: Modifier = Modifier,
    initialAmount: String = "",
    buttonText: String = "다음",
    autoAdvanceOnEmpty: Boolean = true
) {
    var annualIncomeText by remember { mutableStateOf(initialAmount) }

    @Suppress("AssignedValueIsNeverRead")
    StepBaseLayout(
        title = "내 소득 정보를 입력해주세요",
        onNext = { onNext(annualIncomeText) },
        isNextEnabled = annualIncomeText.isNotEmpty(),
        bottomLinkText = "소득이 없어요",
        onBottomLinkClick = {
            annualIncomeText = "0"
            if (autoAdvanceOnEmpty) onNext(annualIncomeText)
        },
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
            showHelpIcon = false
        )
    }
}

// 금융정보 입력_자산
@Composable
fun AssetStep(
    onNext: (totalAssetAmount: String, financialAssetAmount: String) -> Unit,
    modifier: Modifier = Modifier,
    initialTotalAsset: String = "",
    initialFinancialAsset: String = "",
    buttonText: String = "다음",
    autoAdvanceOnEmpty: Boolean = true
) {
    var totalAssetText by remember { mutableStateOf(initialTotalAsset) }
    var financialAssetText by remember { mutableStateOf(initialFinancialAsset) }

    StepBaseLayout(
        title = "내 자산 정보를 입력해주세요",
        onNext = { onNext(totalAssetText, financialAssetText) },
        isNextEnabled = totalAssetText.isNotEmpty() && financialAssetText.isNotEmpty(),
        bottomLinkText = "자산이 없어요",
        onBottomLinkClick = {
            totalAssetText = "0"
            financialAssetText = "0"
            if (autoAdvanceOnEmpty) onNext(totalAssetText, financialAssetText)
        },
        modifier = modifier,
        buttonText = buttonText
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(46.dp)
        ) {
            FinancialInputField(
                title = "총 보유 자산",
                value = totalAssetText,
                onValueChange = { totalAssetText = it },
                onQuickAmountClick = { amount ->
                    val current = totalAssetText.toLongOrNull() ?: 0L
                    totalAssetText = (current + amount).toString()
                },
                helpTerms = listOf(
                    HelpTerm("총 보유 자산", "보유 중인 모든 자산의 합계 (부동산, 자동차, 금융자산 등)"),
                    HelpTerm("금융 자산", "금융기관에 보유한 자산 (예금, 적금, 주식, 펀드 등)")
                )
            )

            FinancialInputField(
                title = "금융 자산",
                value = financialAssetText,
                onValueChange = { financialAssetText = it },
                onQuickAmountClick = { amount ->
                    val current = financialAssetText.toLongOrNull() ?: 0L
                    financialAssetText = (current + amount).toString()
                },
                helpTerms = listOf(
                    HelpTerm("총 보유 자산", "보유 중인 모든 자산의 합계 (부동산, 자동차, 금융자산 등)"),
                    HelpTerm("금융 자산", "금융기관에 보유한 자산 (예금, 적금, 주식, 펀드 등)")
                )
            )
        }
    }
}

// 금융정보 입력_부채
@Composable
fun DebtStep(
    onNext: (totalDebtAmount: String, monthlyDebtPaymentAmount: String) -> Unit,
    modifier: Modifier = Modifier,
    initialTotalDebt: String = "",
    initialMonthlyRepayment: String = "",
    buttonText: String = "다음",
    autoAdvanceOnEmpty: Boolean = true
) {
    var totalDebtText by remember { mutableStateOf(initialTotalDebt) }
    var monthlyRepaymentText by remember { mutableStateOf(initialMonthlyRepayment) }

    StepBaseLayout(
        title = "내 부채 정보를 입력해주세요",
        onNext = { onNext(totalDebtText, monthlyRepaymentText) },
        isNextEnabled = totalDebtText.isNotEmpty() && monthlyRepaymentText.isNotEmpty(),
        bottomLinkText = "부채가 없어요",
        onBottomLinkClick = {
            totalDebtText = "0"
            monthlyRepaymentText = "0"
            if (autoAdvanceOnEmpty) onNext(totalDebtText, monthlyRepaymentText)
        },
        modifier = modifier,
        buttonText = buttonText
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(46.dp)
        ) {
            FinancialInputField(
                title = "총 부채 금액",
                value = totalDebtText,
                onValueChange = { totalDebtText = it },
                onQuickAmountClick = { amount ->
                    val current = totalDebtText.toLongOrNull() ?: 0L
                    totalDebtText = (current + amount).toString()
                },
                helpTerms = listOf(
                    HelpTerm("총 부채 금액", "대출, 카드론 등 현재 보유한 부채의 총액"),
                    HelpTerm("월 상환액", "매월 상환하는 원금과 이자의 합계")
                )
            )

            FinancialInputField(
                title = "월 상환액",
                value = monthlyRepaymentText,
                onValueChange = { monthlyRepaymentText = it },
                onQuickAmountClick = { amount ->
                    val current = monthlyRepaymentText.toLongOrNull() ?: 0L
                    monthlyRepaymentText = (current + amount).toString()
                },
                helpTerms = listOf(
                    HelpTerm("총 부채 금액", "대출, 카드론 등 현재 보유한 부채의 총액"),
                    HelpTerm("월 상환액", "매월 상환하는 원금과 이자의 합계")
                )
            )
        }
    }
}

// 금융정보 입력_주택
@Composable
fun HouseStep(
    onNext: (housingStatus: String) -> Unit,
    modifier: Modifier = Modifier,
    initialOption: String? = null,
    buttonText: String = "다음"
) {
    val options = listOf("완전 무주택", "본인 무주택, 세대원 유주택", "본인 유주택")
    var selectedOption by remember { mutableStateOf(initialOption) }

    val houseHelpTerms = listOf(
        HelpTerm("완전 무주택", "본인과 가족 모두 주택을 보유하고 있지 않은 상태"),
        HelpTerm("본인 무주택, 세대원 유주택", "본인은 무주택이나 배우자·가족 명의로 주택을 보유한 상태"),
        HelpTerm("본인 유주택", "본인 명의로 주택을 보유하고 있는 상태")
    )

    StepBaseLayout(
        title = "주택 보유 여부를 알려주세요",
        onNext = { onNext(mapToHousingStatus(selectedOption!!)) },
        isNextEnabled = selectedOption != null,
        bottomLinkText = "",
        onBottomLinkClick = {},
        modifier = modifier,
        buttonText = buttonText
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            options.forEach { option ->
                HouseOptionRow(
                    text = option,
                    isSelected = selectedOption == option,
                    onSelect = { selectedOption = option },
                    helpTerms = houseHelpTerms
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
    helpTerms: List<HelpTerm>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomRadioButton(
            selected = isSelected,
            onClick = onSelect
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = Color(0xFF161616),
        )

        Spacer(modifier = Modifier.weight(1f))

        HelpIconButton(terms = helpTerms)
    }
}

@Composable
private fun CustomRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Color(0xFF3C45F3) else Color(0xFFD2D9E2)

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3C45F3))
            )
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
        onBack = onBack,
        onIncomeSave = viewModel::onIncomeEditSave,
        onAssetSave = viewModel::onAssetEditSave,
        onDebtSave = viewModel::onDebtEditSave,
        onHouseSave = viewModel::onHouseNextAndSubmit,
        modifier = modifier
    )
}

@Composable
fun FinancialInfoEditScreen(
    step: FinancialInfoStep,
    uiState: FinancialInfoScreenUiState,
    onBack: () -> Unit,
    onIncomeSave: (annualIncomeText: String) -> Unit,
    onAssetSave: (totalAssetText: String, financialAssetText: String) -> Unit,
    onDebtSave: (totalDebtText: String, monthlyRepaymentText: String) -> Unit,
    onHouseSave: (housingStatus: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(uiState) {
        if (uiState is FinancialInfoScreenUiState.Success && uiState.isSubmitted) {
            onBack()
        }
    }

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
                    val draft = uiState.draft
                    when (step) {
                        FinancialInfoStep.INCOME -> IncomeStep(
                            onNext = onIncomeSave,
                            buttonText = "완료",
                            initialAmount = draft.annualIncomeText,
                            autoAdvanceOnEmpty = false
                        )
                        FinancialInfoStep.ASSET -> AssetStep(
                            onNext = onAssetSave,
                            buttonText = "완료",
                            initialTotalAsset = draft.totalAssetText,
                            initialFinancialAsset = draft.financialAssetText,
                            autoAdvanceOnEmpty = false
                        )
                        FinancialInfoStep.DEBT -> DebtStep(
                            onNext = onDebtSave,
                            buttonText = "완료",
                            initialTotalDebt = draft.totalDebtText,
                            initialMonthlyRepayment = draft.monthlyRepaymentText,
                            autoAdvanceOnEmpty = false
                        )
                        FinancialInfoStep.HOUSE -> HouseStep(
                            onNext = onHouseSave,
                            buttonText = "완료",
                            initialOption = draft.housingStatus?.let { mapToHouseOption(it) }
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
