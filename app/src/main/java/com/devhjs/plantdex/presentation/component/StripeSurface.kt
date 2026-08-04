package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.devhjs.plantdex.presentation.designsystem.AppColors

private const val STRIPE_WIDTH = 18f
private const val STRIPE_STEP = STRIPE_WIDTH * 2.4f

@Composable
fun StripeSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    baseColor: Color = AppColors.PlaceholderA,
    stripeColor: Color = AppColors.PlaceholderB,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(baseColor),
        contentAlignment = contentAlignment,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            var x = -size.height
            while (x < size.width) {
                drawLine(
                    color = stripeColor,
                    start = Offset(x, size.height),
                    end = Offset(x + size.height, 0f),
                    strokeWidth = STRIPE_WIDTH,
                )
                x += STRIPE_STEP
            }
        }

        content()
    }
}
