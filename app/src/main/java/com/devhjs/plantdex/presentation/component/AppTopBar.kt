package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
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
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = AppTextStyles.TitleS,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.screenH, vertical = 14.dp),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun AppTopBarPreview() {
    PlantDexTheme {
        AppTopBar(title = "식물도감")
    }
}
