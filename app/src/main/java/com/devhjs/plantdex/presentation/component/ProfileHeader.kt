package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun ProfileHeader(
    name: String,
    levelText: String,
    joinedText: String,
    avatarUri: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlantThumbnail(
            photoUri = avatarUri,
            contentDescription = null,
            modifier = Modifier.size(76.dp),
            shape = CircleShape,
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = name,
                style = AppTextStyles.TitleS.copy(fontSize = 21.sp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = levelText,
                    style = AppTextStyles.Label.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.OnAccent,
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(AppRadii.pill))
                        .background(AppColors.Terracotta)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )
                Text(text = joinedText, style = AppTextStyles.Label)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun ProfileHeaderPreview() {
    PlantDexTheme {
        ProfileHeader(
            name = "홍길동",
            levelText = "Lv.4 관찰자",
            joinedText = "가입 2026.03",
            avatarUri = null,
            modifier = Modifier.padding(AppSpacing.screenH),
        )
    }
}
