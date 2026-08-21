package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun CameraShutterButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .size(78.dp)
            .border(3.dp, AppColors.OnDark, CircleShape)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .background(
                    color = if (enabled) AppColors.Terracotta else AppColors.TerracottaPress,
                    shape = CircleShape,
                ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A)
@Composable
private fun CameraShutterButtonPreview() {
    PlantDexTheme {
        CameraShutterButton(
            onClick = {},
            contentDescription = "촬영",
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}

/** 촬영이 끝날 때까지 다시 눌리지 않는다. */
@Preview(showBackground = true, backgroundColor = 0xFF221E1A)
@Composable
private fun CameraShutterButtonCapturingPreview() {
    PlantDexTheme {
        CameraShutterButton(
            onClick = {},
            contentDescription = "촬영",
            modifier = Modifier.padding(AppSpacing.screenH),
            enabled = false,
        )
    }
}
