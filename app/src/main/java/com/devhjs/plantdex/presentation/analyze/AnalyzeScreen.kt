package com.devhjs.plantdex.presentation.analyze

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.core.util.toDexLabel
import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.presentation.component.AppButton
import com.devhjs.plantdex.presentation.component.AppOutlinedButton
import com.devhjs.plantdex.presentation.component.CodeBlock
import com.devhjs.plantdex.presentation.component.IndeterminateBar
import com.devhjs.plantdex.presentation.component.PhotoPlaceholder
import com.devhjs.plantdex.presentation.component.RevealPhoto
import com.devhjs.plantdex.presentation.component.StepRow
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import com.devhjs.plantdex.presentation.text.bodyRes
import com.devhjs.plantdex.presentation.text.titleRes
import kotlin.time.Instant

@Composable
fun AnalyzeScreen(
    state: AnalyzeState,
    onAction: (AnalyzeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 발견 연출만 액센트 풀스크린이라 배경을 상태에 따라 고른다.
    val background = when (state) {
        is AnalyzeState.Success -> AppColors.Terracotta
        else -> AppColors.Cream
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .safeDrawingPadding(),
    ) {
        when (state) {
            AnalyzeState.Loading -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
                ) {
                    PhotoPlaceholder(
                        modifier = Modifier.size(200.dp),
                        shape = CircleShape,
                        caption = stringResource(R.string.analyze_shot_caption),
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.analyze_progress_title),
                            style = AppTextStyles.TitleM,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = stringResource(R.string.analyze_progress_body),
                            style = AppTextStyles.BodyMuted,
                            textAlign = TextAlign.Center,
                        )
                    }

                    IndeterminateBar()

                    // 목업 분석기가 지연만 두므로 실제 단계가 없다. 연출용 고정값이다.
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        StepRow(
                            label = stringResource(R.string.analyze_step_upload),
                            status = stringResource(R.string.analyze_status_done),
                            statusColor = AppColors.Leaf,
                        )
                        StepRow(
                            label = stringResource(R.string.analyze_step_infer),
                            status = stringResource(R.string.analyze_status_running),
                            statusColor = AppColors.Terracotta,
                        )
                        StepRow(
                            label = stringResource(R.string.analyze_step_register),
                            status = stringResource(R.string.analyze_status_waiting),
                            statusColor = AppColors.InkFaint,
                        )
                    }
                }

                CodeBlock(
                    text = stringResource(R.string.analyze_json_sample),
                    modifier = Modifier.padding(
                        start = AppSpacing.screenH,
                        end = AppSpacing.screenH,
                        bottom = 44.dp,
                    ),
                )
            }

            is AnalyzeState.Success -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(26.dp, Alignment.CenterVertically),
                ) {
                    Text(
                        text = stringResource(R.string.reveal_eyebrow),
                        style = AppTextStyles.Eyebrow,
                    )

                    RevealPhoto(photoUri = null, contentDescription = state.plant.name)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = state.nextDexNumber.toDexLabel(),
                            style = AppTextStyles.DexNumberL,
                        )
                        Text(
                            text = state.plant.name,
                            style = AppTextStyles.Display,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = stringResource(R.string.reveal_body),
                            style = AppTextStyles.BodyStrong.copy(
                                fontSize = 15.sp,
                                color = AppColors.TerracottaOnSoft,
                            ),
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(
                        start = AppSpacing.screenH,
                        end = AppSpacing.screenH,
                        bottom = 44.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.gutter),
                ) {
                    AppButton(
                        text = stringResource(R.string.reveal_register),
                        onClick = { onAction(AnalyzeAction.Register) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    AppOutlinedButton(
                        text = stringResource(R.string.reveal_retake),
                        onClick = { onAction(AnalyzeAction.Retake) },
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = AppColors.OnAccent.copy(alpha = 0.6f),
                        contentColor = AppColors.OnAccent,
                    )
                }
            }

            is AnalyzeState.Error -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = AppSpacing.screenH),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                ) {
                    Text(
                        text = stringResource(state.error.titleRes()),
                        style = AppTextStyles.TitleM,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(state.error.bodyRes()),
                        style = AppTextStyles.BodyMuted,
                        textAlign = TextAlign.Center,
                    )
                    AppButton(
                        text = stringResource(R.string.analyze_retry),
                        onClick = { onAction(AnalyzeAction.Analyze) },
                        modifier = Modifier.padding(top = 10.dp),
                        containerColor = AppColors.Terracotta,
                        contentColor = AppColors.OnAccent,
                    )
                }
            }
        }
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

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 780)
@Composable
private fun AnalyzeScreenLoadingPreview() {
    PlantDexTheme {
        AnalyzeScreen(state = AnalyzeState.Loading, onAction = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 780)
@Composable
private fun AnalyzeScreenSuccessPreview() {
    PlantDexTheme {
        AnalyzeScreen(
            state = AnalyzeState.Success(PreviewPlant, nextDexNumber = 5),
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 780)
@Composable
private fun AnalyzeScreenErrorPreview() {
    PlantDexTheme {
        AnalyzeScreen(state = AnalyzeState.Error(AnalysisError.NotAPlant), onAction = {})
    }
}
