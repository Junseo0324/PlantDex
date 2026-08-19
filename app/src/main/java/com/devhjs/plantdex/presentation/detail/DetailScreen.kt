package com.devhjs.plantdex.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.core.util.toDexLabel
import com.devhjs.plantdex.core.util.toKoreanDate
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.presentation.component.AppButton
import com.devhjs.plantdex.presentation.component.AppIconButton
import com.devhjs.plantdex.presentation.component.BackTopBar
import com.devhjs.plantdex.presentation.component.FavoriteToggleButton
import com.devhjs.plantdex.presentation.component.PhotoPlaceholder
import com.devhjs.plantdex.presentation.component.SectionHeader
import com.devhjs.plantdex.presentation.component.SpecList
import com.devhjs.plantdex.presentation.component.SpecListRow
import com.devhjs.plantdex.presentation.component.StarRating
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import com.devhjs.plantdex.presentation.text.labelRes
import kotlin.time.Instant

private const val DETAIL_PHOTO_ASPECT_RATIO = 1.82f

@Composable
fun DetailScreen(
    state: DetailState,
    onAction: (DetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Cream)
            .safeDrawingPadding(),
    ) {
        // 공유는 아직 갈 곳이 없어서 표시만 한다.
        BackTopBar(
            onBack = { onAction(DetailAction.Back) },
            contentDescription = stringResource(R.string.action_back),
            actions = {
                AppIconButton(
                    iconRes = R.drawable.ic_share,
                    contentDescription = stringResource(R.string.detail_share),
                )
            },
        )

        val entry = state.entry ?: return@Column

        // 화면 좌우 여백은 여기서 한 번만 준다.
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppSpacing.screenH),
        ) {
            PhotoPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(DETAIL_PHOTO_ASPECT_RATIO),
            )

            Column(modifier = Modifier.padding(top = 22.dp)) {
                Text(text = entry.dexNumber.toDexLabel(), style = AppTextStyles.DexNumber)
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(text = entry.plant.name, style = AppTextStyles.TitleXL)
                    Text(text = entry.plant.englishName, style = AppTextStyles.Scientific)
                }
                Spacer(Modifier.height(12.dp))
                Text(text = entry.plant.description, style = AppTextStyles.Body)
            }

            SpecList(modifier = Modifier.padding(top = 20.dp)) {
                SpecListRow(label = stringResource(R.string.spec_origin)) {
                    Text(text = entry.plant.origin, style = AppTextStyles.BodyStrong)
                }
                SpecListRow(label = stringResource(R.string.spec_watering)) {
                    Text(text = entry.plant.watering, style = AppTextStyles.BodyStrong)
                }
                SpecListRow(label = stringResource(R.string.spec_sunlight)) {
                    Text(
                        text = stringResource(entry.plant.sunlight.labelRes()),
                        style = AppTextStyles.BodyStrong,
                    )
                }
                SpecListRow(label = stringResource(R.string.spec_difficulty)) {
                    StarRating(rating = entry.plant.difficulty)
                }
                SpecListRow(
                    label = stringResource(R.string.spec_discovered_at),
                    showDivider = false,
                ) {
                    Text(
                        text = entry.plant.discoveredAt.toKoreanDate(),
                        style = AppTextStyles.BodyStrong,
                    )
                }
            }

            entry.memo?.takeIf { it.isNotBlank() }?.let { memo ->
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    SectionHeader(title = stringResource(R.string.detail_section_memo))
                    Spacer(Modifier.height(8.dp))
                    Text(text = memo, style = AppTextStyles.Body)
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        Row(
            modifier = Modifier.padding(
                start = AppSpacing.screenH,
                end = AppSpacing.screenH,
                top = 16.dp,
                bottom = 40.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.gutter),
        ) {
            FavoriteToggleButton(
                isFavorite = entry.isFavorite,
                onToggle = { onAction(DetailAction.ToggleFavorite) },
                contentDescription = stringResource(
                    if (entry.isFavorite) {
                        R.string.detail_favorite_remove
                    } else {
                        R.string.detail_favorite_add
                    }
                ),
            )
            AppButton(
                text = stringResource(R.string.detail_memo_cta),
                onClick = { onAction(DetailAction.OpenMemoEditor) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private val PreviewEntry = DexEntry(
    id = 4L,
    dexNumber = 4,
    plant = Plant(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "잎에 자연스럽게 구멍이 생기는 열대 관엽식물이다. 공중뿌리를 내리며 자라고, 지지대를 세워주면 잎이 더 크게 벌어진다.",
        origin = "멕시코 남부 열대우림",
        watering = "겉흙이 마르면 2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        difficulty = 2,
        discoveredAt = Instant.fromEpochMilliseconds(1_785_000_000_000),
    ),
)

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 900)
@Composable
private fun DetailScreenPreview() {
    PlantDexTheme {
        DetailScreen(state = DetailState(entry = PreviewEntry), onAction = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 900)
@Composable
private fun DetailScreenFavoritePreview() {
    PlantDexTheme {
        DetailScreen(
            state = DetailState(entry = PreviewEntry.copy(isFavorite = true)),
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 900)
@Composable
private fun DetailScreenWithMemoPreview() {
    PlantDexTheme {
        DetailScreen(
            state = DetailState(
                entry = PreviewEntry.copy(memo = "창가로 옮긴 뒤 새잎이 두 장 났다."),
            ),
            onAction = {},
        )
    }
}
