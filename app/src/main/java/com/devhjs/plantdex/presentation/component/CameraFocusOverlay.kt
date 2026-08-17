package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

private val SOLID_DIAMETER = 214.dp
private val DASHED_DIAMETER = 246.dp

/** 촬영 조준 가이드. 장식이라 접근성 트리에서 감춘다. */
@Composable
fun CameraFocusOverlay(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .size(DASHED_DIAMETER)
            .clearAndSetSemantics {},
    ) {
        drawCircle(
            color = AppColors.OnDark.copy(alpha = 0.75f),
            radius = SOLID_DIAMETER.toPx() / 2f,
            style = Stroke(width = 2.dp.toPx()),
        )
        drawCircle(
            color = AppColors.OnDark.copy(alpha = 0.25f),
            radius = DASHED_DIAMETER.toPx() / 2f,
            style = Stroke(
                width = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(8.dp.toPx(), 8.dp.toPx()),
                ),
            ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF38312A, widthDp = 280, heightDp = 280)
@Composable
private fun CameraFocusOverlayPreview() {
    PlantDexTheme {
        CameraFocusOverlay()
    }
}
