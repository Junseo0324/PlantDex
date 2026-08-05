package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun StatTile(
    label: String,
    value: Int,
    unit: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(AppRadii.tile))
            .background(AppColors.Sand)
            .padding(AppSpacing.cardPadding),
    ) {
        Text(text = label, style = AppTextStyles.Label)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value.toString(), style = AppTextStyles.StatNumber)
            Text(text = unit, style = AppTextStyles.BodyStrong.copy(color = AppColors.InkMuted))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun StatTilePreview() {
    PlantDexTheme {
        Row(
            modifier = Modifier.padding(AppSpacing.screenH),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.gutter),
        ) {
            StatTile(label = "발견한 식물", value = 23, unit = "종", modifier = Modifier.weight(1f))
            StatTile(label = "이번 달", value = 6, unit = "종", modifier = Modifier.weight(1f))
        }
    }
}
