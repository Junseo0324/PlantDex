package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles

/**
 * 카메라가 붙기 전까지 사진 자리를 대신하는 placeholder.
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
