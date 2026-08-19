package com.devhjs.plantdex.presentation.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.domain.model.CategoryCount
import com.devhjs.plantdex.domain.model.DexStats
import com.devhjs.plantdex.domain.model.MonthlyCount
import com.devhjs.plantdex.domain.model.PlantCategory
import com.devhjs.plantdex.presentation.component.CategoryBar
import com.devhjs.plantdex.presentation.component.MonthlyBar
import com.devhjs.plantdex.presentation.component.MonthlyBarChart
import com.devhjs.plantdex.presentation.component.SectionHeader
import com.devhjs.plantdex.presentation.component.StreakCard
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import com.devhjs.plantdex.presentation.text.barColor
import com.devhjs.plantdex.presentation.text.labelRes

@Composable
fun RecordScreen(
    state: RecordState,
    modifier: Modifier = Modifier,
) {
    val stats = state.stats

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Cream)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(modifier = Modifier.padding(horizontal = AppSpacing.screenH)) {
            Text(
                text = stringResource(R.string.record_title),
                style = AppTextStyles.TitleL,
                modifier = Modifier.padding(top = 20.dp),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.record_subtitle, stats.year, stats.totalCount),
                style = AppTextStyles.BodyMuted,
            )

            if (stats.totalCount == 0) {
                // 전부 0 인 차트는 허전하기만 해서 한 줄로 대신한다.
                Text(
                    text = stringResource(R.string.record_empty),
                    style = AppTextStyles.BodyMuted,
                    modifier = Modifier.padding(top = 28.dp),
                )
                return@Column
            }

            StreakCard(
                streakLabel = stringResource(R.string.record_streak),
                streakValue = stats.streakDays,
                streakUnit = stringResource(R.string.record_streak_unit),
                longestLabel = stringResource(R.string.record_longest),
                longestText = stringResource(R.string.record_longest_days, stats.longestStreakDays),
                modifier = Modifier.padding(top = 18.dp),
            )

            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(AppColors.Sand)
                    .padding(20.dp),
            ) {
                // SectionHeader 의 actionLabel 은 링크 색이라 부가 정보에는 안 맞는다.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = stringResource(R.string.record_monthly),
                        style = AppTextStyles.SectionHeader,
                    )
                    Text(
                        text = stringResource(R.string.record_monthly_range),
                        style = AppTextStyles.Label,
                    )
                }

                Spacer(Modifier.height(18.dp))
                MonthlyBarChart(bars = stats.monthly.toBars())
            }

            SectionHeader(
                title = stringResource(R.string.record_by_category),
                modifier = Modifier.padding(top = 16.dp),
            )

            Spacer(Modifier.height(10.dp))
            val topCount = stats.byCategory.maxOfOrNull(CategoryCount::count) ?: 0
            Column(
                modifier = Modifier.padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                stats.byCategory.forEach { item ->
                    CategoryBar(
                        name = stringResource(item.category.labelRes()),
                        value = item.count,
                        fraction = if (topCount == 0) 0f else item.count.toFloat() / topCount,
                        color = item.category.barColor(),
                    )
                }
            }
        }
    }
}

/** 최대값 막대만 악센트로 세운다. */
@Composable
private fun List<MonthlyCount>.toBars(): List<MonthlyBar> {
    val peak = maxOfOrNull(MonthlyCount::count) ?: 0
    return map { monthly ->
        MonthlyBar(
            label = stringResource(R.string.record_month_label, monthly.month),
            value = monthly.count,
            color = if (monthly.count == peak && peak > 0) {
                AppColors.Terracotta
            } else {
                AppColors.ChartBarDefault
            },
        )
    }
}

private val PreviewStats = DexStats(
    year = 2026,
    totalCount = 7,
    streakDays = 3,
    longestStreakDays = 12,
    monthly = listOf(
        MonthlyCount(3, 1),
        MonthlyCount(4, 0),
        MonthlyCount(5, 1),
        MonthlyCount(6, 0),
        MonthlyCount(7, 1),
        MonthlyCount(8, 4),
    ),
    byCategory = listOf(
        CategoryCount(PlantCategory.FOLIAGE, 4),
        CategoryCount(PlantCategory.WILDFLOWER, 2),
        CategoryCount(PlantCategory.BULB, 1),
    ),
)

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 780)
@Composable
private fun RecordScreenPreview() {
    PlantDexTheme {
        RecordScreen(state = RecordState(stats = PreviewStats))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 780)
@Composable
private fun RecordScreenEmptyPreview() {
    PlantDexTheme {
        RecordScreen(state = RecordState(stats = DexStats(year = 2026)))
    }
}
