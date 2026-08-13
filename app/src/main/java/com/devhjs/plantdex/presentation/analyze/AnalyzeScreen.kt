package com.devhjs.plantdex.presentation.analyze

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.core.util.toKoreanDate
import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.presentation.component.PhotoPlaceholder
import com.devhjs.plantdex.presentation.component.SpecRow
import com.devhjs.plantdex.presentation.component.StarRating
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import com.devhjs.plantdex.presentation.text.bodyRes
import com.devhjs.plantdex.presentation.text.labelRes
import com.devhjs.plantdex.presentation.text.titleRes
import kotlin.time.Instant

@Composable
fun AnalyzeScreen(
    state: AnalyzeState,
    onAction: (AnalyzeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Cream)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = AppSpacing.screenH,
                vertical = AppSpacing.sectionGap,
            ),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.sectionGap),
    ) {
        when (state) {
            AnalyzeState.Idle -> IdleContent(onAction = onAction)
            AnalyzeState.Loading -> LoadingContent()
            is AnalyzeState.Success -> SuccessContent(plant = state.plant, onAction = onAction)
            is AnalyzeState.Error -> ErrorContent(error = state.error, onAction = onAction)
        }
    }
}

@Composable
private fun IdleContent(onAction: (AnalyzeAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(R.string.analyze_eyebrow),
            style = AppTextStyles.Eyebrow.copy(color = AppColors.TerracottaDeep),
        )
        Text(text = stringResource(R.string.analyze_title), style = AppTextStyles.TitleL)
        Text(text = stringResource(R.string.analyze_hint), style = AppTextStyles.BodyMuted)
    }

    PhotoPlaceholder(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        caption = stringResource(R.string.analyze_photo_placeholder),
    )

    PrimaryCta(
        text = stringResource(R.string.analyze_cta),
        onClick = { onAction(AnalyzeAction.Analyze) },
    )
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        CircularProgressIndicator(
            color = AppColors.Terracotta,
            trackColor = AppColors.Sand,
            modifier = Modifier.size(44.dp),
        )
        Text(text = stringResource(R.string.analyze_loading_title), style = AppTextStyles.TitleM)
        Text(
            text = stringResource(R.string.analyze_loading_body),
            style = AppTextStyles.BodyMuted,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SuccessContent(plant: Plant, onAction: (AnalyzeAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = plant.name, style = AppTextStyles.TitleXL)
        Text(text = plant.englishName, style = AppTextStyles.Scientific)
    }

    Text(
        text = stringResource(R.string.analyze_discovered_at, plant.discoveredAt.toKoreanDate()),
        style = AppTextStyles.Chip,
        modifier = Modifier
            .clip(RoundedCornerShape(AppRadii.pill))
            .background(AppColors.SandStrong)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.analyze_section_basic_info),
            style = AppTextStyles.SectionHeader,
        )
        Text(text = plant.description, style = AppTextStyles.Body)
    }

    SpecCard(plant = plant)

    OutlinedButton(
        onClick = { onAction(AnalyzeAction.Reset) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadii.button),
        border = BorderStroke(1.dp, AppColors.BorderMuted),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        Text(text = stringResource(R.string.analyze_reset), style = AppTextStyles.LabelAccent)
    }
}

@Composable
private fun SpecCard(plant: Plant) {
    val shape = RoundedCornerShape(AppRadii.card)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(AppColors.Sand)
            .border(1.dp, AppColors.Border, shape)
            .padding(AppSpacing.cardPadding),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.gutter),
    ) {
        SpecRow(
            iconRes = R.drawable.ic_spec_water,
            iconBackground = AppColors.WaterBg,
            iconTint = AppColors.WaterTint,
            label = stringResource(R.string.spec_watering),
        ) {
            Text(text = plant.watering, style = AppTextStyles.BodyStrong)
        }

        SpecDivider()

        SpecRow(
            iconRes = R.drawable.ic_spec_sun,
            iconBackground = AppColors.SunBg,
            iconTint = AppColors.SunTint,
            label = stringResource(R.string.spec_sunlight),
        ) {
            Text(text = stringResource(plant.sunlight.labelRes()), style = AppTextStyles.BodyStrong)
        }

        SpecDivider()

        SpecRow(
            iconRes = R.drawable.ic_spec_origin,
            iconBackground = AppColors.OriginBg,
            iconTint = AppColors.OriginTint,
            label = stringResource(R.string.spec_origin),
        ) {
            Text(text = plant.origin, style = AppTextStyles.BodyStrong)
        }

        SpecDivider()

        SpecRow(
            iconRes = R.drawable.ic_spec_level,
            iconBackground = AppColors.LevelBg,
            iconTint = AppColors.LevelTint,
            label = stringResource(R.string.spec_difficulty),
        ) {
            StarRating(rating = plant.difficulty)
        }
    }
}

@Composable
private fun SpecDivider() {
    HorizontalDivider(color = AppColors.LineStrong)
}

@Composable
private fun ErrorContent(error: AnalysisError, onAction: (AnalyzeAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = stringResource(error.titleRes()),
            style = AppTextStyles.TitleM,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(error.bodyRes()),
            style = AppTextStyles.BodyMuted,
            textAlign = TextAlign.Center,
        )
    }

    PrimaryCta(
        text = stringResource(R.string.analyze_retry),
        onClick = { onAction(AnalyzeAction.Analyze) },
    )
}

@Composable
private fun PrimaryCta(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadii.button),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.Terracotta,
            contentColor = AppColors.OnAccent,
        ),
        contentPadding = PaddingValues(
            horizontal = AppSpacing.ctaPadding,
            vertical = 16.dp,
        ),
    ) {
        Text(text = text, style = AppTextStyles.Button.copy(color = AppColors.OnAccent))
    }
}

private val PreviewPlant = Plant(
    name = "몬스테라",
    englishName = "Monstera deliciosa",
    description = "잎에 자연스럽게 구멍이 생기는 열대 관엽식물이다. 공중뿌리를 내리며 자라고, 지지대를 세워주면 잎이 더 크게 벌어진다.",
    origin = "멕시코 남부 열대우림",
    watering = "겉흙이 마르면 2주에 한 번",
    sunlight = Sunlight.BRIGHT_INDIRECT,
    difficulty = 2,
    discoveredAt = Instant.fromEpochMilliseconds(1_785_000_000_000),
)

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3)
@Composable
private fun AnalyzeScreenIdlePreview() {
    PlantDexTheme {
        AnalyzeScreen(AnalyzeState.Idle, onAction = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3)
@Composable
private fun AnalyzeScreenLoadingPreview() {
    PlantDexTheme {
        AnalyzeScreen(AnalyzeState.Loading, onAction = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 900)
@Composable
private fun AnalyzeScreenSuccessPreview() {
    PlantDexTheme {
        AnalyzeScreen(AnalyzeState.Success(PreviewPlant), onAction = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3)
@Composable
private fun AnalyzeScreenErrorPreview() {
    PlantDexTheme {
        AnalyzeScreen(
            AnalyzeState.Error(AnalysisError.NotAPlant),
            onAction = {},
        )
    }
}
