package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import kotlin.time.Instant

private const val GRID_PHOTO_ASPECT_RATIO = 1.24f
private val GRID_GAP = 14.dp

@Composable
fun PlantGrid(
    entries: List<DexEntry>,
    onEntryClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(GRID_GAP),
        horizontalArrangement = Arrangement.spacedBy(GRID_GAP),
    ) {
        items(entries, key = { it.id }) { entry ->
            PlantCard(
                dexNumber = entry.dexNumber,
                name = entry.plant.name,
                photoUri = entry.photoUri,
                onClick = { onEntryClick(entry.id) },
                photoAspectRatio = GRID_PHOTO_ASPECT_RATIO,
                photoShape = RoundedCornerShape(AppRadii.tile),
                nameStyle = AppTextStyles.BodyStrong,
            )
        }
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

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390, heightDp = 640)
@Composable
private fun PlantGridPreview() {
    PlantDexTheme {
        PlantGrid(
            entries = listOf(
                previewEntry(6, "튤립"),
                previewEntry(5, "해바라기"),
                previewEntry(4, "몬스테라"),
                previewEntry(3, "산세베리아"),
                previewEntry(2, "스투키"),
                previewEntry(1, "아디안텀"),
            ),
            onEntryClick = {},
            contentPadding = PaddingValues(AppSpacing.screenH),
        )
    }
}

/** 홀수 개일 때 마지막 칸이 늘어나지 않는지 확인한다. */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390, heightDp = 320)
@Composable
private fun PlantGridOddPreview() {
    PlantDexTheme {
        PlantGrid(
            entries = listOf(previewEntry(1, "아디안텀")),
            onEntryClick = {},
            contentPadding = PaddingValues(AppSpacing.screenH),
        )
    }
}
