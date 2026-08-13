package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun FavoriteToggleButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .border(1.5.dp, AppColors.BorderMuted, RoundedCornerShape(AppRadii.button))
            .toggleable(
                value = isFavorite,
                role = Role.Checkbox,
                onValueChange = { onToggle() },
            )
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(
                if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_outline
            ),
            contentDescription = null,
            tint = AppColors.Terracotta,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3)
@Composable
private fun FavoriteToggleButtonPreview() {
    PlantDexTheme {
        Row(
            modifier = Modifier.padding(AppSpacing.screenH),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.gutter),
        ) {
            FavoriteToggleButton(isFavorite = false, onToggle = {}, contentDescription = "즐겨찾기")
            FavoriteToggleButton(isFavorite = true, onToggle = {}, contentDescription = "즐겨찾기 해제")
        }
    }
}
