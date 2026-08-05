package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun Greeting(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = title, style = AppTextStyles.TitleL)
        Spacer(Modifier.height(8.dp))
        Text(text = subtitle, style = AppTextStyles.BodyMuted)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun GreetingPreview() {
    PlantDexTheme {
        Greeting(
            title = "오늘도 새로운\n식물을 찾아보세요",
            subtitle = "지금까지 23종을 기록했어요",
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}
