package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun CodeBlock(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = AppTextStyles.Code,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadii.tile))
            .background(AppColors.Sand)
            .padding(horizontal = 18.dp, vertical = 16.dp),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun CodeBlockPreview() {
    PlantDexTheme {
        CodeBlock(
            text = "{ \"name\": \"튤립\", \"scientificName\": \"Tulipa gesneriana\", \"difficulty\": \"쉬움\" }",
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}
