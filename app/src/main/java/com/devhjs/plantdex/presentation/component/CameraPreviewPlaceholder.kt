package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/**
 * CameraX 프리뷰를 띄울 수 없을 때(@Preview · 권한 대기) 자리를 채우는 면.
 * 모서리는 프리뷰를 감싸는 쪽에서 자른다.
 */
@Composable
fun CameraPreviewPlaceholder(modifier: Modifier = Modifier) {
    StripeSurface(
        modifier = modifier.fillMaxSize(),
        baseColor = AppColors.CharcoalRaise,
        stripeColor = AppColors.CharcoalRaise2,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A, widthDp = 390)
@Composable
private fun CameraPreviewPlaceholderPreview() {
    PlantDexTheme {
        CameraPreviewPlaceholder(
            modifier = Modifier
                .padding(AppSpacing.screenH)
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(AppRadii.hero)),
        )
    }
}
