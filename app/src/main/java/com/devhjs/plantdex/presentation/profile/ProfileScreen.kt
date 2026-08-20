package com.devhjs.plantdex.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.core.util.toYearMonth
import com.devhjs.plantdex.domain.model.PlantLevel
import com.devhjs.plantdex.domain.model.ProfileSummary
import com.devhjs.plantdex.domain.model.UserProfile
import com.devhjs.plantdex.presentation.component.AppIconButton
import com.devhjs.plantdex.presentation.component.AppTopBar
import com.devhjs.plantdex.presentation.component.LevelProgressCard
import com.devhjs.plantdex.presentation.component.ProfileHeader
import com.devhjs.plantdex.presentation.component.SettingRow
import com.devhjs.plantdex.presentation.component.SpecList
import com.devhjs.plantdex.presentation.component.StatTile
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import com.devhjs.plantdex.presentation.text.labelRes
import kotlin.time.Instant

@Composable
fun ProfileScreen(
    state: ProfileState,
    modifier: Modifier = Modifier,
) {
    val summary = state.summary

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Cream)
            .verticalScroll(rememberScrollState()),
    ) {
        // 설정은 아직 갈 곳이 없어서 표시만 한다.
        AppTopBar(
            title = stringResource(R.string.profile_title),
            actions = {
                AppIconButton(
                    iconRes = R.drawable.ic_settings,
                    contentDescription = stringResource(R.string.profile_settings),
                )
            },
        )

        Column(modifier = Modifier.padding(horizontal = AppSpacing.screenH)) {
            ProfileHeader(
                name = summary.user?.name.orEmpty(),
                levelText = stringResource(
                    R.string.profile_level_badge,
                    summary.level,
                    stringResource(summary.levelTitle.labelRes()),
                ),
                joinedText = stringResource(
                    R.string.profile_joined,
                    summary.user?.joinedAt?.toYearMonth().orEmpty(),
                ),
                avatarUri = summary.user?.avatarUri,
                modifier = Modifier.padding(top = 18.dp),
            )

            LevelProgressCard(
                label = stringResource(R.string.profile_next_level),
                progressText = stringResource(
                    R.string.profile_next_level_progress,
                    summary.discoveredCount,
                    summary.nextLevelTarget,
                ),
                fraction = if (summary.nextLevelTarget == 0) {
                    0f
                } else {
                    summary.discoveredCount.toFloat() / summary.nextLevelTarget
                },
                modifier = Modifier.padding(top = 18.dp),
            )

            Row(
                modifier = Modifier.padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.gutter),
            ) {
                val valueStyle = AppTextStyles.StatNumber.copy(fontSize = 24.sp)
                StatTile(
                    label = stringResource(R.string.profile_stat_discovered),
                    value = summary.discoveredCount,
                    modifier = Modifier.weight(1f),
                    valueStyle = valueStyle,
                )
                StatTile(
                    label = stringResource(R.string.profile_stat_favorite),
                    value = summary.favoriteCount,
                    modifier = Modifier.weight(1f),
                    valueStyle = valueStyle,
                )
                StatTile(
                    label = stringResource(R.string.profile_stat_memo),
                    value = summary.memoCount,
                    modifier = Modifier.weight(1f),
                    valueStyle = valueStyle,
                )
            }

            SpecList(modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)) {
                SettingRow(
                    label = stringResource(R.string.setting_notification),
                    value = stringResource(R.string.setting_notification_value),
                )
                SettingRow(
                    label = stringResource(R.string.setting_theme),
                    value = stringResource(R.string.setting_theme_value),
                )
                SettingRow(label = stringResource(R.string.setting_backup))
                SettingRow(label = stringResource(R.string.setting_help))
                SettingRow(
                    label = stringResource(R.string.setting_app_info),
                    value = stringResource(R.string.setting_app_info_value),
                    showDivider = false,
                )
            }
        }
    }
}

private val PreviewSummary = ProfileSummary(
    user = UserProfile(
        name = "홍길동",
        joinedAt = Instant.fromEpochMilliseconds(1_772_409_600_000),
    ),
    level = 3,
    levelTitle = PlantLevel.OBSERVER,
    discoveredCount = 23,
    favoriteCount = 5,
    memoCount = 8,
    nextLevelTarget = 30,
)

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 900)
@Composable
private fun ProfileScreenPreview() {
    PlantDexTheme {
        ProfileScreen(state = ProfileState(summary = PreviewSummary))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, heightDp = 900)
@Composable
private fun ProfileScreenEmptyPreview() {
    PlantDexTheme {
        ProfileScreen(state = ProfileState())
    }
}
