package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors

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
