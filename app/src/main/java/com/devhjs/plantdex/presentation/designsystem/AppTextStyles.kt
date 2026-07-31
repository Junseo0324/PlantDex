package com.devhjs.plantdex.presentation.designsystem

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

object AppTextStyles {

    // ── Display / Titles ─────────────────────────────────
    /** 발견 도출 식물 이름 · 40sp */
    val Display = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Bold,
        fontSize = 40.sp, lineHeight = 46.sp, letterSpacing = (-0.8).sp,
        color = AppColors.OnAccent, textAlign = TextAlign.Center,
    )
    /** 상세 화면 식물 이름 · 30sp */
    val TitleXL = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Bold,
        fontSize = 30.sp, lineHeight = 38.sp, letterSpacing = (-0.6).sp, color = AppColors.Ink,
    )
    /** 홈 인사말 · 화면 제목 · 26sp */
    val TitleL = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Bold,
        fontSize = 26.sp, lineHeight = 35.sp, letterSpacing = (-0.5).sp, color = AppColors.Ink,
    )
    /** 통계 숫자 · 28sp */
    val StatNumber = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 32.sp, letterSpacing = (-0.6).sp, color = AppColors.Ink,
    )
    /** 분석 중 안내 · 22sp */
    val TitleM = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Bold,
        fontSize = 22.sp, lineHeight = 30.sp, letterSpacing = (-0.2).sp, color = AppColors.Ink,
    )
    /** 셀렉트 로고 · CTA 카드 제목 · 19sp */
    val TitleS = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Bold,
        fontSize = 19.sp, lineHeight = 26.sp, letterSpacing = (-0.2).sp, color = AppColors.Ink,
    )
    /** 섹션 헤더 · 16sp */
    val SectionHeader = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Bold,
        fontSize = 16.sp, lineHeight = 22.sp, color = AppColors.Ink,
    )

    // ── Body ──────────────────────────────────────────────
    /** 버튼 라벨 · 16sp */
    val Button = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 22.sp, color = AppColors.OnDark,
    )
    /** 그리드 카드 이름 · 스핀 값 · 15sp */
    val BodyStrong = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp, lineHeight = 21.sp, color = AppColors.InkSoft,
    )
    /** 본문 설명 · 14sp / 1.7 */
    val Body = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 24.sp, color = AppColors.InkSecondary,
    )
    /** 보조 설명 · 14sp muted */
    val BodyMuted = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp, color = AppColors.InkPlaceholder,
    )
    /** 학명 · italic 14sp */
    val Scientific = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Italic,
        fontSize = 14.sp, lineHeight = 20.sp, color = AppColors.InkPlaceholder,
    )
    /** 칩 · 필터 · 13sp */
    val Chip = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Medium,
        fontSize = 13.sp, lineHeight = 18.sp, color = AppColors.InkSecondary,
    )
    /** 링크 · 강조 라벨 · 13sp */
    val LabelAccent = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp, lineHeight = 18.sp, color = AppColors.TerracottaDeep,
    )
    /** 스펙 키 · 카드 캡션 · 12~13sp */
    val Label = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp, color = AppColors.InkPlaceholder,
    )
    /** 하단 내비 라벨 · 11sp */
    val NavLabel = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.Medium,
        fontSize = 11.sp, lineHeight = 14.sp, color = AppColors.InkDisabled,
    )

    // ── Mono / Eyebrow ────────────────────────────────────
    /** 발견 도출 No.023 · 15sp */
    val DexNumberL = TextStyle(
        fontFamily = AppFonts.Mono, fontWeight = FontWeight.Medium,
        fontSize = 15.sp, lineHeight = 20.sp, letterSpacing = 1.8.sp, color = AppColors.TerracottaOnLight,
    )
    /** 상세 No.023 · 13sp */
    val DexNumber = TextStyle(
        fontFamily = AppFonts.Mono, fontWeight = FontWeight.Medium,
        fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 1.6.sp, color = AppColors.TerracottaDeep,
    )
    /** 카드 위 No.001 · 11sp */
    val DexNumberS = TextStyle(
        fontFamily = AppFonts.Mono, fontWeight = FontWeight.Medium,
        fontSize = 11.sp, lineHeight = 15.sp, letterSpacing = 1.0.sp, color = AppColors.TerracottaDeep,
    )
    /** JSON 프리뷰 · 11sp */
    val Code = TextStyle(
        fontFamily = AppFonts.Mono, fontWeight = FontWeight.Normal,
        fontSize = 11.sp, lineHeight = 20.sp, color = AppColors.InkSecondary,
    )
    /** NEW DISCOVERY 등 아이브로우 · 13sp */
    val Eyebrow = TextStyle(
        fontFamily = AppFonts.Pretendard, fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 3.9.sp, color = AppColors.TerracottaOnLight,
    )
}
