package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

private val OUTER_SIZE = 236.dp
private val INNER_SIZE = 190.dp


@Composable
fun RevealPhoto(
    photoUri: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(OUTER_SIZE)
            .shadow(20.dp, CircleShape)
            .background(AppColors.Cream, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        PlantThumbnail(
            photoUri = photoUri,
            contentDescription = contentDescription,
            modifier = Modifier.size(INNER_SIZE),
            shape = CircleShape,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFC9764A, widthDp = 300, heightDp = 300)
@Composable
private fun RevealPhotoPreview() {
    PlantDexTheme {
        RevealPhoto(
            photoUri = null,
            contentDescription = "몬스테라",
            modifier = Modifier.padding(AppSpacing.gutter),
        )
    }
}
