package com.devhjs.plantdex.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.presentation.component.AppTopBar
import com.devhjs.plantdex.presentation.component.DiscoverCard
import com.devhjs.plantdex.presentation.component.Greeting
import com.devhjs.plantdex.presentation.component.PlantCardRow
import com.devhjs.plantdex.presentation.component.SectionHeader
import com.devhjs.plantdex.presentation.component.StatTile
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import kotlin.time.Instant

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Cream),
    ) {
        AppTopBar(title = stringResource(R.string.home_title))

        Column(modifier = Modifier.padding(horizontal = AppSpacing.screenH)) {
            Greeting(
                title = stringResource(R.string.home_greeting),
                subtitle = stringResource(R.string.home_record_count, state.discoveredCount),
                modifier = Modifier.padding(top = 18.dp),
            )

            Spacer(Modifier.height(22.dp))
            DiscoverCard(
                title = stringResource(R.string.home_cta_title),
                body = stringResource(R.string.home_cta_body),
                onClick = { onAction(HomeAction.Discover) },
            )

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.gutter)) {
                StatTile(
                    label = stringResource(R.string.home_stat_total),
                    value = state.discoveredCount,
                    unit = stringResource(R.string.home_count_unit),
                    modifier = Modifier.weight(1f),
                )
                StatTile(
                    label = stringResource(R.string.home_stat_month),
                    value = state.thisMonthCount,
                    unit = stringResource(R.string.home_count_unit),
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(28.dp))
            SectionHeader(
                title = stringResource(R.string.home_section_recent),
                actionLabel = stringResource(R.string.home_see_all),
                onActionClick = { onAction(HomeAction.SeeAllCollection) },
            )

            Spacer(Modifier.height(12.dp))
            if (state.recent.isEmpty()) {
                Text(
                    text = stringResource(R.string.home_recent_empty),
                    style = AppTextStyles.BodyMuted,
                )
            } else {
                PlantCardRow(
                    entries = state.recent,
                    slots = RECENT_LIMIT,
                    onEntryClick = { onAction(HomeAction.OpenDetail(it)) },
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 780)
@Composable
private fun HomeScreenPreview() {
    PlantDexTheme {
        HomeScreen(
            state = HomeState(
                discoveredCount = 23,
                thisMonthCount = 6,
                recent = listOf(
                    previewEntry(23, "튤립"),
                    previewEntry(22, "해바라기"),
                    previewEntry(21, "선인장"),
                ),
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 780)
@Composable
private fun HomeScreenEmptyPreview() {
    PlantDexTheme {
        HomeScreen(state = HomeState(), onAction = {})
    }
}

private val PreviewPlant = Plant(
    name = "몬스테라",
    englishName = "Monstera deliciosa",
    description = "잎에 자연스럽게 구멍이 생기는 열대 관엽식물이다.",
    origin = "멕시코 남부 열대우림",
    watering = "겉흙이 마르면 2주에 한 번",
    sunlight = Sunlight.BRIGHT_INDIRECT,
    difficulty = 2,
    discoveredAt = Instant.fromEpochMilliseconds(1_785_000_000_000),
)

private fun previewEntry(number: Int, name: String) = DexEntry(
    id = number.toLong(),
    dexNumber = number,
    plant = PreviewPlant.copy(name = name),
)
