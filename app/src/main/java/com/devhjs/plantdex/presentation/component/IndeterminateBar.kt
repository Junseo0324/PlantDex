package com.devhjs.plantdex.presentation.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

private const val MIN_FRACTION = 0.18f
private const val MAX_FRACTION = 0.82f
private const val CYCLE_MILLIS = 2_400

/**
 * 진행률이 아니라 진행 중임을 보여주는 왕복 애니메이션이다.
 * 실제 단계 수를 알 수 없어서 값이 아니라 움직임만 전달한다.
 */
@Composable
fun IndeterminateBar(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "indeterminateBar")
    val fraction by transition.animateFloat(
        initialValue = MIN_FRACTION,
        targetValue = MAX_FRACTION,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = CYCLE_MILLIS),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "fraction",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(AppColors.Line, RoundedCornerShape(AppRadii.pill)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction)
                .fillMaxHeight()
                .background(AppColors.Terracotta, RoundedCornerShape(AppRadii.pill)),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun IndeterminateBarPreview() {
    PlantDexTheme {
        IndeterminateBar(modifier = Modifier.padding(AppSpacing.screenH))
    }
}
