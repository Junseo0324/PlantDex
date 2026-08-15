package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        style = if (isSelected) {
            AppTextStyles.Chip.copy(
                color = AppColors.OnDark,
                fontWeight = FontWeight.SemiBold,
            )
        } else {
            AppTextStyles.Chip
        },
        modifier = modifier
            .clip(RoundedCornerShape(AppRadii.pill))
            .background(if (isSelected) AppColors.Charcoal else AppColors.Sand)
            .selectable(selected = isSelected, role = Role.RadioButton, onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 8.dp),
    )
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
