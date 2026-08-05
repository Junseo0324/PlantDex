package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

private const val MAX_STARS = 5

@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
) {
    val filled = rating.coerceIn(0, MAX_STARS)
    val description = stringResource(R.string.spec_difficulty_description, filled)

    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        repeat(MAX_STARS) { index ->
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = if (index < filled) AppColors.Star else AppColors.StarEmpty,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

/** 0..5 와, 범위를 벗어난 값이 클램프되는지 함께 본다. */
@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3)
@Composable
private fun StarRatingPreview() {
    PlantDexTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(0, 1, 2, 3, 4, 5, 9).forEach { StarRating(rating = it) }
        }
    }
}
