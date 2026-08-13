package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme


@Composable
fun SpecList(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadii.card))
            .background(AppColors.Sand),
        content = content,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun SpecListPreview() {
    PlantDexTheme {
        SpecList(modifier = Modifier.padding(AppSpacing.screenH)) {
            SpecListRow(label = "원산지") {
                Text(text = "멕시코 남부 열대우림", style = AppTextStyles.BodyStrong)
            }
            SpecListRow(label = "물주기") {
                Text(text = "겉흙이 마르면 2주에 한 번", style = AppTextStyles.BodyStrong)
            }
            SpecListRow(label = "햇빛") {
                Text(text = "밝은 간접광", style = AppTextStyles.BodyStrong)
            }
            SpecListRow(label = "난이도") {
                StarRating(rating = 2)
            }
            SpecListRow(label = "발견일", showDivider = false) {
                Text(text = "2026년 8월 4일", style = AppTextStyles.BodyStrong)
            }
        }
    }
}
