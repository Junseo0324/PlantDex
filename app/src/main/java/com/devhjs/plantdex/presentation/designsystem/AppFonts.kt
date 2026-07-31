package com.devhjs.plantdex.presentation.designsystem

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.devhjs.plantdex.R

object AppFonts {
    val Pretendard = FontFamily(
        Font(R.font.pretendard_regular, FontWeight.Normal),
        Font(R.font.pretendard_medium, FontWeight.Medium),
        Font(R.font.pretendard_semibold, FontWeight.SemiBold),
        Font(R.font.pretendard_bold, FontWeight.Bold),
    )

    // Mono = 도감 번호, JSON, 플레이스홀더 캡션
    val Mono = FontFamily.Monospace
}
