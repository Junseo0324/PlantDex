package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

data class MonthlyBar(val label: String, val value: Int, val color: Color)

/** 막대 높이는 최대값 대비 비율이다. 전부 0 이면 바닥선만 남는다. */
@Composable
fun MonthlyBarChart(
    bars: List<MonthlyBar>,
    modifier: Modifier = Modifier,
) {
    val maxValue = bars.maxOfOrNull(MonthlyBar::value) ?: 0

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        bars.forEach { bar ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(text = bar.value.toString(), style = AppTextStyles.Code)

                Box(
                    modifier = Modifier
                        .height(BAR_AREA_HEIGHT)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    val fraction = if (maxValue == 0) 0f else bar.value.toFloat() / maxValue

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            // 값이 0 이어도 바닥에 흔적은 남겨 칸이 비어 보이지 않게 한다.
                            .fillMaxHeight(fraction.coerceAtLeast(MIN_BAR_FRACTION))
                            .background(bar.color, BAR_SHAPE),
                    )
                }

                Text(text = bar.label, style = AppTextStyles.NavLabel)
            }
        }
    }
}

private val BAR_AREA_HEIGHT = 132.dp
private val BAR_SHAPE = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
private const val MIN_BAR_FRACTION = 0.04f

@Preview(showBackground = true, backgroundColor = 0xFFF1EBE1, widthDp = 390)
@Composable
private fun MonthlyBarChartPreview() {
    PlantDexTheme {
        MonthlyBarChart(
            bars = listOf(
                MonthlyBar("3월", 2, AppColors.ChartBarDefault),
                MonthlyBar("4월", 4, AppColors.ChartBarDefault),
                MonthlyBar("5월", 3, AppColors.ChartBarDefault),
                MonthlyBar("6월", 5, AppColors.ChartBarDefault),
                MonthlyBar("7월", 6, AppColors.Terracotta),
                MonthlyBar("8월", 3, AppColors.ChartBarDefault),
            ),
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF1EBE1, widthDp = 390)
@Composable
private fun MonthlyBarChartEmptyPreview() {
    PlantDexTheme {
        MonthlyBarChart(
            bars = listOf("3월", "4월", "5월", "6월", "7월", "8월").map {
                MonthlyBar(it, 0, AppColors.ChartBarDefault)
            },
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}
