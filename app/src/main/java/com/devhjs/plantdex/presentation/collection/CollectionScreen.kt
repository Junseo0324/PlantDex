package com.devhjs.plantdex.presentation.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.core.util.toDotDate
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.presentation.component.FilterChip
import com.devhjs.plantdex.presentation.component.PlantGrid
import com.devhjs.plantdex.presentation.component.SearchField
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import kotlin.time.Instant

@Composable
fun CollectionScreen(
    state: CollectionState,
    onAction: (CollectionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Cream),
    ) {
        Column(modifier = Modifier.padding(horizontal = AppSpacing.screenH)) {
            Text(
                text = stringResource(R.string.collection_title),
                style = AppTextStyles.TitleL,
                modifier = Modifier.padding(top = 20.dp),
            )
            Spacer(Modifier.height(6.dp))
            Text(text = subtitleOf(state), style = AppTextStyles.BodyMuted)

            SearchField(
                value = state.query,
                onValueChange = { onAction(CollectionAction.QueryChanged(it)) },
                placeholder = stringResource(R.string.collection_search_placeholder),
                modifier = Modifier.padding(top = 18.dp),
            )

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    label = stringResource(R.string.collection_filter_all),
                    isSelected = state.filter == CollectionFilter.All,
                    onClick = { onAction(CollectionAction.FilterChanged(CollectionFilter.All)) },
                )
                FilterChip(
                    label = stringResource(R.string.collection_filter_favorites),
                    isSelected = state.filter == CollectionFilter.Favorites,
                    onClick = {
                        onAction(CollectionAction.FilterChanged(CollectionFilter.Favorites))
                    },
                )
            }
        }

        if (state.entries.isEmpty()) {
            EmptyMessage(
                text = if (state.totalCount == 0) {
                    stringResource(R.string.collection_empty)
                } else {
                    stringResource(R.string.collection_no_result)
                },
                modifier = Modifier.weight(1f),
            )
        } else {
            PlantGrid(
                entries = state.entries,
                onEntryClick = { onAction(CollectionAction.OpenDetail(it)) },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = AppSpacing.screenH,
                    end = AppSpacing.screenH,
                    top = 18.dp,
                    bottom = 18.dp,
                ),
            )
        }
    }
}

@Composable
private fun subtitleOf(state: CollectionState): String {
    val lastDiscoveredAt = state.lastDiscoveredAt
    return if (lastDiscoveredAt == null) {
        stringResource(R.string.collection_subtitle_no_date, state.totalCount)
    } else {
        stringResource(R.string.collection_subtitle, state.totalCount, lastDiscoveredAt.toDotDate())
    }
}

@Composable
private fun EmptyMessage(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = AppTextStyles.BodyMuted)
    }
}

private fun previewEntry(number: Int, name: String) = DexEntry(
    id = number.toLong(),
    dexNumber = number,
    plant = Plant(
        name = name,
        englishName = "Monstera deliciosa",
        description = "열대 관엽식물",
        origin = "멕시코 남부",
        watering = "2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        difficulty = 2,
        discoveredAt = Instant.fromEpochMilliseconds(1_785_000_000_000),
    ),
)

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 780)
@Composable
private fun CollectionScreenPreview() {
    PlantDexTheme {
        CollectionScreen(
            state = CollectionState(
                entries = listOf(
                    previewEntry(6, "튤립"),
                    previewEntry(5, "해바라기"),
                    previewEntry(4, "몬스테라"),
                    previewEntry(3, "산세베리아"),
                    previewEntry(2, "스투키"),
                    previewEntry(1, "아디안텀"),
                ),
                totalCount = 6,
                lastDiscoveredAt = Instant.fromEpochMilliseconds(1_785_000_000_000),
            ),
            onAction = {},
        )
    }
}

/** 도감이 아예 빈 경우 */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 780)
@Composable
private fun CollectionScreenEmptyPreview() {
    PlantDexTheme {
        CollectionScreen(state = CollectionState(), onAction = {})
    }
}

/** 검색 결과만 없는 경우 — 헤더의 개수는 그대로 남는다. */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 780)
@Composable
private fun CollectionScreenNoResultPreview() {
    PlantDexTheme {
        CollectionScreen(
            state = CollectionState(
                query = "존재하지않는식물",
                totalCount = 6,
                lastDiscoveredAt = Instant.fromEpochMilliseconds(1_785_000_000_000),
            ),
            onAction = {},
        )
    }
}
