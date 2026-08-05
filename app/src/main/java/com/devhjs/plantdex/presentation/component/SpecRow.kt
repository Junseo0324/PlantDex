package com.devhjs.plantdex.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun SpecRow(
    @DrawableRes iconRes: Int,
    iconBackground: Color,
    iconTint: Color,
    label: String,
    modifier: Modifier = Modifier,
    value: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBackground, RoundedCornerShape(AppRadii.field)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = label, style = AppTextStyles.Label)
            value()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF1EBE1, widthDp = 360)
@Composable
private fun SpecRowPreview() {
    PlantDexTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.gutter),
        ) {
            SpecRow(
                iconRes = R.drawable.ic_spec_water,
                iconBackground = AppColors.WaterBg,
                iconTint = AppColors.WaterTint,
                label = stringResource(R.string.spec_watering),
            ) {
                Text(text = "겉흙이 마르면 2주에 한 번", style = AppTextStyles.BodyStrong)
            }
            SpecRow(
                iconRes = R.drawable.ic_spec_sun,
                iconBackground = AppColors.SunBg,
                iconTint = AppColors.SunTint,
                label = stringResource(R.string.spec_sunlight),
            ) {
                Text(text = "밝은 간접광", style = AppTextStyles.BodyStrong)
            }
            SpecRow(
                iconRes = R.drawable.ic_spec_origin,
                iconBackground = AppColors.OriginBg,
                iconTint = AppColors.OriginTint,
                label = stringResource(R.string.spec_origin),
            ) {
                Text(text = "멕시코 남부 열대우림", style = AppTextStyles.BodyStrong)
            }
            SpecRow(
                iconRes = R.drawable.ic_spec_level,
                iconBackground = AppColors.LevelBg,
                iconTint = AppColors.LevelTint,
                label = stringResource(R.string.spec_difficulty),
            ) {
                StarRating(rating = 2)
            }
        }
    }
}
