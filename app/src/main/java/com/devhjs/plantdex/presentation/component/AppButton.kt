package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = AppColors.Charcoal,
    contentColor: Color = AppColors.OnDark,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(AppRadii.button),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        contentPadding = PaddingValues(horizontal = AppSpacing.ctaPadding),
    ) {
        Text(text = text, style = AppTextStyles.Button.copy(color = contentColor))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun AppButtonPreview() {
    PlantDexTheme {
        AppButton(
            text = "메모 남기기",
            onClick = {},
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}

/** 발견 연출·분석 화면이 쓰는 액센트 배색. */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun AppButtonAccentPreview() {
    PlantDexTheme {
        AppButton(
            text = "분석하기",
            onClick = {},
            modifier = Modifier.padding(AppSpacing.screenH),
            containerColor = AppColors.Terracotta,
            contentColor = AppColors.OnAccent,
        )
    }
}
