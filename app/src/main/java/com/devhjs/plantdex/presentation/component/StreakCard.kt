package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun StreakCard(
    streakLabel: String,
    streakValue: Int,
    streakUnit: String,
    longestLabel: String,
    longestText: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AppColors.Charcoal)
            .padding(horizontal = 24.dp, vertical = 22.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = streakLabel,
                style = AppTextStyles.Label.copy(color = AppColors.OnDarkFaint),
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = streakValue.toString(),
                    style = AppTextStyles.StatNumber.copy(color = AppColors.OnDark),
                )
                Text(
                    text = streakUnit,
                    style = AppTextStyles.Label.copy(color = AppColors.OnDarkMuted),
                    modifier = Modifier.padding(start = 2.dp, bottom = 4.dp),
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = longestLabel,
                style = AppTextStyles.Label.copy(color = AppColors.OnDarkFaint),
            )
            Text(
                text = longestText,
                style = AppTextStyles.BodyStrong.copy(
                    fontSize = 16.sp,
                    color = AppColors.AccentSoft,
                ),
                textAlign = TextAlign.End,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun StreakCardPreview() {
    PlantDexTheme {
        StreakCard(
            streakLabel = "연속 기록",
            streakValue = 3,
            streakUnit = "일",
            longestLabel = "최장 기록",
            longestText = "12일",
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun StreakCardZeroPreview() {
    PlantDexTheme {
        StreakCard(
            streakLabel = "연속 기록",
            streakValue = 0,
            streakUnit = "일",
            longestLabel = "최장 기록",
            longestText = "0일",
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}
