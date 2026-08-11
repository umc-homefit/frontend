package com.umc.homefit.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.data.dto.common.NoticeStatus
import com.umc.homefit.presentation.theme.BookmarkActive
import com.umc.homefit.presentation.theme.BookmarkInactive
import com.umc.homefit.presentation.theme.CompetitionRateBackground
import com.umc.homefit.presentation.theme.CompetitionRateText
import com.umc.homefit.presentation.theme.RecruitmentBorder
import com.umc.homefit.presentation.theme.RecruitmentTextGray
import com.umc.homefit.presentation.theme.StatusClosingSoonBackground
import com.umc.homefit.presentation.theme.StatusClosingSoonText
import com.umc.homefit.presentation.theme.StatusRecruitingBackground
import com.umc.homefit.presentation.theme.StatusRecruitingText
import com.umc.homefit.presentation.theme.StatusScheduledBackground
import com.umc.homefit.presentation.theme.StatusScheduledText
import com.umc.homefit.presentation.theme.TextBlack

/**
 * 공고 카드 3종(공고 목록/관심공고관리/분석 기록)이 공유하는 UI 모델.
 * 각 화면의 ViewModel이 자신의 DTO를 이 모델로 매핑해서 넘긴다 — 여기엔 domain/data 계층 매핑을 두지 않는다.
 */
data class NoticeCardUiModel(
    val id: String,
    val title: String,
    val infoLine1: String?,
    val infoLine2: String,
    val infoLine3: String,
    val status: NoticeStatus,
    val statusLabel: String,
    val isSaved: Boolean,
    val dDayText: String? = null,
    val competitionRate: String? = null
)

/** 카드 우측 상단 아이콘 슬롯. 카드 자체는 어떤 아이콘이 그려지는지 모르고 이 액션만 그린다. */
sealed interface NoticeCardAction {
    data object None : NoticeCardAction
    data class Bookmark(val isSaved: Boolean, val onToggle: () -> Unit) : NoticeCardAction
    data class Remove(val onRemove: () -> Unit) : NoticeCardAction
}

@Composable
fun NoticeCard(
    uiModel: NoticeCardUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    action: NoticeCardAction = NoticeCardAction.None
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, RecruitmentBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = uiModel.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack,
                    modifier = Modifier.weight(1f)
                )
                NoticeCardActionIcon(action)
            }

            Column(
                modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                uiModel.infoLine1?.let {
                    Text(text = it, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = RecruitmentTextGray)
                }
                Text(text = uiModel.infoLine2, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = RecruitmentTextGray)
                Text(text = uiModel.infoLine3, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = RecruitmentTextGray)
            }

            Row(
                modifier = Modifier.padding(top = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = uiModel.status, label = uiModel.statusLabel)
                uiModel.dDayText?.let {
                    Spacer(modifier = Modifier.width(5.dp))
                    DDayBadge(text = it)
                }
                uiModel.competitionRate?.let {
                    Spacer(modifier = Modifier.width(5.dp))
                    CompetitionRateBadge(rate = it)
                }
            }
        }
    }
}

@Composable
private fun NoticeCardActionIcon(action: NoticeCardAction) {
    when (action) {
        is NoticeCardAction.None -> Unit
        is NoticeCardAction.Bookmark -> Icon(
            imageVector = if (action.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (action.isSaved) "찜 해제" else "찜하기",
            tint = if (action.isSaved) BookmarkActive else BookmarkInactive,
            modifier = Modifier.clickable(onClick = action.onToggle)
        )
        is NoticeCardAction.Remove -> IconButton(
            onClick = action.onRemove,
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "관심 공고 해제",
                tint = RecruitmentTextGray
            )
        }
    }
}

@Composable
private fun StatusBadge(status: NoticeStatus, label: String) {
    val (background, text) = when (status) {
        NoticeStatus.RECRUITING -> StatusRecruitingBackground to StatusRecruitingText
        // TODO: 디자이너 확인 후 CLOSED 전용 색상 토큰으로 교체 (현재는 예정과 같은 톤으로 통일하기로 결정됨)
        NoticeStatus.SCHEDULED, NoticeStatus.CLOSED -> StatusScheduledBackground to StatusScheduledText
        NoticeStatus.CLOSING_SOON -> StatusClosingSoonBackground to StatusClosingSoonText
    }
    Surface(color = background, shape = RoundedCornerShape(percent = 50)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun DDayBadge(text: String) {
    Surface(color = StatusClosingSoonBackground, shape = RoundedCornerShape(percent = 50)) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = StatusClosingSoonText,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun CompetitionRateBadge(rate: String) {
    Surface(color = CompetitionRateBackground, shape = RoundedCornerShape(percent = 50)) {
        Text(
            text = "🔥경쟁률 $rate",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = CompetitionRateText,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoticeCardPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        NoticeCard(
            uiModel = NoticeCardUiModel(
                id = "1",
                title = "강동구 청년안심주택 추가모집",
                infoLine1 = "공고번호 | 2026-강동-003",
                infoLine2 = "전용 24㎡  보증금 3,200만원",
                infoLine3 = "청약접수 | 2026.07.01 ~ 2026.07.10",
                status = NoticeStatus.CLOSING_SOON,
                statusLabel = "마감임박",
                isSaved = false,
                dDayText = "D-3"
            ),
            onClick = {},
            action = NoticeCardAction.Bookmark(isSaved = false, onToggle = {})
        )
        NoticeCard(
            uiModel = NoticeCardUiModel(
                id = "2",
                title = "고덕강일 청년안심주택",
                infoLine1 = null,
                infoLine2 = "전용 59㎡  보증금 8,000만원",
                infoLine3 = "청약접수 | 2026.07.05 ~ 2026.07.08",
                status = NoticeStatus.RECRUITING,
                statusLabel = "모집중",
                isSaved = true,
                competitionRate = "12:1"
            ),
            onClick = {},
            action = NoticeCardAction.Remove(onRemove = {})
        )
    }
}
