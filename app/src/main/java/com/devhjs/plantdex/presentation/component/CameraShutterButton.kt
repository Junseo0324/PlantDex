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
) {
    Box(
        modifier = modifier
            .size(78.dp)
            .border(3.dp, AppColors.OnDark, CircleShape)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .background(AppColors.Terracotta, CircleShape),
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
