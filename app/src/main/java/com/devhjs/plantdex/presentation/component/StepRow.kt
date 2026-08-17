package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/** 좌우 "단계 이름 / 상태" 한 줄. 상태 색은 호출자가 정한다. */
@Composable
fun StepRow(
    label: String,
    status: String,
    statusColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = AppTextStyles.Chip.copy(color = AppColors.InkSecondary),
        )
        Text(
            text = status,
            style = AppTextStyles.Chip.copy(
                color = statusColor,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun StepRowPreview() {
    PlantDexTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.screenH),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StepRow(label = "이미지 업로드", status = "완료", statusColor = AppColors.Leaf)
            StepRow(label = "Vision 모델 추론", status = "진행 중", statusColor = AppColors.Terracotta)
            StepRow(label = "도감 등록", status = "대기", statusColor = AppColors.InkFaint)
        }
    }
}
