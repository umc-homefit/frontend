package com.umc.homefit.presentation.recruitment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.R
import com.umc.homefit.ui.theme.RecruitmentBorder
import com.umc.homefit.ui.theme.RecruitmentTextGray

// Color.kt에 대응 토큰이 아직 없어 로컬로 유지
private val DistrictSelectedTextColor = Color(0xFF161616)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistrictDropdownField(
    districts: List<String>,
    selectedDistrict: String?,
    onDistrictSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val menuItems = remember(districts) {
        listOf("전체") + districts.filterNot { it == "전체" }
    }
    val fieldTextColor = if (selectedDistrict == null || selectedDistrict == "전체") {
        RecruitmentTextGray
    } else {
        DistrictSelectedTextColor
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .height(48.02.dp)
                .clip(RoundedCornerShape(4.00.dp))
                .background(Color.White)
                .border(BorderStroke(1.00.dp, RecruitmentBorder), RoundedCornerShape(4.00.dp))
        ) {
            Text(
                text = selectedDistrict ?: "전체",
                fontSize = 14.01.sp,
                fontWeight = FontWeight.Medium,
                color = fieldTextColor,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 10.00.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_filter_dropdown_arrow),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.82.dp)
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White,
            modifier = Modifier.heightIn(max = 240.dp)
        ) {
            menuItems.forEach { district ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = district,
                            color = if (district == "전체") RecruitmentTextGray else DistrictSelectedTextColor
                        )
                    },
                    onClick = {
                        onDistrictSelected(district)
                        expanded = false
                    }
                )
            }
        }
    }
}
