package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import kotlin.time.Instant

@Composable
fun PlantCardRow(
    entries: List<DexEntry>,
    slots: Int,
    onEntryClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.gutter),
    ) {
        entries.take(slots).forEach { entry ->
            PlantCard(
                dexNumber = entry.dexNumber,
                name = entry.plant.name,
                photoUri = entry.photoUri,
                onClick = { onEntryClick(entry.id) },
                modifier = Modifier.weight(1f),
            )
        }
        repeat(slots - entries.size.coerceAtMost(slots)) {
            Spacer(Modifier.weight(1f))
        }
    }
}

private fun previewEntry(number: Int, name: String) = DexEntry(
    id = number.toLong(),
    dexNumber = number,
    plant = Plant(
        name = name,
        englishName = "Monstera deliciosa",
        description = "잎에 자연스럽게 구멍이 생기는 열대 관엽식물이다.",
        origin = "멕시코 남부 열대우림",
        watering = "겉흙이 마르면 2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        difficulty = 2,
        discoveredAt = Instant.fromEpochMilliseconds(1_785_000_000_000),
    ),
)

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun PlantCardRowPreview() {
    PlantDexTheme {
        PlantCardRow(
            entries = listOf(
                previewEntry(23, "튤립"),
                previewEntry(22, "해바라기"),
                previewEntry(21, "선인장"),
            ),
            slots = 3,
            onEntryClick = {},
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}

/** 항목이 모자랄 때 카드 폭이 늘어나지 않는지 본다. */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun PlantCardRowPartialPreview() {
    PlantDexTheme {
        PlantCardRow(
            entries = listOf(previewEntry(2, "산세베리아")),
            slots = 3,
            onEntryClick = {},
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}
