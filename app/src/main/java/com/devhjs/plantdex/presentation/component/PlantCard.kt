package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devhjs.plantdex.core.util.toDexLabel
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun PlantCard(
    dexNumber: Int,
    name: String,
    photoUri: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    photoHeight: Dp = 104.dp,
) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        PlantThumbnail(
            photoUri = photoUri,
            contentDescription = name,
            modifier = Modifier
                .fillMaxWidth()
                .height(photoHeight),
        )
        Spacer(Modifier.height(8.dp))
        Text(text = dexNumber.toDexLabel(), style = AppTextStyles.DexNumberS)
        Spacer(Modifier.height(2.dp))
        Text(
            text = name,
            style = AppTextStyles.BodyStrong.copy(fontSize = 13.sp, color = AppColors.InkBody),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 160)
@Composable
private fun PlantCardPreview() {
    PlantDexTheme {
        PlantCard(
            dexNumber = 4,
            name = "몬스테라",
            photoUri = null,
            onClick = {},
            modifier = Modifier.padding(AppSpacing.gutter),
        )
    }
}

/** 도감 그리드 크기(132dp) */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 200)
@Composable
private fun PlantCardTallPreview() {
    PlantDexTheme {
        PlantCard(
            dexNumber = 123,
            name = "아디안텀",
            photoUri = null,
            onClick = {},
            modifier = Modifier.padding(AppSpacing.gutter),
            photoHeight = 132.dp,
        )
    }
}
