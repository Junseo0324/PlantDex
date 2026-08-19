package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun CategoryBar(
    name: String,
    value: Int,
    fraction: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = AppTextStyles.Chip,
            modifier = Modifier.width(52.dp),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(AppRadii.pill))
                .background(AppColors.ChartTrack),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction.coerceIn(0f, 1f))
                    .height(8.dp)
                    .background(color, RoundedCornerShape(AppRadii.pill)),
            )
        }

        Text(
            text = value.toString(),
            style = AppTextStyles.Chip.copy(color = AppColors.InkSoft),
            textAlign = TextAlign.End,
            modifier = Modifier.width(30.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun CategoryBarPreview() {
    PlantDexTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.screenH),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CategoryBar("관엽", 4, 1f, AppColors.Leaf)
            CategoryBar("야생화", 2, 0.5f, AppColors.Terracotta)
            CategoryBar("구근", 1, 0.25f, AppColors.CategoryBulb)
        }
    }
}
