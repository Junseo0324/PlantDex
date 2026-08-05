package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
fun DiscoverCard(
    title: String,
    body: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadii.hero))
            .background(AppColors.InkSoft)
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = AppSpacing.ctaPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = title, style = AppTextStyles.TitleS.copy(color = AppColors.OnDark))
            Text(text = body, style = AppTextStyles.Label.copy(color = AppColors.InkDisabled))
        }

        Box(
            modifier = Modifier
                .size(56.dp)
                .background(AppColors.Terracotta, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 22.dp, height = 16.dp)
                    .border(2.dp, AppColors.OnDark, RoundedCornerShape(4.dp)),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun DiscoverCardPreview() {
    PlantDexTheme {
        DiscoverCard(
            title = "식물 발견하기",
            body = "카메라로 촬영하면 AI가 분석해요",
            onClick = {},
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}
