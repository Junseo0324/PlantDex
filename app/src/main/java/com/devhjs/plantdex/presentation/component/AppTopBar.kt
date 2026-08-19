package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            // 40dp 액션 버튼이 들어와도 바 높이가 튀지 않게 최소 높이로 맞춘다.
            .heightIn(min = 56.dp)
            .padding(horizontal = AppSpacing.screenH, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, style = AppTextStyles.TitleS)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun AppTopBarPreview() {
    PlantDexTheme {
        AppTopBar(title = "식물도감")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun AppTopBarActionsPreview() {
    PlantDexTheme {
        AppTopBar(
            title = "식물도감",
            actions = {
                AppIconButton(iconRes = R.drawable.ic_search, contentDescription = "검색")
                AppIconButton(
                    iconRes = R.drawable.ic_bell,
                    contentDescription = "알림",
                    showBadge = true,
                )
            },
        )
    }
}
