package com.devhjs.plantdex.presentation.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

object AppSpacing {
    val screenH = 24.dp   // 좌우 화면 여백
    val gutter = 12.dp    // 카드 사이
    val sectionGap = 20.dp
    val cardPadding = 18.dp
    val ctaPadding = 26.dp
}

object AppRadii {
    val phone = 42.dp
    val hero = 28.dp      // CTA 카드 · 카메라 프리뷰
    val photo = 26.dp     // 상세 사진
    val card = 22.dp      // 스탯 리스트
    val tile = 20.dp      // 그리드 카드 · 통계
    val button = 18.dp
    val field = 14.dp
    val pill = 999.dp
}

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(AppRadii.field),
    medium = RoundedCornerShape(AppRadii.button),
    large = RoundedCornerShape(AppRadii.card),
    extraLarge = RoundedCornerShape(AppRadii.hero),
)

private val AppColorScheme = lightColorScheme(
    primary = AppColors.Terracotta,
    onPrimary = AppColors.OnAccent,
    primaryContainer = AppColors.TerracottaDeep,
    onPrimaryContainer = AppColors.TerracottaOnLight,
    secondary = AppColors.Charcoal,
    onSecondary = AppColors.OnDark,
    tertiary = AppColors.Leaf,
    background = AppColors.Cream,
    onBackground = AppColors.Ink,
    surface = AppColors.Cream,
    onSurface = AppColors.Ink,
    surfaceVariant = AppColors.Sand,
    onSurfaceVariant = AppColors.InkSecondary,
    outline = AppColors.Border,
    outlineVariant = AppColors.Line,
    inverseSurface = AppColors.Charcoal,
    inverseOnSurface = AppColors.OnDark,
)

private val AppTypography = Typography(
    displaySmall = AppTextStyles.Display,
    headlineLarge = AppTextStyles.TitleXL,
    headlineMedium = AppTextStyles.TitleL,
    headlineSmall = AppTextStyles.TitleM,
    titleLarge = AppTextStyles.TitleS,
    titleMedium = AppTextStyles.SectionHeader,
    titleSmall = AppTextStyles.BodyStrong,
    bodyLarge = AppTextStyles.Body,
    bodyMedium = AppTextStyles.BodyMuted,
    bodySmall = AppTextStyles.Chip,
    labelLarge = AppTextStyles.Button,
    labelMedium = AppTextStyles.Label,
    labelSmall = AppTextStyles.NavLabel,
)

@Composable
fun PlantDexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // 다크 테마 미지원, 현재는 라이트 컬러 스킴만 사용
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content,
    )
}
