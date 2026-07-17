package com.umc.homefit.presentation.recruitment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

private val FieldBorderColor = Color(0xFFD2D9E2)
private val PlaceholderColor = Color(0xFF919AA4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistrictDropdownField(
    districts: List<String>,
    selectedDistrict: String?,
    onDistrictSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .height(52.42.dp)
                .clip(RoundedCornerShape(4.37.dp))
                .background(Color.White)
                .border(BorderStroke(1.09.dp, FieldBorderColor), RoundedCornerShape(4.37.dp))
        ) {
            Text(
                text = selectedDistrict ?: "전체",
                fontSize = 15.29.sp,
                fontWeight = FontWeight.Medium,
                color = PlaceholderColor,
                modifier = Modifier.padding(top = 17.47.dp, start = 10.92.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_filter_dropdown_arrow),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 14.dp)
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White
        ) {
            districts.forEach { district ->
                DropdownMenuItem(
                    text = { Text(district) },
                    onClick = {
                        onDistrictSelected(district)
                        expanded = false
                    }
                )
            }
        }
    }
}
