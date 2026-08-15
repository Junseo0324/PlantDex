package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devhjs.plantdex.core.util.toDexLabel
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
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
    photoAspectRatio: Float = 1f,
    photoShape: Shape = RoundedCornerShape(AppRadii.thumb),
    nameStyle: TextStyle = AppTextStyles.BodyStrong.copy(
        fontSize = 13.sp,
        color = AppColors.InkBody,
    ),
) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        PlantThumbnail(
            photoUri = photoUri,
            contentDescription = name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(photoAspectRatio),
            shape = photoShape,
        )
        Spacer(Modifier.height(8.dp))
        Text(text = dexNumber.toDexLabel(), style = AppTextStyles.DexNumberS)
        Spacer(Modifier.height(2.dp))
        Text(text = name, style = nameStyle)
    }
}

/** 홈 최근 발견 — 1:1 */
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

/** 도감 그리드 — 1.24:1, 모서리 20dp, 이름 15sp */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 200)
@Composable
private fun PlantCardGridPreview() {
    PlantDexTheme {
        PlantCard(
            dexNumber = 123,
            name = "아디안텀",
            photoUri = null,
            onClick = {},
            modifier = Modifier.padding(AppSpacing.gutter),
            photoAspectRatio = 1.24f,
            photoShape = RoundedCornerShape(AppRadii.tile),
            nameStyle = AppTextStyles.BodyStrong,
        )
    }
}

/** 폭이 넓어져도 비율이 유지되는지 확인한다. */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 320)
@Composable
private fun PlantCardWidePreview() {
    PlantDexTheme {
        PlantCard(
            dexNumber = 7,
            name = "산세베리아",
            photoUri = null,
            onClick = {},
            modifier = Modifier.padding(AppSpacing.gutter),
        )
    }
}
