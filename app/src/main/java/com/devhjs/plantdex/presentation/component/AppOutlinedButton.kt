package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = AppColors.BorderMuted,
    contentColor: Color = AppColors.TerracottaDeep,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(AppRadii.button),
        border = BorderStroke(1.5.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor),
        contentPadding = PaddingValues(horizontal = AppSpacing.ctaPadding),
    ) {
        Text(text = text, style = AppTextStyles.Button.copy(color = contentColor))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun AppOutlinedButtonPreview() {
    PlantDexTheme {
        AppOutlinedButton(
            text = "다시 분석하기",
            onClick = {},
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFC9764A, widthDp = 390)
@Composable
private fun AppOutlinedButtonOnAccentPreview() {
    PlantDexTheme {
        AppOutlinedButton(
            text = "다시 촬영",
            onClick = {},
            modifier = Modifier.padding(AppSpacing.screenH),
            borderColor = AppColors.OnAccent.copy(alpha = 0.6f),
            contentColor = AppColors.OnAccent,
        )
    }
}
