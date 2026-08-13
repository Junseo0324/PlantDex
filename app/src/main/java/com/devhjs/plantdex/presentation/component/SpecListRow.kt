package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun SpecListRow(
    label: String,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    value: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = label, style = AppTextStyles.Label)
            value()
        }

        if (showDivider) {
            HorizontalDivider(color = AppColors.LineStrong)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF1EBE1, widthDp = 340)
@Composable
private fun SpecListRowPreview() {
    PlantDexTheme {
        SpecListRow(label = "원산지") {
            Text(text = "멕시코 남부 열대우림", style = AppTextStyles.BodyStrong)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF1EBE1, widthDp = 340)
@Composable
private fun SpecListRowWithStarsPreview() {
    PlantDexTheme {
        SpecListRow(label = "난이도") {
            StarRating(rating = 2)
        }
    }
}

/** 마지막 행은 구분선을 그리지 않는다. */
@Preview(showBackground = true, backgroundColor = 0xFFF1EBE1, widthDp = 340)
@Composable
private fun SpecListRowWithoutDividerPreview() {
    PlantDexTheme {
        SpecListRow(label = "발견일", showDivider = false) {
            Text(text = "2026년 8월 4일", style = AppTextStyles.BodyStrong)
        }
    }
}
