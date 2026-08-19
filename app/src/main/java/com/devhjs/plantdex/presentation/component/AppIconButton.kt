package com.devhjs.plantdex.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme


@Composable
fun AppIconButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    containerColor: Color = Color.Transparent,
    tint: Color = AppColors.InkSecondary,
    showBadge: Boolean = false,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(containerColor)
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(21.dp),
            tint = tint,
        )

        if (showBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .border(1.5.dp, AppColors.Cream, CircleShape)
                    .padding(1.5.dp)
                    .size(7.dp)
                    .background(AppColors.Terracotta, CircleShape),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3)
@Composable
private fun AppIconButtonPreview() {
    PlantDexTheme {
        AppIconButton(iconRes = R.drawable.ic_search, contentDescription = "검색", onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3)
@Composable
private fun AppIconButtonBadgePreview() {
    PlantDexTheme {
        AppIconButton(
            iconRes = R.drawable.ic_bell,
            contentDescription = "알림",
            showBadge = true,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF1EBE1)
@Composable
private fun AppIconButtonFilledPreview() {
    PlantDexTheme {
        AppIconButton(
            iconRes = R.drawable.ic_chevron_left,
            contentDescription = "뒤로",
            onClick = {},
            containerColor = AppColors.Sand,
            tint = AppColors.InkBody,
        )
    }
}
