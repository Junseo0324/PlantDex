package com.devhjs.plantdex.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/**
 * 화면이 붙기 전까지 자리만 잡아두는 임시 화면. 실제 화면이 생기는 대로 하나씩 지운다.
 */
@Composable
fun PlaceholderScreen(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actions: List<Pair<String, () -> Unit>> = emptyList(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = AppSpacing.screenH, vertical = AppSpacing.sectionGap),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.gutter, Alignment.CenterVertically),
    ) {
        Text(text = title, style = AppTextStyles.TitleM)
        subtitle?.let { Text(text = it, style = AppTextStyles.Code) }

        actions.forEach { (label, onClick) ->
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppRadii.button),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Terracotta,
                    contentColor = AppColors.OnAccent,
                ),
            ) {
                Text(text = label, style = AppTextStyles.Button)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3)
@Composable
private fun PlaceholderScreenPreview() {
    PlantDexTheme {
        PlaceholderScreen(
            title = "홈",
            subtitle = "entryId=1",
            actions = listOf("식물 발견하기" to {}, "도감" to {}),
        )
    }
}
