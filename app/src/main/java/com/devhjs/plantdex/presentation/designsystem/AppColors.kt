package com.devhjs.plantdex.presentation.designsystem

import androidx.compose.ui.graphics.Color

object AppColors {

    val Background = Color(0xFFF5F3EE) // 앱 전체 배경
    val Black = Color(0xFF1A1A18) // 기본 텍스트
    val Card = Color(0xFFFFFFFF) // 카드 배경
    val Primary = Color(0xFF2A4A22) // 메인 버튼, 강조색 (딥 포레스트 그린)
    val PrimaryFg = Color(0xFFFFFFFF) // 메인 버튼 텍스트
    val Secondary = Color(0xFFEBF2E8) // 보조 버튼, AI팁 배경 (연한 그린)
    val SecondaryFg = Color(0xFF2A4A22) // 보조 버튼 텍스트
    val Muted = Color(0xFFEEECE7) // 프로그레스 바 트랙 등
    val MutedFg = Color(0xFF8A8880) // 보조 텍스트, 라벨
    val Accent = Color(0xFFD6EAD0) // 배지 배경, 보조 포인트
    val AccentFg = Color(0xFF2A4A22) // 배지 텍스트
    val BorderAlpha = Color(0x14000000) // rgba(0,0,0,0.08) 카드·셀 테두리
    val InputBg = Color(0xFFF0EDE8) // 검색창 배경

    // 식물 카드 배경 팔레트 (식물 종류에 종속되지 않는 색상 로테이션용)
    val YellowCard = Color(0xFFFFF9E6)
    val PinkCard = Color(0xFFFFF0F5)
    val CreamCard = Color(0xFFFFFBE6)
    val MintCard = Color(0xFFEDFAF0)
    val PeachCard = Color(0xFFFFF0EE)
    val LavenderCard = Color(0xFFF5F0FF)

    val Star = Color(0xFFFBBF24) // amber-400, 별점 (★)
    val StarEmpty = Color(0xFFE5E7EB) // gray-200, 빈 별
    val Heart = Color(0xFFFB7185) // rose-400, 즐겨찾기 하트

    val WaterBg = Color(0xFFEFF6FF) // blue-50
    val WaterTint = Color(0xFF3B82F6) // blue-500, 물주기 아이콘

    val SunBg = Color(0xFFFFFBEB) // amber-50
    val SunTint = Color(0xFFF59E0B) // amber-500, 햇빛 아이콘

    val OriginBg = Color(0xFFFFF1F2) // rose-50
    val OriginTint = Color(0xFFF43F5E) // rose-500, 원산지 아이콘

    val LevelBg = Color(0xFFF0FDF4) // green-50
    val LevelTint = Color(0xFF16A34A) // green-600, 난이도 아이콘

    // ── Warm neutrals (base) ─────────────────────────────
    val Cream = Color(0xFFFBF8F3) // 화면 배경 / 흰 씬
    val CreamCanvas = Color(0xFFEFE9E1) // 캔버스 배경
    val Sand = Color(0xFFF1EBE1) // 카드·입력 필드 배경
    val SandStrong = Color(0xFFE3DACD) // 태그 칩 배경
    val Line = Color(0xFFEAE2D7) // 구분선 (밝음)
    val LineStrong = Color(0xFFE6DDD0) // 리스트 행 구분선
    val Border = Color(0xFFE4DCD0) // 외곽 보더
    val BorderMuted = Color(0xFFDDD3C5) // 아웃라인 버튼 보더

    // ── Ink (text) ────────────────────────────────────────
    val Ink = Color(0xFF231F1B) // 제목 / 본문 강조
    val InkSoft = Color(0xFF2C2823) // 다크 서피스 · 활성 라벨
    val InkBody = Color(0xFF3A342E) // 본문
    val InkSecondary = Color(0xFF6E635A) // 보조 텍스트
    val InkMuted = Color(0xFF7C7168) // 설명 텍스트
    val InkPlaceholder = Color(0xFF948A80) // 캡션 · 라벨
    val InkDisabled = Color(0xFFA79C90) // 비활성 라벨
    val InkFaint = Color(0xFFB0A79C) // 대기 상태

    // ── Accents ───────────────────────────────────────────
    val Terracotta = Color(0xFFC9764A) // 메인 악센트 (CTA · 발견 연출)
    val TerracottaDeep = Color(0xFFA8613C) // 링크 · 도감 번호
    val TerracottaPress = Color(0xFF8A4B2B) // press 상태
    val TerracottaOnLight = Color(0xFFFBE6D6) // 악센트 위 보조 텍스트
    val TerracottaOnSoft = Color(0xFFFBE0CD)
    val Leaf = Color(0xFF7C9A6B) // 새싹 / 성공

    // ── Dark surfaces (camera) ────────────────────────────
    val Charcoal = Color(0xFF221E1A) // 카메라 배경 · 플로팅 버튼
    val CharcoalRaise = Color(0xFF38312A) // 프리뷰 스트라이프 A
    val CharcoalRaise2 = Color(0xFF3E372F) // 프리뷰 스트라이프 B
    val OnDark = Color(0xFFFBF8F3)
    val OnDarkMuted = Color(0xFFE5DACB)
    val OnDarkFaint = Color(0xFF9C9284)
    val OnAccent = Color(0xFFFFF9F4)

    // ── Photo placeholder stripes ─────────────────────────
    val PlaceholderA = Color(0xFFE7E0D4)
    val PlaceholderB = Color(0xFFEFE9DF)
}
