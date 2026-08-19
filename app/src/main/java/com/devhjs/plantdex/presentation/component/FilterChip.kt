package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/** 기본 배색은 도감 필터 기준이고, 카메라의 줌·플래시 칩은 다크 배색을 넘겨 재사용한다. */
@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedContainerColor: Color = AppColors.Charcoal,
    selectedContentColor: Color = AppColors.OnDark,
    unselectedContainerColor: Color = AppColors.Sand,
    unselectedContentColor: Color = AppColors.InkSecondary,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(AppRadii.pill))
            .background(if (isSelected) selectedContainerColor else unselectedContainerColor)
            .selectable(selected = isSelected, role = Role.RadioButton, onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingIcon?.invoke()

        Text(
            text = label,
            style = if (isSelected) {
                AppTextStyles.Chip.copy(
                    color = selectedContentColor,
                    fontWeight = FontWeight.SemiBold,
                )
            } else {
                AppTextStyles.Chip.copy(color = unselectedContentColor)
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun FilterChipPreview() {
    PlantDexTheme {
        Row(
            modifier = Modifier.padding(AppSpacing.screenH),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(label = "전체", isSelected = true, onClick = {})
            FilterChip(label = "즐겨찾기", isSelected = false, onClick = {})
        }
    }
}

/** 카메라 다크 배색 — 줌 칩과 플래시 칩이 쓴다. */
@Preview(showBackground = true, backgroundColor = 0xFF221E1A, widthDp = 390)
@Composable
private fun FilterChipOnDarkPreview() {
    PlantDexTheme {
        Row(
            modifier = Modifier.padding(AppSpacing.screenH),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            FilterChip(
                label = "1x",
                isSelected = true,
                onClick = {},
                selectedContainerColor = AppColors.Cream,
                selectedContentColor = AppColors.Charcoal,
                unselectedContainerColor = AppColors.OnDark.copy(alpha = 0.14f),
                unselectedContentColor = AppColors.OnDarkMuted,
            )
            FilterChip(
                label = "2x",
                isSelected = false,
                onClick = {},
                selectedContainerColor = AppColors.Cream,
                selectedContentColor = AppColors.Charcoal,
                unselectedContainerColor = AppColors.OnDark.copy(alpha = 0.14f),
                unselectedContentColor = AppColors.OnDarkMuted,
            )
            FilterChip(
                label = "자동",
                isSelected = false,
                onClick = {},
                unselectedContainerColor = AppColors.OnDark.copy(alpha = 0.14f),
                unselectedContentColor = AppColors.OnDark,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_flash),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = AppColors.OnDark,
                    )
                },
            )
        }
    }
}
