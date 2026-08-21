package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun PlantThumbnail(
    photoUri: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(AppRadii.thumb),
) {
    if (photoUri == null) {
        PhotoPlaceholder(modifier = modifier, shape = shape)
    } else {
        // 파일이 지워졌을 수 있어 실패하면 빈 자리 대신 placeholder 로 돌아간다.
        SubcomposeAsyncImage(
            model = photoUri,
            contentDescription = contentDescription,
            modifier = modifier.clip(shape),
            contentScale = ContentScale.Crop,
            loading = { PhotoPlaceholder(shape = shape) },
            error = { PhotoPlaceholder(shape = shape) },
        )
    }
}

/** photoUri 가 null 인 경우. 실제 이미지는 프리뷰에서 로드되지 않는다. */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 200)
@Composable
private fun PlantThumbnailPreview() {
    PlantDexTheme {
        PlantThumbnail(
            photoUri = null,
            contentDescription = "몬스테라",
            modifier = Modifier
                .padding(AppSpacing.gutter)
                .fillMaxWidth()
                .height(104.dp),
        )
    }
}
