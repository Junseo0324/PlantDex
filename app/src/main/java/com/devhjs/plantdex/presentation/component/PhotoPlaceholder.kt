package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/**
 * 사진이 없거나 읽지 못할 때 자리를 대신한다.
 */
@Composable
fun PhotoPlaceholder(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(AppRadii.photo),
    caption: String? = null,
) {
    StripeSurface(modifier = modifier, shape = shape) {
        caption?.let { Text(text = it, style = AppTextStyles.Code) }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun PhotoPlaceholderPreview() {
    PlantDexTheme {
        PhotoPlaceholder(
            modifier = Modifier
                .padding(AppSpacing.screenH)
                .fillMaxWidth()
                .height(200.dp),
            caption = "사진 자리 (임시)",
        )
    }
}

/** 발견 연출 화면에서 쓰는 원형. */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 260)
@Composable
private fun PhotoPlaceholderCirclePreview() {
    PlantDexTheme {
        PhotoPlaceholder(
            modifier = Modifier
                .padding(AppSpacing.screenH)
                .size(190.dp),
            shape = CircleShape,
        )
    }
}
