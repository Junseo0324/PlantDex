package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun BackTopBar(
    onBack: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(horizontal = AppSpacing.screenH, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIconButton(
            iconRes = R.drawable.ic_chevron_left,
            contentDescription = contentDescription,
            onClick = onBack,
            containerColor = AppColors.Sand,
            tint = AppColors.InkBody,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun BackTopBarPreview() {
    PlantDexTheme {
        BackTopBar(onBack = {}, contentDescription = "뒤로")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun BackTopBarWithActionPreview() {
    PlantDexTheme {
        BackTopBar(
            onBack = {},
            contentDescription = "뒤로",
            actions = {
                AppIconButton(iconRes = R.drawable.ic_share, contentDescription = "공유")
            },
        )
    }
}
