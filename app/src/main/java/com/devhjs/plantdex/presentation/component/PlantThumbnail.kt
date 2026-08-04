package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.devhjs.plantdex.presentation.designsystem.AppRadii

/**
 * 도감 사진. 카메라가 붙기 전까지는 photoUri 가 항상 null 이라 placeholder 로 떨어진다.
 */
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
        AsyncImage(
            model = photoUri,
            contentDescription = contentDescription,
            modifier = modifier.clip(shape),
            contentScale = ContentScale.Crop,
        )
    }
}
